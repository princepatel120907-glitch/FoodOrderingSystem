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
import Model.User;

public class UserDAO {

    // Register new user
    public boolean registerUser(User user) {
        String query = "INSERT INTO users (name, email, password, phone, address, security_question, " +
                "security_answer, role, is_blocked, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = db_connection.getConnection();

            // Check if connection is valid
            if (conn == null || conn.isClosed()) {
                System.out.println("Connection is null or closed!");
                return false;
            }

            pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getPhone());
            pstmt.setString(5, user.getAddress());
            pstmt.setString(6, user.getSecurityQuestion());
            pstmt.setString(7, user.getSecurityAnswer());
            pstmt.setString(8, user.getRole() != null ? user.getRole() : "CUSTOMER");
            pstmt.setInt(9, 0);  // MySQL uses 0 for false

            Timestamp now = new Timestamp(System.currentTimeMillis());
            pstmt.setTimestamp(10, now);
            pstmt.setTimestamp(11, now);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // Login user
    public User loginUser(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ? AND is_blocked = 0";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = db_connection.getConnection();

            if (conn == null || conn.isClosed()) {
                System.out.println("Connection is null or closed!");
                return null;
            }

            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // Get user by ID
    public User getUserById(int userId) {
        String query = "SELECT * FROM users WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = db_connection.getConnection();

            if (conn == null || conn.isClosed()) {
                return null;
            }

            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // Get user by Email
    public User getUserByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = db_connection.getConnection();

            if (conn == null || conn.isClosed()) {
                return null;
            }

            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // Update user profile
    public boolean updateUser(User user) {
        String query = "UPDATE users SET name = ?, phone = ?, address = ?, updated_at = ? WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = db_connection.getConnection();

            if (conn == null || conn.isClosed()) {
                return false;
            }

            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getPhone());
            pstmt.setString(3, user.getAddress());
            pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(5, user.getId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // Change password
    public boolean changePassword(int userId, String newPassword) {
        String query = "UPDATE users SET password = ?, updated_at = ? WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = db_connection.getConnection();

            if (conn == null || conn.isClosed()) {
                return false;
            }

            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, newPassword);
            pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(3, userId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // Forgot password - verify security answer
    public boolean verifySecurityAnswer(String email, String answer) {
        String query = "SELECT * FROM users WHERE email = ? AND security_answer = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = db_connection.getConnection();

            if (conn == null || conn.isClosed()) {
                return false;
            }

            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            pstmt.setString(2, answer);

            rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // Reset password using security question
    public boolean resetPassword(String email, String newPassword) {
        String query = "UPDATE users SET password = ?, updated_at = ? WHERE email = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = db_connection.getConnection();

            if (conn == null || conn.isClosed()) {
                return false;
            }

            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, newPassword);
            pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            pstmt.setString(3, email);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // Block/Unblock user
    public boolean toggleBlockUser(int userId, boolean block) {
        String query = "UPDATE users SET is_blocked = ?, updated_at = ? WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = db_connection.getConnection();

            if (conn == null || conn.isClosed()) {
                return false;
            }

            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, block ? 1 : 0);
            pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(3, userId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // Get all users
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users ORDER BY id";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = db_connection.getConnection();

            if (conn == null || conn.isClosed()) {
                return users;
            }

            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    // Get all customers
    public List<User> getAllCustomers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role = 'CUSTOMER' ORDER BY id";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = db_connection.getConnection();

            if (conn == null || conn.isClosed()) {
                return users;
            }

            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    // Delete user
    public boolean deleteUser(int userId) {
        String query = "DELETE FROM users WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = db_connection.getConnection();

            if (conn == null || conn.isClosed()) {
                return false;
            }

            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, userId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // Check if email already exists
    public boolean isEmailExists(String email) {
        String query = "SELECT id FROM users WHERE email = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = db_connection.getConnection();

            if (conn == null || conn.isClosed()) {
                System.out.println("Connection is null or closed!");
                return false;
            }

            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // Helper method to extract User from ResultSet
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setPhone(rs.getString("phone"));
        user.setAddress(rs.getString("address"));
        user.setSecurityQuestion(rs.getString("security_question"));
        user.setSecurityAnswer(rs.getString("security_answer"));
        user.setRole(rs.getString("role"));
        user.setBlocked(rs.getInt("is_blocked") == 1);
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setUpdatedAt(rs.getTimestamp("updated_at"));
        return user;
    }
}