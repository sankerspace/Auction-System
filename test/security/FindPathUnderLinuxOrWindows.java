/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package security;


import java.io.File;
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
public class FindPathUnderLinuxOrWindows {
    String pathtoServerKeys = null;
    String pathtoClientKeys = null;
    public FindPathUnderLinuxOrWindows() {
         pathtoServerKeys = "./keys/Server";
         pathtoClientKeys = "./keysClients";
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
    public void testFindServerKeyDirectory() throws Exception {
        System.out.println("Test if a path to the server key directory is valid(linux and windows):");
        System.out.println("path to Server keys(from outside the test enviromnemt):"+
                "./keys/Server");
       
        File path=new File(pathtoServerKeys);
        assertEquals(true, path.isDirectory());
    }
    @Test
    public void testFindClientKeyDirectory() throws Exception {
        System.out.println("Test if a path to the client key directory is valid(linux and windows):");
        System.out.println("path to Client keys(from outside the test enviromnemt):"+
                "./keys/Clients");
        
        File path=new File(pathtoClientKeys);
        assertEquals(true, path.isDirectory());
    }
    
}
