package org.example.orders;

import io.restassured.response.Response;
import org.example.base.BaseApi;

import java.util.HashMap;
import java.util.Map;

public class CancelOrder extends BaseApi {

    private static final String SERVICE_NAME = "CANCEL_ORDER";

    public Response cancelOrder(String nin, String slno, String userId, String userType,
                                 String lang, String lstLogin) {
        logger.info("Executing Cancel Order - NIN: {}, SLNO: {}", nin, slno);

        Map<String, Object> canOrderRequest = new HashMap<>();
        canOrderRequest.put("nin", nin);
        canOrderRequest.put("slno", slno);
        canOrderRequest.put("userId", userId);
        canOrderRequest.put("userType", userType);
        canOrderRequest.put("lang", lang);

        Map<String, Object> message = new HashMap<>();
        message.put("CanOrderRequest", canOrderRequest);

        Map<String, Object> requestBody = buildRequestBody(SERVICE_NAME, message, lstLogin);
        requestBody.put("sessionId", getSessionId());

        Response response = sendPostRequest(requestBody);

        logger.info("Response JSON: {}", response.getBody().asPrettyString());

        return response;
    }

    // Convenience method with config defaults
    public Response cancelOrder(String nin, String slno, String lstLogin) {
        return cancelOrder(
                nin,
                slno,
                nin,
                config.getOrderUserType(),
                config.getTestLanguage(),
                lstLogin
        );
    }

    // Convenience method using saved order data
    public Response cancelOrder(String lstLogin) {
        String slno = org.example.utils.TestDataManager.getOrderSlNo();
        String nin = config.getTestNin();
        return cancelOrder(nin, slno, lstLogin);
    }
}
