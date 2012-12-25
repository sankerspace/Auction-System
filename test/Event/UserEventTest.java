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
public class UserEventTest {
    
    public UserEventTest() {
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
     * Test of getUserName method, of class UserEvent.
     */
     @Test
     public void testConstructor() {
         System.out.println("\n");
        UserEvent instance = new UserEvent("Marko",UserEvent.UserEventType.USER_DISCONNECTED);
        // TODO review the generated test code and remove the default call to fail.
        System.out.println("test UserEvent Construktor ");
        System.out.println("Event:getID:"+instance.getID());
        System.out.println("Event:getType:"+instance.getType());
         System.out.println("Event:getTimeStamp:"+instance.getTimeStamp());
       //  System.out.println("BidEvent:getAuctionID:"+instance.);
          System.out.println("UserEvent:getUserName:"+instance.getUserName()); 
    }
    
    @Test
    public void testCopyConstructor() {
        System.out.println("\n");
        // TODO review the generated test code and remove the default call to fail.
        System.out.println("test UserEvent CopyConstruktor");
        UserEvent tocopy=new UserEvent("Alice",UserEvent.UserEventType.USER_LOGIN);
        UserEvent instance=new UserEvent(tocopy);
        System.out.println("Event:getID:"+instance.getID());
        System.out.println("Event:getType:"+instance.getType());
       
        System.out.println("UserEvent:getUserName:"+instance.getUserName()); 
     
    }
}
