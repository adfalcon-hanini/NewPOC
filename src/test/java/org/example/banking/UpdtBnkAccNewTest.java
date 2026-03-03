package org.example.banking;

import io.restassured.response.Response;
import org.example.login.Login;
import org.example.utils.ConfigManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UpdtBnkAccNewTest {

    private UpdtBnkAccNew updtBnkAcc;
    private ConfigManager config;
    private String nin;
    private String lstLogin;
    private String currentAccountNo;
    private String swiftAddress;
    private String accountSeq;
    private String accountIBAN;

    @BeforeClass
    public void setup() {
        config = ConfigManager.getInstance();
        updtBnkAcc = new UpdtBnkAccNew();

        // Login first to get session
        Login login = new Login();
        String userName = config.getTestNin();
        String password = config.getTestPassword();
        String lang = config.getTestLanguage();
        lstLogin = config.getTestLstLogin();

        Response loginResponse = login.login(userName, password, lang, lstLogin);
        Assert.assertEquals(loginResponse.getStatusCode(), 200, "Login failed");

        nin = config.getTestNin();
        currentAccountNo = config.getProperty("banking.currentAccountNo", "");
        swiftAddress = config.getProperty("banking.swiftAddress", "");
        accountSeq = config.getProperty("banking.accountSeq", "");
        accountIBAN = config.getProperty("banking.accountIBAN", "");
    }

    @Test(description = "Test Update Bank Account Status")
    public void testUpdateAccountStatus() {
        String status = config.getProperty("banking.account.status", "ACTIVE");

        System.out.println("Updating account status for NIN: " + nin);
        System.out.println("Account: " + currentAccountNo);
        System.out.println("Status: " + status);

        Response response = updtBnkAcc.updateAccountStatus(
                currentAccountNo,
                swiftAddress,
                status,
                accountSeq,
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

    @Test(description = "Test Update Bank Account IBAN")
    public void testUpdateAccountIBAN() {
        String newAccountNo = config.getProperty("banking.newAccountNo", "");

        System.out.println("Updating account IBAN for NIN: " + nin);
        System.out.println("Current Account: " + currentAccountNo);
        System.out.println("New Account: " + newAccountNo);

        Response response = updtBnkAcc.updateAccountIBAN(
                currentAccountNo,
                swiftAddress,
                newAccountNo,
                accountIBAN,
                accountSeq,
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

    @Test(description = "Test Update Bank Account with common parameters")
    public void testUpdateBankAccountCommonParams() {
        String status = config.getProperty("banking.account.status", "");
        String currentCurrencyCode = config.getProperty("banking.currentCurrencyCode", "");
        String newAccountNo = config.getProperty("banking.newAccountNo", "");
        String newCurrencyCode = config.getProperty("banking.newCurrencyCode", "");

        System.out.println("Updating bank account with common parameters for NIN: " + nin);

        Response response = updtBnkAcc.updateBankAccount(
                currentAccountNo,
                swiftAddress,
                status,
                currentCurrencyCode,
                newAccountNo,
                newCurrencyCode,
                accountSeq,
                nin,
                accountIBAN,
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

    @Test(description = "Test Update Bank Account with full parameters")
    public void testUpdateBankAccountFullParams() {
        String status = config.getProperty("banking.account.status", "");
        String currentCurrencyCode = config.getProperty("banking.currentCurrencyCode", "");
        String newAccountNo = config.getProperty("banking.newAccountNo", "");
        String newCurrencyCode = config.getProperty("banking.newCurrencyCode", "");
        String clientUserIP = config.getProperty("client.userIP", "");

        System.out.println("Updating bank account with full parameters for NIN: " + nin);

        Response response = updtBnkAcc.updateBankAccount(
                currentAccountNo,
                swiftAddress,
                status,
                currentCurrencyCode,
                newAccountNo,
                newCurrencyCode,
                accountSeq,
                nin,
                accountIBAN,
                nin,
                "",
                config.getTestLanguage(),
                clientUserIP,
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
