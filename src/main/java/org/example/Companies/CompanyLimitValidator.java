package org.example.Companies;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Validates the stored limitUp / limitDown values of a list of companies
 * against the expected values derived from closePrc (±10%).
 *
 * <p>Formula (BigDecimal, HALF_UP, 2 decimal places):
 * <pre>
 *   expectedLimitUp   = closePrc × 1.10
 *   expectedLimitDown = closePrc × 0.90
 * </pre>
 *
 * <p>A company is marked {@code VALID} when both stored values match the expected
 * values after rounding. Otherwise it is marked {@code INVALID}.
 *
 * <p>Note: the API returns limit fields as {@code limmitUp} / {@code limmitDown}
 * (double-m). This class handles that mapping transparently.
 */
public class CompanyLimitValidator {

    private static final Logger logger = LogManager.getLogger(CompanyLimitValidator.class);

    private static final BigDecimal FACTOR_UP   = new BigDecimal("1.10");
    private static final BigDecimal FACTOR_DOWN = new BigDecimal("0.90");
    private static final int           SCALE    = 2;
    private static final RoundingMode  ROUNDING = RoundingMode.HALF_UP;

    // API field names (note double-m typo in the actual API response)
    private static final String FIELD_CMP_ARB_NAME  = "cmpArbName";
    private static final String FIELD_CLOSE_PRC     = "closePrc";
    private static final String FIELD_LIMIT_UP      = "limmitUp";
    private static final String FIELD_LIMIT_DOWN    = "limmitDown";

    /**
     * Validates every company in the list.
     *
     * @param companies list of company maps as returned by GET_COMPS or
     *                  {@link org.example.utils.TestDataManager#loadCompaniesData()}
     * @return a checklist — one {@link CompanyValidationResult} per company
     * @throws IllegalArgumentException if the list is null or empty
     */
    public List<CompanyValidationResult> validate(List<Map<String, Object>> companies) {
        if (companies == null || companies.isEmpty()) {
            throw new IllegalArgumentException("companies list must not be null or empty");
        }

        List<CompanyValidationResult> results = new ArrayList<>(companies.size());

        for (Map<String, Object> company : companies) {
            CompanyValidationResult result = validateOne(company);
            results.add(result);
            logger.info("{}", result);
        }

        long validCount   = results.stream().filter(CompanyValidationResult::isValid).count();
        long invalidCount = results.size() - validCount;
        logger.info("Validation complete — {} VALID, {} INVALID out of {} companies",
                validCount, invalidCount, results.size());

        return results;
    }

    // ─── Internal ────────────────────────────────────────────────────────────

    private CompanyValidationResult validateOne(Map<String, Object> company) {
        String cmpArbName = extractString(company, FIELD_CMP_ARB_NAME);

        BigDecimal closePrc       = parseBigDecimal(company, FIELD_CLOSE_PRC,  cmpArbName);
        BigDecimal storedLimitUp  = parseBigDecimal(company, FIELD_LIMIT_UP,   cmpArbName);
        BigDecimal storedLimitDown= parseBigDecimal(company, FIELD_LIMIT_DOWN, cmpArbName);

        BigDecimal expectedLimitUp   = closePrc.multiply(FACTOR_UP)  .setScale(SCALE, ROUNDING);
        BigDecimal expectedLimitDown = closePrc.multiply(FACTOR_DOWN).setScale(SCALE, ROUNDING);

        // Round stored values to same scale before comparing
        BigDecimal roundedStoredUp   = storedLimitUp  .setScale(SCALE, ROUNDING);
        BigDecimal roundedStoredDown = storedLimitDown.setScale(SCALE, ROUNDING);

        boolean upMatch   = roundedStoredUp  .compareTo(expectedLimitUp)   == 0;
        boolean downMatch = roundedStoredDown.compareTo(expectedLimitDown) == 0;

        CompanyValidationResult.Status status = (upMatch && downMatch)
                ? CompanyValidationResult.Status.VALID
                : CompanyValidationResult.Status.INVALID;

        return new CompanyValidationResult(
                cmpArbName,
                closePrc.setScale(SCALE, ROUNDING),
                roundedStoredUp,
                roundedStoredDown,
                expectedLimitUp,
                expectedLimitDown,
                status
        );
    }

    private String extractString(Map<String, Object> company, String field) {
        Object value = company.get(field);
        return (value != null) ? String.valueOf(value).trim() : "";
    }

    private BigDecimal parseBigDecimal(Map<String, Object> company, String field, String cmpArbName) {
        String raw = extractString(company, field);
        if (raw.isEmpty()) {
            logger.warn("Missing or empty field '{}' for company '{}'", field, cmpArbName);
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(raw);
        } catch (NumberFormatException e) {
            logger.warn("Cannot parse '{}' as BigDecimal for field '{}', company '{}'",
                    raw, field, cmpArbName);
            return BigDecimal.ZERO;
        }
    }
}
