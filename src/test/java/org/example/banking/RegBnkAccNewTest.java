package org.example.banking;

import io.restassured.response.Response;
import org.example.base.BaseApi;
import org.example.login.Login;
import org.example.utils.ConfigManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class RegBnkAccNewTest {

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
    public void testRegisterBankAccount() {
        RegBnkAccNew regBnkAccNew = new RegBnkAccNew();

        String nin = config.getTestNin();

        // Using config values for bank account details
        Response response = regBnkAccNew.registerBankAccount(nin);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        // Assert response code is 200
        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        // Assert registerMessage
        String registerMessage = response.jsonPath().getString("message.registerBankAccountResponse.registerMessage");
        System.out.println("Register Message: " + registerMessage);
        Assert.assertEquals(registerMessage, "يوجد حساب بنكي أخر بنفس البيانات", "Unexpected register message");

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());
    }

    @Test
    public void testRegisterBankAccountWithCustomValues() {
        RegBnkAccNew regBnkAccNew = new RegBnkAccNew();

        String nin = config.getTestNin();
        String userId = config.getTestNin();
        String accountNo = config.getBankAccountNo();
        String swiftAddress = config.getBankSwiftAddress();
        String currencyCode = config.getBankCurrencyCode();
        String accountIBAN = config.getBankAccountIBAN();

        Response response = regBnkAccNew.registerBankAccount(nin, userId, accountNo, swiftAddress, currencyCode, accountIBAN);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        // Assert response code is 200
        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        // Assert registerMessage
        String registerMessage = response.jsonPath().getString("message.registerBankAccountResponse.registerMessage");
        System.out.println("Register Message: " + registerMessage);
        Assert.assertEquals(registerMessage, "يوجد حساب بنكي أخر بنفس البيانات", "Unexpected register message");

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());
    }
}
