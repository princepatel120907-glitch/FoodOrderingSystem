package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import database.db_connection;
import Model.Order;
import Model.OrderItem;

public class OrderDAO {

    // Place Order
    public int placeOrder(Order order, List<OrderItem> items) {
        Connection conn = null;
        PreparedStatement pstmtOrder = null;
        PreparedStatement pstmtItem = null;
        PreparedStatement pstmtStock = null;
        ResultSet rs = null;
        int orderId = -1;

        try {
            conn = db_connection.getConnection();
            if (conn == null) {
                System.out.println("Order placement error: Unable to connect to database.");
                return -1;
            }
            conn.setAutoCommit(false);

            String fullOrderQuery = "INSERT INTO orders (user_id, restaurant_id, order_number, total_amount, " +
                    "discount, coupon_code, final_amount, status, payment_status, " +
                    "delivery_address, delivery_instructions, estimated_delivery_time, " +
                    "actual_delivery_time, order_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            pstmtOrder = conn.prepareStatement(fullOrderQuery, Statement.RETURN_GENERATED_KEYS);
            pstmtOrder.setInt(1, order.getUserId());
            pstmtOrder.setInt(2, order.getRestaurantId());
            pstmtOrder.setString(3, order.getOrderNumber() != null ? order.getOrderNumber() : ("ORD" + System.currentTimeMillis()));
            pstmtOrder.setDouble(4, order.getTotalAmount());
            pstmtOrder.setDouble(5, order.getDiscount());
            pstmtOrder.setString(6, order.getCouponCode() != null ? order.getCouponCode() : "");
            pstmtOrder.setDouble(7, order.getFinalAmount());
            pstmtOrder.setString(8, order.getStatus() != null ? order.getStatus() : "PENDING");
            pstmtOrder.setString(9, order.getPaymentStatus() != null ? order.getPaymentStatus() : "PENDING");
            pstmtOrder.setString(10, (order.getDeliveryAddress() != null && !order.getDeliveryAddress().trim().isEmpty()) ? order.getDeliveryAddress() : "Default Address");
            pstmtOrder.setString(11, order.getDeliveryInstructions() != null ? order.getDeliveryInstructions() : "");
            pstmtOrder.setInt(12, order.getEstimatedDeliveryTime() > 0 ? order.getEstimatedDeliveryTime() : 30);
            pstmtOrder.setNull(13, java.sql.Types.TIMESTAMP);
            pstmtOrder.setTimestamp(14, new Timestamp(System.currentTimeMillis()));
            pstmtOrder.executeUpdate();

            rs = pstmtOrder.getGeneratedKeys();
            if (rs.next()) {
                orderId = rs.getInt(1);
                order.setId(orderId);
            } else {
                conn.rollback();
                return -1;
            }

            // Insert Order Items (standard columns: order_id, menu_id, quantity, price, subtotal)
            String itemQuery = "INSERT INTO order_items (order_id, menu_id, quantity, price, subtotal) VALUES (?, ?, ?, ?, ?)";
            pstmtItem = conn.prepareStatement(itemQuery);

            // Deduct Menu Stock and update availability status
            String stockQuery = "UPDATE menu SET stock_quantity = stock_quantity - ?, " +
                    "availability = CASE WHEN stock_quantity - ? <= 0 THEN 'OUT_OF_STOCK' ELSE 'AVAILABLE' END " +
                    "WHERE id = ?";
            pstmtStock = conn.prepareStatement(stockQuery);

            for (OrderItem item : items) {
                pstmtItem.setInt(1, orderId);
                pstmtItem.setInt(2, item.getMenuId());
                pstmtItem.setInt(3, item.getQuantity());
                pstmtItem.setDouble(4, item.getPrice());
                pstmtItem.setDouble(5, item.getSubtotal());
                pstmtItem.addBatch();

                pstmtStock.setInt(1, item.getQuantity());
                pstmtStock.setInt(2, item.getQuantity());
                pstmtStock.setInt(3, item.getMenuId());
                pstmtStock.addBatch();
            }

            pstmtItem.executeBatch();
            pstmtStock.executeBatch();

            conn.commit();
            return orderId;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmtOrder != null) pstmtOrder.close();
                if (pstmtItem != null) pstmtItem.close();
                if (pstmtStock != null) pstmtStock.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    // Get Order by ID
    public Order getOrderById(int orderId) {
        String query = "SELECT o.*, r.name as restaurant_name, u.name as user_name " +
                "FROM orders o " +
                "LEFT JOIN restaurants r ON o.restaurant_id = r.id " +
                "LEFT JOIN users u ON o.user_id = u.id " +
                "WHERE o.id = ?";

        try (Connection conn = db_connection.getConnection();
             PreparedStatement pstmt = conn != null ? conn.prepareStatement(query) : null) {

            if (conn == null || pstmt == null) return null;
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Order order = extractOrderFromResultSet(rs);
                String restName = rs.getString("restaurant_name");
                order.setRestaurantName(restName != null ? restName : "Unknown Restaurant");
                order.setUserName(rs.getString("user_name"));
                return order;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get Orders by User ID
    public List<Order> getOrdersByUser(int userId) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT o.*, r.name as restaurant_name FROM orders o " +
                "LEFT JOIN restaurants r ON o.restaurant_id = r.id " +
                "WHERE o.user_id = ? ORDER BY o.order_date DESC";

        try (Connection conn = db_connection.getConnection();
             PreparedStatement pstmt = conn != null ? conn.prepareStatement(query) : null) {

            if (conn == null || pstmt == null) return orders;
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Order order = extractOrderFromResultSet(rs);
                String restName = rs.getString("restaurant_name");
                order.setRestaurantName(restName != null ? restName : "Unknown Restaurant");
                orders.add(order);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching user orders: " + e.getMessage());
            e.printStackTrace();
        }
        return orders;
    }

    // Get Orders by Restaurant ID (Admin)
    public List<Order> getOrdersByRestaurant(int restaurantId) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT o.*, u.name as user_name FROM orders o " +
                "LEFT JOIN users u ON o.user_id = u.id " +
                "WHERE o.restaurant_id = ? ORDER BY o.order_date DESC";

        try (Connection conn = db_connection.getConnection();
             PreparedStatement pstmt = conn != null ? conn.prepareStatement(query) : null) {

            if (conn == null || pstmt == null) return orders;
            pstmt.setInt(1, restaurantId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Order order = extractOrderFromResultSet(rs);
                order.setUserName(rs.getString("user_name"));
                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    // Get All Orders (Admin)
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT o.*, r.name as restaurant_name, u.name as user_name " +
                "FROM orders o " +
                "LEFT JOIN restaurants r ON o.restaurant_id = r.id " +
                "LEFT JOIN users u ON o.user_id = u.id " +
                "ORDER BY o.order_date DESC";

        try (Connection conn = db_connection.getConnection();
             PreparedStatement pstmt = conn != null ? conn.prepareStatement(query) : null) {

            if (conn == null || pstmt == null) return orders;
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Order order = extractOrderFromResultSet(rs);
                order.setRestaurantName(rs.getString("restaurant_name"));
                order.setUserName(rs.getString("user_name"));
                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    // Update Order Status
    public boolean updateOrderStatus(int orderId, String status) {
        if ("CANCELLED".equalsIgnoreCase(status) || "FAILED".equalsIgnoreCase(status)) {
            return cancelOrder(orderId);
        }

        String query = "UPDATE orders SET status = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmtCheck = null;
        PreparedStatement pstmtUpdateOrderPay = null;
        PreparedStatement pstmtUpdatePay = null;
        ResultSet rs = null;

        try {
            conn = db_connection.getConnection();
            if (conn == null) return false;
            conn.setAutoCommit(false);

            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, status);
            pstmt.setInt(2, orderId);
            int updated = pstmt.executeUpdate();

            if (updated > 0) {
                if ("DELIVERED".equalsIgnoreCase(status) || "COMPLETED".equalsIgnoreCase(status)) {
                    String checkQuery = "SELECT method FROM payments WHERE order_id = ?";
                    pstmtCheck = conn.prepareStatement(checkQuery);
                    pstmtCheck.setInt(1, orderId);
                    rs = pstmtCheck.executeQuery();
                    if (rs.next()) {
                        String method = rs.getString("method");
                        if ("CASH".equalsIgnoreCase(method)) {
                            // Update order payment status in orders table
                            String updateOrderPayQuery = "UPDATE orders SET payment_status = 'COMPLETED' WHERE id = ?";
                            pstmtUpdateOrderPay = conn.prepareStatement(updateOrderPayQuery);
                            pstmtUpdateOrderPay.setInt(1, orderId);
                            pstmtUpdateOrderPay.executeUpdate();

                            // Update payment status in payments table
                            String updatePayQuery = "UPDATE payments SET status = 'COMPLETED' WHERE order_id = ?";
                            pstmtUpdatePay = conn.prepareStatement(updatePayQuery);
                            pstmtUpdatePay.setInt(1, orderId);
                            pstmtUpdatePay.executeUpdate();
                        }
                    }
                }
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error updating order status: " + e.getMessage());
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmtCheck != null) pstmtCheck.close();
                if (pstmtUpdateOrderPay != null) pstmtUpdateOrderPay.close();
                if (pstmtUpdatePay != null) pstmtUpdatePay.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // Update Payment Status
    public boolean updatePaymentStatus(int orderId, String paymentStatus) {
        String query = "UPDATE orders SET payment_status = ? WHERE id = ?";

        try (Connection conn = db_connection.getConnection();
             PreparedStatement pstmt = conn != null ? conn.prepareStatement(query) : null) {

            if (conn == null || pstmt == null) return false;
            pstmt.setString(1, paymentStatus);
            pstmt.setInt(2, orderId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cancel Order and restore inventory stock
    public boolean cancelOrder(int orderId) {
        Connection conn = null;
        PreparedStatement pstmtStatus = null;
        PreparedStatement pstmtPayStatus = null;
        PreparedStatement pstmtGetItems = null;
        PreparedStatement pstmtRestoreStock = null;
        PreparedStatement pstmtGetOrder = null;
        ResultSet rs = null;

        try {
            conn = db_connection.getConnection();
            if (conn == null) return false;
            conn.setAutoCommit(false);

            String getOrderQuery = "SELECT status, payment_status FROM orders WHERE id = ?";
            pstmtGetOrder = conn.prepareStatement(getOrderQuery);
            pstmtGetOrder.setInt(1, orderId);
            rs = pstmtGetOrder.executeQuery();

            if (!rs.next()) {
                conn.rollback();
                return false;
            }

            String currentStatus = rs.getString("status");
            String currentPayStatus = rs.getString("payment_status");
            rs.close();
            pstmtGetOrder.close();

            if (currentStatus == null || !("PENDING".equalsIgnoreCase(currentStatus.trim()) ||
                                           "CONFIRMED".equalsIgnoreCase(currentStatus.trim()) ||
                                           "PREPARING".equalsIgnoreCase(currentStatus.trim()))) {
                conn.rollback();
                return false;
            }

            String newPayStatus = "COMPLETED".equalsIgnoreCase(currentPayStatus) ? "REFUNDED" : "CANCELLED";

            // 1. Update order status and payment_status
            String updateOrderQuery = "UPDATE orders SET status = 'CANCELLED', payment_status = ? WHERE id = ?";
            pstmtStatus = conn.prepareStatement(updateOrderQuery);
            pstmtStatus.setString(1, newPayStatus);
            pstmtStatus.setInt(2, orderId);
            pstmtStatus.executeUpdate();

            // 2. Update payments table if record exists
            String updatePayQuery = "UPDATE payments SET status = ? WHERE order_id = ?";
            pstmtPayStatus = conn.prepareStatement(updatePayQuery);
            pstmtPayStatus.setString(1, newPayStatus);
            pstmtPayStatus.setInt(2, orderId);
            pstmtPayStatus.executeUpdate();

            // 3. Restore stock to menu table
            String getItemsQuery = "SELECT menu_id, quantity FROM order_items WHERE order_id = ?";
            pstmtGetItems = conn.prepareStatement(getItemsQuery);
            pstmtGetItems.setInt(1, orderId);
            rs = pstmtGetItems.executeQuery();

            String restoreStockQuery = "UPDATE menu SET stock_quantity = stock_quantity + ?, availability = 'AVAILABLE' WHERE id = ?";
            pstmtRestoreStock = conn.prepareStatement(restoreStockQuery);

            while (rs.next()) {
                int menuId = rs.getInt("menu_id");
                int qty = rs.getInt("quantity");
                pstmtRestoreStock.setInt(1, qty);
                pstmtRestoreStock.setInt(2, menuId);
                pstmtRestoreStock.addBatch();
            }
            pstmtRestoreStock.executeBatch();

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.out.println("Error cancelling order: " + e.getMessage());
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmtGetOrder != null) pstmtGetOrder.close();
                if (pstmtStatus != null) pstmtStatus.close();
                if (pstmtPayStatus != null) pstmtPayStatus.close();
                if (pstmtGetItems != null) pstmtGetItems.close();
                if (pstmtRestoreStock != null) pstmtRestoreStock.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // Update Delivery Time
    public boolean updateDeliveryTime(int orderId, Timestamp actualDeliveryTime) {
        String query = "UPDATE orders SET actual_delivery_time = ? WHERE id = ?";

        try (Connection conn = db_connection.getConnection();
             PreparedStatement pstmt = conn != null ? conn.prepareStatement(query) : null) {

            if (conn == null || pstmt == null) return false;
            pstmt.setTimestamp(1, actualDeliveryTime);
            pstmt.setInt(2, orderId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get Order Items
    public List<OrderItem> getOrderItems(int orderId) {
        List<OrderItem> items = new ArrayList<>();
        String query = "SELECT oi.*, m.name as menu_name FROM order_items oi " +
                "LEFT JOIN menu m ON oi.menu_id = m.id " +
                "WHERE oi.order_id = ?";

        try (Connection conn = db_connection.getConnection();
             PreparedStatement pstmt = conn != null ? conn.prepareStatement(query) : null) {

            if (conn == null || pstmt == null) return items;
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                OrderItem item = new OrderItem();
                item.setId(rs.getInt("id"));
                item.setOrderId(rs.getInt("order_id"));
                item.setMenuId(rs.getInt("menu_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setPrice(rs.getDouble("price"));
                item.setSubtotal(rs.getDouble("subtotal"));
                try {
                    item.setSpecialInstructions(rs.getString("special_instructions"));
                } catch (SQLException ignore) {
                    item.setSpecialInstructions("");
                }
                item.setMenuName(rs.getString("menu_name"));
                items.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    // Get Orders by Status
    public List<Order> getOrdersByStatus(String status) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT o.*, r.name as restaurant_name, u.name as user_name " +
                "FROM orders o " +
                "LEFT JOIN restaurants r ON o.restaurant_id = r.id " +
                "LEFT JOIN users u ON o.user_id = u.id " +
                "WHERE UPPER(TRIM(o.status)) = UPPER(TRIM(?)) ORDER BY o.order_date DESC";

        try (Connection conn = db_connection.getConnection();
             PreparedStatement pstmt = conn != null ? conn.prepareStatement(query) : null) {

            if (conn == null || pstmt == null) return orders;
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Order order = extractOrderFromResultSet(rs);
                order.setRestaurantName(rs.getString("restaurant_name"));
                order.setUserName(rs.getString("user_name"));
                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    // Get Pending/Active Orders (PENDING, CONFIRMED, PREPARING, OUT_FOR_DELIVERY)
    public List<Order> getPendingOrders() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT o.*, r.name as restaurant_name, u.name as user_name " +
                "FROM orders o " +
                "LEFT JOIN restaurants r ON o.restaurant_id = r.id " +
                "LEFT JOIN users u ON o.user_id = u.id " +
                "WHERE UPPER(TRIM(o.status)) IN ('PENDING', 'CONFIRMED', 'PREPARING', 'OUT_FOR_DELIVERY') " +
                "ORDER BY o.order_date DESC";

        try (Connection conn = db_connection.getConnection();
             PreparedStatement pstmt = conn != null ? conn.prepareStatement(query) : null) {

            if (conn == null || pstmt == null) return orders;
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Order order = extractOrderFromResultSet(rs);
                order.setRestaurantName(rs.getString("restaurant_name"));
                order.setUserName(rs.getString("user_name"));
                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    // Get Orders for Delivery
    public List<Order> getOrdersForDelivery() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT o.*, r.name as restaurant_name, u.name as user_name, u.address " +
                "FROM orders o " +
                "LEFT JOIN restaurants r ON o.restaurant_id = r.id " +
                "LEFT JOIN users u ON o.user_id = u.id " +
                "WHERE UPPER(TRIM(o.status)) IN ('CONFIRMED', 'PREPARING') " +
                "ORDER BY o.order_date ASC";

        try (Connection conn = db_connection.getConnection();
             PreparedStatement pstmt = conn != null ? conn.prepareStatement(query) : null) {

            if (conn == null || pstmt == null) return orders;
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Order order = extractOrderFromResultSet(rs);
                order.setRestaurantName(rs.getString("restaurant_name"));
                order.setUserName(rs.getString("user_name"));
                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    // Helper method to extract Order from ResultSet
    private Order extractOrderFromResultSet(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setUserId(rs.getInt("user_id"));
        order.setRestaurantId(rs.getInt("restaurant_id"));
        order.setOrderNumber(rs.getString("order_number"));
        order.setTotalAmount(rs.getDouble("total_amount"));
        order.setDiscount(rs.getDouble("discount"));
        order.setCouponCode(rs.getString("coupon_code"));
        order.setFinalAmount(rs.getDouble("final_amount"));
        order.setStatus(rs.getString("status"));
        order.setPaymentStatus(rs.getString("payment_status"));
        order.setDeliveryAddress(rs.getString("delivery_address"));
        order.setDeliveryInstructions(rs.getString("delivery_instructions"));
        order.setEstimatedDeliveryTime(rs.getInt("estimated_delivery_time"));
        order.setActualDeliveryTime(rs.getTimestamp("actual_delivery_time"));
        order.setOrderDate(rs.getTimestamp("order_date"));
        return order;
    }

    // Record Payment
    public boolean recordPayment(int orderId, double amount, String method, String status, String transactionId, String upiId) {
        String query = "INSERT INTO payments (order_id, amount, method, status, transaction_id, payment_date, upi_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement pstmt = conn != null ? conn.prepareStatement(query) : null) {
            if (conn == null || pstmt == null) return false;
            pstmt.setInt(1, orderId);
            pstmt.setDouble(2, amount);
            pstmt.setString(3, method);
            pstmt.setString(4, status);
            pstmt.setString(5, transactionId);
            pstmt.setTimestamp(6, new java.sql.Timestamp(System.currentTimeMillis()));
            pstmt.setString(7, upiId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error recording payment: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
