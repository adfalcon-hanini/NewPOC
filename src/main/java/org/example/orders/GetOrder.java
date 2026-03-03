package org.example.orders;

import io.restassured.response.Response;
import org.example.base.BaseApi;

import java.util.HashMap;
import java.util.Map;

public class GetOrder extends BaseApi {

    private static final String SERVICE_NAME = "GETORDERS";

    public Response getOrders(String searchWay, String nin, String bySlNo, String byDate,
                              String userId, String userType, String lang, String lstLogin) {
        logger.info("Executing GetOrders for NIN: {}, SearchWay: {}", nin, searchWay);

        Map<String, Object> getOrderRequest = new HashMap<>();
        getOrderRequest.put("searchWay", searchWay);
        getOrderRequest.put("nin", nin);
        getOrderRequest.put("bySlNO", bySlNo);
        getOrderRequest.put("byDate", byDate);
        getOrderRequest.put("userId", userId);
        getOrderRequest.put("userType", userType);
        getOrderRequest.put("lang", lang);

        Map<String, Object> message = new HashMap<>();
        message.put("GetOrderRequest", getOrderRequest);

        Map<String, Object> requestBody = buildRequestBody(SERVICE_NAME, message, lstLogin);
        requestBody.put("sessionId", getSessionId());

        Response response = sendPostRequest(requestBody);

        logger.info("Response JSON: {}", response.getBody().asPrettyString());

        return response;
    }

    // Search by Serial Number
    public Response getOrdersBySlNo(String nin, String slNo, String lstLogin) {
        return getOrders(
                "SLNO",
                nin,
                slNo,
                "",
                nin,
                config.getOrderUserType(),
                config.getTestLanguage(),
                lstLogin
        );
    }

    // Search by Date
    public Response getOrdersByDate(String nin, String date, String lstLogin) {
        return getOrders(
                "DATE",
                nin,
                "",
                date,
                nin,
                config.getOrderUserType(),
                config.getTestLanguage(),
                lstLogin
        );
    }

    // Convenience method using config values
    public Response getOrdersBySlNo(String nin, String slNo) {
        return getOrdersBySlNo(nin, slNo, config.getTestLstLogin());
    }

    public Response getOrdersByDate(String nin, String date) {
        return getOrdersByDate(nin, date, config.getTestLstLogin());
    }
}
