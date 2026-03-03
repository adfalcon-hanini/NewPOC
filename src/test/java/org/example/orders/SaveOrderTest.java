package org.example.orders;

import io.restassured.response.Response;
import org.example.Companies.GetComps;
import org.example.login.Login;
import org.example.utils.ConfigManager;
import org.example.utils.TestDataManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class SaveOrderTest {

    private SaveOrder saveOrder;
    private GetComps getComps;
    private ConfigManager config;
    private String nin;
    private String lstLogin;
    private List<Map<String, Object>> companies;

    @BeforeClass
    public void setup() {
        config = ConfigManager.getInstance();
        saveOrder = new SaveOrder();
        getComps = new GetComps();

        // Login first to get session
        Login login = new Login();
        String userName = config.getTestNin();
        String password = config.getTestPassword();
        String lang = config.getTestLanguage();
        lstLogin = config.getTestLstLogin();

        Response loginResponse = login.login(userName, password, lang, lstLogin);
        Assert.assertEquals(loginResponse.getStatusCode(), 200, "Login failed");

        nin = config.getTestNin();

        // Get companies list from API
        Response compsResponse = getComps.getComps(lstLogin);
        Assert.assertEquals(compsResponse.getStatusCode(), 200, "GetComps failed");

        companies = compsResponse.jsonPath().getList("message.cmpdetailsResponse.comps.comp");

        // Save companies data to file for subsequent tests
        if (companies != null && !companies.isEmpty()) {
            TestDataManager.saveCompaniesData(companies);
            System.out.println("Saved " + companies.size() + " companies to file");
        } else {
            // Try to load from file if API returned empty
            companies = TestDataManager.loadCompaniesData();
            System.out.println("Loaded " + (companies != null ? companies.size() : 0) + " companies from file");
        }

        System.out.println("Found " + (companies != null ? companies.size() : 0) + " companies");
    }

    // Helper method to get first company data
    private Map<String, Object> getFirstCompany() {
        if (companies != null && !companies.isEmpty()) {
            return companies.get(0);
        }
        return null;
    }

    // Helper method to get company symbol (compCode)
    private String getCompanySymbol() {
        Map<String, Object> company = getFirstCompany();
        if (company != null) {
            return String.valueOf(company.get("compCode"));
        }
        return config.getOrderSymbol();
    }

    // Helper method to get company price (closePrc)
    private String getCompanyPrice() {
        Map<String, Object> company = getFirstCompany();
        if (company != null) {
            return String.valueOf(company.get("closePrc"));
        }
        return config.getOrderSharePrice();
    }

    @Test(description = "Test Save Order for All Companies")
    public void testSaveOrderForAllCompanies() {
        Assert.assertNotNull(companies, "Companies list is null");
        Assert.assertFalse(companies.isEmpty(), "Companies list is empty");

        String ordType = config.getOrderOrdType();
        System.out.println("Order Type: " + ordType);

        int successCount = 0;
        int failCount = 0;
        List<Map<String, String>> savedOrders = new java.util.ArrayList<>();

        for (Map<String, Object> company : companies) {
            String compCode = String.valueOf(company.get("compCode"));
            String limitUp = String.valueOf(company.get("limmitUp"));

            System.out.println("Creating " + ordType + " order for Company: " + compCode + ", Price: " + limitUp);

            Response response;
            if ("SELL".equalsIgnoreCase(ordType)) {
                response = saveOrder.saveSellOrder(
                        nin,
                        compCode,
                        config.getOrderSharesNo(),
                        limitUp,
                        nin,
                        config.getOrderUserType(),
                        config.getOrderFixType(),
                        config.getOrderClientAltIP(),
                        lstLogin
                );
            } else {
                response = saveOrder.saveBuyOrder(
                        nin,
                        compCode,
                        config.getOrderSharesNo(),
                        limitUp,
                        nin,
                        config.getOrderUserType(),
                        config.getOrderFixType(),
                        config.getOrderClientAltIP(),
                        lstLogin
                );
            }

            int statusCode = response.getStatusCode();
            String resCode = response.jsonPath().getString("responseStatus.resCode");

            System.out.println("Company: " + compCode + " | Status: " + statusCode + " | ResCode: " + resCode);

            if (statusCode == 200 && "0".equals(resCode)) {
                successCount++;
                // Extract slNo from response (field is newSLNO in the response)
                String slNo = response.jsonPath().getString("message.saveOrdResponse.newSLNO");
                if (slNo != null) {
                    System.out.println("Order created with slNo: " + slNo);

                    // Add to saved orders list
                    Map<String, String> orderData = new java.util.HashMap<>();
                    orderData.put("slNo", slNo);
                    orderData.put("compCode", compCode);
                    orderData.put("price", limitUp);
                    orderData.put("ordType", ordType);
                    orderData.put("timestamp", String.valueOf(System.currentTimeMillis()));
                    savedOrders.add(orderData);
                }
            } else {
                failCount++;
                System.out.println("Failed Response: " + response.getBody().asPrettyString());
            }
        }

        // Save all orders to file
        if (!savedOrders.isEmpty()) {
            TestDataManager.saveBuyOrdersData(savedOrders);
            System.out.println("Saved " + savedOrders.size() + " " + ordType + " orders to file");

            // Also save the last order for backward compatibility
            Map<String, String> lastOrder = savedOrders.get(savedOrders.size() - 1);
            TestDataManager.saveOrderData(lastOrder.get("slNo"), lastOrder.get("compCode"), lastOrder.get("price"));
        }

        System.out.println("=== Summary ===");
        System.out.println("Order Type: " + ordType);
        System.out.println("Total Companies: " + companies.size());
        System.out.println("Successful Orders: " + successCount);
        System.out.println("Failed Orders: " + failCount);
    }
/*
    @Test(description = "Test Save Buy Order")
    public void testSaveBuyOrder() {
        String symbol = getCompanySymbol();
        String price = getCompanyPrice();

        System.out.println("Using company data - Symbol: " + symbol + ", Price: " + price);

        Response response = saveOrder.saveBuyOrder(
                nin,
                symbol,
                config.getOrderSharesNo(),
                price,
                nin,
                config.getOrderUserType(),
                config.getOrderFixType(),
                config.getOrderClientAltIP(),
                lstLogin
        );

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());

        String resCode = response.jsonPath().getString("responseStatus.resCode");
        System.out.println("Response Code: " + resCode);

        // Save order data if successful
        if ("0".equals(resCode)) {
            String slNo = response.jsonPath().getString("message.saveOrdResponse.newSLNO");
            if (slNo != null) {
                TestDataManager.saveOrderData(slNo, symbol, price);
                System.out.println("Saved order data to file - slNo: " + slNo);
            }
        }
    }

 */

    @Test(description = "Test Save Sell Order")
    public void testSaveSellOrder() {
        String symbol = getCompanySymbol();
        String price = getCompanyPrice();

        System.out.println("Using company data - Symbol: " + symbol + ", Price: " + price);

        Response response = saveOrder.saveSellOrder(
                nin,
                symbol,
                config.getOrderSharesNo(),
                price,
                nin,
                config.getOrderUserType(),
                config.getOrderFixType(),
                config.getOrderClientAltIP(),
                lstLogin
        );

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());

        // Save order data if successful
        String resCode = response.jsonPath().getString("responseStatus.resCode");
        if ("0".equals(resCode)) {
            String slNo = response.jsonPath().getString("message.saveOrdResponse.slNo");
            if (slNo != null) {
                TestDataManager.saveOrderData(slNo, symbol, price);
                System.out.println("Saved order data to file - slNo: " + slNo);
            }
        }
    }

    @Test(description = "Test Save Order with Full Parameters")
    public void testSaveOrderFullParams() {
        String symbol = getCompanySymbol();
        String price = getCompanyPrice();

        System.out.println("Using company data - Symbol: " + symbol + ", Price: " + price);

        Response response = saveOrder.saveOrder(
                config.getOrderOrdType(),
                nin,
                symbol,
                config.getOrderSharesNo(),
                price,
                config.getOrderMkPrc(),
                config.getOrderMPayType(),
                config.getOrderOrdVald(),
                "",
                nin,
                config.getOrderUserType(),
                config.getOrderInvestorType(),
                config.getTestLanguage(),
                config.getAppId(),
                "",
                config.getOrderFixType(),
                config.getOrderDiscloseVolume(),
                config.getOrderClientAltIP(),
                config.getOrderClientUserIP(),
                lstLogin
        );

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());

        // Save order data if successful
        String resCode = response.jsonPath().getString("responseStatus.resCode");
        if ("0".equals(resCode)) {
            String slNo = response.jsonPath().getString("message.saveOrdResponse.slNo");
            if (slNo != null) {
                TestDataManager.saveOrderData(slNo, symbol, price);
                System.out.println("Saved order data to file - slNo: " + slNo);
            }
        }
    }

    @Test(description = "Test Save Order with Market Price")
    public void testSaveOrderMarketPrice() {
        String symbol = getCompanySymbol();
        String price = getCompanyPrice();

        System.out.println("Using company data - Symbol: " + symbol + ", Price: " + price);

        Response response = saveOrder.saveOrder(
                config.getOrderOrdType(),
                nin,
                symbol,
                config.getOrderSharesNo(),
                price,
                "Y",
                config.getOrderMPayType(),
                config.getOrderOrdVald(),
                "",
                nin,
                config.getOrderUserType(),
                config.getOrderInvestorType(),
                config.getTestLanguage(),
                config.getAppId(),
                "",
                config.getOrderFixType(),
                config.getOrderDiscloseVolume(),
                config.getOrderClientAltIP(),
                config.getOrderClientUserIP(),
                lstLogin
        );

        int statusCode = response.getStatusCode();
        System.out.println("Status Code: " + statusCode);

        Assert.assertEquals(statusCode, 200, "Expected status code 200 but got " + statusCode);

        System.out.println("Response JSON:");
        System.out.println(response.getBody().asPrettyString());

        // Save order data if successful
        String resCode = response.jsonPath().getString("responseStatus.resCode");
        if ("0".equals(resCode)) {
            String slNo = response.jsonPath().getString("message.saveOrdResponse.slNo");
            if (slNo != null) {
                TestDataManager.saveOrderData(slNo, symbol, price);
                System.out.println("Saved order data to file - slNo: " + slNo);
            }
        }
    }
}
