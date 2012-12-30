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
public class BidEventTest {
    
    public BidEventTest() {
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
        BidEvent instance = new BidEvent("Marko",2,44,BidEvent.BidEventType.BID_OVERBID);
        System.out.println("test BidEvent Construktor ");
        System.out.println("Event:getID:"+instance.getID());
        System.out.println("Event:getType:"+instance.getType());
         System.out.println("Event:getTimeStamp:"+instance.getTimeStamp());
       //  System.out.println("BidEvent:getAuctionID:"+instance.);
    }
    
    @Test
    public void testCopy() {
        System.out.println("\n");
        System.out.println("test BidEvent CopyConstruktor");
        BidEvent tocopy=new BidEvent("Alice",0,300.34,BidEvent.BidEventType.BID_WON);
        BidEvent instance=new BidEvent(tocopy);
        System.out.println("Event:getID:"+instance.getID());
        System.out.println("Event:getType:"+instance.getType());
        System.out.println("Event:getTimeStamp:"+instance.getTimeStamp());
        System.out.println("Bidevent:getUserName:"+instance.getUserName()); 
        System.out.println("Bidevent:getPrice:"+instance.getPrice());
    }
}
