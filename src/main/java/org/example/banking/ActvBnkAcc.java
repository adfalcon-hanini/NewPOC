package org.example.banking;

import io.restassured.response.Response;
import org.example.base.BaseApi;

import java.util.HashMap;
import java.util.Map;

public class ActvBnkAcc extends BaseApi {

    private static final String SERVICE_NAME = "ACTVBNKACC";

    public Response activateBankAccount(String nin, String userId, String userType, String lang,
                                        String transactionId, String intBnkCustomerId, String status,
                                        String accountNo, String swiftAddress, String currencyCode,
                                        String accountIBAN, String accountSeq, String lstLogin) {
        logger.info("Executing ActvBnkAcc for NIN: {}", nin);

        Map<String, Object> activateRequest = new HashMap<>();
        activateRequest.put("NIN", nin);
        activateRequest.put("UserId", userId);
        activateRequest.put("userType", userType);
        activateRequest.put("lang", lang);
        activateRequest.put("transactionId", transactionId);
        activateRequest.put("IntBnkCustomerId", intBnkCustomerId);
        activateRequest.put("status", status);
        activateRequest.put("accountNo", accountNo);
        activateRequest.put("swiftAddress", swiftAddress);
        activateRequest.put("currencyCode", currencyCode);
        activateRequest.put("accountIBAN", accountIBAN);
        activateRequest.put("accountSeq", accountSeq);

        Map<String, Object> message = new HashMap<>();
        message.put("ActivateBankAccountRequest", activateRequest);

        Map<String, Object> requestBody = buildRequestBodyWithSession(SERVICE_NAME, message);
        requestBody.put("LstLogin", lstLogin);

        Response response = sendPostRequest(requestBody);

        logger.info("Response JSON: {}", response.getBody().asPrettyString());

        return response;
    }

    public Response activateBankAccount(String nin, String accountNo, String swiftAddress,
                                        String currencyCode, String accountIBAN, String accountSeq) {
        return activateBankAccount(
                nin, nin, config.getBankingUserType(), config.getTestLanguage(),
                "", "", "",
                accountNo, swiftAddress, currencyCode, accountIBAN, accountSeq,
                config.getTestLstLogin()
        );
    }

    public Response activateBankAccount(String nin, String accountSeq) {
        return activateBankAccount(
                nin, nin, config.getBankingUserType(), config.getTestLanguage(),
                "", "", "",
                config.getBankAccountNo(), config.getBankSwiftAddress(),
                config.getBankCurrencyCode(), config.getBankAccountIBAN(), accountSeq,
                config.getTestLstLogin()
        );
    }
}
