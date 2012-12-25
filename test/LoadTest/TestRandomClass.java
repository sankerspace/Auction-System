/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LoadTest;


import java.util.Random;
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
public class TestRandomClass {
    
    public TestRandomClass()        
    {
            
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
    
    
    @Test
    public void testParseList() {
         int maximum=30;
         Random random=new Random();
         boolean b=true;
         for(int i=0;i<maximum;i++)
         {
              int select = random.nextInt(maximum);
              if((i%10)==0)
                  System.out.println("\n");
              System.out.print(select+" ");
              if(select > maximum)
                  b=false;
                  
         }
         
         assertEquals(true,b);
     }
}
