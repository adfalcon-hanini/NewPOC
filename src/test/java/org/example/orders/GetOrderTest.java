package org.example.orders;

import io.restassured.response.Response;
import org.example.login.Login;
import org.example.utils.ConfigManager;
import org.example.utils.TestDataManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class GetOrderTest {

    private GetOrder getOrder;
    private ConfigManager config;
    private String nin;
    private String lstLogin;
    private String slNo;

    @BeforeClass
    public void setup() {
        config = ConfigManager.getInstance();
        getOrder = new GetOrder();

        // Login first to get session
        Login login = new Login();
        String userName = config.getTestNin();
        String password = config.getTestPassword();
        String lang = config.getTestLanguage();
        lstLogin = config.getTestLstLogin();

        Response loginResponse = login.login(userName, password, lang, lstLogin);
        Assert.assertEquals(loginResponse.getStatusCode(), 200, "Login failed");

        nin = config.getTestNin();

        // Load slNo from saved order data file
        if (TestDataManager.orderDataExists()) {
            slNo = TestDataManager.getOrderSlNo();
            System.out.println("Loaded slNo from file: " + slNo);
        } else {
            slNo = config.getProperty("order.slNo", "1");
            System.out.println("Using slNo from config: " + slNo);
        }
    }

    @Test(description = "Test Get Orders by Serial Number")
    public void testGetOrdersBySlNo() {
        Assert.assertNotNull(slNo, "slNo is null - run SaveOrderTest first to generate order data");

        System.out.println("Getting order with slNo: " + slNo);

        Response response = getOrder.getOrdersBySlNo(nin, slNo, lstLogin);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());

        String resCode = response.jsonPath().getString("responseStatus.resCode");
        System.out.println("Response Code: " + resCode);
    }

    @Test(description = "Test Get Orders by Date")
    public void testGetOrdersByDate() {
        String date = config.getProperty("order.searchDate", java.time.LocalDate.now().toString());

        Response response = getOrder.getOrdersByDate(nin, date, lstLogin);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());

        String resCode = response.jsonPath().getString("responseStatus.resCode");
        System.out.println("Response Code: " + resCode);
    }

    @Test(description = "Test Get Orders with Full Parameters")
    public void testGetOrdersFullParams() {
        Assert.assertNotNull(slNo, "slNo is null - run SaveOrderTest first to generate order data");

        System.out.println("Getting order with slNo: " + slNo);

        Response response = getOrder.getOrders(
                "SLNO",
                nin,
                slNo,
                "",
                nin,
                config.getOrderUserType(),
                config.getTestLanguage(),
                lstLogin
        );

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());
    }
}
