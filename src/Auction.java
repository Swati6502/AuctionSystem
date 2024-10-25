import java.math.BigDecimal;
import java.sql.Timestamp;

public class Auction {
    private int id;
    private String itemName;
    private String description;
    private BigDecimal startingBid;
    private Timestamp endTime;
    private String status;

    public Auction(String itemName, String description, BigDecimal startingBid, Timestamp endTime) {
        this.itemName = itemName;
        this.description = description;
        this.startingBid = startingBid;
        this.endTime = endTime;
        this.status = "RUNNING"; // Auction starts as 'RUNNING'
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getItemName() { return itemName; }
    public String getDescription() { return description; }
    public BigDecimal getStartingBid() { return startingBid; }
    public Timestamp getEndTime() { return endTime; }
    public void setEndTime(Timestamp endTime) { this.endTime = endTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
