# 🍔 Food Ordering System (Java Console Application)

A robust, feature-rich Java console application for managing food ordering workflows. Built using core Java OOP principles, Data Access Object (DAO) architecture pattern, and MySQL database persistence.

---

## ✨ Features

### 👤 Customer Features
- **User Registration & Authentication**: Secure registration with strict email, password, mobile number, and address validation. Security question-based password recovery.
- **Browse Restaurants & Menus**: View open restaurants sorted by rating or category, and browse menu items with real-time stock availability.
- **Smart Shopping Cart**:
  - Add items to cart with cumulative stock checking.
  - Modify item quantities or remove items from cart.
  - Cart state preservation when navigating back or reviewing checkout details.
- **Checkout & Payment Options**:
  - Supports Cash on Delivery (COD) and instant UPI payments.
  - Minimum order amount validation per restaurant.
- **Automatic Bill Generation**:
  - Generates a formatted text file invoice (`Bill_<OrderNumber>.txt`) upon successful order completion.
  - Option to re-generate/download text file bills for any past order.
- **Order Management & Cancellations**: View past order details or cancel pending orders with automatic inventory stock restoration.
- **Ratings & Reviews**: Submit 1 to 5-star ratings and review text for delivered/completed orders, updating restaurant average ratings in real-time.

### 🛡️ Admin Features
- **Restaurant Management**: Add, view, update, delete, or toggle restaurant operating status (OPEN/CLOSED).
- **Menu Management**: Add, update, delete menu items and adjust stock quantities. Automatically marks items as `OUT_OF_STOCK` when quantity hits 0.
- **Order Management**: Track pending, completed, or failed orders, and update order statuses.
- **User Management**: View user accounts and block/unblock customers.

---

## 🛠️ Tech Stack & Architecture

- **Language**: Java (JDK 8+)
- **Database**: MySQL Server
- **Database Connectivity**: JDBC (`mysql-connector-j`)
- **Design Pattern**: Data Access Object (DAO) Pattern

---

## 📂 Project Structure

```
FoodOrderingSystem/
├── src/
│   ├── authentication/
│   │   └── login.java              # Authentication logic
│   ├── dao/
│   │   ├── UserDAO.java            # User database operations
│   │   ├── RestaurantDAO.java      # Restaurant & menu DB operations
│   │   ├── OrderDAO.java           # Order placement & stock DB operations
│   │   └── ReviewDAO.java          # Review & rating DB operations
│   ├── database/
│   │   └── db_connection.java      # JDBC connection setup
│   ├── Model/
│   │   ├── User.java
│   │   ├── Restaurant.java
│   │   ├── Menu.java
│   │   ├── Order.java
│   │   ├── OrderItem.java
│   │   ├── Payment.java
│   │   ├── Review.java
│   │   └── Cart.java
│   └── Main/
│       └── Main.java               # Application entry point & console UI
├── out/                            # Compiled class files
├── .gitignore                      # Git ignore rules
└── README.md                       # Documentation
```

---

## 🚀 How to Run

1. **Prerequisites**:
   - Install Java JDK (8 or higher).
   - Install MySQL Server (e.g. via XAMPP, WAMP, or MySQL installer).

2. **Database Setup**:
   Create a database named `foodordersystem` in MySQL:
   ```sql
   CREATE DATABASE foodordersystem;
   ```

3. **Compilation**:
   Compile the Java source files into the `out` directory:
   ```powershell
   javac -d out (Get-ChildItem -Recurse src/*.java).FullName
   ```

4. **Execution**:
   Run the main class (ensure MySQL connector JAR is in classpath):
   ```powershell
   java -cp "out;lib/mysql-connector-j-8.x.x.jar" Main.Main
   ```
