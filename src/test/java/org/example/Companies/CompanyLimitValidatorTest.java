package org.example.Companies;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompanyLimitValidatorTest {

    private CompanyLimitValidator validator;

    @BeforeClass
    public void setup() {
        validator = new CompanyLimitValidator();
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private Map<String, Object> company(String name, String closePrc,
                                        String limmitUp, String limmitDown) {
        Map<String, Object> c = new HashMap<>();
        c.put("cmpArbName",  name);
        c.put("closePrc",    closePrc);
        c.put("limmitUp",    limmitUp);
        c.put("limmitDown",  limmitDown);
        return c;
    }

    private List<Map<String, Object>> listOf(Map<String, Object>... companies) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Map<String, Object> c : companies) list.add(c);
        return list;
    }

    // ─── VALID cases ─────────────────────────────────────────────────────────

    @Test(description = "Exact match — stored limits equal expected limits")
    public void testValidWhenLimitsMatchExactly() {
        // closePrc=10.00 → limitUp=11.00, limitDown=9.00
        List<CompanyValidationResult> results = validator.validate(
                listOf(company("الشركة أ", "10.00", "11.00", "9.00")));

        Assert.assertEquals(results.size(), 1);
        Assert.assertEquals(results.get(0).getStatus(), CompanyValidationResult.Status.VALID);
    }

    @Test(description = "Stored values with more decimal places still round to match")
    public void testValidWhenStoredHasExtraDecimals() {
        // closePrc=2.40 → limitUp=2.64, limitDown=2.16
        List<CompanyValidationResult> results = validator.validate(
                listOf(company("مقدام", "2.4", "2.640", "2.160")));

        Assert.assertEquals(results.get(0).getStatus(), CompanyValidationResult.Status.VALID);
    }

    @Test(description = "HALF_UP rounding applied — closePrc=18.29 → limitUp=20.12, limitDown=16.46")
    public void testValidWithHalfUpRounding() {
        // 18.29 * 1.10 = 20.119 → HALF_UP = 20.12
        // 18.29 * 0.90 = 16.461 → HALF_UP = 16.46
        List<CompanyValidationResult> results = validator.validate(
                listOf(company("Ooredoo", "18.29", "20.12", "16.46")));

        Assert.assertEquals(results.get(0).getStatus(), CompanyValidationResult.Status.VALID);
    }

    @Test(description = "All companies in list are VALID")
    public void testAllCompaniesValid() {
        List<Map<String, Object>> companies = listOf(
                company("شركة أ", "10.00", "11.00", "9.00"),
                company("شركة ب", "20.00", "22.00", "18.00"),
                company("شركة ج", "5.00",  "5.50",  "4.50")
        );

        List<CompanyValidationResult> results = validator.validate(companies);

        Assert.assertEquals(results.size(), 3);
        results.forEach(r ->
                Assert.assertEquals(r.getStatus(), CompanyValidationResult.Status.VALID,
                        r.getCmpArbName() + " should be VALID"));
    }

    // ─── INVALID cases ───────────────────────────────────────────────────────

    @Test(description = "Stored limitUp is wrong → INVALID")
    public void testInvalidWhenLimitUpIsWrong() {
        // expected limitUp = 11.00, stored = 12.00
        List<CompanyValidationResult> results = validator.validate(
                listOf(company("شركة خاطئة", "10.00", "12.00", "9.00")));

        Assert.assertEquals(results.get(0).getStatus(), CompanyValidationResult.Status.INVALID);
    }

    @Test(description = "Stored limitDown is wrong → INVALID")
    public void testInvalidWhenLimitDownIsWrong() {
        // expected limitDown = 9.00, stored = 8.00
        List<CompanyValidationResult> results = validator.validate(
                listOf(company("شركة خاطئة", "10.00", "11.00", "8.00")));

        Assert.assertEquals(results.get(0).getStatus(), CompanyValidationResult.Status.INVALID);
    }

    @Test(description = "Both limits wrong → INVALID")
    public void testInvalidWhenBothLimitsAreWrong() {
        List<CompanyValidationResult> results = validator.validate(
                listOf(company("شركة خاطئة", "10.00", "99.00", "1.00")));

        Assert.assertEquals(results.get(0).getStatus(), CompanyValidationResult.Status.INVALID);
    }

    @Test(description = "Mixed list returns correct VALID/INVALID per company")
    public void testMixedList() {
        List<Map<String, Object>> companies = listOf(
                company("صحيح",   "10.00", "11.00", "9.00"),   // VALID
                company("خاطئ",   "10.00", "12.00", "9.00"),   // INVALID — wrong limitUp
                company("صحيح 2", "20.00", "22.00", "18.00")   // VALID
        );

        List<CompanyValidationResult> results = validator.validate(companies);

        Assert.assertEquals(results.get(0).getStatus(), CompanyValidationResult.Status.VALID);
        Assert.assertEquals(results.get(1).getStatus(), CompanyValidationResult.Status.INVALID);
        Assert.assertEquals(results.get(2).getStatus(), CompanyValidationResult.Status.VALID);
    }

    // ─── Result content ──────────────────────────────────────────────────────

    @Test(description = "Result contains all expected fields with correct values")
    public void testResultFields() {
        List<CompanyValidationResult> results = validator.validate(
                listOf(company("QNB", "15.80", "17.38", "14.22")));

        CompanyValidationResult r = results.get(0);

        Assert.assertEquals(r.getCmpArbName(), "QNB");
        Assert.assertEquals(r.getClosePrc().toPlainString(),        "15.80");
        Assert.assertEquals(r.getStoredLimitUp().toPlainString(),   "17.38");
        Assert.assertEquals(r.getStoredLimitDown().toPlainString(), "14.22");
        // 15.80 * 1.10 = 17.38 → VALID
        Assert.assertEquals(r.getExpectedLimitUp().toPlainString(),   "17.38");
        Assert.assertEquals(r.getExpectedLimitDown().toPlainString(), "14.22");
        Assert.assertEquals(r.getStatus(), CompanyValidationResult.Status.VALID);
    }

    // ─── Edge cases ──────────────────────────────────────────────────────────

    @Test(description = "Null list throws IllegalArgumentException",
            expectedExceptions = IllegalArgumentException.class)
    public void testNullListThrows() {
        validator.validate(null);
    }

    @Test(description = "Empty list throws IllegalArgumentException",
            expectedExceptions = IllegalArgumentException.class)
    public void testEmptyListThrows() {
        validator.validate(new ArrayList<>());
    }

    @Test(description = "Missing closePrc field defaults to ZERO and marks INVALID")
    public void testMissingClosePrcMarksInvalid() {
        Map<String, Object> c = new HashMap<>();
        c.put("cmpArbName", "شركة ناقصة");
        c.put("limmitUp",   "11.00");
        c.put("limmitDown", "9.00");
        // closePrc not set

        List<CompanyValidationResult> results = validator.validate(listOf(c));

        Assert.assertEquals(results.get(0).getStatus(), CompanyValidationResult.Status.INVALID);
    }
}
