/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Event;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sanker
 */
public class AuctionEventTest {
    
    public AuctionEventTest() {
       
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

    @Test
    public void testSomeMethod() {
        System.out.println("\n");
        AuctionEvent instance = new AuctionEvent(AuctionEvent.AuctionType.AUCTION_STARTED,1);
        System.out.println("test AuctionEvent Construktor ");
        System.out.println("getID:"+instance.getID());
        System.out.println("getType:"+instance.getType());
        System.out.println("getAuctionID:"+instance.getAuctionID());
         System.out.println("getTimeStamp:"+instance.getTimeStamp());
    }
    
    @Test
    public void testCopy() {
        System.out.println("\n");
        System.out.println("test AuctionEvent CopyConstruktor");
        AuctionEvent tocopy=new AuctionEvent(AuctionEvent.AuctionType.AUCTION_ENDED,3);
        AuctionEvent instance=new AuctionEvent(tocopy);
        System.out.println("test AuctionEvent CopyConstruktor ");
        System.out.println("getID:"+instance.getID());
        System.out.println("getType:"+instance.getType());
        System.out.println("getAuctionID:"+instance.getAuctionID());
         System.out.println("getTimeStamp:"+instance.getTimeStamp());
    }
}
