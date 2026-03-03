package org.example.login;

import io.restassured.response.Response;
import org.example.base.BaseApi;

import java.util.HashMap;
import java.util.Map;

public class ChgPass extends BaseApi {

    private static final String SERVICE_NAME = "CHGPASS";

    public Response changePassword(String oldPass, String newPass, String userName, String userType,
                                   String lang, String os, String browser, String appName,
                                   String clientUserIP, String lstLogin) {
        logger.info("Executing ChgPass for UserName: {}", userName);

        Map<String, Object> chgPassRequest = new HashMap<>();
        chgPassRequest.put("OldPass", oldPass);
        chgPassRequest.put("NewPass", newPass);
        chgPassRequest.put("UserName", userName);
        chgPassRequest.put("UserType", userType);
        chgPassRequest.put("Lang", lang);
        chgPassRequest.put("OS", os);
        chgPassRequest.put("Browser", browser);
        chgPassRequest.put("AppName", appName);
        chgPassRequest.put("ClientUserIP", clientUserIP);

        Map<String, Object> message = new HashMap<>();
        message.put("ChgPassRequest", chgPassRequest);

        Map<String, Object> requestBody = buildRequestBodyWithSession(SERVICE_NAME, message);
        requestBody.put("LstLogin", lstLogin);

        Response response = sendPostRequest(requestBody);

        logger.info("Response JSON: {}", response.getBody().asPrettyString());

        return response;
    }

    public Response changePassword(String oldPass, String newPass, String userName) {
        return changePassword(
                oldPass, newPass, userName, config.getDefaultUserType(),
                config.getTestLanguage(), config.getProperty("client.os", "Windows"), config.getProperty("client.browser", "Chrome"),
                config.getAppId(), config.getClientUserIP(),
                config.getTestLstLogin()
        );
    }

    public Response changePassword(String oldPass, String newPass) {
        return changePassword(
                oldPass, newPass, config.getTestNin(), config.getDefaultUserType(),
                config.getTestLanguage(), config.getProperty("client.os", "Windows"), config.getProperty("client.browser", "Chrome"),
                config.getAppId(), config.getClientUserIP(),
                config.getTestLstLogin()
        );
    }
}
