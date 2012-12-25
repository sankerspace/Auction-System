
package utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.Properties;
import java.util.regex.Pattern;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author sanker
 */
public class TestProperties {
    
    public TestProperties() {
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
    public void testReadProperties() throws FileNotFoundException, IOException {
        String propertyFile="./src/registry.properties";
         FileReader reader = new FileReader(propertyFile);
            Properties prop = new Properties();
            prop.load( reader );
            
            int port = Integer.parseInt(prop.getProperty("registry.port", "1099"));
        
            
            System.out.println(port);
        
    }
     @Test
    public void testWriteProperties() throws FileNotFoundException, IOException, NoSuchAlgorithmException {
     /*   
        String propertyFile="./test/user.properties";
        String user=null,pass=null;
        FileReader reader = new FileReader(propertyFile);
        MessageDigest md = MessageDigest.getInstance("MD5");
       //String pass = md.digest(input)
        Properties prop = new Properties();
        prop.load( reader );
            
        user=prop.getProperty("john");
           
        
            
        System.out.println(user);
        */
    }
}
