package org.example.myCalc;

import io.restassured.response.Response;
import org.example.base.BaseApi;
import org.example.login.Login;
import org.example.utils.ConfigManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class MyCalcTest {

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
    public void testGetMyCalcWithConfigPeriod() {
        MyCalc myCalc = new MyCalc();

        String nin = config.getTestNin();

        // Uses period from config.properties (mycalc.period)
        Response response = myCalc.getMyCalc(nin);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);
        System.out.println("Using Period from Config: " + config.getMyCalcPeriod());

        // Assert response code is 200
        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());
    }

    @Test
    public void testGetMyCalcAll() {
        MyCalc myCalc = new MyCalc();

        String nin = config.getTestNin();

        Response response = myCalc.getMyCalc(nin, MyCalc.Period.ALL);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        // Assert response code is 200
        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());
    }

    @Test
    public void testGetMyCalcDay() {
        MyCalc myCalc = new MyCalc();

        String nin = config.getTestNin();

        Response response = myCalc.getMyCalc(nin, MyCalc.Period.DAY);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        // Assert response code is 200
        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());
    }

    @Test
    public void testGetMyCalcWeek() {
        MyCalc myCalc = new MyCalc();

        String nin = config.getTestNin();

        Response response = myCalc.getMyCalc(nin, MyCalc.Period.WEEK);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        // Assert response code is 200
        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());
    }

    @Test
    public void testGetMyCalcMonth() {
        MyCalc myCalc = new MyCalc();

        String nin = config.getTestNin();

        Response response = myCalc.getMyCalc(nin, MyCalc.Period.MONTH);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        // Assert response code is 200
        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());
    }

    @Test
    public void testGetMyCalcYear() {
        MyCalc myCalc = new MyCalc();

        String nin = config.getTestNin();

        Response response = myCalc.getMyCalc(nin, MyCalc.Period.YEAR);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        // Assert response code is 200
        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());
    }

    @Test
    public void testGetMyCalcWithDateRange() {
        MyCalc myCalc = new MyCalc();

        String nin = config.getTestNin();
        String dateFrom = "2025-09-11";
        String dateTo = "2025-09-11";

        // Uses period from config.properties
        Response response = myCalc.getMyCalc(nin, dateFrom, dateTo);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        // Assert response code is 200
        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());
    }
}
