import java.math.BigDecimal;
import java.sql.*;

public class AuctionService {
    private Connection connection;

    public AuctionService(Connection connection) {
        this.connection = connection;
    }

    // Method to create a new auction
    public Auction createAuction(String itemName, String description, BigDecimal startingBid, int durationInSeconds) {
        // Calculate the auction end time
        long durationInMillis = durationInSeconds * 1000; // Convert seconds to milliseconds
        Timestamp endTime = new Timestamp(System.currentTimeMillis() + durationInMillis);

        Auction auction = new Auction(itemName, description, startingBid, endTime);  // Assuming Auction has an endTime field

        // Save the auction to the database
        String sql = "INSERT INTO auctions (item_name, description, starting_bid, start_time, end_time, status) VALUES (?, ?, ?, ?, ?, 'RUNNING')";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, auction.getItemName());
            stmt.setString(2, auction.getDescription());
            stmt.setBigDecimal(3, auction.getStartingBid());
            stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis())); // Start time is current time
            stmt.setTimestamp(5, auction.getEndTime());  // Set the calculated end time

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                auction.setId(generatedKeys.getInt(1)); // Set auction ID
            }

            System.out.println("Auction created with ID: " + auction.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Start the timer for the auction
        TimerThread timerThread = new TimerThread(auction, connection);
        timerThread.start(); // Start the auction timer thread

        return auction;
    }
}
