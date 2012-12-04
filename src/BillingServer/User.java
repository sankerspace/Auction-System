package BillingServer;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Dave
 */
public class User implements Serializable {

    private String name;
    // private ArrayList<Auction> auctions = new ArrayList<Auction>();
    private ConcurrentHashMap<Long, Auction> auctions = null;

    public User(String name) {
        this.name = name;
        auctions = new ConcurrentHashMap<Long, Auction>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Auction[] getAuctionList() {
        return auctions.values().toArray(new Auction[auctions.size()]);
    }

    public void putAuction(Auction a) {
        auctions.put(a.getId(), a);
    }

    public Auction getAuction(long id) {
        Iterator<Entry<Long, Auction>> iter = auctions.entrySet().iterator();
        Auction a = null;
        while (iter.hasNext()) {
            Entry<Long, Auction> entry = iter.next();
            if ((a = entry.getValue()).getId() == id) {
                return a;
            }
        }
        return null;
    }
}
