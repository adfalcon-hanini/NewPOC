package org.example.Companies;

import io.restassured.response.Response;
import org.example.base.BaseApi;

import java.util.HashMap;
import java.util.Map;

public class GetComps extends BaseApi {

    private static final String SERVICE_NAME = "GET_COMPS";

    public Response getComps(String lstLogin) {
        logger.info("Executing GetComps");

        Map<String, Object> message = new HashMap<>();

        Map<String, Object> requestBody = buildRequestBody(SERVICE_NAME, message, lstLogin);

        Response response = sendPostRequest(requestBody);

        logger.info("Response JSON: {}", response.getBody().asPrettyString());

        return response;
    }

    public Response getComps() {
        return getComps(config.getTestLstLogin());
    }
}
