


import BillingServer.BillingsServer;

/**
 *
 * @author Dave
 */
public class BillingServer {

    public static void main(String[] args) {
        try {
            BillingsServer billingserver = new BillingsServer("./src/registry.properties", args[0]);
            billingserver.run();
        } catch (Exception ex) {
            System.out.print("BillingServer:" + ex.getMessage());
        }
        System.exit(0);
    }
}
