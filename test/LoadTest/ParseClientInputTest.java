/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LoadTest;

import auctionmanagement.Auction;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assume.*;

/**
 *
 * @author sanker
 */
public class ParseClientInputTest {
    
    HashMap<Long,Auction> map = null;
    long numberAuctions=30;
    
    public ParseClientInputTest() {
       
        
      map=this.createAuctions(numberAuctions);
       
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
    
    String printMap()
    {
        StringBuffer list=new StringBuffer();
        Iterator<Entry<Long,Auction>> iter = map.entrySet().iterator();
        while(iter.hasNext())
        {
           
            Entry<Long,Auction> entry = iter.next();
            list.append(entry.getKey().toString() + "." + " '"
                  + entry.getValue().getDescription() + "' "
                  + entry.getValue().getOwner() + " "
                  + entry.getValue().getEndDate() + " "
                  + Double.toString(entry.getValue().getHighestBid()) + " "
                  + entry.getValue().getHighestBidder() + "\n");
        }
        
        return list.toString();
    }
   
    
    HashMap<Long,Auction> createAuctions(long numbers)
    {
        HashMap<Long,Auction> map_=new HashMap<Long,Auction>();
        for(long i=1;i!=numberAuctions;i++)
       {
           Auction a = new Auction(("client"+i),(i*60),("description"+i+" of the "+i +" auction."));
           map_.put(i, a);
       }
        
        return map_;
    }
    
    boolean compareMaps(HashMap<Long,Auction> map1,HashMap<Long,Auction> map2)
    {
        if(map1.size()!=map2.size())return false;
        Iterator<Entry<Long,Auction>> iter = map1.entrySet().iterator();
        Auction a1=null,a2=null;
        while(iter.hasNext())
        {
            Entry entry = iter.next();
            a1=(Auction)entry.getValue();
            a2=map2.get(entry.getKey());
            if(a2==null)return false;
            if(!(a1.getOwner().contentEquals(a2.getOwner())))return false;
            if(a1.getHighestBid()!=a2.getHighestBid())return false;
            if(!(a1.getHighestBidder().contentEquals(a2.getHighestBidder())))return false;
                        
        }
        
        return true;
        
    }
    
   
    @Test
    public void testParseList() {
        System.out.println("parseList");
        System.out.println("Created Auction List with "+this.numberAuctions+" Auctions:");
        String message = printMap();
        String clientOwner = "client"+(this.numberAuctions/2);
        HashMap expResult = this.map;
        expResult.remove(this.numberAuctions/2);
        HashMap result = ParseClientInput.parseList(message, clientOwner);
        assertTrue(compareMaps(expResult, result));
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
}
