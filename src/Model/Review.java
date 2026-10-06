
package Model;

import java.sql.Timestamp;

public class Review {
    private int id;
    private int userId;
    private int restaurantId;
    private int menuId;
    private int rating; // 1-5
    private String comment;
    private String type; // RESTAURANT / FOOD
    private Timestamp reviewDate;
    private boolean isVerified;

    // For display
    private String userName;
    private String restaurantName;
    private String menuName;

    public Review() {}

    public Review(int id, int userId, int restaurantId, int menuId, int rating,
                  String comment, String type, Timestamp reviewDate, boolean isVerified) {
        this.id = id;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.menuId = menuId;
        this.rating = rating;
        this.comment = comment;
        this.type = type;
        this.reviewDate = reviewDate;
        this.isVerified = isVerified;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getRestaurantId() { return restaurantId; }
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }

    public int getMenuId() { return menuId; }
    public void setMenuId(int menuId) { this.menuId = menuId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Timestamp getReviewDate() { return reviewDate; }
    public void setReviewDate(Timestamp reviewDate) { this.reviewDate = reviewDate; }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getRestaurantName() { return restaurantName; }
    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName; }

    public String getMenuName() { return menuName; }
    public void setMenuName(String menuName) { this.menuName = menuName; }

    @Override
    public String toString() {
        return "Review{" +
                "userId=" + userId +
                ", restaurantId=" + restaurantId +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}