package org.example.banking;

import io.restassured.response.Response;
import org.example.base.BaseApi;
import org.example.login.Login;
import org.example.utils.ConfigManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class RmvBnkAccTest {

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
    public void testRemoveBankAccount() {
        RmvBnkAcc rmvBnkAcc = new RmvBnkAcc();

        String nin = config.getTestNin();
        String accountSeq = "1"; // Account sequence to remove

        Response response = rmvBnkAcc.removeBankAccount(nin, accountSeq);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        // Assert response code is 200
        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());
    }

    @Test
    public void testRemoveBankAccountWithCustomValues() {
        RmvBnkAcc rmvBnkAcc = new RmvBnkAcc();

        String nin = config.getTestNin();
        String accountNo = config.getBankAccountNo();
        String swiftAddress = config.getBankSwiftAddress();
        int currencyCode = Integer.parseInt(config.getBankCurrencyCode());
        String accountSeq = "1";

        Response response = rmvBnkAcc.removeBankAccount(nin, accountNo, swiftAddress, currencyCode, accountSeq);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        // Assert response code is 200
        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());
    }
}
