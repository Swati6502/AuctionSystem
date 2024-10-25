import java.sql.*;
import java.util.concurrent.TimeUnit;

public class TimerThread extends Thread {
    private Auction auction;
    private Connection connection; // Ensure this connection is set correctly

    public TimerThread(Auction auction, Connection connection) {
        this.auction = auction;
        this.connection = connection; // Pass the connection for DB updates
    }

    @Override
    public void run() {
        System.out.println("Timer started for Auction ID: " + auction.getId());

        try {
            while (true) {
                Timestamp currentTime = new Timestamp(System.currentTimeMillis());

                System.out.println("Current time: " + currentTime + ", Auction end time: " + auction.getEndTime());

                if (currentTime.after(auction.getEndTime())) {
                    // Auction has reached its end time
                    auction.setStatus("COMPLETED");
                    System.out.println("Auction with ID: " + auction.getId() + " has ended.");

                    // Update auction status in the database
                    updateAuctionStatusInDatabase(auction.getId(), "COMPLETED");
                    break;  // Exit the loop, auction is over
                }

                // Sleep for 1 second before checking again
                Thread.sleep(1000); // Sleep for 1 second to check the time
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updateAuctionStatusInDatabase(int auctionId, String status) {
        try {
            String sql = "UPDATE auctions SET status = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, status);
            statement.setInt(2, auctionId);
            statement.executeUpdate();
            System.out.println("Auction ID " + auctionId + " status updated to " + status);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
