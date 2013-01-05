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
public class StatisticsEventTest {
    
    public StatisticsEventTest() {
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

    /**
     * Test of getTypeValue method, of class StatisticsEvent.
     */
    @Test
     public void testConstructor() {
         System.out.println("\n");
        StatisticsEvent instance = new StatisticsEvent(1.34,StatisticsEvent.StatisticsEventType.AUCTION_SUCCESS_RATIO);
        System.out.println("test StatisticsEvent Construktor ");
        System.out.println("Event:getID:"+instance.getID());
        System.out.println("Event:getType:"+instance.getType());
         System.out.println("Event:getTimeStamp:"+instance.getTimeStamp());
       //  System.out.println("BidEvent:getAuctionID:"+instance.);
          System.out.println("StatisticsEvent:getTypeValue:"+instance.getTypeValue()); 
    }
    
    @Test
    public void testCopyConstructor() {
        System.out.println("\n");
        System.out.println("test StatisticsEvent CopyConstruktor");
        StatisticsEvent tocopy=new StatisticsEvent(2.34,StatisticsEvent.StatisticsEventType.AUCTION_TIME_AVG);
        StatisticsEvent instance=new StatisticsEvent(tocopy);
        System.out.println("Event:getID:"+instance.getID());
        System.out.println("Event:getType:"+instance.getType());
       
        System.out.println("StatisticsEvent:getTypeValue:"+instance.getTypeValue()); 
     
    }
}
