package org.example.login;

import io.restassured.response.Response;
import org.example.base.BaseApi;

import java.util.HashMap;
import java.util.Map;

public class Login extends BaseApi {

    private static final String SERVICE_NAME = "Login";

    public Response login(String userName, String password, String lang, String lstLogin) {
        logger.info("Executing Login for user: {}", userName);

        Map<String, Object> loginData = new HashMap<>();
        loginData.put("UserName", userName);
        loginData.put("Password", password);
        loginData.put("Lang", lang);

        Map<String, Object> message = new HashMap<>();
        message.put("Login", loginData);

        Map<String, Object> requestBody = buildRequestBody(SERVICE_NAME, message, lstLogin);

        Response response = sendPostRequest(requestBody);

        // Print response as JSON
        logger.info("Response JSON: {}", response.getBody().asPrettyString());

        // Extract and store sessionId if login successful
        if (response.getStatusCode() == 200) {
            String resCode = response.jsonPath().getString("responseStatus.resCode");
            if ("0".equals(resCode)) {
                String sessionId = response.jsonPath().getString("message.logResponse.sessionID");
                if (sessionId != null && !sessionId.isEmpty()) {
                    setSessionId(sessionId);
                    logger.info("Login successful, sessionId stored");
                }
            }
        }

        return response;
    }
}
