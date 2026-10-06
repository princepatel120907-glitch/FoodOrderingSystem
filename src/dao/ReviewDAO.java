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
import Model.Review;

public class ReviewDAO {

    // Add Review and update restaurant average rating
    public boolean addReview(Review review) {
        String insertQuery = "INSERT INTO reviews (user_id, restaurant_id, menu_id, rating, comment, type, review_date, is_verified) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmtUpdateRest = null;
        ResultSet rs = null;

        try {
            conn = db_connection.getConnection();
            if (conn == null) return false;

            conn.setAutoCommit(false);

            pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, review.getUserId());
            pstmt.setInt(2, review.getRestaurantId());
            if (review.getMenuId() > 0) {
                pstmt.setInt(3, review.getMenuId());
            } else {
                pstmt.setNull(3, java.sql.Types.INTEGER);
            }
            pstmt.setInt(4, review.getRating());
            pstmt.setString(5, review.getComment() != null ? review.getComment() : "");
            pstmt.setString(6, review.getType() != null ? review.getType() : "RESTAURANT");
            pstmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            pstmt.setBoolean(8, review.isVerified());

            int affected = pstmt.executeUpdate();

            if (affected > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    review.setId(rs.getInt(1));
                }

                // Recalculate and update restaurant rating & total_ratings in restaurants table
                String updateRestQuery = "UPDATE restaurants SET " +
                        "rating = (SELECT COALESCE(AVG(rating), 0) FROM reviews WHERE restaurant_id = ?), " +
                        "total_ratings = (SELECT COUNT(*) FROM reviews WHERE restaurant_id = ?) " +
                        "WHERE id = ?";

                pstmtUpdateRest = conn.prepareStatement(updateRestQuery);
                pstmtUpdateRest.setInt(1, review.getRestaurantId());
                pstmtUpdateRest.setInt(2, review.getRestaurantId());
                pstmtUpdateRest.setInt(3, review.getRestaurantId());
                pstmtUpdateRest.executeUpdate();

                conn.commit();
                return true;
            } else {
                conn.rollback();
            }

        } catch (SQLException e) {
            System.out.println("Error adding review: " + e.getMessage());
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmtUpdateRest != null) pstmtUpdateRest.close();
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

    // Get Reviews for a Restaurant
    public List<Review> getReviewsByRestaurant(int restaurantId) {
        List<Review> reviews = new ArrayList<>();
        String query = "SELECT r.*, u.name as user_name, rest.name as restaurant_name " +
                "FROM reviews r " +
                "LEFT JOIN users u ON r.user_id = u.id " +
                "LEFT JOIN restaurants rest ON r.restaurant_id = rest.id " +
                "WHERE r.restaurant_id = ? ORDER BY r.review_date DESC";

        try (Connection conn = db_connection.getConnection();
             PreparedStatement pstmt = conn != null ? conn.prepareStatement(query) : null) {

            if (conn == null || pstmt == null) return reviews;
            pstmt.setInt(1, restaurantId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Review rev = extractReviewFromResultSet(rs);
                rev.setUserName(rs.getString("user_name"));
                rev.setRestaurantName(rs.getString("restaurant_name"));
                reviews.add(rev);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    // Get Reviews by User
    public List<Review> getReviewsByUser(int userId) {
        List<Review> reviews = new ArrayList<>();
        String query = "SELECT r.*, rest.name as restaurant_name " +
                "FROM reviews r " +
                "LEFT JOIN restaurants rest ON r.restaurant_id = rest.id " +
                "WHERE r.user_id = ? ORDER BY r.review_date DESC";

        try (Connection conn = db_connection.getConnection();
             PreparedStatement pstmt = conn != null ? conn.prepareStatement(query) : null) {

            if (conn == null || pstmt == null) return reviews;
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Review rev = extractReviewFromResultSet(rs);
                rev.setRestaurantName(rs.getString("restaurant_name"));
                reviews.add(rev);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    // Check if user has a completed/delivered order from restaurant (for verified review)
    public boolean hasUserOrderedFromRestaurant(int userId, int restaurantId) {
        String query = "SELECT id FROM orders WHERE user_id = ? AND restaurant_id = ? AND UPPER(TRIM(status)) IN ('DELIVERED', 'COMPLETED') LIMIT 1";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement pstmt = conn != null ? conn.prepareStatement(query) : null) {
            if (conn == null || pstmt == null) return false;
            pstmt.setInt(1, userId);
            pstmt.setInt(2, restaurantId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Review extractReviewFromResultSet(ResultSet rs) throws SQLException {
        Review rev = new Review();
        rev.setId(rs.getInt("id"));
        rev.setUserId(rs.getInt("user_id"));
        rev.setRestaurantId(rs.getInt("restaurant_id"));
        rev.setMenuId(rs.getInt("menu_id"));
        rev.setRating(rs.getInt("rating"));
        rev.setComment(rs.getString("comment"));
        rev.setType(rs.getString("type"));
        rev.setReviewDate(rs.getTimestamp("review_date"));
        rev.setVerified(rs.getBoolean("is_verified"));
        return rev;
    }
}
