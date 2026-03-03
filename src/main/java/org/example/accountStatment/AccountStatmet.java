package org.example.accountStatment;

import io.restassured.response.Response;
import org.example.base.BaseApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AccountStatmet extends BaseApi {

    private static final String SERVICE_NAME = "ACCOUNT_STATEMENT";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public enum StatementType {
        COMPACT, TRADE, MARGIN, CASH
    }

    public Response getAccountStatement(String nin, String dateFrom, String dateTo,
                                         StatementType type, String lang, String lstLogin) {
        logger.info("Executing AccountStatement for NIN: {}, Type: {}, FromDate: {}, ToDate: {}",
                    nin, type, dateFrom, dateTo);

        Map<String, Object> accStateRequest = new HashMap<>();
        accStateRequest.put("NIN", nin);
        accStateRequest.put("dateFrom", dateFrom);
        accStateRequest.put("dateTo", dateTo);
        accStateRequest.put("type", type.name());

        Map<String, Object> message = new HashMap<>();
        message.put("AccStateRequest", accStateRequest);

        Map<String, Object> requestBody = buildRequestBodyWithSession(SERVICE_NAME, message);
        requestBody.put("Lang", lang);
        requestBody.put("LstLogin", lstLogin);
        requestBody.put("RandomToken", generateRandomToken());

        Response response = sendPostRequest(requestBody);

        logger.info("Response JSON: {}", response.getBody().asPrettyString());

        return response;
    }

    public Response getAccountStatement(String nin, String dateFrom, String dateTo, StatementType type) {
        return getAccountStatement(nin, dateFrom, dateTo, type,
                config.getTestLanguage(), config.getTestLstLogin());
    }

    public Response getAccountStatement(String nin, StatementType type) {
        String dateTo = LocalDate.now().format(DATE_FORMATTER);
        String dateFrom = LocalDate.now().minusDays(1).format(DATE_FORMATTER);
        return getAccountStatement(nin, dateFrom, dateTo, type);
    }

    public Response getAccountStatement(String nin) {
        return getAccountStatement(nin, StatementType.MARGIN);
    }

    private String generateRandomToken() {
        Random random = new Random();
        return String.valueOf(1000000000L + (long)(random.nextDouble() * 9000000000L));
    }
}
