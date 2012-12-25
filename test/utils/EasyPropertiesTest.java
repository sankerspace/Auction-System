/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.File;
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
public class EasyPropertiesTest {
    private  static String file_1 = "./test/TestLogin.properties";
    private  static String file_2 = "./test/Random.properties";
    public EasyPropertiesTest() {
        
    }
    
    @BeforeClass
    public static void setUpClass() {
       System.out.println("setUpClass.");
       File file = new File(file_1);
        if(file.exists()&&file.isFile())
        {
            file.delete();
            System.out.println(file_1+" deleted!!");
        }
        
         File file_ = new File(file_1);
        if(file_.exists()&&file_.isFile())
        {
            file_.delete();
            System.out.println(file_2+" deleted!!");
        }
       
    }
    
    @AfterClass
    public static void tearDownClass() {
        System.out.println("tearDownClass.");
    }
    
    @Before
    public void setUp() {
         System.out.println("SettingUP.");
        
    }
    
    @After
    public void tearDown() {
        System.out.println("Teardown.");
    }

    /**
     * Test of writeProperty method, of class EasyProperties.
     */
 
    /**
     * Test of login method, of class EasyProperties.
     */
    @Test
    public void testCreateLoginandFile() throws UtilsException {
        
        System.out.println("testCreateLoginandFile");
        
        String user = "Marko";
        String pass = "Stanisic";
        boolean expResult = true;
        boolean result = EasyProperties.login(file_1, user, pass);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testLoginExist() throws UtilsException {
        System.out.println("testLoginExist");
        
        String user = "Marko";
        String pass = "Stanisic";
        boolean expResult = true;
        boolean result = EasyProperties.login(file_1, user, pass);
        assertEquals(expResult, result);
        pass = "Staisic";expResult=false;
        result = EasyProperties.login(file_1, user, pass);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testcreateAdditionalLogin() throws UtilsException {
        System.out.println("testcreateAdditionalLogin");
        
        String user_1 = "David";String pass_1 = "Ifraimov";
        String user_2 = "Josef";String pass_2 = "Stalin";
         String user_3 = "Winston";String pass_3 = "Churchill";
         
        boolean expResult = true;
        boolean result = EasyProperties.login(file_1, user_1, pass_1);
        assertEquals(expResult, result);
        
        expResult = true;
        result = EasyProperties.login(file_1, user_2, pass_2);
        assertEquals(expResult, result);
        
        expResult = true;
        result = EasyProperties.login(file_1, user_3, pass_3);
        assertEquals(expResult, result);
       
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    
    @Test
    public void testWriteProperty() {
        System.out.println("testWriteProperty");
        
        String user_1 = "Papa";String pass_1 = "Mama";
        
         
        boolean expResult = true;
        boolean result = EasyProperties.writeProperty(file_2, user_1, pass_1,"");
        assertEquals(expResult, result);
        
        
       
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    @Test
    public void testReadProperty() throws UtilsException {
        System.out.println("testReadProperty");
        
       String user="Papa";
       String pass=null;
        
         
        boolean expResult = true;
        pass = EasyProperties.getProperty(file_2, user);
        assertEquals(true,pass.contentEquals("Mama"));
        
        
       
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
}
