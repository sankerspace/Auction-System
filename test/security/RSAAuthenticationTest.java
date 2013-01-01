/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Scanner;
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
public class RSAAuthenticationTest {
    
    private String ServerkeyDirectory=null; 
    private String ClientkeyDirectory=null;
    public static InputStream stdin=System.in;
    
   // ByteArrayInputStream stream
    public RSAAuthenticationTest() throws IOException {
        ServerkeyDirectory="./keys/Server/";
        ClientkeyDirectory="./keys/Clients/";
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
        System.setIn(stdin);
        
        
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() throws IOException {
       
       
    }

    /**
     * Test of getPublicKey method, of class RSAAuthentication.
     */
    @Test
    public void testGetServerPublicKey() throws Exception {
        System.out.println("getPublicKey from Server:");
      
        RSAAuthentication instance = new RSAAuthentication(ClientkeyDirectory,
                ServerkeyDirectory);
        
        String file = instance.getServerKeyDirectorypath()+"/auction-server.pub.pem";
        PublicKey expResult = null;
        PublicKey result = instance.getPublicKey(file);
        
        assertNotNull(result);
        System.out.println("Server PublicKey:");
        System.out.println(result);
        // TODO review the generated test code and remove the default call to fail.
       // fail("The test case is a prototype.");
    }

    /**
     * Test of getPrivateKey method, of class RSAAuthentication.
     */
    @Test
    public void testServerGetPrivateKey() throws Exception {
      
        PipedInputStream inputPipe=new PipedInputStream();
        PipedOutputStream outputPipe=new PipedOutputStream(inputPipe);
        System.setIn(inputPipe);
        PrintWriter print=new PrintWriter(outputPipe,true);
        
        
        System.out.println("getPrivateKey from Server");
        RSAAuthentication instance = new RSAAuthentication(ClientkeyDirectory,
                ServerkeyDirectory);
        String file = instance.getServerKeyDirectorypath()+"/auction-server.pem";
        PrivateKey expResult = null;
        
        print.println("23456");print.flush();
        PrivateKey result = instance.getPrivateKey(file);
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
        assertNotNull(result);
        System.out.println("Server Private Key:");
        System.out.println(result);
    }
  
}
