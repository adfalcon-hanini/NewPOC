package org.example.Companies;

import io.restassured.response.Response;
import org.example.base.BaseApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetComps extends BaseApi {

    private static final String SERVICE_NAME = "GET_COMPS";

    // JSON path to the companies list in the GET_COMPS response
    private static final String COMPS_JSON_PATH = "message.cmpdetailsResponse.comps.comp";

    public Response getComps(String lstLogin) {
        logger.info("Executing GetComps");

        Map<String, Object> message = new HashMap<>();
        Map<String, Object> requestBody = buildRequestBody(SERVICE_NAME, message, lstLogin);
        Response response = sendPostRequest(requestBody);

        logger.info("Response JSON: {}", response.getBody().asPrettyString());

        return response;
    }

    public Response getComps() {
        return getComps(config.getTestLstLogin());
    }

    /**
     * Fetches the closing price (closePrc) for a given company code from the GET_COMPS API.
     *
     * @param compCode the company code to look up (e.g. "QNBK")
     * @return the closing price as a double
     * @throws IllegalArgumentException if compCode is null/empty or not found in the API response
     * @throws IllegalStateException    if the API call fails or closePrc is invalid
     */
    public double getClosePrcFromApi(String compCode) {
        if (compCode == null || compCode.isBlank()) {
            throw new IllegalArgumentException("compCode must not be null or empty");
        }

        Response response = getComps();

        if (response.getStatusCode() != 200) {
            throw new IllegalStateException("GET_COMPS API call failed with status: " + response.getStatusCode());
        }

        List<Map<String, Object>> companies = response.jsonPath().getList(COMPS_JSON_PATH);

        if (companies == null || companies.isEmpty()) {
            throw new IllegalStateException("GET_COMPS returned no companies");
        }

        for (Map<String, Object> company : companies) {
            if (compCode.equals(String.valueOf(company.get("compCode")))) {
                String rawPrice = String.valueOf(company.get("closePrc"));
                try {
                    double closePrc = Double.parseDouble(rawPrice);
                    logger.info("Retrieved closePrc={} for compCode={}", closePrc, compCode);
                    return closePrc;
                } catch (NumberFormatException e) {
                    throw new IllegalStateException(
                            "Invalid closePrc value '" + rawPrice + "' for compCode: " + compCode);
                }
            }
        }

        throw new IllegalArgumentException("Company not found in GET_COMPS response: " + compCode);
    }

    /**
     * Checks whether a given current price is within the allowed trading range (±10%)
     * for the specified company. The closing price is fetched live from the GET_COMPS API.
     *
     * <p>Formula:
     * <pre>
     *   limitUp   = closePrc * 1.10
     *   limitDown = closePrc * 0.90
     * </pre>
     *
     * @param compCode     the company code used to retrieve closePrc from the API (e.g. "QNBK")
     * @param currentPrice the price to validate against the computed range (must be >= 0)
     * @return true if currentPrice is within [limitDown, limitUp], false otherwise
     * @throws IllegalArgumentException if compCode is invalid or not found, or currentPrice is negative
     * @throws IllegalStateException    if the API call fails or returns bad data
     */
    public boolean checkLimit(String compCode, double currentPrice) {
        if (currentPrice < 0) {
            throw new IllegalArgumentException("currentPrice cannot be negative, got: " + currentPrice);
        }

        // Fetch closePrc live from the API — not passed as a parameter
        double closePrc = getClosePrcFromApi(compCode);

        double limitUp   = closePrc * 1.10;
        double limitDown = closePrc * 0.90;

        logger.info("CheckLimit → compCode={}, closePrc={}, limitDown={}, limitUp={}, currentPrice={}",
                compCode, closePrc, limitDown, limitUp, currentPrice);

        return currentPrice >= limitDown && currentPrice <= limitUp;
    }
}
