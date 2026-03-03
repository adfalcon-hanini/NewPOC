package org.example.alerts;

import io.restassured.response.Response;
import org.example.login.Login;
import org.example.utils.ConfigManager;
import org.example.utils.TestDataManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class RemoveAlertTest {

    private RemoveAlert removeAlert;
    private ConfigManager config;
    private String nin;
    private String lstLogin;
    private String alertId;

    @BeforeClass
    public void setup() {
        config = ConfigManager.getInstance();
        removeAlert = new RemoveAlert();

        // Login first to get session
        Login login = new Login();
        String userName = config.getTestNin();
        String password = config.getTestPassword();
        String lang = config.getTestLanguage();
        lstLogin = config.getTestLstLogin();

        Response loginResponse = login.login(userName, password, lang, lstLogin);
        Assert.assertEquals(loginResponse.getStatusCode(), 200, "Login failed");

        nin = config.getTestNin();

        // Load alert data from saved file
        if (TestDataManager.alertDataExists()) {
            alertId = TestDataManager.getAlertId();
            System.out.println("Loaded alert ID from file: " + alertId);
        } else {
            alertId = config.getProperty("alert.id", "");
            System.out.println("Using alert ID from config: " + alertId);
        }
    }

    @Test(description = "Test Remove Alert with default parameters")
    public void testRemoveAlert() {
        Assert.assertNotNull(alertId, "alertId is null - run AddAlertTest first to generate alert data");
        Assert.assertFalse(alertId.isEmpty(), "alertId is empty - run AddAlertTest first to generate alert data");

        System.out.println("Removing alert ID: " + alertId + " for NIN: " + nin);

        Response response = removeAlert.removeAlert(alertId, nin);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());

        String resCode = response.jsonPath().getString("responseStatus.resCode");
        System.out.println("Response Code: " + resCode);

        // Clear saved alert data after successful removal
        if ("0".equals(resCode)) {
            TestDataManager.clearAlertData();
            System.out.println("Alert data cleared after successful removal");
        }
    }

    @Test(description = "Test Remove Alert with lstLogin parameter")
    public void testRemoveAlertWithLstLogin() {
        // First, create a new alert to remove
        AddAlert addAlert = new AddAlert();
        String alertTitle = "Alert to Remove " + System.currentTimeMillis();

        Response addResponse = addAlert.addAlert(
                alertTitle,
                config.getProperty("alert.symbolId", "10"),
                config.getProperty("alert.interestId", "62"),
                config.getProperty("alert.currentValue", "10"),
                config.getProperty("alert.value", "11"),
                config.getProperty("alert.dateFrom", "2"),
                config.getProperty("alert.type", "CMP"),
                nin,
                lstLogin
        );

        String newAlertId = addResponse.jsonPath().getString("message.AlertResponse.AlertId");
        Assert.assertNotNull(newAlertId, "Failed to create alert for removal test");

        System.out.println("Created alert ID: " + newAlertId + " for removal test");
        System.out.println("Removing alert with lstLogin: " + lstLogin);

        Response response = removeAlert.removeAlert(newAlertId, nin, lstLogin);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());

        String resCode = response.jsonPath().getString("responseStatus.resCode");
        System.out.println("Response Code: " + resCode);
    }

    @Test(description = "Test Remove Alert with full parameters")
    public void testRemoveAlertFullParams() {
        // First, create a new alert to remove
        AddAlert addAlert = new AddAlert();
        String alertTitle = "Alert to Remove Full " + System.currentTimeMillis();

        Response addResponse = addAlert.addAlert(
                alertTitle,
                config.getProperty("alert.symbolId", "10"),
                config.getProperty("alert.interestId", "62"),
                config.getProperty("alert.currentValue", "10"),
                config.getProperty("alert.value", "11"),
                config.getProperty("alert.dateFrom", "2"),
                config.getProperty("alert.type", "CMP"),
                nin,
                lstLogin
        );

        String newAlertId = addResponse.jsonPath().getString("message.AlertResponse.AlertId");
        Assert.assertNotNull(newAlertId, "Failed to create alert for removal test");

        System.out.println("Created alert ID: " + newAlertId + " for removal test with full parameters");

        Response response = removeAlert.removeAlert(
                newAlertId,
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
