package org.example.orders;

import org.example.utils.ConfigManager;
import org.example.utils.TestDataManager;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class GetOrdersDBTest {

    private GetOrdersDB getOrdersDB;
    private ConfigManager config;
    private String nin;

    @BeforeClass
    public void setup() {
        config = ConfigManager.getInstance();
        getOrdersDB = new GetOrdersDB();
        nin = config.getTestNin();

        System.out.println("Testing with NIN: " + nin);
    }

    @AfterClass
    public void tearDown() {
        getOrdersDB.close();
    }

    @Test(description = "Test Get Active Orders from Database")
    public void testGetActiveOrders() {
        List<Map<String, Object>> orders = getOrdersDB.getActiveOrders(nin);

        Assert.assertNotNull(orders, "Orders list is null");

        System.out.println("Found " + orders.size() + " active orders");
        getOrdersDB.printOrders(orders);

        // Save first order slNo to file for use in other tests
        if (!orders.isEmpty()) {
            Map<String, Object> firstOrder = orders.get(0);
            String slNo = String.valueOf(firstOrder.get("SLNO"));
            String compCode = String.valueOf(firstOrder.get("COMP_CODE"));
            String price = String.valueOf(firstOrder.get("ORD_PRICE"));

            if (slNo != null && !"null".equals(slNo)) {
                TestDataManager.saveOrderData(slNo, compCode, price);
                System.out.println("Saved first order to file - slNo: " + slNo);
            }
        }
    }

    @Test(description = "Test Get Active Orders Count")
    public void testGetActiveOrdersCount() {
        int count = getOrdersDB.getActiveOrdersCount(nin);

        System.out.println("Active orders count: " + count);
        Assert.assertTrue(count >= 0, "Count should be >= 0");
    }

    @Test(description = "Test Get Order by SLNO")
    public void testGetOrderBySlNo() {
        // First get active orders to find a valid slNo
        List<Map<String, Object>> orders = getOrdersDB.getActiveOrders(nin);

        if (orders.isEmpty()) {
            System.out.println("No active orders found, skipping test");
            return;
        }

        String slNo = String.valueOf(orders.get(0).get("SLNO"));
        System.out.println("Looking up order with slNo: " + slNo);

        Map<String, Object> order = getOrdersDB.getOrderBySlNo(nin, slNo);

        Assert.assertNotNull(order, "Order should not be null");
        System.out.println("Order found:");
        for (Map.Entry<String, Object> entry : order.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    @Test(description = "Test Get Active Orders using config NIN")
    public void testGetActiveOrdersWithConfigNin() {
        List<Map<String, Object>> orders = getOrdersDB.getActiveOrders();

        Assert.assertNotNull(orders, "Orders list is null");
        System.out.println("Found " + orders.size() + " active orders for config NIN");
    }
}
