package auctionmanagement;

import java.text.ParseException;
import java.util.Date;
import security.EasySignature;
import security.EasySignatureException;

/**
 * A timestamp contains the complete message in form of "!timestamp <auction-id>
 * <price> <timestamp> <signature>"
 *
 * @author Dave
 */
public class Timestamp {

    private long auctionID;
    private double price;
    private long timestamp;
    private String signature;
    private String keyDir;
    private String username;

    public Timestamp(long auctionId, double price, String keyDir, String username) {
        this.price = price;
        this.auctionID = auctionId;
        this.timestamp = System.currentTimeMillis();
        this.keyDir = keyDir;
        this.username = username;
        sign();
    }

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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * Signs a message using EasySignature.
     */
    private void sign() {
        try {
            EasySignature es = new EasySignature(this.keyDir, this.username, true);
            this.signature = es.sign(signature);
        } catch (EasySignatureException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Verifies a message.
     *
     * @param msg The message that needs to be verified.
     * @return true if verification was successful. False otherwise.
     */
    public boolean verifyTimestamp(String signature) throws ParseException, EasySignatureException {
        //<a-id> <price> <timestamp>
        EasySignature es = new EasySignature(this.keyDir, this.username, true);
        String plaintext = this.auctionID + " " + this.price + " " + this.timestamp;
        //String[] splitted = signature.split(" ");
        return es.verify(plaintext,signature);
//        if (this.auctionID == Long.parseLong(splitted[0])
//                && this.price == Double.parseDouble(splitted[1])
//                && this.timestamp == Long.parseLong(splitted[2])) {
//            return true;
//        } else {
//            return false;
//        }
    }
}
