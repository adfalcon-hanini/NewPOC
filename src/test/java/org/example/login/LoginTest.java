package org.example.login;

import io.restassured.response.Response;
import org.example.utils.ConfigManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LoginTest {

    private ConfigManager config;

    @BeforeClass
    public void setup() {
        config = ConfigManager.getInstance();
    }

    @Test
    public void testLogin() {
        Login login = new Login();

        String userName = config.getTestNin();
        String password = config.getTestPassword();
        String lang = config.getTestLanguage();
        String lstLogin = config.getTestLstLogin();

        Response response = login.login(userName, password, lang, lstLogin);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());
    }
}
