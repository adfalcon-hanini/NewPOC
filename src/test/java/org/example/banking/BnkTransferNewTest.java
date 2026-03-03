package org.example.banking;

import io.restassured.response.Response;
import org.example.login.Login;
import org.example.utils.ConfigManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class BnkTransferNewTest {

    private BnkTransferNew bnkTransfer;
    private ConfigManager config;
    private String nin;
    private String lstLogin;
    private String accountNo;
    private String accountStatus;
    private String swiftCode;
    private String transferTimeId;

    @BeforeClass
    public void setup() {
        config = ConfigManager.getInstance();
        bnkTransfer = new BnkTransferNew();

        // Login first to get session
        Login login = new Login();
        String userName = config.getTestNin();
        String password = config.getTestPassword();
        String lang = config.getTestLanguage();
        lstLogin = config.getTestLstLogin();

        Response loginResponse = login.login(userName, password, lang, lstLogin);
        Assert.assertEquals(loginResponse.getStatusCode(), 200, "Login failed");

        nin = config.getTestNin();
        accountNo = config.getProperty("banking.accountNo", "");
        accountStatus = config.getProperty("banking.accountStatus", "ACTIVE");
        swiftCode = config.getProperty("banking.swiftCode", "CBQAQAQAXXX");
        transferTimeId = config.getProperty("banking.transferTimeId", "");
    }

    @Test(description = "Test Bank Transfer FROM with default parameters")
    public void testTransferFromBank() {
        String amount = config.getProperty("banking.transfer.amount", "100");

        System.out.println("Executing transfer FROM bank for NIN: " + nin);
        System.out.println("Account: " + accountNo);
        System.out.println("Amount: " + amount);

        Response response = bnkTransfer.transferFromBank(
                swiftCode,
                accountNo,
                accountStatus,
                amount,
                transferTimeId,
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

    @Test(description = "Test Bank Transfer TO with default parameters")
    public void testTransferToBank() {
        String amount = config.getProperty("banking.transfer.amount", "100");

        System.out.println("Executing transfer TO bank for NIN: " + nin);
        System.out.println("Account: " + accountNo);
        System.out.println("Amount: " + amount);

        Response response = bnkTransfer.transferToBank(
                swiftCode,
                accountNo,
                accountStatus,
                amount,
                transferTimeId,
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

    @Test(description = "Test Bank Transfer with full parameters")
    public void testBankTransferFullParams() {
        String amount = config.getProperty("banking.transfer.amount", "100");

        System.out.println("Executing bank transfer with full parameters for NIN: " + nin);

        Response response = bnkTransfer.bankTransfer(
                swiftCode,
                accountNo,
                accountStatus,
                "FROM",
                amount,
                nin,
                nin,
                config.getProperty("banking.userType", "INVESTOR"),
                transferTimeId,
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
