package org.example.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.utils.ConfigManager;

import java.util.HashMap;
import java.util.Map;

public class BaseApi {

    protected static final Logger logger = LogManager.getLogger(BaseApi.class);
    protected static final ConfigManager config = ConfigManager.getInstance();

    protected static final String BASE_URL = config.getBaseUrl();
    protected static final String VERSION = config.getVersion();
    protected static final String APP_ID = config.getAppId();

    // Session Management
    private static String sessionId;

    protected Response response;
    protected RequestSpecification request;

    public BaseApi() {
        RestAssured.baseURI = BASE_URL;
        request = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
    }

    protected Map<String, Object> buildRequestBody(String serviceName, Map<String, Object> message) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("Srv", serviceName);
        requestBody.put("Message", message);
        requestBody.put("Version", VERSION);
        requestBody.put("AppId", APP_ID);
        return requestBody;
    }

    protected Map<String, Object> buildRequestBody(String serviceName, Map<String, Object> message, String lstLogin) {
        Map<String, Object> requestBody = buildRequestBody(serviceName, message);
        requestBody.put("LstLogin", lstLogin);
        return requestBody;
    }

    protected Map<String, Object> buildRequestBodyWithSession(String serviceName, Map<String, Object> message) {
        Map<String, Object> requestBody = buildRequestBody(serviceName, message);
        if (sessionId != null && !sessionId.isEmpty()) {
            requestBody.put("SessionId", sessionId);
        }
        return requestBody;
    }

    // Session Management Methods
    public static void setSessionId(String session) {
        sessionId = session;
        logger.info("Session ID stored successfully");
    }

    public static String getSessionId() {
        return sessionId;
    }

    public static void clearSession() {
        sessionId = null;
        logger.info("Session cleared");
    }

    public static boolean isSessionActive() {
        return sessionId != null && !sessionId.isEmpty();
    }

    protected Response sendPostRequest(Map<String, Object> body) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(body);

        logger.info("Sending POST request to: {}", BASE_URL);
        System.out.println("==================== REQUEST ====================");
        System.out.println("URL: " + BASE_URL);
        System.out.println("Request Body:");
        System.out.println(prettyJson);
        System.out.println("=================================================");

        response = request.body(body).post();

        logger.info("Response Status Code: {}", response.getStatusCode());

        return response;
    }

    public Response getResponse() {
        return response;
    }

    public int getStatusCode() {
        return response.getStatusCode();
    }

    public String getResponseBody() {
        return response.getBody().asString();
    }

    public <T> T getResponseAs(Class<T> clazz) {
        return response.as(clazz);
    }
}
