package Model;

import java.sql.Timestamp;

public class Cart {
    private int id;
    private int userId;
    private int menuId;
    private int quantity;
    private double price; // Price at add time
    private Timestamp addedAt;

    // For display purposes
    private String menuName;
    private String restaurantName;
    private int restaurantId;

    public Cart() {}

    public Cart(int id, int userId, int menuId, int quantity, double price, Timestamp addedAt) {
        this.id = id;
        this.userId = userId;
        this.menuId = menuId;
        this.quantity = quantity;
        this.price = price;
        this.addedAt = addedAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getMenuId() { return menuId; }
    public void setMenuId(int menuId) { this.menuId = menuId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public Timestamp getAddedAt() { return addedAt; }
    public void setAddedAt(Timestamp addedAt) { this.addedAt = addedAt; }

    public String getMenuName() { return menuName; }
    public void setMenuName(String menuName) { this.menuName = menuName; }

    public String getRestaurantName() { return restaurantName; }
    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName; }

    public int getRestaurantId() { return restaurantId; }
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }

    public double getSubtotal() {
        return price * quantity;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "menuId=" + menuId +
                ", quantity=" + quantity +
                ", price=" + price +
                ", subtotal=" + getSubtotal() +
                '}';
    }
}
