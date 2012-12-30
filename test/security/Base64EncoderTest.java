package security;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author sanker
 */
public class Base64EncoderTest {
    
    String plaintext=null;
    String storeEncoding=null;
    
    public Base64EncoderTest() {
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
     * Test of encodeBase64 method, of class Base64Encoder.
     */
    @Test
    public void testEncodeDecodeBase64() {
        ///////////////////////////////
       // plaintext="Text to convert.";
          plaintext="ABCDEFGHIJKLMNOPQRSTUPVWYZ123456789";
        /////////////////////////////////
        System.out.println("Convert following text: "+plaintext);
        
        System.out.println("::encodeBase64::");
        
        byte[] plaindata = plaintext.getBytes();
        byte[] tmp = Base64Encoder.encodeBase64(plaindata);
        this.storeEncoding=new String(tmp);
        System.out.println(storeEncoding);
        System.out.println("::decodeBase64::");
        byte[] base64Message = this.storeEncoding.getBytes();
        byte[] expResult = plaindata;
        byte[] result = Base64Encoder.decodeBase64(base64Message);
        String decodedText=new String(result);
        System.out.println("Decoded Text: "+decodedText);
        assertArrayEquals(expResult, result);
    }
}
