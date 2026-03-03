package org.example.orders;

import io.restassured.response.Response;
import org.example.login.Login;
import org.example.utils.ConfigManager;
import org.example.utils.TestDataManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class CancelOrderTest {

    private CancelOrder cancelOrder;
    private ConfigManager config;
    private String nin;
    private String lstLogin;
    private String slNo;
    private List<Map<String, String>> buyOrders;

    @BeforeClass
    public void setup() {
        config = ConfigManager.getInstance();
        cancelOrder = new CancelOrder();

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

        // Load all buy orders from file
        if (TestDataManager.buyOrdersDataExists()) {
            buyOrders = TestDataManager.loadBuyOrdersData();
            System.out.println("Loaded " + (buyOrders != null ? buyOrders.size() : 0) + " buy orders from file");
        }
    }

    @Test(description = "Test Cancel Order")
    public void testCancelOrder() {
        Assert.assertNotNull(slNo, "slNo is null - run SaveOrderTest first to generate order data");

        System.out.println("Cancelling order with slNo: " + slNo);

        Response response = cancelOrder.cancelOrder(nin, slNo, lstLogin);

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());

        String resCode = response.jsonPath().getString("responseStatus.resCode");
        System.out.println("Response Code: " + resCode);
    }

    @Test(description = "Test Cancel Order with Full Parameters")
    public void testCancelOrderFullParams() {
        Assert.assertNotNull(slNo, "slNo is null - run SaveOrderTest first to generate order data");

        System.out.println("Cancelling order with slNo: " + slNo);

        Response response = cancelOrder.cancelOrder(
                nin,
                slNo,
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

        String resCode = response.jsonPath().getString("responseStatus.resCode");
        System.out.println("Response Code: " + resCode);
    }

    @Test(description = "Test Cancel All Buy Orders")
    public void testCancelAllBuyOrders() {
        // Reload buy orders from file in case it was created after @BeforeClass
        if (buyOrders == null && TestDataManager.buyOrdersDataExists()) {
            buyOrders = TestDataManager.loadBuyOrdersData();
            System.out.println("Reloaded " + (buyOrders != null ? buyOrders.size() : 0) + " buy orders from file");
        }

        Assert.assertNotNull(buyOrders, "Buy orders list is null - run SaveOrderTest.testSaveBuyOrderForAllCompanies first");
        Assert.assertFalse(buyOrders.isEmpty(), "Buy orders list is empty");

        int successCount = 0;
        int failCount = 0;

        System.out.println("Cancelling " + buyOrders.size() + " buy orders...");

        for (Map<String, String> order : buyOrders) {
            String orderSlNo = order.get("slNo");
            String compCode = order.get("compCode");

            System.out.println("Cancelling order - slNo: " + orderSlNo + ", Company: " + compCode);

            Response response = cancelOrder.cancelOrder(nin, orderSlNo, lstLogin);

            int statusCode = response.getStatusCode();
            String resCode = response.jsonPath().getString("responseStatus.resCode");

            System.out.println("slNo: " + orderSlNo + " | Status: " + statusCode + " | ResCode: " + resCode);

            if (statusCode == 200 && "0".equals(resCode)) {
                successCount++;
                System.out.println("Order cancelled successfully");
            } else {
                failCount++;
                System.out.println("Failed Response: " + response.getBody().asPrettyString());
            }
        }

        System.out.println("=== Summary ===");
        System.out.println("Total Orders: " + buyOrders.size());
        System.out.println("Successfully Cancelled: " + successCount);
        System.out.println("Failed: " + failCount);
    }
}
