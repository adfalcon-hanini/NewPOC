package org.example.myCalc;

import io.restassured.response.Response;
import org.example.base.BaseApi;
import org.example.login.Login;
import org.example.utils.ConfigManager;
import org.example.utils.TestDataManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class MyCalcDetailsTest {

    private ConfigManager config = ConfigManager.getInstance();
    private String nin;
    private String dateFrom;
    private String dateTo;
    private String bal;
    private String prevBal;
    private String with;
    private String depo;
    private String cashDiv;
    private String stkTransfer;
    private String regTransfer;
    private String fees;

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

        nin = config.getTestNin();
        dateFrom = config.getMyCalcDateFrom();
        dateTo = config.getMyCalcDateTo();

        // Call MyCalc API first to get the values for MyCalcDetails
        System.out.println("\n========== PRE-REQUISITE: Calling MyCalc API ==========");
        MyCalc myCalc = new MyCalc();
        Response myCalcResponse = myCalc.getMyCalc(nin, dateFrom, dateTo);

        Assert.assertEquals(myCalcResponse.getStatusCode(), 200, "MyCalc API call failed");

        String resCode = myCalcResponse.jsonPath().getString("responseStatus.resCode");
        Assert.assertEquals(resCode, "0", "MyCalc API returned error: " + resCode);

        // Extract values from MyCalc response (from myCalcRecords.trnx[0])
        String basePath = "message.myCalcResponse.myCalcRecords.trnx[0]";
        bal = myCalcResponse.jsonPath().getString(basePath + ".bal");
        prevBal = myCalcResponse.jsonPath().getString(basePath + ".prevBal");
        with = myCalcResponse.jsonPath().getString(basePath + ".with");
        depo = myCalcResponse.jsonPath().getString(basePath + ".depo");
        cashDiv = myCalcResponse.jsonPath().getString(basePath + ".cashDiv");
        stkTransfer = myCalcResponse.jsonPath().getString(basePath + ".stkTransfer");
        regTransfer = myCalcResponse.jsonPath().getString(basePath + ".regTransfer");
        fees = myCalcResponse.jsonPath().getString(basePath + ".fees");

        // Validate that we got values from MyCalc API
        Assert.assertNotNull(bal, "bal not found in MyCalc response");
        Assert.assertNotNull(prevBal, "prevBal not found in MyCalc response");
        Assert.assertNotNull(with, "with not found in MyCalc response");
        Assert.assertNotNull(depo, "depo not found in MyCalc response");
        Assert.assertNotNull(cashDiv, "cashDiv not found in MyCalc response");
        Assert.assertNotNull(stkTransfer, "stkTransfer not found in MyCalc response");
        Assert.assertNotNull(regTransfer, "regTransfer not found in MyCalc response");
        Assert.assertNotNull(fees, "fees not found in MyCalc response");

        System.out.println("========== MyCalc Values Extracted ==========");
        System.out.println("bal: " + bal);
        System.out.println("prevBal: " + prevBal);
        System.out.println("with: " + with);
        System.out.println("depo: " + depo);
        System.out.println("cashDiv: " + cashDiv);
        System.out.println("stkTransfer: " + stkTransfer);
        System.out.println("regTransfer: " + regTransfer);
        System.out.println("fees: " + fees);
        System.out.println("=============================================\n");

        // Save MyCalc response to testdata JSON file
        TestDataManager.saveMyCalcData(bal, prevBal, with, depo, cashDiv, stkTransfer, regTransfer, fees);
        System.out.println("MyCalc data saved to testdata/mycalc_data.json");

        // Update config.properties with new values
        config.updateMyCalcProperties(bal, prevBal, with, depo, cashDiv, stkTransfer, regTransfer, fees);
        System.out.println("config.properties updated with MyCalc values");
    }

    @Test(description = "Test Get MyCalcDetails with full parameters")
    public void testGetMyCalcDetailsFullParams() {
        MyCalcDetails myCalcDetails = new MyCalcDetails();

        System.out.println("Testing MyCalcDetails with full parameters");
        System.out.println("NIN: " + nin);
        System.out.println("DateFrom: " + dateFrom + ", DateTo: " + dateTo);
        System.out.println("Period: " + config.getMyCalcPeriod());
        System.out.println("Using values from MyCalc API response");

        Response response = myCalcDetails.getMyCalcDetails(
                nin,
                dateFrom,
                dateTo,
                MyCalc.Period.fromString(config.getMyCalcPeriod()),
                bal,
                prevBal,
                with,
                depo,
                cashDiv,
                stkTransfer,
                regTransfer,
                fees,
                config.getTestLanguage(),
                config.getTestLstLogin()
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
