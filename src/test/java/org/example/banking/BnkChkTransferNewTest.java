package org.example.banking;

import io.restassured.response.Response;
import org.example.login.Login;
import org.example.utils.ConfigManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class BnkChkTransferNewTest {

    private BnkChkTransferNew bnkChkTransfer;
    private ConfigManager config;
    private String nin;
    private String lstLogin;
    private String iban;
    private String swiftCode;

    @BeforeClass
    public void setup() {
        config = ConfigManager.getInstance();
        bnkChkTransfer = new BnkChkTransferNew();

        // Login first to get session
        Login login = new Login();
        String userName = config.getTestNin();
        String password = config.getTestPassword();
        String lang = config.getTestLanguage();
        lstLogin = config.getTestLstLogin();

        Response loginResponse = login.login(userName, password, lang, lstLogin);
        Assert.assertEquals(loginResponse.getStatusCode(), 200, "Login failed");

        nin = config.getTestNin();
        iban = config.getProperty("banking.iban", "");
        swiftCode = config.getProperty("banking.qiib.swiftCode", "QISBQAQA");
    }

    @Test(description = "Test Check Bank Transfer with default parameters")
    public void testCheckBankTransfer() {
        String amount = config.getProperty("banking.transfer.amount", "100");

        System.out.println("Checking bank transfer for NIN: " + nin);
        System.out.println("IBAN: " + iban);
        System.out.println("Amount: " + amount);

        Response response = bnkChkTransfer.checkBankTransfer(swiftCode, iban, amount, nin);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());

        String resCode = response.jsonPath().getString("responseStatus.resCode");
        System.out.println("Response Code: " + resCode);
    }

    @Test(description = "Test Check Bank Transfer with QIIB")
    public void testCheckQiibTransfer() {
        String amount = config.getProperty("banking.transfer.amount", "100");

        System.out.println("Checking QIIB transfer for NIN: " + nin);
        System.out.println("IBAN: " + iban);
        System.out.println("Amount: " + amount);

        Response response = bnkChkTransfer.checkQiibTransfer(iban, amount, nin);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());

        String resCode = response.jsonPath().getString("responseStatus.resCode");
        System.out.println("Response Code: " + resCode);
    }

    @Test(description = "Test Check Bank Transfer with full parameters")
    public void testCheckBankTransferFullParams() {
        String amount = config.getProperty("banking.transfer.amount", "100");

        System.out.println("Checking bank transfer with full parameters for NIN: " + nin);

        Response response = bnkChkTransfer.checkBankTransfer(
                swiftCode,
                iban,
                amount,
                nin,
                nin,
                config.getProperty("banking.userType", "INVESTOR"),
                config.getTestLanguage(),
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
