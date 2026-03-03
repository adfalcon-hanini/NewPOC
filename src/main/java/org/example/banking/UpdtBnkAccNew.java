package org.example.banking;

import io.restassured.response.Response;
import org.example.base.BaseApi;

import java.util.HashMap;
import java.util.Map;

public class UpdtBnkAccNew extends BaseApi {

    private static final String SERVICE_NAME = "UPDTBNKACCNEW";

    public Response updateBankAccount(String currentAccountNo, String swiftAddress, String status,
                                      String currentCurrencyCode, String newAccountNo,
                                      String newCurrencyCode, String accountSeq, String nin,
                                      String accountIBAN, String userId, String userType,
                                      String lang, String clientUserIP, String lstLogin) {
        logger.info("Executing UpdtBnkAccNew for NIN: {}, CurrentAccount: {}", nin, currentAccountNo);

        Map<String, Object> updateRequest = new HashMap<>();
        updateRequest.put("CurrentAccountNo", currentAccountNo);
        updateRequest.put("SwiftAddress", swiftAddress);
        updateRequest.put("Status", status);
        updateRequest.put("CurrentCurrencyCode", currentCurrencyCode);
        updateRequest.put("NewAccountNo", newAccountNo);
        updateRequest.put("NewCurrencyCode", newCurrencyCode);
        updateRequest.put("AccountSeq", accountSeq);
        updateRequest.put("NIN", nin);
        updateRequest.put("AccountIBAN", accountIBAN);
        updateRequest.put("UserId", userId);
        updateRequest.put("UserType", userType);
        updateRequest.put("Lang", lang);
        updateRequest.put("ClientUserIP", clientUserIP);

        Map<String, Object> message = new HashMap<>();
        message.put("UpdateBankAccountRequest", updateRequest);

        Map<String, Object> requestBody = buildRequestBody(SERVICE_NAME, message, lstLogin);
        requestBody.put("SessionID", getSessionId());

        Response response = sendPostRequest(requestBody);

        logger.info("Response JSON: {}", response.getBody().asPrettyString());

        return response;
    }

    // Convenience method with common parameters
    public Response updateBankAccount(String currentAccountNo, String swiftAddress, String status,
                                      String currentCurrencyCode, String newAccountNo,
                                      String newCurrencyCode, String accountSeq, String nin,
                                      String accountIBAN, String lstLogin) {
        return updateBankAccount(
                currentAccountNo,
                swiftAddress,
                status,
                currentCurrencyCode,
                newAccountNo,
                newCurrencyCode,
                accountSeq,
                nin,
                accountIBAN,
                nin,
                "",
                config.getTestLanguage(),
                config.getClientUserIP(),
                lstLogin
        );
    }

    // Convenience method using config values
    public Response updateBankAccount(String currentAccountNo, String swiftAddress, String status,
                                      String currentCurrencyCode, String newAccountNo,
                                      String newCurrencyCode, String accountSeq, String nin,
                                      String accountIBAN) {
        return updateBankAccount(
                currentAccountNo,
                swiftAddress,
                status,
                currentCurrencyCode,
                newAccountNo,
                newCurrencyCode,
                accountSeq,
                nin,
                accountIBAN,
                config.getTestLstLogin()
        );
    }

    // Update account status only
    public Response updateAccountStatus(String currentAccountNo, String swiftAddress,
                                        String status, String accountSeq, String nin) {
        return updateBankAccount(
                currentAccountNo,
                swiftAddress,
                status,
                "",
                "",
                "",
                accountSeq,
                nin,
                ""
        );
    }

    // Update account number (IBAN)
    public Response updateAccountIBAN(String currentAccountNo, String swiftAddress,
                                      String newAccountNo, String accountIBAN,
                                      String accountSeq, String nin) {
        return updateBankAccount(
                currentAccountNo,
                swiftAddress,
                "",
                "",
                newAccountNo,
                "",
                accountSeq,
                nin,
                accountIBAN
        );
    }
}
