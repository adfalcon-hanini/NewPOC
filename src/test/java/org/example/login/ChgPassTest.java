package org.example.login;

import io.restassured.response.Response;
import org.example.base.BaseApi;
import org.example.utils.ConfigManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ChgPassTest {

    private ConfigManager config = ConfigManager.getInstance();

    @BeforeClass
    public void setup() {
        // Login first to get sessionId
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
    public void testChangePassword() {
        ChgPass chgPass = new ChgPass();

        String oldPass = config.getTestPassword();
        String newPass = config.getTestPassword(); // Using same password for test

        Response response = chgPass.changePassword(oldPass, newPass);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        // Assert response code is 200
        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());
    }

    @Test
    public void testChangePasswordWithCustomUser() {
        ChgPass chgPass = new ChgPass();

        String oldPass = config.getTestPassword();
        String newPass = config.getTestPassword();
        String userName = config.getTestNin();

        Response response = chgPass.changePassword(oldPass, newPass, userName);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        // Assert response code is 200
        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());
    }
}
