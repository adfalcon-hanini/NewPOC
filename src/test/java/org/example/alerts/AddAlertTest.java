package org.example.alerts;

import io.restassured.response.Response;
import org.example.login.Login;
import org.example.utils.ConfigManager;
import org.example.utils.TestDataManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class AddAlertTest {

    private AddAlert addAlert;
    private ConfigManager config;
    private String nin;
    private String lstLogin;

    @BeforeClass
    public void setup() {
        config = ConfigManager.getInstance();
        addAlert = new AddAlert();

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

    @Test(description = "Test Add Alert with default parameters")
    public void testAddAlert() {
        String alertTitle = "Test Alert " + System.currentTimeMillis();
        String symbolId = config.getProperty("alert.symbolId", "10");
        String interestId = config.getProperty("alert.interestId", "62");
        String currentValue = config.getProperty("alert.currentValue", "10");
        String alertValue = config.getProperty("alert.value", "11");
        String dateFrom = config.getProperty("alert.dateFrom", "2");
        String alertType = config.getProperty("alert.type", "CMP");

        System.out.println("Adding alert for NIN: " + nin);
        System.out.println("Alert Title: " + alertTitle);

        Response response = addAlert.addAlert(
                alertTitle,
                symbolId,
                interestId,
                currentValue,
                alertValue,
                dateFrom,
                alertType,
                nin
        );

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());

        String resCode = response.jsonPath().getString("responseStatus.resCode");
        System.out.println("Response Code: " + resCode);

        // Save alert ID for update/remove tests
        String alertId = response.jsonPath().getString("message.AlertResponse.AlertId");
        if (alertId != null && !alertId.isEmpty()) {
            TestDataManager.saveAlertData(alertId, symbolId, currentValue);
            System.out.println("Saved alert ID: " + alertId);
        }
    }

    @Test(description = "Test Add Alert with lstLogin parameter")
    public void testAddAlertWithLstLogin() {
        String alertTitle = "Test Alert LstLogin " + System.currentTimeMillis();
        String symbolId = config.getProperty("alert.symbolId", "10");
        String interestId = config.getProperty("alert.interestId", "62");
        String currentValue = config.getProperty("alert.currentValue", "10");
        String alertValue = config.getProperty("alert.value", "11");
        String dateFrom = config.getProperty("alert.dateFrom", "2");
        String alertType = config.getProperty("alert.type", "CMP");

        System.out.println("Adding alert for NIN: " + nin + " with lstLogin: " + lstLogin);

        Response response = addAlert.addAlert(
                alertTitle,
                symbolId,
                interestId,
                currentValue,
                alertValue,
                dateFrom,
                alertType,
                nin,
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

    @Test(description = "Test Add Alert with full parameters")
    public void testAddAlertFullParams() {
        String alertTitle = "Test Alert Full " + System.currentTimeMillis();

        System.out.println("Adding alert for NIN: " + nin + " with full parameters");

        Response response = addAlert.addAlert(
                alertTitle,
                "10",
                "62",
                "",
                "10",
                "11",
                "",
                "",
                "2",
                "",
                "CMP",
                "Y",
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
