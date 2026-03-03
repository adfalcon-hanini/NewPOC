package org.example.banking;

import io.restassured.response.Response;
import org.example.base.BaseApi;

import java.util.HashMap;
import java.util.Map;

public class RmvBnkAcc extends BaseApi {

    private static final String SERVICE_NAME = "RMVBNKACC";

    public Response removeBankAccount(String nin, String userId, String userType, String lang,
                                      String status, String accountNo, String swiftAddress,
                                      int currencyCode, String accountSeq, String lstLogin) {
        logger.info("Executing RmvBnkAcc for NIN: {}", nin);

        Map<String, Object> removeRequest = new HashMap<>();
        removeRequest.put("NIN", nin);
        removeRequest.put("UserId", userId);
        removeRequest.put("userType", userType);
        removeRequest.put("lang", lang);
        removeRequest.put("status", status);
        removeRequest.put("accountNo", accountNo);
        removeRequest.put("swiftAddress", swiftAddress);
        removeRequest.put("currencyCode", currencyCode);
        removeRequest.put("accountSeq", accountSeq);

        Map<String, Object> message = new HashMap<>();
        message.put("RemoveBankAccountRequest", removeRequest);

        Map<String, Object> requestBody = buildRequestBodyWithSession(SERVICE_NAME, message);
        requestBody.put("LstLogin", lstLogin);

        Response response = sendPostRequest(requestBody);

        logger.info("Response JSON: {}", response.getBody().asPrettyString());

        return response;
    }

    public Response removeBankAccount(String nin, String accountNo, String swiftAddress,
                                      int currencyCode, String accountSeq) {
        return removeBankAccount(
                nin, nin, config.getBankingUserType(), config.getTestLanguage(),
                config.getBankingRemoveStatus(), accountNo, swiftAddress, currencyCode, accountSeq,
                config.getTestLstLogin()
        );
    }

    public Response removeBankAccount(String nin, String accountSeq) {
        return removeBankAccount(
                nin, nin, config.getBankingUserType(), config.getTestLanguage(),
                config.getBankingRemoveStatus(), accountSeq, config.getBankSwiftAddress(),
                Integer.parseInt(config.getBankCurrencyCode()), accountSeq,
                config.getTestLstLogin()
        );
    }
}
