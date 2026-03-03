package org.example.login;

import io.restassured.response.Response;
import org.example.base.BaseApi;
import org.example.utils.ConfigManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class GenerateOTPTest {

    private ConfigManager config = ConfigManager.getInstance();

    @BeforeClass
    public void setup() {
        Login login = new Login();
        Response loginResponse = login.login(
                config.getTestNin(),
                config.getTestPassword(),
                config.getTestLanguage(),
                config.getTestLstLogin()
        );

        Assert.assertEquals(loginResponse.getStatusCode(), 200, "Login failed");
        Assert.assertTrue(BaseApi.isSessionActive(), "Session ID not stored after login");

        System.out.println("Login successful, Session ID: " + BaseApi.getSessionId());
    }

    @Test
    public void testGenerateMobileNumberOTP() {
        GenerateOTP generateOTP = new GenerateOTP();

        String nin = config.getTestNin();
        String clientIP = config.getProperty("client.ip", "212.70.111.16");

        Response response = generateOTP.generateMobileNumberOTP(nin, clientIP);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());
    }

    @Test
    public void testGenerateMarginActivationOTP() {
        GenerateOTP generateOTP = new GenerateOTP();

        String nin = config.getTestNin();
        String clientIP = config.getProperty("client.ip", "212.70.111.16");

        Response response = generateOTP.generateMarginActivationOTP(nin, clientIP);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());
    }

    @Test
    public void testGenerateOTPWithAction() {
        GenerateOTP generateOTP = new GenerateOTP();

        String nin = config.getTestNin();
        String clientIP = config.getProperty("client.ip", "212.70.111.16");

        Response response = generateOTP.generateOTP(nin, clientIP, GenerateOTP.OTPAction.MOBILE_NUMBER_OTP);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());
    }
}
