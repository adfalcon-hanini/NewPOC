package org.example.orders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.utils.ConfigManager;
import org.example.utils.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetOrdersDB {

    private static final Logger logger = LogManager.getLogger(GetOrdersDB.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    private final DatabaseManager dbManager;

    private static final String ACTIVE_ORDERS_QUERY =
            "SELECT sl_no FROM sec_orders WHERE cl_id = ? AND Order_Status = 'WAP'";

    public GetOrdersDB() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public List<Map<String, Object>> getActiveOrders(String nin) {
        logger.info("Fetching active orders for NIN: {}", nin);

        List<Map<String, Object>> orders = new ArrayList<>();
        Connection connection = dbManager.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(ACTIVE_ORDERS_QUERY)) {
            stmt.setString(1, nin);

            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (rs.next()) {
                    Map<String, Object> order = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        Object value = rs.getObject(i);
                        order.put(columnName, value);
                    }
                    orders.add(order);
                }
            }

            logger.info("Found {} active orders for NIN: {}", orders.size(), nin);

        } catch (SQLException e) {
            logger.error("Error fetching active orders: {}", e.getMessage());
            throw new RuntimeException("Error fetching active orders", e);
        }

        return orders;
    }

    // Get active orders using NIN from config
    public List<Map<String, Object>> getActiveOrders() {
        return getActiveOrders(config.getTestNin());
    }

    // Get order by SLNO
    public Map<String, Object> getOrderBySlNo(String nin, String slno) {
        logger.info("Fetching order for NIN: {}, SLNO: {}", nin, slno);

        String query = "SELECT * FROM sec_orders WHERE cl_id = ? AND slno = ?";
        Connection connection = dbManager.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nin);
            stmt.setString(2, slno);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    Map<String, Object> order = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        Object value = rs.getObject(i);
                        order.put(columnName, value);
                    }
                    logger.info("Found order with SLNO: {}", slno);
                    return order;
                }
            }

        } catch (SQLException e) {
            logger.error("Error fetching order by SLNO: {}", e.getMessage());
            throw new RuntimeException("Error fetching order by SLNO", e);
        }

        logger.warn("No order found with SLNO: {}", slno);
        return null;
    }

    // Get count of active orders
    public int getActiveOrdersCount(String nin) {
        logger.info("Counting active orders for NIN: {}", nin);

        String query = "SELECT COUNT(*) FROM sec_orders WHERE cl_id = ? AND Order_Status = 'WAP'";
        Connection connection = dbManager.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nin);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    logger.info("Active orders count for NIN {}: {}", nin, count);
                    return count;
                }
            }

        } catch (SQLException e) {
            logger.error("Error counting active orders: {}", e.getMessage());
            throw new RuntimeException("Error counting active orders", e);
        }

        return 0;
    }

    // Print orders to console
    public void printOrders(List<Map<String, Object>> orders) {
        if (orders == null || orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }

        System.out.println("=== Active Orders (" + orders.size() + ") ===");
        for (int i = 0; i < orders.size(); i++) {
            System.out.println("\n--- Order " + (i + 1) + " ---");
            Map<String, Object> order = orders.get(i);
            for (Map.Entry<String, Object> entry : order.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    // Close database connection
    public void close() {
        dbManager.closeConnection();
    }
}
