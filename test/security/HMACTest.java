/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Dave
 */
public class HMACTest {
    
    public HMACTest() {
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
     * Test of generateHmac method, of class HMAC.
     */
    @Test
    public void validateHMac() {
       String serverMessage = "0.item1400bob";
       HMAC hmac = new HMAC();
       byte[] serverMessageHmac = hmac.generateHmac(serverMessage);
       String plaintextWithHmac = hmac.getPlaintextWithHmac();
       
       assertTrue(hmac.validateHMac(plaintextWithHmac));
    }
}
