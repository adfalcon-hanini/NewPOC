package org.example.alerts;

import io.restassured.response.Response;
import org.example.login.Login;
import org.example.utils.ConfigManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class GetAlertTest {

    private GetAlert getAlert;
    private ConfigManager config;
    private String nin;
    private String lstLogin;

    @BeforeClass
    public void setup() {
        config = ConfigManager.getInstance();
        getAlert = new GetAlert();

        // Login first to get session
        Login login = new Login();
        String userName = config.getTestNin();
        String password = config.getTestPassword();
        String lang = config.getTestLanguage();
        lstLogin = config.getTestLstLogin();

        Response loginResponse = login.login(userName, password, lang, lstLogin);
        Assert.assertEquals(loginResponse.getStatusCode(), 200, "Login failed");

        nin = config.getTestNin();
    }

    @Test(description = "Test Get Alert with default parameters")
    public void testGetAlert() {
        System.out.println("Getting alerts for NIN: " + nin);

        Response response = getAlert.getAlert(nin);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());

        String resCode = response.jsonPath().getString("responseStatus.resCode");
        System.out.println("Response Code: " + resCode);
    }

    @Test(description = "Test Get Alert with lstLogin parameter")
    public void testGetAlertWithLstLogin() {
        System.out.println("Getting alerts for NIN: " + nin + " with lstLogin: " + lstLogin);

        Response response = getAlert.getAlert(nin, lstLogin);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());

        String resCode = response.jsonPath().getString("responseStatus.resCode");
        System.out.println("Response Code: " + resCode);
    }

    @Test(description = "Test Get Alert with full parameters")
    public void testGetAlertFullParams() {
        System.out.println("Getting alerts for NIN: " + nin + " with full parameters");

        Response response = getAlert.getAlert(
                nin,
                "",
                config.getTestLanguage(),
                nin,
                config.getProperty("alert.userType", "INVESTOR"),
                lstLogin
        );

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());

        String resCode = response.jsonPath().getString("responseStatus.resCode");
        System.out.println("Response Code: " + resCode);
    }
}
