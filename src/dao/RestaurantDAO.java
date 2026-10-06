package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import database.db_connection;
import Model.Menu;
import Model.Restaurant;

public class RestaurantDAO {

    // ==================== RESTAURANT OPERATIONS ====================

    // Add Restaurant
    public boolean addRestaurant(Restaurant restaurant) {
        String query = "INSERT INTO restaurants (name, address, phone, email, category, cuisine, " +
                "status, rating, total_ratings, opening_time, closing_time, " +
                "delivery_charge, min_order_amount) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = db_connection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, restaurant.getName());
            pstmt.setString(2, restaurant.getAddress());
            pstmt.setString(3, restaurant.getPhone());
            pstmt.setString(4, restaurant.getEmail());
            pstmt.setString(5, restaurant.getCategory());
            pstmt.setString(6, restaurant.getCuisine());
            pstmt.setString(7, restaurant.getStatus() != null ? restaurant.getStatus() : "OPEN");
            pstmt.setDouble(8, 0.0);
            pstmt.setInt(9, 0);
            pstmt.setTime(10, restaurant.getOpeningTime());
            pstmt.setTime(11, restaurant.getClosingTime());
            pstmt.setDouble(12, restaurant.getDeliveryCharge());
            pstmt.setDouble(13, restaurant.getMinOrderAmount());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    restaurant.setId(rs.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update Restaurant
    public boolean updateRestaurant(Restaurant restaurant) {
        String query = "UPDATE restaurants SET name = ?, address = ?, phone = ?, email = ?, " +
                "category = ?, cuisine = ?, status = ?, opening_time = ?, closing_time = ?, " +
                "delivery_charge = ?, min_order_amount = ? WHERE id = ?";

        try (Connection conn = db_connection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, restaurant.getName());
            pstmt.setString(2, restaurant.getAddress());
            pstmt.setString(3, restaurant.getPhone());
            pstmt.setString(4, restaurant.getEmail());
            pstmt.setString(5, restaurant.getCategory());
            pstmt.setString(6, restaurant.getCuisine());
            pstmt.setString(7, restaurant.getStatus());
            pstmt.setTime(8, restaurant.getOpeningTime());
            pstmt.setTime(9, restaurant.getClosingTime());
            pstmt.setDouble(10, restaurant.getDeliveryCharge());
            pstmt.setDouble(11, restaurant.getMinOrderAmount());
            pstmt.setInt(12, restaurant.getId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete Restaurant
    public boolean deleteRestaurant(int restaurantId) {
        String query = "DELETE FROM restaurants WHERE id = ?";

        try (Connection conn = db_connection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, restaurantId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get Restaurant by ID
    public Restaurant getRestaurantById(int restaurantId) {
        String query = "SELECT * FROM restaurants WHERE id = ?";

        try (Connection conn = db_connection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, restaurantId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractRestaurantFromResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get all Restaurants
    public List<Restaurant> getAllRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();
        String query = "SELECT * FROM restaurants ORDER BY id";

        try (Connection conn = db_connection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                restaurants.add(extractRestaurantFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return restaurants;
    }

    // Get Open Restaurants
    public List<Restaurant> getOpenRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();
        String query = "SELECT * FROM restaurants WHERE status = 'OPEN' ORDER BY rating DESC";

        try (Connection conn = db_connection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                restaurants.add(extractRestaurantFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return restaurants;
    }

    // Toggle Restaurant Status (OPEN/CLOSED)
    public boolean toggleRestaurantStatus(int restaurantId) {
        String query = "UPDATE restaurants SET status = CASE WHEN status = 'OPEN' THEN 'CLOSED' " +
                "ELSE 'OPEN' END WHERE id = ?";

        try (Connection conn = db_connection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, restaurantId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Helper method to extract Restaurant from ResultSet
    private Restaurant extractRestaurantFromResultSet(ResultSet rs) throws SQLException {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(rs.getInt("id"));
        restaurant.setName(rs.getString("name"));
        restaurant.setAddress(rs.getString("address"));
        restaurant.setPhone(rs.getString("phone"));
        restaurant.setEmail(rs.getString("email"));
        restaurant.setCategory(rs.getString("category"));
        restaurant.setCuisine(rs.getString("cuisine"));
        restaurant.setStatus(rs.getString("status"));
        restaurant.setRating(rs.getDouble("rating"));
        restaurant.setTotalRatings(rs.getInt("total_ratings"));
        restaurant.setOpeningTime(rs.getTime("opening_time"));
        restaurant.setClosingTime(rs.getTime("closing_time"));
        restaurant.setDeliveryCharge(rs.getDouble("delivery_charge"));
        restaurant.setMinOrderAmount(rs.getDouble("min_order_amount"));
        return restaurant;
    }

    // ==================== MENU OPERATIONS ====================

    // Add Menu Item
    public boolean addMenuItem(Menu menu) {
        String query = "INSERT INTO menu (restaurant_id, name, description, price, category, " +
                "cuisine, availability, stock_quantity, is_featured, preparation_time) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = db_connection.getConnection();
             PreparedStatement pstmt = conn != null ? conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS) : null) {

            if (conn == null || pstmt == null) return false;
            String availability = (menu.getStockQuantity() <= 0) ? "OUT_OF_STOCK" : (menu.getAvailability() != null ? menu.getAvailability() : "AVAILABLE");

            pstmt.setInt(1, menu.getRestaurantId());
            pstmt.setString(2, menu.getName());
            pstmt.setString(3, menu.getDescription());
            pstmt.setDouble(4, menu.getPrice());
            pstmt.setString(5, menu.getCategory());
            pstmt.setString(6, menu.getCuisine());
            pstmt.setString(7, availability);
            pstmt.setInt(8, menu.getStockQuantity());
            pstmt.setBoolean(9, menu.isFeatured());
            pstmt.setInt(10, menu.getPreparationTime());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    menu.setId(rs.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update Menu Item
    public boolean updateMenuItem(Menu menu) {
        String query = "UPDATE menu SET name = ?, description = ?, price = ?, category = ?, " +
                "cuisine = ?, availability = ?, stock_quantity = ?, is_featured = ?, " +
                "preparation_time = ? WHERE id = ?";

        try (Connection conn = db_connection.getConnection();
             PreparedStatement pstmt = conn != null ? conn.prepareStatement(query) : null) {

            if (conn == null || pstmt == null) return false;
            String availability = (menu.getStockQuantity() <= 0) ? "OUT_OF_STOCK" : (menu.getAvailability() != null ? menu.getAvailability() : "AVAILABLE");

            pstmt.setString(1, menu.getName());
            pstmt.setString(2, menu.getDescription());
            pstmt.setDouble(3, menu.getPrice());
            pstmt.setString(4, menu.getCategory());
            pstmt.setString(5, menu.getCuisine());
            pstmt.setString(6, availability);
            pstmt.setInt(7, menu.getStockQuantity());
            pstmt.setBoolean(8, menu.isFeatured());
            pstmt.setInt(9, menu.getPreparationTime());
            pstmt.setInt(10, menu.getId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete Menu Item
    public boolean deleteMenuItem(int menuId) {
        String query = "DELETE FROM menu WHERE id = ?";

        try (Connection conn = db_connection.getConnection();
             PreparedStatement pstmt = conn != null ? conn.prepareStatement(query) : null) {

            if (conn == null || pstmt == null) return false;
            pstmt.setInt(1, menuId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get Menu Item by ID
    public Menu getMenuItemById(int menuId) {
        String query = "SELECT * FROM menu WHERE id = ?";

        try (Connection conn = db_connection.getConnection();
             PreparedStatement pstmt = conn != null ? conn.prepareStatement(query) : null) {

            if (conn == null || pstmt == null) return null;
            pstmt.setInt(1, menuId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractMenuFromResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get Menu Items by Restaurant (or all if restaurantId <= 0)
    public List<Menu> getMenuItemsByRestaurant(int restaurantId) {
        List<Menu> menuItems = new ArrayList<>();
        String query = (restaurantId > 0)
                ? "SELECT * FROM menu WHERE restaurant_id = ? ORDER BY name"
                : "SELECT * FROM menu ORDER BY name";

        try (Connection conn = db_connection.getConnection();
             PreparedStatement pstmt = conn != null ? conn.prepareStatement(query) : null) {

            if (conn == null || pstmt == null) return menuItems;
            if (restaurantId > 0) {
                pstmt.setInt(1, restaurantId);
            }
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                menuItems.add(extractMenuFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuItems;
    }

    // Update Stock Quantity
    public boolean updateStockQuantity(int menuId, int quantity) {
        String query = "UPDATE menu SET stock_quantity = ?, availability = CASE " +
                "WHEN ? > 0 THEN 'AVAILABLE' ELSE 'OUT_OF_STOCK' END WHERE id = ?";

        try (Connection conn = db_connection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, quantity);
            pstmt.setInt(2, quantity);
            pstmt.setInt(3, menuId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Reduce Stock (after order)
    public boolean reduceStock(int menuId, int quantity) {
        String query = "UPDATE menu SET stock_quantity = stock_quantity - ?, " +
                "availability = CASE WHEN stock_quantity - ? > 0 THEN 'AVAILABLE' " +
                "ELSE 'OUT_OF_STOCK' END WHERE id = ? AND stock_quantity >= ?";

        try (Connection conn = db_connection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, quantity);
            pstmt.setInt(2, quantity);
            pstmt.setInt(3, menuId);
            pstmt.setInt(4, quantity);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Helper method to extract Menu from ResultSet
    private Menu extractMenuFromResultSet(ResultSet rs) throws SQLException {
        Menu menu = new Menu();
        menu.setId(rs.getInt("id"));
        menu.setRestaurantId(rs.getInt("restaurant_id"));
        menu.setName(rs.getString("name"));
        menu.setDescription(rs.getString("description"));
        menu.setPrice(rs.getDouble("price"));
        menu.setCategory(rs.getString("category"));
        menu.setCuisine(rs.getString("cuisine"));
        menu.setAvailability(rs.getString("availability"));
        menu.setStockQuantity(rs.getInt("stock_quantity"));
        menu.setFeatured(rs.getBoolean("is_featured"));
        menu.setPreparationTime(rs.getInt("preparation_time"));
        return menu;
    }
}