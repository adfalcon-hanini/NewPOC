package org.example.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Utility for saving and restoring file content snapshots.
 *
 * <p>Typical usage pattern:
 * <pre>
 *   FileSnapshotManager snap = FileSnapshotManager.getInstance();
 *
 *   snap.snapshot("src/test/resources/config.properties");  // save before modifying
 *   // ... modify the file ...
 *   snap.rollback("src/test/resources/config.properties");  // restore original content
 *   snap.clearSnapshot("src/test/resources/config.properties");
 * </pre>
 *
 * Snapshots are held in memory for the lifetime of the JVM process.
 * Call {@link #clearAllSnapshots()} after a test suite to free memory.
 */
public class FileSnapshotManager {

    private static final Logger logger = LogManager.getLogger(FileSnapshotManager.class);

    private static final FileSnapshotManager INSTANCE = new FileSnapshotManager();

    // Stores absolute path -> original file bytes
    private final Map<String, byte[]> snapshots = new HashMap<>();

    private FileSnapshotManager() {}

    public static FileSnapshotManager getInstance() {
        return INSTANCE;
    }

    // ─── Snapshot ────────────────────────────────────────────────────────────

    /**
     * Saves the current content of a file as a snapshot.
     * If a snapshot already exists for this file it is overwritten.
     *
     * @param filePath path to the file to snapshot
     * @throws IllegalArgumentException if the file does not exist
     * @throws IOException              if the file cannot be read
     */
    public void snapshot(String filePath) throws IOException {
        Path path = toAbsolutePath(filePath);

        if (!Files.exists(path)) {
            throw new IllegalArgumentException("File does not exist: " + path);
        }

        byte[] content = Files.readAllBytes(path);
        snapshots.put(path.toString(), content);

        logger.info("Snapshot saved → {} ({} bytes)", path, content.length);
    }

    // ─── Rollback ────────────────────────────────────────────────────────────

    /**
     * Restores a single file to the content captured in its snapshot.
     *
     * @param filePath path to the file to restore
     * @throws IllegalStateException if no snapshot exists for this file
     * @throws IOException           if the file cannot be written
     */
    public void rollback(String filePath) throws IOException {
        String key = toAbsolutePath(filePath).toString();
        byte[] content = snapshots.get(key);

        if (content == null) {
            throw new IllegalStateException("No snapshot found for: " + filePath);
        }

        Files.write(Paths.get(key), content);
        logger.info("Rolled back → {}", key);
    }

    /**
     * Rolls back every file that has a saved snapshot.
     * Files that cannot be written are logged and skipped; processing continues.
     *
     * @throws IOException if one or more files cannot be restored
     */
    public void rollbackAll() throws IOException {
        if (snapshots.isEmpty()) {
            logger.warn("rollbackAll called but no snapshots are held");
            return;
        }

        IOException lastError = null;

        for (Map.Entry<String, byte[]> entry : snapshots.entrySet()) {
            try {
                Files.write(Paths.get(entry.getKey()), entry.getValue());
                logger.info("Rolled back → {}", entry.getKey());
            } catch (IOException e) {
                logger.error("Failed to roll back {}: {}", entry.getKey(), e.getMessage());
                lastError = e;
            }
        }

        if (lastError != null) {
            throw lastError;
        }
    }

    // ─── Clear ───────────────────────────────────────────────────────────────

    /**
     * Removes the snapshot for a single file without restoring it.
     * Call this after a test completes successfully and the changes should be kept.
     *
     * @param filePath path whose snapshot should be discarded
     */
    public void clearSnapshot(String filePath) {
        String key = toAbsolutePath(filePath).toString();
        if (snapshots.remove(key) != null) {
            logger.info("Snapshot cleared → {}", key);
        } else {
            logger.warn("clearSnapshot: no snapshot found for {}", filePath);
        }
    }

    /**
     * Removes all held snapshots without restoring any files.
     */
    public void clearAllSnapshots() {
        int count = snapshots.size();
        snapshots.clear();
        logger.info("All snapshots cleared ({} entries)", count);
    }

    // ─── Query ───────────────────────────────────────────────────────────────

    /** Returns true if a snapshot exists for the given file. */
    public boolean hasSnapshot(String filePath) {
        return snapshots.containsKey(toAbsolutePath(filePath).toString());
    }

    /** Returns the number of snapshots currently held. */
    public int snapshotCount() {
        return snapshots.size();
    }

    /** Returns an unmodifiable view of all snapshotted file paths. */
    public Set<String> snapshotPaths() {
        return Collections.unmodifiableSet(snapshots.keySet());
    }

    // ─── Internal ────────────────────────────────────────────────────────────

    private Path toAbsolutePath(String filePath) {
        return Paths.get(filePath).toAbsolutePath().normalize();
    }
}
