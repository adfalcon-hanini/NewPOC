package org.example.myCalc;

import io.restassured.response.Response;
import org.example.login.Login;
import org.example.utils.ConfigManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ApplyTodayCashDistributionTest {

    private ApplyTodayCashDistribution applyTodayCashDistribution;
    private ConfigManager config;
    private String nin;

    @BeforeClass
    public void setup() {
        config = ConfigManager.getInstance();
        applyTodayCashDistribution = new ApplyTodayCashDistribution();

        // Login first to get session
        Login login = new Login();
        String userName = config.getTestNin();
        String password = config.getTestPassword();
        String lang = config.getTestLanguage();
        String lstLogin = config.getTestLstLogin();

        Response loginResponse = login.login(userName, password, lang, lstLogin);
        Assert.assertEquals(loginResponse.getStatusCode(), 200, "Login failed");

        nin = config.getTestNin();
    }

    @Test(description = "Test Apply Today Cash Distribution false")
    public void testApplyTodayCashDistributionfalse() {
        Response response = applyTodayCashDistribution.applyTodayCashDistribution(
                nin,
              false
        );

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());

        String resCode = response.jsonPath().getString("responseStatus.resCode");
        System.out.println("Response Code: " + resCode);
    }
/*
    @Test(description = "Test Apply Today Cash Distribution true")
    public void testApplyTodayCashDistributiontrue() {
        Response response = applyTodayCashDistribution.applyTodayCashDistribution(
                nin,
                true
        );

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());

        String resCode = response.jsonPath().getString("responseStatus.resCode");
        System.out.println("Response Code: " + resCode);
    }
*/
}
