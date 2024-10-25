import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AuthenticationService {
    private User loggedInUser = null;

    public boolean register() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a username:");
        String username = scanner.nextLine();
        System.out.println("Enter a password:");
        String password = scanner.nextLine();
        System.out.println("Are you registering as 'auctioneer' or 'bidder'?");
        String role = scanner.nextLine().toUpperCase();

        if (!role.equals("AUCTIONEER") && !role.equals("BIDDER")) {
            System.out.println("Invalid role! Please choose 'auctioneer' or 'bidder'.");
            return false;
        }

        try (Connection connection = DBConnection.getConnection()) {
            String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password); // For demo, not hashing password (should be hashed in real use)
            statement.setString(3, role);
            statement.executeUpdate();
            System.out.println("Registration successful!");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean login() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter username:");
        String username = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String role = resultSet.getString("role");
                loggedInUser = new User(id, username, password, role);
                System.out.println("Login successful! Logged in as: " + role);
                return true;
            } else {
                System.out.println("Invalid credentials!");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void logout() {
        if (loggedInUser != null) {
            System.out.println("Logged out successfully!");
            loggedInUser = null;
        } else {
            System.out.println("No user is logged in!");
        }
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }
}
