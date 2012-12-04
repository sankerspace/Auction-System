

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
        // TODO code application logic here
        //PartBClient client=new PartBClient(args);
        
        //client.printUsage();
     PartBServer server = new PartBServer(args);
      
     int code = server.run();
      
      
      System.exit(code);
    }
}
