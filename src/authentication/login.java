package authentication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import database.db_connection;
import Model.User;

public class login {

    private static Scanner scanner = new Scanner(System.in);
    private static User loggedInUser = null;

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static boolean isLoggedIn() {
        return loggedInUser != null;
    }

    public static boolean login() {

        System.out.println("\n===== LOGIN =====");

        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        String query = "SELECT * FROM users WHERE email = ? AND password = ? AND is_blocked = false";

        try (Connection conn = db_connection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {

                    loggedInUser = new User();

                    loggedInUser.setId(rs.getInt("id"));
                    loggedInUser.setName(rs.getString("name"));
                    loggedInUser.setEmail(rs.getString("email"));
                    loggedInUser.setPassword(rs.getString("password"));
                    loggedInUser.setPhone(rs.getString("phone"));
                    loggedInUser.setAddress(rs.getString("address"));
                    loggedInUser.setSecurityQuestion(rs.getString("security_question"));
                    loggedInUser.setSecurityAnswer(rs.getString("security_answer"));
                    loggedInUser.setRole(rs.getString("role"));
                    loggedInUser.setBlocked(rs.getBoolean("is_blocked"));
                    loggedInUser.setCreatedAt(rs.getTimestamp("created_at"));
                    loggedInUser.setUpdatedAt(rs.getTimestamp("updated_at"));

                    System.out.println("\nLogin Successful!");
                    System.out.println("Welcome, " + loggedInUser.getName());
                    System.out.println("Role: " + loggedInUser.getRole());

                    return true;
                } else {
                    System.out.println("\nInvalid email or password.");
                }
            }

        } catch (Exception e) {
            System.out.println("Login Error: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public static void logout() {
        loggedInUser = null;
        System.out.println("Logged out successfully.");
    }
}