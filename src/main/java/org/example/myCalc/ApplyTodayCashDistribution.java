package org.example.myCalc;

import io.restassured.response.Response;
import org.example.base.BaseApi;

import java.util.HashMap;
import java.util.Map;

public class ApplyTodayCashDistribution extends BaseApi {

    private static final String SERVICE_NAME = "APPLY_TODAY_CASH_DISTRIBUTION";

    public Response applyTodayCashDistribution(String userId, boolean withCashDistribution) {
        logger.info("Executing Apply Today Cash Distribution for UserId: {}, WithCashDistribution: {}",
                userId, withCashDistribution);

        Map<String, Object> request = new HashMap<>();
        request.put("UserId", userId);
        request.put("WithCashDistribution", withCashDistribution);

        Map<String, Object> message = new HashMap<>();
        message.put("Request", request);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("Srv", SERVICE_NAME);
        requestBody.put("Message", message);
        requestBody.put("SessionID", getSessionId());

        Response response = sendPostRequest(requestBody);

        logger.info("Response JSON: {}", response.getBody().asPrettyString());

        return response;
    }

    // Convenience method with default withCashDistribution = true
    public Response applyTodayCashDistribution(String userId) {
        return applyTodayCashDistribution(userId, true);
    }
}
