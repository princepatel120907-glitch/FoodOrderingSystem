package Main;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import dao.OrderDAO;
import dao.RestaurantDAO;
import dao.ReviewDAO;
import dao.UserDAO;
import Model.Cart;
import Model.Menu;
import Model.Order;
import Model.OrderItem;
import Model.Restaurant;
import Model.Review;
import Model.User;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static UserDAO userDAO = new UserDAO();
    private static RestaurantDAO restaurantDAO = new RestaurantDAO();
    private static OrderDAO orderDAO = new OrderDAO();
    private static ReviewDAO reviewDAO = new ReviewDAO();

    private static User loggedInUser = null;

    // Predefined Security Questions
    private static final String[] SECURITY_QUESTIONS = {
            "What is your mother's maiden name?",
            "What is the name of your first pet?",
            "What is your favorite color?",
            "What is your birthplace?",
            "What is the name of your first school?",
            "What is your favorite food?"
    };

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("   WELCOME TO FOOD ORDERING SYSTEM       ");
        System.out.println("==========================================");

        while (true) {
            if (loggedInUser == null) {
                showMainMenu();
            } else {
                if (loggedInUser.getRole().equalsIgnoreCase("ADMIN")) {
                    showAdminMenu();
                } else {
                    showCustomerMenu();
                }
            }
        }
    }

    private static int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static double getDoubleInput() {
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1.0;
        }
    }

    private static void printTable(String[] headers, List<String[]> rows) {
        if (headers == null || headers.length == 0) return;
        int numCols = headers.length;
        int[] colWidths = new int[numCols];

        for (int i = 0; i < numCols; i++) {
            colWidths[i] = headers[i] != null ? headers[i].length() : 0;
        }

        if (rows != null) {
            for (String[] row : rows) {
                if (row == null) continue;
                for (int i = 0; i < Math.min(numCols, row.length); i++) {
                    if (row[i] != null && row[i].length() > colWidths[i]) {
                        colWidths[i] = row[i].length();
                    }
                }
            }
        }

        StringBuilder borderBuilder = new StringBuilder("+");
        for (int width : colWidths) {
            for (int j = 0; j < width + 2; j++) {
                borderBuilder.append("-");
            }
            borderBuilder.append("+");
        }
        String border = borderBuilder.toString();

        System.out.println(border);
        System.out.print("|");
        for (int i = 0; i < numCols; i++) {
            String h = headers[i] != null ? headers[i] : "";
            System.out.printf(" %-" + colWidths[i] + "s |", h);
        }
        System.out.println();
        System.out.println(border);

        if (rows != null) {
            for (String[] row : rows) {
                if (row == null) continue;
                System.out.print("|");
                for (int i = 0; i < numCols; i++) {
                    String val = "";
                    if (i < row.length && row[i] != null) {
                        val = row[i];
                    }
                    System.out.printf(" %-" + colWidths[i] + "s |", val);
                }
                System.out.println();
            }
        }
        System.out.println(border);
    }


    // ==================== VALIDATION METHODS ====================

    // Validate Name (letters and spaces only, 2-50 chars)
    private static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return name.trim().matches("^[a-zA-Z\\s]{2,50}$");
    }

    // Validate Email (must end with @gmail.com, username must be letters-only or letters+numbers)
    private static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String trimmed = email.trim().toLowerCase();
        if (!trimmed.endsWith("@gmail.com")) {
            return false;
        }

        String username = trimmed.substring(0, trimmed.length() - 10);

        // Username length must be between 3 and 30 characters
        if (username.length() < 3 || username.length() > 30) {
            return false;
        }

        // Must contain at least one letter (characters only OR characters + numbers)
        boolean hasLetter = false;
        for (char c : username.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
                break;
            }
        }
        if (!hasLetter) {
            return false;
        }

        // Must consist of letters, digits, and optional single dots (no leading/trailing or consecutive dots)
        return username.matches("^[a-z0-9]+(\\.[a-z0-9]+)*$");
    }

    // Validate Password (8 to 12 chars, min 1 uppercase, min 1 lowercase, EXACTLY 1 special character)
    private static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8 || password.length() > 12) {
            return false;
        }

        int upperCount = 0;
        int lowerCount = 0;
        int specialCount = 0;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                upperCount++;
            } else if (Character.isLowerCase(c)) {
                lowerCount++;
            } else if (!Character.isDigit(c)) {
                specialCount++;
            }
        }

        return upperCount >= 1 && lowerCount >= 1 && specialCount == 1;
    }

    private static void displayPasswordRequirements() {
        System.out.println("Invalid password!");
        System.out.println("   - Must be between 8 and 12 characters");
        System.out.println("   - Must contain at least 1 uppercase letter (A-Z)");
        System.out.println("   - Must contain at least 1 lowercase letter (a-z)");
        System.out.println("   - Must contain EXACTLY 1 special character (e.g. @, #, $, !, %)");
    }

    // Validate Mobile Number (10 digits starting with 6, 7, 8, or 9)
    private static boolean isValidMobileNumber(String phone) {
        if (phone == null || phone.length() != 10) {
            return false;
        }

        for (char c : phone.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        char firstDigit = phone.charAt(0);
        return firstDigit == '6' || firstDigit == '7' || firstDigit == '8' || firstDigit == '9';
    }

    // Validate City (letters and spaces only, 2-30 chars)
    private static boolean isValidCity(String city) {
        if (city == null || city.trim().isEmpty()) {
            return false;
        }
        return city.trim().matches("^[a-zA-Z\\s]{2,30}$");
    }

    // Validate Security Answer (letters and spaces only, max 10 chars)
    private static boolean isValidSecurityAnswer(String answer) {
        if (answer == null || answer.trim().isEmpty()) {
            return false;
        }
        String trimmed = answer.trim();
        return trimmed.length() <= 10 && trimmed.matches("^[a-zA-Z\\s]+$");
    }

    // Validate Pincode (6 digits)
    private static boolean isValidPincode(String pincode) {
        if (pincode == null || pincode.trim().isEmpty()) {
            return false;
        }
        return pincode.trim().matches("^[0-9]{6}$");
    }

    // Validate Street / Area (letters and spaces only, 2-100 chars)
    private static boolean isValidStreetOrArea(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        return input.trim().matches("^[a-zA-Z\\s]{2,100}$");
    }

    // Prompt for multi-part Address (Building No, Street, City, Pincode)
    private static String getAddressInput() {
        System.out.println("\n--- Enter Delivery Address Details ---");

        String buildingNo;
        while (true) {
            System.out.print("Enter Building / Flat No (Required): ");
            buildingNo = scanner.nextLine().trim();
            if (!buildingNo.isEmpty()) {
                break;
            }
            System.out.println("Building / Flat No is required!");
        }

        String street;
        while (true) {
            System.out.print("Enter Street / Area (Required): ");
            street = scanner.nextLine().trim();
            if (isValidStreetOrArea(street)) {
                break;
            }
            System.out.println("Invalid Street / Area!");
        }

        String city;
        while (true) {
            System.out.print("Enter City (Required): ");
            city = scanner.nextLine().trim();
            if (isValidCity(city)) {
                break;
            }
            System.out.println("Invalid City! Must contain only letters and spaces ");
        }

        String pincode;
        while (true) {
            System.out.print("Enter Pincode (Required, 6 digits): ");
            pincode = scanner.nextLine().trim();
            if (isValidPincode(pincode)) {
                break;
            }
            System.out.println("Invalid Pincode! Must be exactly 6 digits.");
        }

        return buildingNo + ", " + street + ", " + city + " - " + pincode;
    }

    // Validate Security Question (letters and spaces only, max 10 chars)
    private static boolean isValidSecurityQuestion(String question) {
        if (question == null || question.trim().isEmpty()) {
            return false;
        }
        String trimmed = question.trim();
        return trimmed.length() <= 10 && trimmed.matches("^[a-zA-Z\\s]+$");
    }

    // Display Security Questions and get user choice
    private static String selectSecurityQuestion() {
        System.out.println("\n--- Select a Security Question ---");
        for (int i = 0; i < SECURITY_QUESTIONS.length; i++) {
            System.out.println((i + 1) + ". " + SECURITY_QUESTIONS[i]);
        }
        System.out.println((SECURITY_QUESTIONS.length + 1) + ". Enter Custom Security Question (Max 10 chars, letters only)");

        int choice;
        while (true) {
            System.out.print("Enter your choice (1-" + (SECURITY_QUESTIONS.length + 1) + "): ");
            choice = getIntInput();
            if (choice >= 1 && choice <= SECURITY_QUESTIONS.length + 1) {
                break;
            } else {
                System.out.println("Invalid choice! Please select between 1 and " + (SECURITY_QUESTIONS.length + 1));
            }
        }

        if (choice == SECURITY_QUESTIONS.length + 1) {
            while (true) {
                System.out.print("Enter custom Security Question (letters only, max 10 chars): ");
                String customQ = scanner.nextLine().trim();
                if (isValidSecurityQuestion(customQ)) {
                    return customQ;
                } else {
                    System.out.println("Invalid security question!");
                    System.out.println("   - Must contain only letters and spaces");
                    System.out.println("   - Must be maximum 10 characters long");
                }
            }
        }

        return SECURITY_QUESTIONS[choice - 1];
    }

    // ==================== MAIN MENU ====================

    private static void showMainMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Forgot Password");
        System.out.println("4. View Restaurants");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");

        int choice = getIntInput();

        switch (choice) {
            case 1: registerUser(); break;
            case 2: loginUser(); break;
            case 3: forgotPassword(); break;
            case 4: viewRestaurants(); break;
            case 5:
                System.out.println("Thank you for using Food Ordering System!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }

    // ==================== REGISTRATION WITH VALIDATION ====================

    private static void registerUser() {
        System.out.println("\n--- USER REGISTRATION ---");

        User user = new User();

        // Name Validation
        String name;
        while (true) {
            System.out.print("Enter Full Name: ");
            name = scanner.nextLine().trim();
            if (isValidName(name)) {
                break;
            } else {
                System.out.println("Invalid name! Must contain only letters and spaces (min 2 characters).");
            }
        }
        user.setName(name);

        // Email Validation - Must end with @gmail.com (characters only OR characters + numbers)
        String email;
        while (true) {
            System.out.print("Enter Email (must end with @gmail.com): ");
            email = scanner.nextLine().trim();
            if (isValidEmail(email)) {
                break;
            } else {
                System.out.println("Invalid email!");
                System.out.println("   - Must end with @gmail.com");
                System.out.println("   - Username must contain letters (e.g., user@gmail.com) or letters with numbers (e.g., user123@gmail.com)");
                System.out.println("   - Numbers-only usernames (e.g., 12345@gmail.com) are not allowed");
            }
        }
        user.setEmail(email);

        // Check if email already exists
        if (userDAO.isEmailExists(user.getEmail())) {
            System.out.println("Email already registered! Please login.");
            return;
        }

        // Password Validation - 8 to 12 chars, 1 uppercase, 1 lowercase, exactly 1 special char
        String password;
        while (true) {
            System.out.print("Enter Password (8-12 chars, 1 upper, 1 lower, 1 special char): ");
            password = scanner.nextLine();
            if (isValidPassword(password)) {
                break;
            } else {
                displayPasswordRequirements();
            }
        }
        user.setPassword(password);

        // Mobile Number Validation - 10 digits, start with 6,7,8,9
        String phone;
        while (true) {
            System.out.print("Enter Phone Number (10 digits, start with 6,7,8,9): ");
            phone = scanner.nextLine().trim();
            if (isValidMobileNumber(phone)) {
                break;
            } else {
                System.out.println("Invalid mobile number!");
                System.out.println("   - Must be exactly 10 digits");
                System.out.println("   - Must start with 6, 7, 8, or 9");
            }
        }
        user.setPhone(phone);

        // Delivery Address Component Validation (Building No, Street, City, Pincode)
        String address = getAddressInput();
        user.setAddress(address);

        // Security Question Selection
        String selectedQuestion = selectSecurityQuestion();
        user.setSecurityQuestion(selectedQuestion);

        // Security Answer Validation (letters only, max 10 characters)
        String answer;
        while (true) {
            System.out.print("Enter Security Answer (letters only, max 10 chars): ");
            answer = scanner.nextLine().trim();
            if (isValidSecurityAnswer(answer)) {
                break;
            } else {
                System.out.println("Invalid security answer!");
                System.out.println("   - Must contain only letters and spaces");
                System.out.println("   - Must be maximum 10 characters long");
            }
        }
        user.setSecurityAnswer(answer);

        user.setRole("CUSTOMER");

        if (userDAO.registerUser(user)) {
            System.out.println("Registration Successful!");
            System.out.println("Your Security Question: " + selectedQuestion);
        } else {
            System.out.println("Registration Failed!");
        }
    }

    // ==================== LOGIN ====================

    private static void loginUser() {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        User user = userDAO.loginUser(email, password);

        if (user != null) {
            loggedInUser = user;
            System.out.println("Login Successful! Welcome, " + user.getName() + "!");
        } else {
            System.out.println("Invalid credentials or account blocked!");
        }
    }

    // ==================== FORGOT PASSWORD ====================

    private static void forgotPassword() {
        System.out.println("\n--- FORGOT PASSWORD ---");
        System.out.print("Enter Email: ");
        String email = scanner.nextLine().trim();

        User user = userDAO.getUserByEmail(email);
        if (user == null) {
            System.out.println("Email not found!");
            return;
        }

        System.out.println("\nSecurity Question: " + user.getSecurityQuestion());
        System.out.print("Enter your answer: ");
        String answer = scanner.nextLine().trim();

        if (userDAO.verifySecurityAnswer(email, answer)) {
            String newPassword;
            while (true) {
                System.out.print("Enter new password: ");
                newPassword = scanner.nextLine();
                if (isValidPassword(newPassword)) {
                    break;
                } else {
                    displayPasswordRequirements();
                }
            }
            if (userDAO.resetPassword(email, newPassword)) {
                System.out.println("Password reset successful!");
            } else {
                System.out.println("Failed to reset password!");
            }
        } else {
            System.out.println("Incorrect answer! Password reset failed.");
        }
    }

    private static void logout() {
        System.out.println("Goodbye, " + loggedInUser.getName() + "!");
        loggedInUser = null;
    }

    // ==================== RESTAURANT ====================

    private static void viewRestaurants() {
        System.out.println("\n--- VIEW RESTAURANTS ---");
        System.out.println("1. View Restaurants (Standard List)");
        System.out.println("2. View Restaurants (Sorted by Rating High-Low");
        System.out.println("3. Go Back");
        System.out.print("Choice: ");
        int choice = getIntInput();

        List<Restaurant> restaurants = restaurantDAO.getOpenRestaurants();
        if (restaurants.isEmpty()) {
            System.out.println("No restaurants available.");
            return;
        }

        String[] headers = {"ID", "Name", "Category", "Cuisine", "Rating"};
        List<String[]> rows = new ArrayList<>();

        if (choice == 1) {
            for (Restaurant r : restaurants) {
                rows.add(new String[]{
                    String.valueOf(r.getId()),
                    r.getName(),
                    r.getCategory(),
                    r.getCuisine(),
                    String.valueOf(r.getRating())
                });
            }
            System.out.println("\n--- OPEN RESTAURANTS ---");
            printTable(headers, rows);
        } else if (choice == 2) {
            java.util.PriorityQueue<Restaurant> pq = new java.util.PriorityQueue<>(
                (r1, r2) -> Double.compare(r2.getRating(), r1.getRating())
            );
            pq.addAll(restaurants);

            while (!pq.isEmpty()) {
                Restaurant r = pq.poll();
                rows.add(new String[]{
                    String.valueOf(r.getId()),
                    r.getName(),
                    r.getCategory(),
                    r.getCuisine(),
                    String.valueOf(r.getRating())
                });
            }
            System.out.println("\n--- OPEN RESTAURANTS (RATING:High-Low");
            printTable(headers, rows);
        } else {
            return;
        }

        System.out.print("Enter Restaurant ID to view menu (0 to go back): ");
        int id = getIntInput();
        if (id > 0) {
            viewMenu(id);
        }
    }

    private static void viewMenu(int restaurantId) {
        Restaurant restaurant = restaurantDAO.getRestaurantById(restaurantId);
        if (restaurant == null) {
            System.out.println("Restaurant not found!");
            return;
        }

        List<OrderItem> cartItems = new ArrayList<>();

        while (true) {
            System.out.println("\n--- " + restaurant.getName().toUpperCase() + " MENU ---");
            System.out.println("Category: " + restaurant.getCategory() + " | Cuisine: " + restaurant.getCuisine() + " | Rating: " + restaurant.getRating());
            System.out.println("Delivery Charge: Rs." + restaurant.getDeliveryCharge() + " | Min Order: Rs." + restaurant.getMinOrderAmount());

            List<Menu> menuItems = restaurantDAO.getMenuItemsByRestaurant(restaurantId);
            if (menuItems.isEmpty()) {
                System.out.println("No items available.");
                return;
            }

            String[] headers = {"ID", "Item", "Price", "Category", "Stock", "Status"};
            List<String[]> rows = new ArrayList<>();
            for (Menu m : menuItems) {
                rows.add(new String[]{
                    String.valueOf(m.getId()),
                    m.getName(),
                    "Rs." + m.getPrice(),
                    m.getCategory(),
                    String.valueOf(m.getStockQuantity()),
                    (m.getStockQuantity() <= 0 || "OUT_OF_STOCK".equalsIgnoreCase(m.getAvailability())) ? "OUT OF STOCK" : "AVAILABLE"
                });
            }
            printTable(headers, rows);

            if (!cartItems.isEmpty()) {
                double cartSubtotal = 0;
                System.out.println("\nYOUR CART (" + cartItems.size() + " items):");
                for (OrderItem item : cartItems) {
                    Menu menu = restaurantDAO.getMenuItemById(item.getMenuId());
                    String itemName = (menu != null) ? menu.getName() : ("Item #" + item.getMenuId());
                    System.out.println("   - " + itemName + " | Qty: " + item.getQuantity() + " | Subtotal: Rs." + item.getSubtotal());
                    cartSubtotal += item.getSubtotal();
                }
                System.out.println("   Cart Subtotal: Rs." + cartSubtotal);
                System.out.println("----------------------------------------");
            }

            if (loggedInUser != null && loggedInUser.getRole().equalsIgnoreCase("CUSTOMER")) {
                System.out.println("\n1. Add Item to Cart");
                System.out.println("2. View Cart & Checkout");
                System.out.println("3. Modify / Remove Cart Item");
                System.out.println("4. Clear Cart");
                System.out.println("5. Go Back");
                System.out.print("Choice: ");
                int choice = getIntInput();

                if (choice == 1) {
                    System.out.print("Enter Menu ID to add: ");
                    int menuId = getIntInput();
                    Menu menu = restaurantDAO.getMenuItemById(menuId);
                    if (menu == null || menu.getRestaurantId() != restaurantId) {
                        System.out.println("Invalid Menu ID for this restaurant!");
                        continue;
                    }
                    if (menu.getStockQuantity() <= 0 || "OUT_OF_STOCK".equalsIgnoreCase(menu.getAvailability())) {
                        System.out.println("Sorry! " + menu.getName() + " is currently OUT OF STOCK.");
                        continue;
                    }

                    System.out.print("Enter Quantity: ");
                    int qty = getIntInput();
                    if (qty <= 0) {
                        System.out.println("Quantity must be greater than 0!");
                        continue;
                    }

                    // Cumulative stock checking against items already in cart
                    int currentInCart = 0;
                    for (OrderItem item : cartItems) {
                        if (item.getMenuId() == menuId) {
                            currentInCart = item.getQuantity();
                            break;
                        }
                    }

                    if (currentInCart + qty > menu.getStockQuantity()) {
                        int availableToAdd = menu.getStockQuantity() - currentInCart;
                        if (availableToAdd <= 0) {
                            System.out.println("Cannot add more! You already have maximum available stock (" + menu.getStockQuantity() + ") of " + menu.getName() + " in your cart.");
                        } else {
                            System.out.println("Cannot add " + qty + "! Only " + availableToAdd + " more available (You already have " + currentInCart + " in cart).");
                        }
                        continue;
                    }

                    boolean found = false;
                    for (OrderItem item : cartItems) {
                        if (item.getMenuId() == menuId) {
                            item.setQuantity(item.getQuantity() + qty);
                            item.setSubtotal(item.getQuantity() * item.getPrice());
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        OrderItem item = new OrderItem();
                        item.setMenuId(menuId);
                        item.setQuantity(qty);
                        item.setPrice(menu.getPrice());
                        item.setSubtotal(qty * menu.getPrice());
                        cartItems.add(item);
                    }
                    System.out.println("Added " + menu.getName() + " (x" + qty + ") to cart!");
                } else if (choice == 2) {
                    if (cartItems.isEmpty()) {
                        System.out.println("Your cart is empty! Add items first.");
                        continue;
                    }
                    boolean orderPlaced = checkoutCart(restaurant, cartItems);
                    if (orderPlaced) {
                        cartItems.clear();
                        break;
                    }
                    // If order was not placed (user clicked Go Back or cancelled), loop continues with cartItems preserved!
                } else if (choice == 3) {
                    if (cartItems.isEmpty()) {
                        System.out.println("Your cart is empty!");
                        continue;
                    }
                    System.out.print("Enter Menu ID to modify/remove: ");
                    int modId = getIntInput();
                    OrderItem targetItem = null;
                    for (OrderItem item : cartItems) {
                        if (item.getMenuId() == modId) {
                            targetItem = item;
                            break;
                        }
                    }
                    if (targetItem == null) {
                        System.out.println("Item not found in your cart!");
                        continue;
                    }
                    System.out.print("Enter new quantity (0 to remove): ");
                    int newQty = getIntInput();
                    if (newQty <= 0) {
                        cartItems.remove(targetItem);
                        System.out.println("Item removed from cart.");
                    } else {
                        Menu menu = restaurantDAO.getMenuItemById(modId);
                        if (menu != null && newQty > menu.getStockQuantity()) {
                            System.out.println("Cannot update! Only " + menu.getStockQuantity() + " available in stock.");
                        } else {
                            targetItem.setQuantity(newQty);
                            targetItem.setSubtotal(newQty * targetItem.getPrice());
                            System.out.println("Cart quantity updated!");
                        }
                    }
                } else if (choice == 4) {
                    cartItems.clear();
                    System.out.println("Cart cleared.");
                } else if (choice == 5) {
                    break;
                } else {
                    System.out.println("Invalid choice!");
                }
            } else {
                break;
            }
        }
    }

    // ==================== CHECKOUT & ORDER ====================

    private static boolean checkoutCart(Restaurant restaurant, List<OrderItem> items) {
        System.out.println("\n--- CHECKOUT ---");
        System.out.println("Restaurant: " + restaurant.getName());
        System.out.println("Delivery Address: " + loggedInUser.getAddress());
        System.out.println("----------------------------------------");

        double total = 0;
        for (OrderItem item : items) {
            Menu menu = restaurantDAO.getMenuItemById(item.getMenuId());
            String name = (menu != null) ? menu.getName() : ("Item #" + item.getMenuId());
            System.out.println(" - " + name + " x " + item.getQuantity() + " = Rs." + item.getSubtotal());
            total += item.getSubtotal();
        }

        if (total < restaurant.getMinOrderAmount()) {
            System.out.println("----------------------------------------");
            System.out.printf("Cart Subtotal: Rs.%.2f\n", total);
            System.out.printf("Minimum Order Amount Required: Rs.%.2f\n", restaurant.getMinOrderAmount());
            System.out.println("Cannot proceed to checkout! Your cart subtotal is below the restaurant's minimum order requirement.");
            System.out.println("Please add more items to your cart.");
            return false;
        }

        double finalAmount = total + restaurant.getDeliveryCharge();
        System.out.println("----------------------------------------");
        System.out.println("Subtotal: Rs." + total);
        System.out.println("Delivery Fee: Rs." + restaurant.getDeliveryCharge());
        System.out.println("Total Amount: Rs." + finalAmount);
        System.out.println("----------------------------------------");

        System.out.println("\nSelect Payment Method:");
        System.out.println("1. Cash on Delivery (COD)");
        System.out.println("2. UPI Payment");
        System.out.println("3. Go Back to Menu");
        System.out.print("Choice: ");
        int payChoice = getIntInput();

        String paymentStatus = "PENDING";
        String upiId = "";
        if (payChoice == 1) {
            paymentStatus = "PENDING";
            System.out.println("Payment Method Selected: Cash on Delivery (COD)");
        } else if (payChoice == 2) {
            while (true) {
                System.out.print("Enter UPI ID (e.g. user@upi): ");
                upiId = scanner.nextLine().trim();
                if (!upiId.isEmpty() && upiId.contains("@")) {
                    break;
                } else {
                    System.out.println("Invalid UPI ID! Must contain '@'.");
                }
            }
            System.out.println("Processing UPI payment for " + upiId + "...");
            System.out.println("UPI Payment successful!");
            paymentStatus = "COMPLETED";
        } else if (payChoice == 3) {
            System.out.println("Returning to menu (Items remain in cart)...");
            return false;
        } else {
            System.out.println("Invalid payment option! Returning to menu...");
            return false;
        }

        System.out.print("Confirm order placement? (y/n): ");
        if (!scanner.nextLine().equalsIgnoreCase("y")) {
            System.out.println("Order cancelled. Items remain in your cart.");
            return false;
        }

        Order order = new Order();
        order.setUserId(loggedInUser.getId());
        order.setRestaurantId(restaurant.getId());
        order.setOrderNumber("ORD" + System.currentTimeMillis());
        order.setTotalAmount(total);
        order.setDiscount(0.0);
        order.setCouponCode("");
        order.setFinalAmount(finalAmount);
        String addr = loggedInUser.getAddress();
        order.setDeliveryAddress((addr != null && !addr.trim().isEmpty()) ? addr : "Customer Address");
        order.setDeliveryInstructions("");
        order.setEstimatedDeliveryTime(30);
        order.setStatus(paymentStatus.equals("FAILED") ? "FAILED" : (paymentStatus.equals("COMPLETED") ? "CONFIRMED" : "PENDING"));
        order.setPaymentStatus(paymentStatus);

        for (OrderItem item : items) {
            item.setSpecialInstructions("");
        }

        int orderId = orderDAO.placeOrder(order, items);
        if (orderId > 0) {
            order.setId(orderId);
            if (payChoice == 1) {
                orderDAO.recordPayment(orderId, finalAmount, "CASH", "PENDING", "TXN_COD_" + System.currentTimeMillis(), "");
            } else if (payChoice == 2) {
                orderDAO.recordPayment(orderId, finalAmount, "UPI", "COMPLETED", "TXN_UPI_" + System.currentTimeMillis(), upiId);
            }
            System.out.println("Order placed successfully! Order ID: " + orderId);
            
            // Automatically generate bill text file
            generateBillFile(order, items, restaurant, loggedInUser);
            return true;
        } else {
            System.out.println("Failed to place order!");
            return false;
        }
    }

    // ==================== BILL GENERATION TO TEXT FILE ====================

    private static void generateBillFile(Order order, List<OrderItem> items, Restaurant restaurant, User user) {
        String fileName = "Bill_" + order.getOrderNumber() + ".txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("==================================================");
            writer.println("               FOOD ORDERING SYSTEM               ");
            writer.println("                 OFFICIAL INVOICE                 ");
            writer.println("==================================================");
            writer.println("Order Number : " + order.getOrderNumber());
            writer.println("Order Date   : " + (order.getOrderDate() != null ? order.getOrderDate() : new java.util.Date()));
            writer.println("--------------------------------------------------");
            writer.println("RESTAURANT DETAILS:");
            writer.println("  Name       : " + (restaurant != null ? restaurant.getName() : (order.getRestaurantName() != null ? order.getRestaurantName() : "N/A")));
            if (restaurant != null) {
                writer.println("  Address    : " + restaurant.getAddress());
                writer.println("  Phone      : " + restaurant.getPhone());
            }
            writer.println("--------------------------------------------------");
            writer.println("CUSTOMER DETAILS:");
            writer.println("  Name       : " + user.getName());
            writer.println("  Phone      : " + user.getPhone());
            writer.println("  Address    : " + order.getDeliveryAddress());
            writer.println("--------------------------------------------------");
            writer.println("ORDERED ITEMS:");
            writer.printf("%-4s %-25s %-6s %-10s %-10s\n", "No.", "Item Name", "Qty", "Price", "Subtotal");
            writer.println("--------------------------------------------------");

            int count = 1;
            for (OrderItem item : items) {
                Menu menu = restaurantDAO.getMenuItemById(item.getMenuId());
                String itemName = (item.getMenuName() != null && !item.getMenuName().isEmpty())
                        ? item.getMenuName()
                        : (menu != null ? menu.getName() : ("Item #" + item.getMenuId()));
                writer.printf("%-4d %-25s %-6d Rs.%-7.2f Rs.%-7.2f\n",
                        count++, itemName, item.getQuantity(), item.getPrice(), item.getSubtotal());
            }

            writer.println("--------------------------------------------------");
            writer.printf("Subtotal     : Rs.%.2f\n", order.getTotalAmount());
            if (restaurant != null) {
                writer.printf("Delivery Fee : Rs.%.2f\n", restaurant.getDeliveryCharge());
            }
            writer.printf("TOTAL AMOUNT : Rs.%.2f\n", order.getFinalAmount());
            writer.println("Payment Status: " + (order.getPaymentStatus() != null ? order.getPaymentStatus() : "PENDING"));
            writer.println("Order Status  : " + (order.getStatus() != null ? order.getStatus() : "PENDING"));
            writer.println("==================================================");
            writer.println("       Thank you for ordering with us!            ");
            writer.println("==================================================");

            System.out.println("Receipt bill generated successfully -> " + fileName);
        } catch (IOException e) {
            System.out.println("Error writing bill text file: " + e.getMessage());
        }
    }

    private static void viewMyOrders() {
        if (loggedInUser == null) {
            System.out.println("Please login first.");
            return;
        }
        List<Order> orders = orderDAO.getOrdersByUser(loggedInUser.getId());

        if (orders == null || orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }

        System.out.println("\n--- MY ORDERS ---");
        String[] headers = {"ID", "Order No.", "Restaurant", "Status", "Payment", "Amount"};
        List<String[]> rows = new ArrayList<>();
        for (Order o : orders) {
            String restName = o.getRestaurantName() != null ? o.getRestaurantName() : "N/A";
            String status = o.getStatus() != null ? o.getStatus() : "PENDING";
            String payStatus = o.getPaymentStatus() != null ? o.getPaymentStatus() : "PENDING";
            rows.add(new String[]{
                String.valueOf(o.getId()),
                o.getOrderNumber(),
                restName,
                status,
                payStatus,
                String.format("Rs.%.2f", o.getFinalAmount())
            });
        }
        printTable(headers, rows);

        while (true) {
            System.out.println("\n1. View Order Details");
            System.out.println("2. Generate / Save Bill Text File (.txt)");
            System.out.println("3. Rate & Write Review for Order");
            System.out.println("4. Cancel a Pending/Confirmed Order");
            System.out.println("5. Go Back");
            System.out.print("Choice: ");
            int choice = getIntInput();
            if (choice == 1) {
                System.out.print("Enter Order ID to view details: ");
                int orderId = getIntInput();
                Order order = orderDAO.getOrderById(orderId);
                if (order == null || order.getUserId() != loggedInUser.getId()) {
                    System.out.println("Order not found or does not belong to you!");
                } else {
                    System.out.println("\n==========================================");
                    System.out.println("            ORDER #" + order.getId() + " DETAILS");
                    System.out.println("==========================================");
                    System.out.println("Order Number  : " + order.getOrderNumber());
                    System.out.println("Restaurant    : " + (order.getRestaurantName() != null ? order.getRestaurantName() : "N/A"));
                    System.out.println("Status        : " + order.getStatus());
                    System.out.println("Payment       : " + order.getPaymentStatus());
                    System.out.println("Address       : " + order.getDeliveryAddress());
                    System.out.println("Order Date    : " + order.getOrderDate());
                    System.out.println("------------------------------------------");
                    System.out.println("ITEMS:");
                    List<OrderItem> items = orderDAO.getOrderItems(orderId);
                    if (items.isEmpty()) {
                        System.out.println("  (No item details found)");
                    } else {
                        for (OrderItem item : items) {
                            String name = item.getMenuName() != null ? item.getMenuName() : ("Item #" + item.getMenuId());
                            System.out.printf("  - %-25s x%-3d @ Rs.%.2f = Rs.%.2f\n", name, item.getQuantity(), item.getPrice(), item.getSubtotal());
                        }
                    }
                    System.out.println("------------------------------------------");
                    System.out.printf("TOTAL AMOUNT  : Rs.%.2f\n", order.getFinalAmount());
                    System.out.println("==========================================");
                }
            } else if (choice == 2) {
                System.out.print("Enter Order ID to generate bill text file: ");
                int orderId = getIntInput();
                Order order = orderDAO.getOrderById(orderId);
                if (order == null || order.getUserId() != loggedInUser.getId()) {
                    System.out.println("Order not found or does not belong to you!");
                } else {
                    List<OrderItem> items = orderDAO.getOrderItems(orderId);
                    Restaurant restaurant = restaurantDAO.getRestaurantById(order.getRestaurantId());
                    generateBillFile(order, items, restaurant, loggedInUser);
                }
            } else if (choice == 3) {
                System.out.print("Enter Order ID to rate & review: ");
                int orderId = getIntInput();
                Order order = orderDAO.getOrderById(orderId);
                if (order == null || order.getUserId() != loggedInUser.getId()) {
                    System.out.println("Order not found or does not belong to you!");
                } else {
                    String status = order.getStatus() != null ? order.getStatus().trim().toUpperCase() : "";
                    if (!status.equals("DELIVERED") && !status.equals("COMPLETED")) {
                        System.out.println("You can only submit reviews for delivered/completed orders! Current status: " + status);
                    } else {
                        int rating = 0;
                        while (true) {
                            System.out.print("Enter Rating (1 to 5 stars): ");
                            rating = getIntInput();
                            if (rating >= 1 && rating <= 5) break;
                            System.out.println("Rating must be between 1 and 5!");
                        }
                        System.out.print("Enter your review comment: ");
                        String comment = scanner.nextLine().trim();

                        Review review = new Review();
                        review.setUserId(loggedInUser.getId());
                        review.setRestaurantId(order.getRestaurantId());
                        review.setRating(rating);
                        review.setComment(comment);
                        review.setType("RESTAURANT");
                        review.setVerified(true);

                        if (reviewDAO.addReview(review)) {
                            System.out.println("Thank you for your rating & review! Restaurant average rating updated.");
                        } else {
                            System.out.println("Failed to submit review.");
                        }
                    }
                }
            } else if (choice == 4) {
                System.out.print("Enter Order ID to cancel: ");
                int orderId = getIntInput();
                Order orderToCancel = null;
                for (Order o : orders) {
                    if (o.getId() == orderId) {
                        orderToCancel = o;
                        break;
                    }
                }
                if (orderToCancel == null) {
                    System.out.println("Order not found or does not belong to you!");
                } else {
                    if (orderDAO.cancelOrder(orderId)) {
                        System.out.println("Order successfully cancelled and inventory stock restored!");
                        orders = orderDAO.getOrdersByUser(loggedInUser.getId());
                        rows.clear();
                        for (Order o : orders) {
                            String restName = o.getRestaurantName() != null ? o.getRestaurantName() : "N/A";
                            String status = o.getStatus() != null ? o.getStatus() : "PENDING";
                            String payStatus = o.getPaymentStatus() != null ? o.getPaymentStatus() : "PENDING";
                            rows.add(new String[]{
                                String.valueOf(o.getId()),
                                o.getOrderNumber(),
                                restName,
                                status,
                                payStatus,
                                String.format("Rs.%.2f", o.getFinalAmount())
                            });
                        }
                        printTable(headers, rows);
                    } else {
                        System.out.println("Failed to cancel order!");
                    }
                }
            } else if (choice == 5) {
                break;
            } else {
                System.out.println("Invalid choice!");
            }
        }
    }

    // ==================== CUSTOMER MENU ====================

    private static void showCustomerMenu() {
        System.out.println("\n--- CUSTOMER MENU ---");
        System.out.println("Welcome, " + loggedInUser.getName() + "!");
        System.out.println("1. View Restaurants");
        System.out.println("2. My Orders");
        System.out.println("3. My Profile");
        System.out.println("4. Change Password");
        System.out.println("5. Logout");
        System.out.print("Choice: ");

        switch (getIntInput()) {
            case 1: viewRestaurants(); break;
            case 2: viewMyOrders(); break;
            case 3: viewProfile(); break;
            case 4: changePassword(); break;
            case 5: logout(); break;
            default: System.out.println("Invalid choice!");
        }
    }

    private static void viewProfile() {
        System.out.println("\n--- MY PROFILE ---");
        System.out.println("Name: " + loggedInUser.getName());
        System.out.println("Email: " + loggedInUser.getEmail());
        System.out.println("Phone: " + loggedInUser.getPhone());
        System.out.println("Address: " + loggedInUser.getAddress());
        System.out.println("Security Question: " + loggedInUser.getSecurityQuestion());
    }

    private static void changePassword() {
        System.out.println("\n--- CHANGE PASSWORD ---");
        System.out.print("Current password: ");
        String old = scanner.nextLine();
        if (!old.equals(loggedInUser.getPassword())) {
            System.out.println("Incorrect password!");
            return;
        }

        System.out.print("New password: ");
        String newPass = scanner.nextLine();
        if (userDAO.changePassword(loggedInUser.getId(), newPass)) {
            loggedInUser.setPassword(newPass);
            System.out.println("Password changed!");
        }
    }

    // ==================== ADMIN MENU ====================

    private static void showAdminMenu() {
        System.out.println("\n--- ADMIN MENU ---");
        System.out.println("Welcome, " + loggedInUser.getName() + "!");
        System.out.println("1. Manage Restaurants");
        System.out.println("2. Manage Menu");
        System.out.println("3. Manage Orders");
        System.out.println("4. Manage Users");
        System.out.println("5. Logout");
        System.out.print("Choice: ");

        switch (getIntInput()) {
            case 1: manageRestaurants(); break;
            case 2: manageMenu(); break;
            case 3: manageOrders(); break;
            case 4: manageUsers(); break;
            case 5: logout(); break;
            default: System.out.println("Invalid choice!");
        }
    }

    // ==================== ADMIN: RESTAURANT ====================

    private static void manageRestaurants() {
        while (true) {
            System.out.println("\n--- MANAGE RESTAURANTS ---");
            System.out.println("1. Add Restaurant");
            System.out.println("2. View All Restaurants");
            System.out.println("3. Update Restaurant");
            System.out.println("4. Delete Restaurant");
            System.out.println("5. Toggle Status");
            System.out.println("6. Back");
            System.out.print("Choice: ");

            int choice = getIntInput();
            if (choice == 6) {
                break;
            }

            switch (choice) {
                case 1: addRestaurant(); break;
                case 2: viewAllRestaurants(); break;
                case 3: updateRestaurant(); break;
                case 4: deleteRestaurant(); break;
                case 5: toggleRestaurantStatus(); break;
                default: System.out.println("Invalid choice!");
            }
        }
    }

    private static void addRestaurant() {
        System.out.println("\n--- ADD RESTAURANT ---");
        Restaurant r = new Restaurant();

        System.out.print("Name: "); r.setName(scanner.nextLine().trim());
        System.out.print("Address: "); r.setAddress(scanner.nextLine().trim());
        System.out.print("Phone: "); r.setPhone(scanner.nextLine().trim());
        System.out.print("Email: "); r.setEmail(scanner.nextLine().trim());
        System.out.print("Category (VEG/NON_VEG/BOTH): "); r.setCategory(scanner.nextLine().trim().toUpperCase());
        System.out.print("Cuisine: "); r.setCuisine(scanner.nextLine().trim());
        System.out.print("Delivery Charge: "); r.setDeliveryCharge(getDoubleInput());
        System.out.print("Min Order Amount: "); r.setMinOrderAmount(getDoubleInput());

        if (restaurantDAO.addRestaurant(r)) {
            System.out.println("Restaurant added! ID: " + r.getId());
        } else {
            System.out.println("Failed to add!");
        }
    }

    private static void viewAllRestaurants() {
        List<Restaurant> restaurants = restaurantDAO.getAllRestaurants();
        if (restaurants.isEmpty()) {
            System.out.println("No restaurants found.");
            return;
        }
        String[] headers = {"ID", "Name", "Category", "Cuisine", "Status", "Rating"};
        List<String[]> rows = new ArrayList<>();
        for (Restaurant r : restaurants) {
            rows.add(new String[]{
                String.valueOf(r.getId()),
                r.getName(),
                r.getCategory(),
                r.getCuisine(),
                r.getStatus(),
                String.valueOf(r.getRating())
            });
        }
        printTable(headers, rows);
    }

    private static void updateRestaurant() {
        viewAllRestaurants();
        System.out.print("Enter Restaurant ID: ");
        int id = getIntInput();

        Restaurant r = restaurantDAO.getRestaurantById(id);
        if (r == null) {
            System.out.println("Restaurant not found!");
            return;
        }

        System.out.print("Name (" + r.getName() + "): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) r.setName(name);

        System.out.print("Address (" + r.getAddress() + "): ");
        String addr = scanner.nextLine();
        if (!addr.isEmpty()) r.setAddress(addr);

        System.out.print("Phone (" + r.getPhone() + "): ");
        String phone = scanner.nextLine();
        if (!phone.isEmpty()) r.setPhone(phone);

        System.out.print("Category (" + r.getCategory() + "): ");
        String cat = scanner.nextLine();
        if (!cat.isEmpty()) r.setCategory(cat.toUpperCase());

        System.out.print("Cuisine (" + r.getCuisine() + "): ");
        String cuis = scanner.nextLine();
        if (!cuis.isEmpty()) r.setCuisine(cuis);

        System.out.print("Delivery Charge (" + r.getDeliveryCharge() + "): ");
        String dc = scanner.nextLine();
        if (!dc.isEmpty()) r.setDeliveryCharge(Double.parseDouble(dc));

        System.out.print("Min Order (" + r.getMinOrderAmount() + "): ");
        String mo = scanner.nextLine();
        if (!mo.isEmpty()) r.setMinOrderAmount(Double.parseDouble(mo));

        if (restaurantDAO.updateRestaurant(r)) {
            System.out.println("Restaurant updated!");
        }
    }

    private static void deleteRestaurant() {
        viewAllRestaurants();
        System.out.print("Enter Restaurant ID to delete: ");
        int id = getIntInput();

        System.out.print("Are you sure? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            if (restaurantDAO.deleteRestaurant(id)) {
                System.out.println("Restaurant deleted!");
            }
        }
    }

    private static void toggleRestaurantStatus() {
        viewAllRestaurants();
        System.out.print("Enter Restaurant ID: ");
        int id = getIntInput();

        Restaurant r = restaurantDAO.getRestaurantById(id);
        if (r == null) {
            System.out.println("Restaurant not found!");
            return;
        }

        if (restaurantDAO.toggleRestaurantStatus(id)) {
            Restaurant updated = restaurantDAO.getRestaurantById(id);
            String newStatus = (updated != null) ? updated.getStatus() : (r.getStatus().equalsIgnoreCase("OPEN") ? "CLOSED" : "OPEN");
            System.out.println("Restaurant '" + r.getName() + "' status changed to: " + newStatus);
        } else {
            System.out.println("Failed to toggle restaurant status!");
        }
    }

    // ==================== ADMIN: MENU ====================

    private static void manageMenu() {
        while (true) {
            System.out.println("\n--- MANAGE MENU ---");
            System.out.println("1. Add Item");
            System.out.println("2. View All Items");
            System.out.println("3. Update Item");
            System.out.println("4. Delete Item");
            System.out.println("5. Update Stock");
            System.out.println("6. Back");
            System.out.print("Choice: ");

            int choice = getIntInput();
            if (choice == 6) {
                break;
            }

            switch (choice) {
                case 1: addMenuItem(); break;
                case 2: viewAllMenu(); break;
                case 3: updateMenuItem(); break;
                case 4: deleteMenuItem(); break;
                case 5: updateStock(); break;
                default: System.out.println("Invalid choice!");
            }
        }
    }

    private static void addMenuItem() {
        viewAllRestaurants();
        System.out.print("Enter Restaurant ID: ");
        int restId = getIntInput();

        Menu m = new Menu();
        m.setRestaurantId(restId);

        System.out.print("Name: "); m.setName(scanner.nextLine().trim());
        System.out.print("Description: "); m.setDescription(scanner.nextLine().trim());
        System.out.print("Price: "); m.setPrice(getDoubleInput());
        System.out.print("Category (VEG/NON_VEG): "); m.setCategory(scanner.nextLine().trim().toUpperCase());
        System.out.print("Cuisine: "); m.setCuisine(scanner.nextLine().trim());
        System.out.print("Stock Quantity: "); m.setStockQuantity(getIntInput());

        if (restaurantDAO.addMenuItem(m)) {
            System.out.println("Item added! ID: " + m.getId());
        }
    }

    private static void viewAllMenu() {
        System.out.print("Enter Restaurant ID (0 for all): ");
        int restId = getIntInput();

        List<Menu> items = restaurantDAO.getMenuItemsByRestaurant(restId);
        if (items.isEmpty()) {
            System.out.println("No items found.");
            return;
        }

        String[] headers = {"ID", "Name", "Price", "Stock", "Featured"};
        List<String[]> rows = new ArrayList<>();
        for (Menu m : items) {
            rows.add(new String[]{
                String.valueOf(m.getId()),
                m.getName(),
                "Rs." + m.getPrice(),
                String.valueOf(m.getStockQuantity()),
                m.isFeatured() ? "Yes" : "No"
            });
        }
        printTable(headers, rows);
    }

    private static void updateMenuItem() {
        System.out.print("Enter Menu ID: ");
        int id = getIntInput();

        Menu m = restaurantDAO.getMenuItemById(id);
        if (m == null) {
            System.out.println("Item not found!");
            return;
        }

        System.out.print("Name (" + m.getName() + "): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) m.setName(name);

        System.out.print("Price (" + m.getPrice() + "): ");
        String price = scanner.nextLine();
        if (!price.isEmpty()) m.setPrice(Double.parseDouble(price));

        System.out.print("Category (" + m.getCategory() + "): ");
        String cat = scanner.nextLine();
        if (!cat.isEmpty()) m.setCategory(cat.toUpperCase());

        System.out.print("Stock (" + m.getStockQuantity() + "): ");
        String stock = scanner.nextLine();
        if (!stock.isEmpty()) m.setStockQuantity(Integer.parseInt(stock));

        if (restaurantDAO.updateMenuItem(m)) {
            System.out.println("Item updated!");
        }
    }

    private static void deleteMenuItem() {
        System.out.print("Enter Menu ID to delete: ");
        int id = getIntInput();

        System.out.print("Are you sure? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            if (restaurantDAO.deleteMenuItem(id)) {
                System.out.println("Item deleted!");
            }
        }
    }

    private static void updateStock() {
        System.out.print("Enter Menu ID: ");
        int id = getIntInput();
        System.out.print("New Stock Quantity: ");
        int qty = getIntInput();

        if (restaurantDAO.updateStockQuantity(id, qty)) {
            System.out.println("Stock updated!");
        }
    }

    // ==================== ADMIN: ORDERS ====================

    private static void manageOrders() {
        while (true) {
            System.out.println("\n--- MANAGE ORDERS ---");
            System.out.println("1. View All Orders");
            System.out.println("2. View Pending Orders");
            System.out.println("3. View Completed Orders");
            System.out.println("4. View Failed Orders");
            System.out.println("5. Update Order Status");
            System.out.println("6. Back");
            System.out.print("Choice: ");

            int choice = getIntInput();
            if (choice == 6) {
                break;
            }
            switch (choice) {
                case 1: viewAllOrders(); break;
                case 2: viewPendingOrders(); break;
                case 3: viewCompletedOrders(); break;
                case 4: viewFailedOrders(); break;
                case 5: updateOrderStatus(); break;
                default: System.out.println("Invalid choice!");
            }
        }
    }

    private static void viewAllOrders() {
        List<Order> orders = orderDAO.getAllOrders();
        printOrderTable(orders);
    }

    private static void viewPendingOrders() {
        List<Order> orders = orderDAO.getPendingOrders();
        printOrderTable(orders);
    }

    private static void viewCompletedOrders() {
        List<Order> orders = new ArrayList<>();
        orders.addAll(orderDAO.getOrdersByStatus("COMPLETED"));
        orders.addAll(orderDAO.getOrdersByStatus("DELIVERED"));
        printOrderTable(orders);
    }

    private static void viewFailedOrders() {
        List<Order> orders = new ArrayList<>();
        orders.addAll(orderDAO.getOrdersByStatus("FAILED"));
        orders.addAll(orderDAO.getOrdersByStatus("CANCELLED"));
        printOrderTable(orders);
    }

    private static void printOrderTable(List<Order> orders) {
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }
        String[] headers = {"ID", "Order No.", "Customer", "Status", "Amount"};
        List<String[]> rows = new ArrayList<>();
        for (Order o : orders) {
            rows.add(new String[]{
                String.valueOf(o.getId()),
                o.getOrderNumber(),
                o.getUserName() != null ? o.getUserName() : "N/A",
                o.getStatus(),
                String.format("Rs.%.2f", o.getFinalAmount())
            });
        }
        printTable(headers, rows);
    }

    private static void updateOrderStatus() {
        viewAllOrders();
        System.out.print("Enter Order ID: ");
        int id = getIntInput();

        System.out.println("Status: PENDING, CONFIRMED, PREPARING, OUT_FOR_DELIVERY, DELIVERED, COMPLETED, CANCELLED, FAILED");
        System.out.print("Enter new status: ");
        String status = scanner.nextLine().toUpperCase();

        if (orderDAO.updateOrderStatus(id, status)) {
            System.out.println("Order status updated!");
        }
    }

    // ==================== ADMIN: USERS ====================

    private static void manageUsers() {
        while (true) {
            System.out.println("\n--- MANAGE USERS ---");
            System.out.println("1. View All Users");
            System.out.println("2. View Customers");
            System.out.println("3. Block/Unblock User");
            System.out.println("4. Back");
            System.out.print("Choice: ");

            int choice = getIntInput();
            if (choice == 4) {
                break;
            }

            switch (choice) {
                case 1: viewAllUsers(); break;
                case 2: viewCustomers(); break;
                case 3: toggleBlockUser(); break;
                default: System.out.println("Invalid choice!");
            }
        }
    }
    private static void viewAllUsers() {
        List<User> users = userDAO.getAllUsers();
        printUserTable(users);
    }
    private static void viewCustomers() {
        List<User> users = userDAO.getAllCustomers();
        printUserTable(users);
    }
    private static void printUserTable(List<User> users) {
        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }
        String[] headers = {"ID", "Name", "Email", "Role", "Blocked"};
        List<String[]> rows = new ArrayList<>();
        for (User u : users) {
            rows.add(new String[]{
                String.valueOf(u.getId()),
                u.getName(),
                u.getEmail(),
                u.getRole(),
                u.isBlocked() ? "Yes" : "No"
            });
        }
        printTable(headers, rows);
    }
    private static void toggleBlockUser() {
        viewAllUsers();
        System.out.print("Enter User ID: ");
        int id = getIntInput();
        User user = userDAO.getUserById(id);
        if (user == null) {
            System.out.println("User not found!");
            return;
        }
        boolean newStatus = !user.isBlocked();
        if (userDAO.toggleBlockUser(id, newStatus)) {
            System.out.println("User " + (newStatus ? "blocked" : "unblocked") + "!");
        }
    }
}