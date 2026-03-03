package org.example.customer;

import io.restassured.response.Response;
import org.example.base.BaseApi;

import java.util.HashMap;
import java.util.Map;

public class CustomerAssets extends BaseApi {

    private static final String SERVICE_NAME = "CustomerAssets";

    public Response getCustomerAssets(String nin, String userId, String userType, String lang, String isAddBuyBal, String lstLogin) {
        logger.info("Executing CustomerAssets for NIN: {}", nin);

        Map<String, Object> custReq = new HashMap<>();
        custReq.put("NIN", nin);
        custReq.put("UserId", userId);
        custReq.put("UserType", userType);
        custReq.put("Lang", lang);
        custReq.put("IsAddBuyBal", isAddBuyBal);

        Map<String, Object> message = new HashMap<>();
        message.put("CustReq", custReq);

        Map<String, Object> requestBody = buildRequestBodyWithSession(SERVICE_NAME, message);
        requestBody.put("LstLogin", lstLogin);

        Response response = sendPostRequest(requestBody);

        logger.info("Response JSON: {}", response.getBody().asPrettyString());

        return response;
    }

    public Response getCustomerAssets(String nin, String userId, String lang) {
        return getCustomerAssets(nin, userId, config.getDefaultUserType(), lang, config.getProperty("customer.isAddBuyBal", "F"), config.getTestLstLogin());
    }

    public Response getCustomerAssets(String nin) {
        return getCustomerAssets(nin, nin, config.getDefaultUserType(), config.getTestLanguage(), config.getProperty("customer.isAddBuyBal", "F"), config.getTestLstLogin());
    }
}
