/**
 *
 * @author sanker
 */
import auctionmanagement.PartBServer;
import auctionmanagement.AuctionManagementSystem;

public class AuctionServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //PartBClient client=new PartBClient(args);
        //client.printUsage();
     PartBServer server = new PartBServer(args);
      
     int code = server.run();
      
      
      System.exit(code);
    }
}
