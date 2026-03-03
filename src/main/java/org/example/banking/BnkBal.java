package org.example.banking;

import io.restassured.response.Response;
import org.example.base.BaseApi;

import java.util.HashMap;
import java.util.Map;

public class BnkBal extends BaseApi {

    private static final String SERVICE_NAME = "BNKBAL";

    public Response getBankBalance(String nin, String userId, String userType, String accRegisterType, String lang, String lstLogin) {
        logger.info("Executing BnkBal for NIN: {}", nin);

        Map<String, Object> balDlsRequest = new HashMap<>();
        balDlsRequest.put("NIN", nin);
        balDlsRequest.put("UserId", userId);
        balDlsRequest.put("UserType", userType);
        balDlsRequest.put("AccRegisterType", accRegisterType);
        balDlsRequest.put("Lang", lang);

        Map<String, Object> message = new HashMap<>();
        message.put("BalDlsRequest", balDlsRequest);

        Map<String, Object> requestBody = buildRequestBodyWithSession(SERVICE_NAME, message);
        requestBody.put("LstLogin", lstLogin);

        Response response = sendPostRequest(requestBody);

        logger.info("Response JSON: {}", response.getBody().asPrettyString());

        return response;
    }

    public Response getBankBalance(String nin, String userId, String lang) {
        return getBankBalance(nin, userId, config.getBankingUserType(), config.getBankingAccRegisterType(), lang, config.getTestLstLogin());
    }

    public Response getBankBalance(String nin) {
        return getBankBalance(nin, nin, config.getBankingUserType(), config.getBankingAccRegisterType(), config.getTestLanguage(), config.getTestLstLogin());
    }
}
