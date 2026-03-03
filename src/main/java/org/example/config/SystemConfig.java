package org.example.config;

import io.restassured.response.Response;
import org.example.base.BaseApi;

import java.util.HashMap;
import java.util.Map;

public class SystemConfig extends BaseApi {

    private static final String SERVICE_NAME = "SYSTEM_CONFIG";

    public Response getSystemConfig(String nin, String paramName) {
        logger.info("Executing SystemConfig with paramName: {}", paramName);

        Map<String, Object> paramReq = new HashMap<>();
        paramReq.put("nin", nin);
        paramReq.put("paramName", paramName);

        Map<String, Object> message = new HashMap<>();
        message.put("paramReq", paramReq);

        Map<String, Object> requestBody = buildRequestBody(SERVICE_NAME, message);

        Response response = sendPostRequest(requestBody);

        logger.info("Response JSON: {}", response.getBody().asPrettyString());

        return response;
    }

    public Response getSystemConfig(String paramName) {
        return getSystemConfig("", paramName);
    }
}
