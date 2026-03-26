package org.example.Companies;

import io.restassured.response.Response;
import org.example.base.BaseApi;
import org.example.login.Login;
import org.example.utils.ConfigManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CheckLimitTest {

    private GetComps getComps;
    private ConfigManager config;

    // A real company code present in the GET_COMPS response (QNBK closePrc ≈ 15.8)
    private static final String VALID_COMP_CODE = "QNBK";

    @BeforeClass
    public void setup() {
        config = ConfigManager.getInstance();
        getComps = new GetComps();

        // Login is required before calling any authenticated API
        Login login = new Login();
        Response loginResponse = login.login(
                config.getTestNin(),
                config.getTestPassword(),
                config.getTestLanguage(),
                config.getTestLstLogin()
        );

        Assert.assertEquals(loginResponse.getStatusCode(), 200, "Login failed");
        Assert.assertTrue(BaseApi.isSessionActive(), "Session not active after login");
    }

    // ─── Within range ────────────────────────────────────────────────────────

    @Test(description = "Price equal to closePrc is within ±10% range")
    public void testCurrentPriceAtClosePrcIsWithinRange() {
        // Fetch closePrc from API, then pass it back as currentPrice — must always be in range
        double closePrc = getComps.getClosePrcFromApi(VALID_COMP_CODE);
        Assert.assertTrue(getComps.checkLimit(VALID_COMP_CODE, closePrc),
                "Price equal to closePrc should be within range");
    }

    @Test(description = "Price just above limitDown is within range")
    public void testCurrentPriceJustAboveLimitDown() {
        double closePrc = getComps.getClosePrcFromApi(VALID_COMP_CODE);
        double justAboveLimitDown = (closePrc * 0.90) + 0.01;
        Assert.assertTrue(getComps.checkLimit(VALID_COMP_CODE, justAboveLimitDown),
                "Price just above limitDown should be within range");
    }

    @Test(description = "Price just below limitUp is within range")
    public void testCurrentPriceJustBelowLimitUp() {
        double closePrc = getComps.getClosePrcFromApi(VALID_COMP_CODE);
        double justBelowLimitUp = (closePrc * 1.10) - 0.01;
        Assert.assertTrue(getComps.checkLimit(VALID_COMP_CODE, justBelowLimitUp),
                "Price just below limitUp should be within range");
    }

    @Test(description = "Price exactly at limitDown boundary is within range")
    public void testCurrentPriceExactlyAtLimitDown() {
        double closePrc = getComps.getClosePrcFromApi(VALID_COMP_CODE);
        double limitDown = closePrc * 0.90;
        Assert.assertTrue(getComps.checkLimit(VALID_COMP_CODE, limitDown),
                "Price exactly at limitDown should be within range (inclusive)");
    }

    @Test(description = "Price exactly at limitUp boundary is within range")
    public void testCurrentPriceExactlyAtLimitUp() {
        double closePrc = getComps.getClosePrcFromApi(VALID_COMP_CODE);
        double limitUp = closePrc * 1.10;
        Assert.assertTrue(getComps.checkLimit(VALID_COMP_CODE, limitUp),
                "Price exactly at limitUp should be within range (inclusive)");
    }

    // ─── Outside range ───────────────────────────────────────────────────────

    @Test(description = "Price above limitUp is out of range")
    public void testCurrentPriceAboveLimitUp() {
        double closePrc = getComps.getClosePrcFromApi(VALID_COMP_CODE);
        double aboveLimitUp = (closePrc * 1.10) + 1.0;
        Assert.assertFalse(getComps.checkLimit(VALID_COMP_CODE, aboveLimitUp),
                "Price above limitUp should be out of range");
    }

    @Test(description = "Price below limitDown is out of range")
    public void testCurrentPriceBelowLimitDown() {
        double closePrc = getComps.getClosePrcFromApi(VALID_COMP_CODE);
        double belowLimitDown = (closePrc * 0.90) - 1.0;
        Assert.assertFalse(getComps.checkLimit(VALID_COMP_CODE, belowLimitDown),
                "Price below limitDown should be out of range");
    }

    @Test(description = "Price of 0 is below limitDown for any valid company")
    public void testCurrentPriceZeroIsOutOfRange() {
        Assert.assertFalse(getComps.checkLimit(VALID_COMP_CODE, 0.0),
                "Price of 0 should be below limitDown and out of range");
    }

    // ─── Edge cases ──────────────────────────────────────────────────────────

    @Test(description = "Negative currentPrice throws IllegalArgumentException",
            expectedExceptions = IllegalArgumentException.class)
    public void testNegativeCurrentPriceThrowsException() {
        getComps.checkLimit(VALID_COMP_CODE, -1.0);
    }

    @Test(description = "Null compCode throws IllegalArgumentException",
            expectedExceptions = IllegalArgumentException.class)
    public void testNullCompCodeThrowsException() {
        getComps.checkLimit(null, 10.0);
    }

    @Test(description = "Empty compCode throws IllegalArgumentException",
            expectedExceptions = IllegalArgumentException.class)
    public void testEmptyCompCodeThrowsException() {
        getComps.checkLimit("", 10.0);
    }

    @Test(description = "Unknown compCode throws IllegalArgumentException",
            expectedExceptions = IllegalArgumentException.class)
    public void testUnknownCompCodeThrowsException() {
        getComps.checkLimit("INVALID_CODE_XYZ", 10.0);
    }
}
