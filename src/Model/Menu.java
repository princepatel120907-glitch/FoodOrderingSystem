package Model;

public class Menu {
    private int id;
    private int restaurantId;
    private String name;
    private String description;
    private double price;
    private String category; // VEG / NON_VEG
    private String cuisine;
    private String availability; // AVAILABLE / OUT_OF_STOCK
    private int stockQuantity;
    private boolean isFeatured;
    private int preparationTime; // in minutes

    public Menu() {}

    public Menu(int id, int restaurantId, String name, String description, double price,
                String category, String cuisine, String availability, int stockQuantity,
                boolean isFeatured, int preparationTime) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.cuisine = cuisine;
        this.availability = availability;
        this.stockQuantity = stockQuantity;
        this.isFeatured = isFeatured;
        this.preparationTime = preparationTime;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getRestaurantId() { return restaurantId; }
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getCuisine() { return cuisine; }
    public void setCuisine(String cuisine) { this.cuisine = cuisine; }

    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public boolean isFeatured() { return isFeatured; }
    public void setFeatured(boolean featured) { isFeatured = featured; }

    public int getPreparationTime() { return preparationTime; }
    public void setPreparationTime(int preparationTime) { this.preparationTime = preparationTime; }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", availability='" + availability + '\'' +
                ", stockQuantity=" + stockQuantity +
                '}';
    }
}
