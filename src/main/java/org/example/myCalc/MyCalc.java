package org.example.myCalc;

import io.restassured.response.Response;
import org.example.base.BaseApi;

import java.util.HashMap;
import java.util.Map;

public class MyCalc extends BaseApi {

    private static final String SERVICE_NAME = "MY_CALC";

    // Period enum for configurable values
    public enum Period {
        ALL("ALL"),
        DAY("DAY"),
        WEEK("WEEK"),
        MONTH("MONTH"),
        YEAR("YEAR");

        private final String value;

        Period(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Period fromString(String text) {
            for (Period p : Period.values()) {
                if (p.value.equalsIgnoreCase(text)) {
                    return p;
                }
            }
            return ALL; // Default to ALL if not found
        }
    }

    public Period getConfigPeriod() {
        return Period.fromString(config.getMyCalcPeriod());
    }

    public Response getMyCalc(String nin, String dateFrom, String dateTo, Period period,
                              String lang, String lstLogin) {
        logger.info("Executing MyCalc for NIN: {} with period: {}", nin, period.getValue());

        Map<String, Object> myCalcRequest = new HashMap<>();
        myCalcRequest.put("NIN", nin);
        myCalcRequest.put("dateFrom", dateFrom);
        myCalcRequest.put("dateTo", dateTo);
        myCalcRequest.put("period", period.getValue());

        Map<String, Object> message = new HashMap<>();
        message.put("MyCalcRequest", myCalcRequest);

        Map<String, Object> requestBody = buildRequestBodyWithSession(SERVICE_NAME, message);
        requestBody.put("Lang", lang);
        requestBody.put("LstLogin", lstLogin);

        Response response = sendPostRequest(requestBody);

        logger.info("Response JSON: {}", response.getBody().asPrettyString());

        return response;
    }

    public Response getMyCalc(String nin, String dateFrom, String dateTo, Period period) {
        return getMyCalc(nin, dateFrom, dateTo, period,
                config.getTestLanguage(), config.getTestLstLogin());
    }

    public Response getMyCalc(String nin, Period period) {
        // Default to today's date for both dateFrom and dateTo
        String today = java.time.LocalDate.now().toString();
        return getMyCalc(nin, today, today, period,
                config.getTestLanguage(), config.getTestLstLogin());
    }

    public Response getMyCalc(String nin, String dateFrom, String dateTo) {
        // Use period from config
        return getMyCalc(nin, dateFrom, dateTo, getConfigPeriod(),
                config.getTestLanguage(), config.getTestLstLogin());
    }

    public Response getMyCalc(String nin) {
        // Use period from config and today's date
        String today = java.time.LocalDate.now().toString();
        return getMyCalc(nin, today, today, getConfigPeriod(),
                config.getTestLanguage(), config.getTestLstLogin());
    }
}
