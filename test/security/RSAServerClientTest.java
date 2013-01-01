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
import junit.framework.Assert;
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
public class RSAServerClientTest {
    
    private String ServerkeyDirectory=null; 
    private String ClientkeyDirectory=null;
    public static InputStream stdin=System.in;
    
   // ByteArrayInputStream stream
    public RSAServerClientTest() throws IOException {
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

    
    @Test
    public void testServerClientRSAAuthentication() throws Exception {
        System.out.println("testServerClientRSAAuthentication:\n");
        InputStream input1 = new ByteArrayInputStream(("23456\n").getBytes());
        InputStream input2 = new ByteArrayInputStream(("12345\n").getBytes());
        boolean b=false;
        
        
        try{
            //Initialize RSA SERVER
            System.out.println("Client sends first authenticationMessage to Server.");
            System.out.println("Create RSA Server instance....");
            System.setIn(input1);
            RSAServer server = new RSAServer("auction-server",ClientkeyDirectory,
                    ServerkeyDirectory);
            
            //Initialize RSA CLIENT
            System.out.println("Create RSA Client instance....");
            System.setIn(input2);
            RSAClient client = new RSAClient("alice","auction-server",ClientkeyDirectory,
                    ServerkeyDirectory);
            
            //first message RSA Client to RSA Server [HANDSHAKEPROTOCOL]
            System.out.println("[HANDSHAKEPROTOCOL] First Message:");
            byte[] firstmessage=client.HandshakeProtocolMessageOneCreate(client.getUsername(), "1234567");
            b=server.HandshakeProtocolMessageOneHandle(firstmessage);
            Assert.assertTrue(b);
            
            
            //second message RSA Server reponds to RSA Client [HANDSHAKEPROTOCOL]
            System.out.println("[HANDSHAKEPROTOCOL] Second Message:");
            byte[] secondMessage=server.HandshakeProtocolMessageTwoCreate();
            b=client.HandshakeProtocolMessageTwoHandle(secondMessage);
            Assert.assertTrue(b);
            
            //third message AES Client sends first AES message to AES Server [HANDSHAKEPROTOCOL]
            System.out.println("[HANDSHAKEPROTOCOL] Third Message:");
            byte[] thirdMessage=client.HandshakeProtocolMessageThreeCreate();
            b=server.HandshakeProtocolMessageThreeHandle(thirdMessage);
            Assert.assertTrue(b);
            
            //get AES object from client 
            System.out.println("\nGet CLIENT AES object after Authentication:");
            AES AESClient=client.getAES();
            Assert.assertNotNull(AESClient);
            System.out.print(AESClient);
            
            //get AES object from server 
            System.out.println("\nGet SERVER AES object after Authentication:");
            AES AESServer=server.getAES();
            Assert.assertNotNull(AESServer);
            System.out.print(AESServer);
            
        }catch(RSAAuthenticationException ex)
        {
            System.out.println("Error:"+ex.getMessage());
            
        }finally
        {
            input1.close();
            input2.close();
        
        }
      
        
    }
    
    
}
