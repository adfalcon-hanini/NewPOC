package org.example.banking;

import io.restassured.response.Response;
import org.example.base.BaseApi;

import java.util.HashMap;
import java.util.Map;

public class ClntBnkAccsNew extends BaseApi {

    private static final String SERVICE_NAME = "CLNTBNKACCSNEW";

    public Response getClientBankAccounts(String nin, String userId, String userType,
                                          String lang, String accRegisterType, String lstLogin) {
        logger.info("Executing ClntBnkAccsNew for NIN: {}", nin);

        Map<String, Object> accountsRequest = new HashMap<>();
        accountsRequest.put("NIN", nin);
        accountsRequest.put("UserId", userId);
        accountsRequest.put("UserType", userType);
        accountsRequest.put("Lang", lang);
        accountsRequest.put("AccRegisterType", accRegisterType);

        Map<String, Object> message = new HashMap<>();
        message.put("BanksAccountsRequest", accountsRequest);

        Map<String, Object> requestBody = buildRequestBody(SERVICE_NAME, message, lstLogin);
        requestBody.put("SessionID", getSessionId());

        Response response = sendPostRequest(requestBody);

        logger.info("Response JSON: {}", response.getBody().asPrettyString());

        return response;
    }

    // Convenience method with defaults
    public Response getClientBankAccounts(String nin, String lstLogin) {
        return getClientBankAccounts(
                nin,
                nin,
                "",
                config.getTestLanguage(),
                "",
                lstLogin
        );
    }

    // Convenience method using config values
    public Response getClientBankAccounts(String nin) {
        return getClientBankAccounts(nin, config.getTestLstLogin());
    }

    // Get accounts with specific register type
    public Response getClientBankAccountsByType(String nin, String accRegisterType) {
        return getClientBankAccounts(
                nin,
                nin,
                "",
                config.getTestLanguage(),
                accRegisterType,
                config.getTestLstLogin()
        );
    }
}
