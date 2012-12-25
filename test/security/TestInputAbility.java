/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import java.io.BufferedReader;
import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author sanker
 */



import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
public class TestInputAbility {
    
    public TestInputAbility() {
        
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
     * Test of getPublicKey method, of class RSAAuthentication.
     */
    @Test
    public void testInput() throws Exception {
       System.out.println("Test if junit allows User Input:");
        String line=null;
        BufferedReader input=null;
        try {

            input = new BufferedReader(new InputStreamReader(System.in));
            //input.read();
            //line=input.readLine();
             System.out.println("Begin Input:");
             System.out.println( input.toString());
            while((line=input.readLine())!=null)
            {
                System.out.print("\n>:");
                if(line.contains("exit"))
                break;
                System.out.println(line);
            }
           
        } catch (IOException ex) {
                       
                   
        }finally
        {
            try {
                input.close();
            } catch (IOException ex) {

            }
        }
      
        assertEquals(true, true);
    }
    
    
}

