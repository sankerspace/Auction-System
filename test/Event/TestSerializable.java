/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Event;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
public class TestSerializable {
    Event event=null;
    public TestSerializable() {
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
     * Test of getID method, of class Event.
     */
    @Test
    public void testCopyAbility() throws IOException, ClassNotFoundException {
        System.out.println("getID");
     //create Object with content
        Event event_source = new Event(AuctionEvent.AuctionType.AUCTION_ENDED.name());
        
        //write Object to MemoryBuffer
        //only for internal Memory copy
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(event_source);
        
      //read Object from MemoryBuffer  
        
        ByteArrayInputStream bais= new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Event event_dest = new Event((Event)ois.readObject());
       
        System.out.println("test serialized object copy Output: ");
        System.out.println("Event:getID:"+event_dest.getID());
        System.out.println("Event:getType:"+event_dest.getType());
        System.out.println("Event:getTimeStamp:"+event_dest.getTimeStamp());
    }

    
}
