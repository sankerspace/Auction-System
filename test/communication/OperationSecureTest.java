/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import security.AES;
import security.RSAAuthenticationException;
import security.RSAClient;
import security.RSAServer;

/**
 *
 * @author sanker
 */
public class OperationSecureTest {
    
    public OperationSecureTest() {
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
    /*
    class ServerThread implements Runnable
    {
        Client client=null;
        String servername=null;
        String serverkeydirectory=null;
        String clientkeydirectory=null;
        ServerThread(int port,String servername
                ,String serverkeydirectory
                ,String clientkeydirectory) throws RSAAuthenticationException
        {
            
            
        }
        @Override
        public void run()
        {
            
               
             } catch (RSAAuthenticationException ex) {
                 System.out.println("ServerThread:RSAAuthenticationException:"+ex.getMessage());
             } catch (Exception ex) {
                 System.out.println("ServerThread:Exception:"+ex.getMessage());
             }
        }
    
    }
    
    
     class ClientThread implements Runnable
    {
        Client c=null;
        String clientname=null;
        String userinfo=null;
        String servername=null;
        String serverkeydirectory=null;
        String clientkeydirectory=null;
        ClientThread(String host,int port,
                String userinfo,String servername,
                String serverkeydirectory,
                String clientkeydirectory) throws RSAAuthenticationException, ClientException
        {
             c= new Client(host,port);
             this.clientkeydirectory=clientkeydirectory;
             this.serverkeydirectory=serverkeydirectory;
             this.userinfo=userinfo;
             this.servername=servername;
        }
        @Override
        public void run()
        {
            
            OperationSecure opsec=null;
            try{
                
                 opsec = new OperationSecure();
             } catch (RSAAuthenticationException ex) {
                 System.out.println("ClientThread:RSAAuthenticationException:"+ex.getMessage());
             } catch (Exception ex) {
                 System.out.println("ClientThread:Exception:"+ex.getMessage());
             }
        }
    
    }
    */

    /**
     * Test of writeString method, of class OperationSecure.
     */
    @Test
    public void testWriteString() throws Exception {
        System.out.println("writeString");
        String s = "";
        OperationSecure instance = null;
        //instance.writeString(s);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readString method, of class OperationSecure.
     */
    @Test
    public void testReadString() throws Exception {
        System.out.println("readString");
        OperationSecure instance = null;
        String expResult = "";
        //String result = instance.readString();
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
