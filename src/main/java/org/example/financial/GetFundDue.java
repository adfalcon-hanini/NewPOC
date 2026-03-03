package org.example.financial;

import io.restassured.response.Response;
import org.example.base.BaseApi;

import java.util.HashMap;
import java.util.Map;

public class GetFundDue extends BaseApi {

    private static final String SERVICE_NAME = "GETFUNDDUE";

    public Response getFundDue(String lang, String userId, String userType, String lstLogin) {
        logger.info("Executing GetFundDue for UserId: {}", userId);

        Map<String, Object> paymentDueRequest = new HashMap<>();
        paymentDueRequest.put("Lang", lang);
        paymentDueRequest.put("UserId", userId);
        paymentDueRequest.put("UserType", userType);

        Map<String, Object> message = new HashMap<>();
        message.put("PaymentDueRequest", paymentDueRequest);

        Map<String, Object> requestBody = buildRequestBodyWithSession(SERVICE_NAME, message);
        requestBody.put("LstLogin", lstLogin);

        Response response = sendPostRequest(requestBody);

        logger.info("Response JSON: {}", response.getBody().asPrettyString());

        return response;
    }

    public Response getFundDue(String userId) {
        return getFundDue(config.getTestLanguage(), userId, config.getDefaultUserType(), config.getTestLstLogin());
    }
}
