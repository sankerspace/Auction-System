


import auctionmanagement.PartBClient;



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sanker
 */

public class AuctionClient {
   

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
       PartBClient client=new PartBClient(args);
       
       int code = client.run();
        
       System.exit(code); 
    }
}
