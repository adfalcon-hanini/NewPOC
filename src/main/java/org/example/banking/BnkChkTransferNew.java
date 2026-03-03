package org.example.banking;

import io.restassured.response.Response;
import org.example.base.BaseApi;

import java.util.HashMap;
import java.util.Map;

public class BnkChkTransferNew extends BaseApi {

    private static final String SERVICE_NAME = "BNKCHKTRANSFERNEW";

    public Response checkBankTransfer(String swiftCode, String accountNo, String amount,
                                      String nin, String userId, String userType,
                                      String lang, String lstLogin) {
        logger.info("Executing BnkChkTransferNew for NIN: {}, Account: {}, Amount: {}", nin, accountNo, amount);

        Map<String, Object> transferRequest = new HashMap<>();
        transferRequest.put("SwiftCode", swiftCode);
        transferRequest.put("AccountNO", accountNo);
        transferRequest.put("Amount", amount);
        transferRequest.put("NIN", nin);
        transferRequest.put("UserId", userId);
        transferRequest.put("UserType", userType);
        transferRequest.put("Lang", lang);

        Map<String, Object> message = new HashMap<>();
        message.put("IsBankTransferRequest", transferRequest);

        Map<String, Object> requestBody = buildRequestBody(SERVICE_NAME, message, lstLogin);
        requestBody.put("SessionID", getSessionId());

        Response response = sendPostRequest(requestBody);

        logger.info("Response JSON: {}", response.getBody().asPrettyString());

        return response;
    }

    // Convenience method with defaults
    public Response checkBankTransfer(String swiftCode, String accountNo, String amount,
                                      String nin, String lstLogin) {
        return checkBankTransfer(
                swiftCode,
                accountNo,
                amount,
                nin,
                nin,
                config.getBankingUserType(),
                config.getTestLanguage(),
                lstLogin
        );
    }

    // Convenience method using config values
    public Response checkBankTransfer(String swiftCode, String accountNo, String amount, String nin) {
        return checkBankTransfer(swiftCode, accountNo, amount, nin, config.getTestLstLogin());
    }

    // Convenience method with default QIIB swift code
    public Response checkQiibTransfer(String accountNo, String amount, String nin) {
        return checkBankTransfer(
                config.getBankingSwiftCode(),
                accountNo,
                amount,
                nin
        );
    }
}
