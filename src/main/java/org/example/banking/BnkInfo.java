package org.example.banking;

import io.restassured.response.Response;
import org.example.base.BaseApi;

import java.util.HashMap;
import java.util.Map;

public class BnkInfo extends BaseApi {

    private static final String SERVICE_NAME = "BNKINFO";

    public Response getBankInfo(String nin, String userId, String userType, String lang, String lstLogin) {
        logger.info("Executing BnkInfo for NIN: {}", nin);

        Map<String, Object> banksInfoRequest = new HashMap<>();
        banksInfoRequest.put("NIN", nin);
        banksInfoRequest.put("UserId", userId);
        banksInfoRequest.put("UserType", userType);
        banksInfoRequest.put("Lang", lang);

        Map<String, Object> message = new HashMap<>();
        message.put("BanksInformationRequest", banksInfoRequest);

        Map<String, Object> requestBody = buildRequestBodyWithSession(SERVICE_NAME, message);
        requestBody.put("LstLogin", lstLogin);

        Response response = sendPostRequest(requestBody);

        logger.info("Response JSON: {}", response.getBody().asPrettyString());

        return response;
    }

    public Response getBankInfo(String nin, String userId, String lang) {
        return getBankInfo(nin, userId, config.getBankingUserType(), lang, config.getTestLstLogin());
    }

    public Response getBankInfo(String nin) {
        return getBankInfo(nin, nin, config.getBankingUserType(), config.getTestLanguage(), config.getTestLstLogin());
    }
}
