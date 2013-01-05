package security;

import Main.AuctionClient;
import Main.AuctionServer;
import Main.BillingServer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Dave
 */
public class ServerOutageTest {

    public ServerOutageTest() {
    }

    @BeforeClass
    public static void setUpClass() {
       
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /*
     * Test of Stage4:getClientList
     */
    @Test
    public void testgetClientList() {
        /*
         * 1. Create 3 new clients and servers
         */

        /*
         * Init AuctionServer
         */
        AuctionServer auctionserver = new AuctionServer();

        /*
         * Init Clients
         */
        AuctionClient auctionClient1 = new AuctionClient();
        AuctionClient auctionClient2 = new AuctionClient();
        AuctionClient auctionClient3 = new AuctionClient();
        
     //   auctionClient1.
        /*
         * 2. Use command !getClientList with one of the clients (interactive)
         * Save in list one.
         */ 
        
        /*
         * 3. Get clientList of client (parameter: login)
         * Save in list two.
         */ 
        
        /*
         * 4. Make an own list of created clients
         */ 
        
        /*
         * 5. Compare step 2. and step 3. with step 4.
         */
        
    }
}
