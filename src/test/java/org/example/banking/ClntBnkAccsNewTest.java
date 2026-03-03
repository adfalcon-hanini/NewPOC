package org.example.banking;

import io.restassured.response.Response;
import org.example.login.Login;
import org.example.utils.ConfigManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ClntBnkAccsNewTest {

    private ClntBnkAccsNew clntBnkAccs;
    private ConfigManager config;
    private String nin;
    private String lstLogin;

    @BeforeClass
    public void setup() {
        config = ConfigManager.getInstance();
        clntBnkAccs = new ClntBnkAccsNew();

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

    @Test(description = "Test Get Client Bank Accounts with default parameters")
    public void testGetClientBankAccounts() {
        System.out.println("Getting bank accounts for NIN: " + nin);

        Response response = clntBnkAccs.getClientBankAccounts(nin);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());

        String resCode = response.jsonPath().getString("responseStatus.resCode");
        System.out.println("Response Code: " + resCode);
    }

    @Test(description = "Test Get Client Bank Accounts with lstLogin parameter")
    public void testGetClientBankAccountsWithLstLogin() {
        System.out.println("Getting bank accounts for NIN: " + nin + " with lstLogin: " + lstLogin);

        Response response = clntBnkAccs.getClientBankAccounts(nin, lstLogin);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());

        String resCode = response.jsonPath().getString("responseStatus.resCode");
        System.out.println("Response Code: " + resCode);
    }

    @Test(description = "Test Get Client Bank Accounts by register type")
    public void testGetClientBankAccountsByType() {
        String accRegisterType = config.getProperty("banking.accRegisterType", "");

        System.out.println("Getting bank accounts for NIN: " + nin + " with type: " + accRegisterType);

        Response response = clntBnkAccs.getClientBankAccountsByType(nin, accRegisterType);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());

        String resCode = response.jsonPath().getString("responseStatus.resCode");
        System.out.println("Response Code: " + resCode);
    }

    @Test(description = "Test Get Client Bank Accounts with full parameters")
    public void testGetClientBankAccountsFullParams() {
        System.out.println("Getting bank accounts with full parameters for NIN: " + nin);

        Response response = clntBnkAccs.getClientBankAccounts(
                nin,
                nin,
                "",
                config.getTestLanguage(),
                "",
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
