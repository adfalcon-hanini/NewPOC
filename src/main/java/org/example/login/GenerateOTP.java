package org.example.login;

import io.restassured.response.Response;
import org.example.base.BaseApi;

import java.util.HashMap;
import java.util.Map;

public class GenerateOTP extends BaseApi {

    private static final String SERVICE_NAME = "GENERATE_OTP";

    public enum OTPAction {
        MARGIN_ACTIVATION_OTP,
        MOBILE_NUMBER_OTP
    }

    public Response generateOTP(String nin, String clientIP, String mobileNumber, OTPAction action,
                                 String otp, String token, String qid, String lang) {
        logger.info("Executing GenerateOTP for NIN: {}, Action: {}", nin, action);

        Map<String, Object> generateOTPRequest = new HashMap<>();
        generateOTPRequest.put("NIN", nin);
        generateOTPRequest.put("ClientIP", clientIP);
        generateOTPRequest.put("MobileNumber", mobileNumber);
        generateOTPRequest.put("Action", action.name());
        generateOTPRequest.put("OTP", otp != null ? otp : "");
        generateOTPRequest.put("Token", token);
        generateOTPRequest.put("Qid", qid);

        Map<String, Object> message = new HashMap<>();
        message.put("GenerateOTPRequest", generateOTPRequest);

        Map<String, Object> requestBody = buildRequestBodyWithSession(SERVICE_NAME, message);
        requestBody.put("Lang", lang);
        requestBody.put("LstLogin", null);

        Response response = sendPostRequest(requestBody);

        logger.info("Response JSON: {}", response.getBody().asPrettyString());

        return response;
    }

    public Response generateOTP(String nin, String clientIP, OTPAction action) {
        return generateOTP(nin, clientIP, null, action, "", null, null, config.getTestLanguage());
    }

    public Response generateOTP(String nin, OTPAction action) {
        return generateOTP(nin, config.getProperty("client.ip", "0.0.0.0"), null, action, "", null, null, config.getTestLanguage());
    }

    public Response generateMobileNumberOTP(String nin, String clientIP) {
        return generateOTP(nin, clientIP, OTPAction.MOBILE_NUMBER_OTP);
    }

    public Response generateMarginActivationOTP(String nin, String clientIP) {
        return generateOTP(nin, clientIP, OTPAction.MARGIN_ACTIVATION_OTP);
    }
}
