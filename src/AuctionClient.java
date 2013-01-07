

import auctionmanagement.PartBClient;

/**
 *
 * @author sanker
 */
public class AuctionClient {
    public static void main(String[] args) {
       PartBClient client=new PartBClient(args);
       int code = client.run();
       System.exit(code); 
    }
}
