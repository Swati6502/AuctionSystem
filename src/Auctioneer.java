import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Scanner;

public class Auctioneer {
    private int id;
    private String username;

    public Auctioneer(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public void createAuction() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter item name:");
        String itemName = scanner.nextLine();
        System.out.println("Enter description:");
        String description = scanner.nextLine();
        System.out.println("Enter starting bid:");
        double startingBid = scanner.nextDouble();
        System.out.println("Enter duration in seconds:");
        int durationInSeconds = scanner.nextInt();

        // Get current time using Instant to avoid time zone issues
        Instant startTime = Instant.now();

        // Calculate end time by adding duration in seconds
        Instant endTime = startTime.plusSeconds(durationInSeconds);

        try (Connection connection = DBConnection.getConnection()) {
            String query = "INSERT INTO auctions (item_name, description, starting_bid, start_time, end_time) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, itemName);
            statement.setString(2, description);
            statement.setDouble(3, startingBid);
            statement.setTimestamp(4, Timestamp.from(startTime));
            statement.setTimestamp(5, Timestamp.from(endTime));
            statement.executeUpdate();
            System.out.println("Auction created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
