package org.example.utils;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileSnapshotManagerTest {

    private FileSnapshotManager snap;
    private Path tempFile;

    private static final String ORIGINAL_CONTENT = "original content";
    private static final String MODIFIED_CONTENT = "modified content";

    @BeforeMethod
    public void setup() throws IOException {
        snap = FileSnapshotManager.getInstance();
        snap.clearAllSnapshots();

        // Create a temporary file with known content for each test
        tempFile = Files.createTempFile("snapshot_test_", ".txt");
        Files.writeString(tempFile, ORIGINAL_CONTENT);
    }

    @AfterMethod
    public void teardown() throws IOException {
        snap.clearAllSnapshots();
        Files.deleteIfExists(tempFile);
    }

    // ─── snapshot() ──────────────────────────────────────────────────────────

    @Test(description = "snapshot() saves file content and hasSnapshot() returns true")
    public void testSnapshotSavesContent() throws IOException {
        snap.snapshot(tempFile.toString());

        Assert.assertTrue(snap.hasSnapshot(tempFile.toString()),
                "hasSnapshot should return true after snapshot()");
        Assert.assertEquals(snap.snapshotCount(), 1);
    }

    @Test(description = "snapshot() on non-existent file throws IllegalArgumentException",
            expectedExceptions = IllegalArgumentException.class)
    public void testSnapshotNonExistentFileThrows() throws IOException {
        snap.snapshot("/non/existent/file.txt");
    }

    @Test(description = "snapshot() overwrites previous snapshot for the same file")
    public void testSnapshotOverwritesPreviousSnapshot() throws IOException {
        snap.snapshot(tempFile.toString());

        // Modify the file and snapshot again
        Files.writeString(tempFile, "intermediate content");
        snap.snapshot(tempFile.toString());

        // Rollback should restore the SECOND snapshot (intermediate), not the first
        Files.writeString(tempFile, MODIFIED_CONTENT);
        snap.rollback(tempFile.toString());

        Assert.assertEquals(Files.readString(tempFile), "intermediate content",
                "Rollback should restore the most recent snapshot");
    }

    // ─── rollback() ──────────────────────────────────────────────────────────

    @Test(description = "rollback() restores file to its original content")
    public void testRollbackRestoresContent() throws IOException {
        snap.snapshot(tempFile.toString());

        // Modify the file
        Files.writeString(tempFile, MODIFIED_CONTENT);
        Assert.assertEquals(Files.readString(tempFile), MODIFIED_CONTENT);

        // Rollback
        snap.rollback(tempFile.toString());

        Assert.assertEquals(Files.readString(tempFile), ORIGINAL_CONTENT,
                "File content should match original after rollback");
    }

    @Test(description = "rollback() without a snapshot throws IllegalStateException",
            expectedExceptions = IllegalStateException.class)
    public void testRollbackWithoutSnapshotThrows() throws IOException {
        snap.rollback(tempFile.toString());
    }

    @Test(description = "rollback() does not clear the snapshot — can rollback multiple times")
    public void testRollbackDoesNotClearSnapshot() throws IOException {
        snap.snapshot(tempFile.toString());
        Files.writeString(tempFile, MODIFIED_CONTENT);
        snap.rollback(tempFile.toString());

        // Snapshot should still be present
        Assert.assertTrue(snap.hasSnapshot(tempFile.toString()),
                "Snapshot should remain after rollback");

        // Can rollback again after another modification
        Files.writeString(tempFile, "second modification");
        snap.rollback(tempFile.toString());
        Assert.assertEquals(Files.readString(tempFile), ORIGINAL_CONTENT);
    }

    // ─── rollbackAll() ───────────────────────────────────────────────────────

    @Test(description = "rollbackAll() restores all snapshotted files")
    public void testRollbackAllRestoresAllFiles() throws IOException {
        Path tempFile2 = Files.createTempFile("snapshot_test2_", ".txt");
        Files.writeString(tempFile2, "second original");

        try {
            snap.snapshot(tempFile.toString());
            snap.snapshot(tempFile2.toString());

            Files.writeString(tempFile, MODIFIED_CONTENT);
            Files.writeString(tempFile2, "second modified");

            snap.rollbackAll();

            Assert.assertEquals(Files.readString(tempFile), ORIGINAL_CONTENT);
            Assert.assertEquals(Files.readString(tempFile2), "second original");
        } finally {
            Files.deleteIfExists(tempFile2);
        }
    }

    @Test(description = "rollbackAll() with no snapshots logs a warning without throwing")
    public void testRollbackAllWithNoSnapshotsDoesNotThrow() throws IOException {
        snap.rollbackAll(); // should not throw
        Assert.assertEquals(snap.snapshotCount(), 0);
    }

    // ─── clearSnapshot() ─────────────────────────────────────────────────────

    @Test(description = "clearSnapshot() removes the snapshot without restoring the file")
    public void testClearSnapshotRemovesEntry() throws IOException {
        snap.snapshot(tempFile.toString());
        Assert.assertTrue(snap.hasSnapshot(tempFile.toString()));

        Files.writeString(tempFile, MODIFIED_CONTENT);
        snap.clearSnapshot(tempFile.toString());

        Assert.assertFalse(snap.hasSnapshot(tempFile.toString()),
                "hasSnapshot should return false after clearSnapshot()");

        // File should still have the modified content — not restored
        Assert.assertEquals(Files.readString(tempFile), MODIFIED_CONTENT,
                "clearSnapshot should not restore the file");
    }

    // ─── clearAllSnapshots() ─────────────────────────────────────────────────

    @Test(description = "clearAllSnapshots() removes all entries")
    public void testClearAllSnapshots() throws IOException {
        snap.snapshot(tempFile.toString());
        Assert.assertEquals(snap.snapshotCount(), 1);

        snap.clearAllSnapshots();

        Assert.assertEquals(snap.snapshotCount(), 0);
        Assert.assertFalse(snap.hasSnapshot(tempFile.toString()));
    }
}
