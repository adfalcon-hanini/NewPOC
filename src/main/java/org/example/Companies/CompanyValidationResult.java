package org.example.Companies;

import java.math.BigDecimal;

/**
 * Immutable result of a single company's limit validation.
 */
public class CompanyValidationResult {

    public enum Status { VALID, INVALID }

    private final String       cmpArbName;
    private final BigDecimal   closePrc;
    private final BigDecimal   storedLimitUp;
    private final BigDecimal   storedLimitDown;
    private final BigDecimal   expectedLimitUp;
    private final BigDecimal   expectedLimitDown;
    private final Status       status;

    CompanyValidationResult(String cmpArbName,
                            BigDecimal closePrc,
                            BigDecimal storedLimitUp,
                            BigDecimal storedLimitDown,
                            BigDecimal expectedLimitUp,
                            BigDecimal expectedLimitDown,
                            Status status) {
        this.cmpArbName        = cmpArbName;
        this.closePrc          = closePrc;
        this.storedLimitUp     = storedLimitUp;
        this.storedLimitDown   = storedLimitDown;
        this.expectedLimitUp   = expectedLimitUp;
        this.expectedLimitDown = expectedLimitDown;
        this.status            = status;
    }

    public String       getCmpArbName()        { return cmpArbName; }
    public BigDecimal   getClosePrc()          { return closePrc; }
    public BigDecimal   getStoredLimitUp()     { return storedLimitUp; }
    public BigDecimal   getStoredLimitDown()   { return storedLimitDown; }
    public BigDecimal   getExpectedLimitUp()   { return expectedLimitUp; }
    public BigDecimal   getExpectedLimitDown() { return expectedLimitDown; }
    public Status       getStatus()            { return status; }
    public boolean      isValid()              { return status == Status.VALID; }

    @Override
    public String toString() {
        return String.format(
            "%-40s | closePrc=%-8s | storedUp=%-8s | storedDown=%-8s | expectedUp=%-8s | expectedDown=%-8s | %s",
            cmpArbName, closePrc, storedLimitUp, storedLimitDown,
            expectedLimitUp, expectedLimitDown, status
        );
    }
}
