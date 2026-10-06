package Model;


import java.sql.Time;
import java.sql.Timestamp;

public class Restaurant {
    private int id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String category; // VEG / NON_VEG / BOTH
    private String cuisine;
    private String status; // OPEN / CLOSED
    private double rating;
    private int totalRatings;
    private Time openingTime;
    private Time closingTime;
    private double deliveryCharge;
    private double minOrderAmount;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Default Constructor
    public Restaurant() {}

    // Parameterized Constructor
    public Restaurant(int id, String name, String address, String phone, String email,
                      String category, String cuisine, String status, double rating,
                      int totalRatings, Time openingTime, Time closingTime,
                      double deliveryCharge, double minOrderAmount) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.category = category;
        this.cuisine = cuisine;
        this.status = status;
        this.rating = rating;
        this.totalRatings = totalRatings;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.deliveryCharge = deliveryCharge;
        this.minOrderAmount = minOrderAmount;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getCuisine() { return cuisine; }
    public void setCuisine(String cuisine) { this.cuisine = cuisine; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public int getTotalRatings() { return totalRatings; }
    public void setTotalRatings(int totalRatings) { this.totalRatings = totalRatings; }

    public Time getOpeningTime() { return openingTime; }
    public void setOpeningTime(Time openingTime) { this.openingTime = openingTime; }

    public Time getClosingTime() { return closingTime; }
    public void setClosingTime(Time closingTime) { this.closingTime = closingTime; }

    public double getDeliveryCharge() { return deliveryCharge; }
    public void setDeliveryCharge(double deliveryCharge) { this.deliveryCharge = deliveryCharge; }

    public double getMinOrderAmount() { return minOrderAmount; }
    public void setMinOrderAmount(double minOrderAmount) { this.minOrderAmount = minOrderAmount; }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", cuisine='" + cuisine + '\'' +
                ", status='" + status + '\'' +
                ", rating=" + rating +
                '}';
    }
}

