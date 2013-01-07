package auctionmanagement;

import java.util.Date;

/**
 * A timestamp contains the complete message in form of "!timestamp <auction-id> <price> <timestamp> <signature>"
 * @author Dave
 */
public class Timestamp {
    
    private long auctionID;
    private double price;
    private Date timestamp;
    private String signature;

    public long getAuctionID() {
        return auctionID;
    }

    public void setAuctionID(long auctionID) {
        this.auctionID = auctionID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
