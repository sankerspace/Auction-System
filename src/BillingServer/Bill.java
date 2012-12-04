package BillingServer;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Dave
 */
public class Bill implements Serializable {

    private ConcurrentHashMap<String, User> users;
    String billReport = "";
    double fee_fixed;
    double fee_variable;
    double fee_total;

    public Bill() {
        users = new ConcurrentHashMap<String, User>();
    }

    public void createBill(String user, PriceSteps ps) {
        this.billReport = "|StrikePrice|FeeFixed|FeeVariable|FeeTotal|\n";
        if (!this.isUserAvailable(user)) {
            System.out.println("Error:User " + user + " is not available.");
        } else {
            double strike_price;
            User u = users.get(user);
            for (Auction a : u.getAuctionList()) {
                strike_price = a.getPrice();
                if (calculateFee(strike_price, ps)) {
                    fee_total = fee_fixed + fee_variable;
                    String[] values = {"" + strike_price, "" + fee_fixed, "" + Math.rint(fee_variable*100)/100., "" + Math.rint(fee_total*100)/100.};
                     String format = "|%1$-11s|%2$-8s|%3$-11s|%4$-8s|\n";
                    billReport = billReport + String.format(format, (Object[]) values);
                }
            }
        }
    }

    public ConcurrentHashMap<String, User> getUsers() {
        return users;
    }

    public synchronized void putAuctiontoUser(Auction a, String name) {
        User u = this.users.get(name);
        u.putAuction(a);
    }

    public boolean adduser(User u) {
        this.users.put(u.getName(), u);
        if (!this.users.containsKey(u.getName())) {
            return false;
        } else {
            return true;
        }


    }

    public boolean isUserAvailable(String name) {
        return users.containsKey(name);
    }

    private boolean calculateFee(double strike_price, PriceSteps ps) {
        for (PriceStep pS : ps.priceSteps) {
            if ((strike_price >= pS.getMin_value() && strike_price <= pS.getMax_value()) || pS.getMax_value() == 0) {
                fee_fixed = pS.getFee_fixed();
                fee_variable = pS.getFee_variable() * strike_price;
                System.out.println("Fee was set successfully.");
                return true;
            }
        }
        System.out.println("Strike Price is not inside Price Configuration.");
        return false;
    }

    @Override
    public String toString() {
        return billReport;
    }
}
