package org.example.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestDataManager {

    private static final Logger logger = LogManager.getLogger(TestDataManager.class);
    private static final String TEST_DATA_DIR = "src/test/resources/testdata/";
    private static final String ORDER_DATA_FILE = TEST_DATA_DIR + "order_data.json";
    private static final String COMPANIES_DATA_FILE = TEST_DATA_DIR + "companies_data.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    static {
        // Create testdata directory if not exists
        try {
            Files.createDirectories(Paths.get(TEST_DATA_DIR));
        } catch (IOException e) {
            logger.error("Failed to create test data directory: {}", e.getMessage());
        }
    }

    // ==================== ORDER DATA ====================

    private static final String BUY_ORDERS_DATA_FILE = TEST_DATA_DIR + "buy_orders_data.json";

    // Save order data to file
    public static void saveOrderData(String slNo, String compCode, String price) {
        Map<String, String> orderData = new HashMap<>();
        orderData.put("slNo", slNo);
        orderData.put("compCode", compCode);
        orderData.put("price", price);
        orderData.put("timestamp", String.valueOf(System.currentTimeMillis()));

        saveToFile(ORDER_DATA_FILE, orderData);
        logger.info("Order data saved - slNo: {}, compCode: {}, price: {}", slNo, compCode, price);
    }

    // Save multiple buy orders to file
    public static void saveBuyOrdersData(List<Map<String, String>> buyOrders) {
        try (Writer writer = new FileWriter(BUY_ORDERS_DATA_FILE)) {
            gson.toJson(buyOrders, writer);
            logger.info("Buy orders data saved - {} orders", buyOrders.size());
        } catch (IOException e) {
            logger.error("Failed to save buy orders data: {}", e.getMessage());
        }
    }

    // Load buy orders data from file
    public static List<Map<String, String>> loadBuyOrdersData() {
        try (Reader reader = new FileReader(BUY_ORDERS_DATA_FILE)) {
            Type listType = new TypeToken<List<Map<String, String>>>(){}.getType();
            List<Map<String, String>> orders = gson.fromJson(reader, listType);
            logger.info("Buy orders data loaded - {} orders", orders != null ? orders.size() : 0);
            return orders;
        } catch (FileNotFoundException e) {
            logger.warn("Buy orders file not found: {}", BUY_ORDERS_DATA_FILE);
            return null;
        } catch (IOException e) {
            logger.error("Failed to load buy orders data: {}", e.getMessage());
            return null;
        }
    }

    // Check if buy orders data file exists
    public static boolean buyOrdersDataExists() {
        return Files.exists(Paths.get(BUY_ORDERS_DATA_FILE));
    }

    // Get all slNo values from buy orders
    public static List<String> getBuyOrderSlNos() {
        List<Map<String, String>> orders = loadBuyOrdersData();
        if (orders != null) {
            List<String> slNos = new java.util.ArrayList<>();
            for (Map<String, String> order : orders) {
                slNos.add(order.get("slNo"));
            }
            return slNos;
        }
        return null;
    }

    // Load order data from file
    public static Map<String, String> loadOrderData() {
        return loadFromFile(ORDER_DATA_FILE);
    }

    // Get slNo from saved order data
    public static String getOrderSlNo() {
        Map<String, String> data = loadOrderData();
        return data != null ? data.get("slNo") : null;
    }

    // Check if order data file exists
    public static boolean orderDataExists() {
        return Files.exists(Paths.get(ORDER_DATA_FILE));
    }

    // ==================== COMPANIES DATA ====================

    // Save companies data to file
    public static void saveCompaniesData(List<Map<String, Object>> companies) {
        try (Writer writer = new FileWriter(COMPANIES_DATA_FILE)) {
            gson.toJson(companies, writer);
            logger.info("Companies data saved - {} companies", companies.size());
        } catch (IOException e) {
            logger.error("Failed to save companies data: {}", e.getMessage());
        }
    }

    // Load companies data from file
    public static List<Map<String, Object>> loadCompaniesData() {
        try (Reader reader = new FileReader(COMPANIES_DATA_FILE)) {
            Type listType = new TypeToken<List<Map<String, Object>>>(){}.getType();
            List<Map<String, Object>> companies = gson.fromJson(reader, listType);
            logger.info("Companies data loaded - {} companies", companies != null ? companies.size() : 0);
            return companies;
        } catch (FileNotFoundException e) {
            logger.warn("Companies file not found: {}", COMPANIES_DATA_FILE);
            return null;
        } catch (IOException e) {
            logger.error("Failed to load companies data: {}", e.getMessage());
            return null;
        }
    }

    // Check if companies data file exists
    public static boolean companiesDataExists() {
        return Files.exists(Paths.get(COMPANIES_DATA_FILE));
    }

    // Get company by code
    public static Map<String, Object> getCompanyByCode(String compCode) {
        List<Map<String, Object>> companies = loadCompaniesData();
        if (companies != null) {
            for (Map<String, Object> company : companies) {
                if (compCode.equals(String.valueOf(company.get("compCode")))) {
                    return company;
                }
            }
        }
        return null;
    }

    // ==================== ALERT DATA ====================

    private static final String ALERT_DATA_FILE = TEST_DATA_DIR + "alert_data.json";

    // Save alert data to file
    public static void saveAlertData(String alertId, String symbolId, String currentValue) {
        Map<String, String> alertData = new HashMap<>();
        alertData.put("alertId", alertId);
        alertData.put("symbolId", symbolId);
        alertData.put("currentValue", currentValue);
        alertData.put("timestamp", String.valueOf(System.currentTimeMillis()));

        saveToFile(ALERT_DATA_FILE, alertData);
        logger.info("Alert data saved - alertId: {}, symbolId: {}, currentValue: {}", alertId, symbolId, currentValue);
    }

    // Load alert data from file
    public static Map<String, String> loadAlertData() {
        return loadFromFile(ALERT_DATA_FILE);
    }

    // Get alertId from saved alert data
    public static String getAlertId() {
        Map<String, String> data = loadAlertData();
        return data != null ? data.get("alertId") : null;
    }

    // Get symbolId from saved alert data
    public static String getAlertSymbolId() {
        Map<String, String> data = loadAlertData();
        return data != null ? data.get("symbolId") : null;
    }

    // Get currentValue from saved alert data
    public static String getAlertCurrentValue() {
        Map<String, String> data = loadAlertData();
        return data != null ? data.get("currentValue") : null;
    }

    // Check if alert data file exists
    public static boolean alertDataExists() {
        return Files.exists(Paths.get(ALERT_DATA_FILE));
    }

    // Clear alert data file
    public static void clearAlertData() {
        try {
            Files.deleteIfExists(Paths.get(ALERT_DATA_FILE));
            logger.info("Alert data file cleared");
        } catch (IOException e) {
            logger.error("Failed to clear alert data file: {}", e.getMessage());
        }
    }

    // ==================== MYCALC DATA ====================

    private static final String MYCALC_DATA_FILE = TEST_DATA_DIR + "mycalc_data.json";

    // Save MyCalc response data to file
    public static void saveMyCalcData(String bal, String prevBal, String with, String depo,
                                       String cashDiv, String stkTransfer, String regTransfer, String fees) {
        Map<String, String> myCalcData = new HashMap<>();
        myCalcData.put("bal", bal);
        myCalcData.put("prevBal", prevBal);
        myCalcData.put("with", with);
        myCalcData.put("depo", depo);
        myCalcData.put("cashDiv", cashDiv);
        myCalcData.put("stkTransfer", stkTransfer);
        myCalcData.put("regTransfer", regTransfer);
        myCalcData.put("fees", fees);
        myCalcData.put("timestamp", String.valueOf(System.currentTimeMillis()));

        saveToFile(MYCALC_DATA_FILE, myCalcData);
        logger.info("MyCalc data saved - bal: {}, prevBal: {}, with: {}, depo: {}, cashDiv: {}, stkTransfer: {}, regTransfer: {}, fees: {}",
                bal, prevBal, with, depo, cashDiv, stkTransfer, regTransfer, fees);
    }

    // Load MyCalc data from file
    public static Map<String, String> loadMyCalcData() {
        return loadFromFile(MYCALC_DATA_FILE);
    }

    // Check if MyCalc data file exists
    public static boolean myCalcDataExists() {
        return Files.exists(Paths.get(MYCALC_DATA_FILE));
    }

    // Get individual MyCalc values
    public static String getMyCalcBal() {
        Map<String, String> data = loadMyCalcData();
        return data != null ? data.get("bal") : null;
    }

    public static String getMyCalcPrevBal() {
        Map<String, String> data = loadMyCalcData();
        return data != null ? data.get("prevBal") : null;
    }

    public static String getMyCalcWith() {
        Map<String, String> data = loadMyCalcData();
        return data != null ? data.get("with") : null;
    }

    public static String getMyCalcDepo() {
        Map<String, String> data = loadMyCalcData();
        return data != null ? data.get("depo") : null;
    }

    public static String getMyCalcCashDiv() {
        Map<String, String> data = loadMyCalcData();
        return data != null ? data.get("cashDiv") : null;
    }

    public static String getMyCalcStkTransfer() {
        Map<String, String> data = loadMyCalcData();
        return data != null ? data.get("stkTransfer") : null;
    }

    public static String getMyCalcRegTransfer() {
        Map<String, String> data = loadMyCalcData();
        return data != null ? data.get("regTransfer") : null;
    }

    public static String getMyCalcFees() {
        Map<String, String> data = loadMyCalcData();
        return data != null ? data.get("fees") : null;
    }

    // ==================== GENERIC METHODS ====================

    // Generic save method for Map<String, String>
    public static void saveToFile(String filePath, Map<String, String> data) {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(data, writer);
            logger.info("Data saved to file: {}", filePath);
        } catch (IOException e) {
            logger.error("Failed to save data to file: {}", e.getMessage());
        }
    }

    // Generic load method for Map<String, String>
    @SuppressWarnings("unchecked")
    public static Map<String, String> loadFromFile(String filePath) {
        try (Reader reader = new FileReader(filePath)) {
            Map<String, String> data = gson.fromJson(reader, Map.class);
            logger.info("Data loaded from file: {}", filePath);
            return data;
        } catch (FileNotFoundException e) {
            logger.warn("File not found: {}", filePath);
            return null;
        } catch (IOException e) {
            logger.error("Failed to load data from file: {}", e.getMessage());
            return null;
        }
    }
}
