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
public class EventTest {
    Event event=null;
    public EventTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        event = new Event(AuctionEvent.AuctionType.AUCTION_ENDED.name());
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getID method, of class Event.
     */
    @Test
    public void testGetID() {
        System.out.println("getID");
        
        //assertEquals(expResult, result);
        //fail("The test case is a prototype.");
        System.out.println(event.getID());
    }

    /**
     * Test of getTimeStamp method, of class Event.
     */
    @Test
    public void testGetTimeStamp() {
        System.out.println("getTimeStamp");
       
        //assertEquals(expResult, result);
        //fail("The test case is a prototype.");
        System.out.println(event.getTimeStamp());
    }

    /**
     * Test of getType method, of class Event.
     */
    @Test
    public void testGetType() {
        System.out.println("getType");
        System.out.println(event.getType());
    }
}
