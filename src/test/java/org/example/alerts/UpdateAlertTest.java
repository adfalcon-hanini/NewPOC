package org.example.alerts;

import io.restassured.response.Response;
import org.example.login.Login;
import org.example.utils.ConfigManager;
import org.example.utils.TestDataManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UpdateAlertTest {

    private UpdateAlert updateAlert;
    private ConfigManager config;
    private String nin;
    private String lstLogin;
    private String alertId;
    private String symbolId;
    private String currentValue;

    @BeforeClass
    public void setup() {
        config = ConfigManager.getInstance();
        updateAlert = new UpdateAlert();

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
            symbolId = TestDataManager.getAlertSymbolId();
            currentValue = TestDataManager.getAlertCurrentValue();
            System.out.println("Loaded alert data from file - AlertId: " + alertId);
        } else {
            alertId = config.getProperty("alert.id", "");
            symbolId = config.getProperty("alert.symbolId", "10");
            currentValue = config.getProperty("alert.currentValue", "10");
            System.out.println("Using alert data from config - AlertId: " + alertId);
        }
    }

    @Test(description = "Test Update Alert with default parameters")
    public void testUpdateAlert() {
        Assert.assertNotNull(alertId, "alertId is null - run AddAlertTest first to generate alert data");
        Assert.assertFalse(alertId.isEmpty(), "alertId is empty - run AddAlertTest first to generate alert data");

        String updatedTitle = "Updated Alert " + System.currentTimeMillis();
        String newAlertValue = "9";
        String interestId = config.getProperty("alert.interestId", "62");

        System.out.println("Updating alert ID: " + alertId + " for NIN: " + nin);
        System.out.println("New Title: " + updatedTitle);

        Response response = updateAlert.updateAlert(
                alertId,
                updatedTitle,
                symbolId,
                interestId,
                currentValue,
                newAlertValue,
                nin
        );

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());

        String resCode = response.jsonPath().getString("responseStatus.resCode");
        System.out.println("Response Code: " + resCode);
    }

    @Test(description = "Test Update Alert with lstLogin parameter")
    public void testUpdateAlertWithLstLogin() {
        Assert.assertNotNull(alertId, "alertId is null - run AddAlertTest first to generate alert data");
        Assert.assertFalse(alertId.isEmpty(), "alertId is empty - run AddAlertTest first to generate alert data");

        String updatedTitle = "Updated Alert LstLogin " + System.currentTimeMillis();
        String newAlertValue = "8";
        String interestId = config.getProperty("alert.interestId", "62");

        System.out.println("Updating alert ID: " + alertId + " with lstLogin: " + lstLogin);

        Response response = updateAlert.updateAlert(
                alertId,
                updatedTitle,
                symbolId,
                interestId,
                currentValue,
                newAlertValue,
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

    @Test(description = "Test Update Alert with full parameters")
    public void testUpdateAlertFullParams() {
        Assert.assertNotNull(alertId, "alertId is null - run AddAlertTest first to generate alert data");
        Assert.assertFalse(alertId.isEmpty(), "alertId is empty - run AddAlertTest first to generate alert data");

        String updatedTitle = "Updated Alert Full " + System.currentTimeMillis();

        System.out.println("Updating alert ID: " + alertId + " with full parameters");

        Response response = updateAlert.updateAlert(
                alertId,
                updatedTitle,
                symbolId,
                "62",
                "",
                currentValue,
                "7",
                "",
                "",
                "",
                "",
                "",
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
