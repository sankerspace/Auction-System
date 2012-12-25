/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalyticsServer;


import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
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

public class ReferenztoFilterTest {
    
    private  ConcurrentHashMap<Long,Filter> mclient_map;
    public ReferenztoFilterTest() {
        
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
     * Test of checkMessage method, of class Filter.
     */
    @Test
    public void testCheckMessage() {
        
        mclient_map=new ConcurrentHashMap<Long,Filter>();
        
        
        
        System.out.println("checkMessage");
        String message = "";
        int test_1=0,test_2=0,test_3=0,test_4=0,test_5=0,test_6=0,test_7=0;
        
        Filter filter_1 = new Filter();
        Long IDfilter_1 = new Long(0);
        mclient_map.put(IDfilter_1,filter_1);
        Filter filter_2 = new Filter();
        Long IDfilter_2 = new Long(1);
        mclient_map.put(IDfilter_2,filter_2);
        
        
        String Regex_1 = new String("(USER_.*)");
        String Regex_2 = new String("(USER_.*)|(BID_.*)");
        String Regex_3 = new String("(USER_.*)+(.*_AVG$)");
        String Regex_4 = new String("(.*MAX$)");
        String Regex_5 = new String("(AUCTION.*)");
        String Regex_6 = new String("(.*AVG$)");
        
        String [] messages ={"USER_LOGIN","USER_LOGOUT","USER_DISCONNECTED"
                              ,"AUCTION_STARTED","AUCTION_ENDED"
                              ,"BID_PLACED","BID_OVERBID","BID_WON"
                              ,"USER_SESSIONTIME_MIN","USER_SESSIONTIME_MAX"
                              ,"USER_SESSIONTIME_AVG"
                              ,"BID_PRICE_MAX","BID_COUNT_PER_MINUTE"
                               ,"AUCTION_TIME_AVG"
                               ,"AUCTION_SUCCESS_RATIO"};
        
         Pattern p = Pattern.compile(Regex_5);
        
         Filter filter_3=mclient_map.get(IDfilter_1);
        filter_3.subscribeRegex(new Long(0),Pattern.compile(Regex_1));
         Filter filter_4=mclient_map.get(IDfilter_2);
        filter_4.subscribeRegex(new Long(0),Pattern.compile(Regex_2));
       
        
       
        for(int i=0;i<messages.length;i++)
        {
            if(filter_1.checkMessage(messages[i]))
                test_1++;
            if(filter_2.checkMessage(messages[i]))
                test_2++;
           
        }
        
        assertEquals(test_1, 6);
        assertEquals(test_2, 11);
        
        
        
    }
}
