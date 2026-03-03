package org.example.banking;

import io.restassured.response.Response;
import org.example.base.BaseApi;

import java.util.HashMap;
import java.util.Map;

public class RegBnkAccNew extends BaseApi {

    private static final String SERVICE_NAME = "REGBNKACCNEW";

    public Response registerBankAccount(String nin, String userId, String userType, String lang,
                                        String transactionId, String intBnkCustomerId, String status,
                                        String accountNo, String swiftAddress, String currencyCode,
                                        String accountIBAN, String accountSeq, String lstLogin) {
        logger.info("Executing RegBnkAccNew for NIN: {}", nin);

        Map<String, Object> registerRequest = new HashMap<>();
        registerRequest.put("NIN", nin);
        registerRequest.put("UserId", userId);
        registerRequest.put("userType", userType);
        registerRequest.put("lang", lang);
        registerRequest.put("transactionId", transactionId);
        registerRequest.put("IntBnkCustomerId", intBnkCustomerId);
        registerRequest.put("status", status);
        registerRequest.put("accountNo", accountNo);
        registerRequest.put("swiftAddress", swiftAddress);
        registerRequest.put("currencyCode", currencyCode);
        registerRequest.put("accountIBAN", accountIBAN);
        registerRequest.put("accountSeq", accountSeq);

        Map<String, Object> message = new HashMap<>();
        message.put("RegisterBankAccountRequest", registerRequest);

        Map<String, Object> requestBody = buildRequestBodyWithSession(SERVICE_NAME, message);
        requestBody.put("LstLogin", lstLogin);

        Response response = sendPostRequest(requestBody);

        logger.info("Response JSON: {}", response.getBody().asPrettyString());

        return response;
    }

    public Response registerBankAccount(String nin, String userId, String accountNo,
                                        String swiftAddress, String currencyCode, String accountIBAN) {
        return registerBankAccount(
                nin, userId, config.getBankingUserType(), config.getTestLanguage(),
                "", "", "",
                accountNo, swiftAddress, currencyCode, accountIBAN, "",
                config.getTestLstLogin()
        );
    }

    public Response registerBankAccount(String nin) {
        return registerBankAccount(
                nin, nin, config.getBankingUserType(), config.getTestLanguage(),
                "", "", "",
                config.getBankAccountNo(), config.getBankSwiftAddress(),
                config.getBankCurrencyCode(), config.getBankAccountIBAN(), "",
                config.getTestLstLogin()
        );
    }
}
