/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalyticsServer;

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
public class FilterTest {
    
    public FilterTest() {
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
        System.out.println("checkMessage");
        String message = "";
        int test_1=0,test_2=0,test_3=0,test_4=0,test_5=0,test_6=0,test_7=0;
        Filter filter_1 = new Filter();
        Filter filter_2 = new Filter();
        Filter filter_3 = new Filter();
        Filter filter_4 = new Filter();
        Filter filter_5 = new Filter();
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
        
        filter_1.subscribeRegex(new Long(0),Pattern.compile(Regex_1));
        filter_2.subscribeRegex(new Long(0),Pattern.compile(Regex_2));
        filter_3.subscribeRegex(new Long(0),Pattern.compile(Regex_3));
        
        filter_4.subscribeRegex(new Long(0),Pattern.compile(Regex_1));
        filter_4.subscribeRegex(new Long(1),Pattern.compile(Regex_4));
        
        filter_5.subscribeRegex(new Long(0),p);
        filter_5.subscribeRegex(new Long(1),Pattern.compile(Regex_6));
        
       
        for(int i=0;i<messages.length;i++)
        {
            if(filter_1.checkMessage(messages[i]))
                test_1++;
            if(filter_2.checkMessage(messages[i]))
                test_2++;
            if(filter_3.checkMessage(messages[i]))
                test_3++;
            if(filter_4.checkMessage(messages[i]))
                test_4++;
            if(filter_5.checkMessage(messages[i]))
                test_5++;
        }
        
        
        boolean b=filter_5.unsubscribeRegex(new Long(0));
        assertEquals(b,true);
        b=filter_4.unsubscribeRegex(new Long(0));
        assertEquals(b,true);
        
        for(int j=0;j<messages.length;j++)
        {
             if(filter_5.checkMessage(messages[j]))
                test_6++;
             if(filter_4.checkMessage(messages[j]))
                test_7++;
        }
        
        
        boolean expResult = false;
        //boolean result = instance.checkMessage(message);
        assertEquals(test_1, 6);
        assertEquals(test_2, 11);
        assertEquals(test_3, 1);
        assertEquals(test_4, 7);
        assertEquals(test_5, 5);
        assertEquals(test_6, 2);
        assertEquals(test_7, 2);
        //fail("The test case is a prototype.");
    }
}