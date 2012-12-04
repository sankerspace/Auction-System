package BillingServer;

import java.io.Serializable;

/**
 *
 * @author Dave
 */
public class Auction implements Serializable{

    private long id;
    private double price;

    public Auction(long id, double price) {
        this.id = id;
        this.price = price;
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    
}
