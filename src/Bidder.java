import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Bidder {
    private int id;
    private String username;

    public Bidder(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public void placeBid(int auctionId) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your bid amount:");
        double bidAmount = scanner.nextDouble();

        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT current_bid FROM auctions WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, auctionId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                double currentBid = resultSet.getDouble("current_bid");
                if (bidAmount > currentBid) {
                    String updateBid = "UPDATE auctions SET current_bid = ?, highest_bidder_id = ? WHERE id = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateBid);
                    updateStatement.setDouble(1, bidAmount);
                    updateStatement.setInt(2, this.id);
                    updateStatement.setInt(3, auctionId);
                    updateStatement.executeUpdate();

                    String insertBid = "INSERT INTO bids (auction_id, bidder_id, bid_amount) VALUES (?, ?, ?)";
                    PreparedStatement insertStatement = connection.prepareStatement(insertBid);
                    insertStatement.setInt(1, auctionId);
                    insertStatement.setInt(2, this.id);
                    insertStatement.setDouble(3, bidAmount);
                    insertStatement.executeUpdate();

                    System.out.println("Bid placed successfully!");
                } else {
                    System.out.println("Your bid must be higher than the current bid!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
