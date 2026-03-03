package org.example.banking;

import io.restassured.response.Response;
import org.example.base.BaseApi;

import java.util.HashMap;
import java.util.Map;

public class BnkTransferNew extends BaseApi {

    private static final String SERVICE_NAME = "BNKTRANSFERNEW";

    public Response bankTransfer(String swiftCode, String accountNo, String accountStatus,
                                 String transferType, String amount, String nin,
                                 String userId, String userType, String transferTimeId,
                                 String lstLogin) {
        logger.info("Executing BnkTransferNew for NIN: {}, Account: {}, Amount: {}, Type: {}",
                nin, accountNo, amount, transferType);

        Map<String, Object> transferRequest = new HashMap<>();
        transferRequest.put("SwiftCode", swiftCode);
        transferRequest.put("AccountNO", accountNo);
        transferRequest.put("AccountStatus", accountStatus);
        transferRequest.put("TransferType", transferType);
        transferRequest.put("Amount", amount);
        transferRequest.put("NIN", nin);
        transferRequest.put("UserId", userId);
        transferRequest.put("UserType", userType);
        transferRequest.put("TransferTimeId", transferTimeId);

        Map<String, Object> message = new HashMap<>();
        message.put("BankTransferRequest", transferRequest);

        Map<String, Object> requestBody = buildRequestBody(SERVICE_NAME, message, lstLogin);
        requestBody.put("SessionID", getSessionId());

        Response response = sendPostRequest(requestBody);

        logger.info("Response JSON: {}", response.getBody().asPrettyString());

        return response;
    }

    // Convenience method with defaults
    public Response bankTransfer(String swiftCode, String accountNo, String accountStatus,
                                 String transferType, String amount, String transferTimeId,
                                 String nin, String lstLogin) {
        return bankTransfer(
                swiftCode,
                accountNo,
                accountStatus,
                transferType,
                amount,
                nin,
                nin,
                config.getBankingUserType(),
                transferTimeId,
                lstLogin
        );
    }

    // Convenience method using config values
    public Response bankTransfer(String swiftCode, String accountNo, String accountStatus,
                                 String transferType, String amount, String transferTimeId,
                                 String nin) {
        return bankTransfer(
                swiftCode,
                accountNo,
                accountStatus,
                transferType,
                amount,
                transferTimeId,
                nin,
                config.getTestLstLogin()
        );
    }

    // Transfer FROM bank account
    public Response transferFromBank(String swiftCode, String accountNo, String accountStatus,
                                     String amount, String transferTimeId, String nin) {
        return bankTransfer(
                swiftCode,
                accountNo,
                accountStatus,
                "FROM",
                amount,
                transferTimeId,
                nin
        );
    }

    // Transfer TO bank account
    public Response transferToBank(String swiftCode, String accountNo, String accountStatus,
                                   String amount, String transferTimeId, String nin) {
        return bankTransfer(
                swiftCode,
                accountNo,
                accountStatus,
                "TO",
                amount,
                transferTimeId,
                nin
        );
    }
}
