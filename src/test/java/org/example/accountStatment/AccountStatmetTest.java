package org.example.accountStatment;

import io.restassured.response.Response;
import org.example.base.BaseApi;
import org.example.login.Login;
import org.example.utils.ConfigManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class AccountStatmetTest {

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
    public void testGetAccountStatementMargin() {
        AccountStatmet accountStatmet = new AccountStatmet();

        String nin = config.getTestNin();

        Response response = accountStatmet.getAccountStatement(nin, AccountStatmet.StatementType.MARGIN);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());
    }

    @Test
    public void testGetAccountStatementCash() {
        AccountStatmet accountStatmet = new AccountStatmet();

        String nin = config.getTestNin();

        Response response = accountStatmet.getAccountStatement(nin, AccountStatmet.StatementType.CASH);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());
    }

    @Test
    public void testGetAccountStatementTrade() {
        AccountStatmet accountStatmet = new AccountStatmet();

        String nin = config.getTestNin();

        Response response = accountStatmet.getAccountStatement(nin, AccountStatmet.StatementType.TRADE);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());
    }

    @Test
    public void testGetAccountStatementCompact() {
        AccountStatmet accountStatmet = new AccountStatmet();

        String nin = config.getTestNin();

        Response response = accountStatmet.getAccountStatement(nin, AccountStatmet.StatementType.COMPACT);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());
    }

    @Test
    public void testGetAccountStatementWithDateRange() {
        AccountStatmet accountStatmet = new AccountStatmet();

        String nin = config.getTestNin();
        String dateFrom = "2025-01-01";
        String dateTo = "2025-12-31";

        Response response = accountStatmet.getAccountStatement(nin, dateFrom, dateTo, AccountStatmet.StatementType.MARGIN);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());
    }
}
