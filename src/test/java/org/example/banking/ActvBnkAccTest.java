package org.example.banking;

import io.restassured.response.Response;
import org.example.base.BaseApi;
import org.example.login.Login;
import org.example.utils.ConfigManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ActvBnkAccTest {

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
    public void testActivateBankAccount() {
        ActvBnkAcc actvBnkAcc = new ActvBnkAcc();

        String nin = config.getTestNin();
        String accountSeq = "1"; // Account sequence to activate

        Response response = actvBnkAcc.activateBankAccount(nin, accountSeq);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        // Assert response code is 200
        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());
    }

    @Test
    public void testActivateBankAccountWithCustomValues() {
        ActvBnkAcc actvBnkAcc = new ActvBnkAcc();

        String nin = config.getTestNin();
        String accountNo = config.getBankAccountNo();
        String swiftAddress = config.getBankSwiftAddress();
        String currencyCode = config.getBankCurrencyCode();
        String accountIBAN = config.getBankAccountIBAN();
        String accountSeq = "1";

        Response response = actvBnkAcc.activateBankAccount(nin, accountNo, swiftAddress, currencyCode, accountIBAN, accountSeq);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        // Assert response code is 200
        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());
    }
}
