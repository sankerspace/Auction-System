/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class RSAServerClientExtendedTest {
    
    private String ServerkeyDirectory=null; 
    private String ClientkeyDirectory=null;
    public static InputStream stdin=System.in;
    
   // ByteArrayInputStream stream
    public RSAServerClientExtendedTest() throws IOException {
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

    
    
    class ServerThread implements Runnable
    {
        
        RSAServer server=null;
        PipedOutputStream out_client=null;
        PipedInputStream  in_server=null;
        DataOutputStream write=null;
        DataInputStream read=null;
        ServerThread(RSAServer server,
                 PipedOutputStream out_client,
                 PipedInputStream  in_server) throws RSAAuthenticationException
        {
            this.server=server;
            this.out_client=out_client;
            this.in_server=in_server;
            write = new DataOutputStream(this.out_client);
            read = new DataInputStream(this.in_server);
            
        }
        @Override
        public void run()
        {
            String encrypted=null,decrypted=null;
            byte[] tmp=null;
            try {
                this.server.startServerAuthenticationProcedure(in_server, out_client);
                AES aes=this.server.getAES();
                
                //read a regular message after authentication
                encrypted=read.readUTF();
                tmp=aes.decrypt(encrypted.getBytes());
                decrypted=new String(tmp);
                System.out.println("\nMESSAGE:"+decrypted);
                
                
               
             } catch (RSAAuthenticationException ex) {
                 System.out.println("ServerThread:RSAAuthenticationException:"+ex.getMessage());
             } catch (Exception ex) {
                 System.out.println("ServerThread:Exception:"+ex.getMessage());
             }
        }
    
    }
    
    
     class ClientThread implements Runnable
    {
        RSAClient client=null;
        PipedOutputStream out_server=null;
        PipedInputStream  in_client=null;
        DataOutputStream write=null;
        DataInputStream read=null;
        ClientThread(RSAClient client,
                PipedOutputStream out_server,
                PipedInputStream  in_client) throws RSAAuthenticationException
        {
            this.client=client;
            this.out_server=out_server;
            this.in_client=in_client;
            write = new DataOutputStream(this.out_server);
            read = new DataInputStream(this.in_client);
        }
        @Override
        public void run()
        {
             String encrypted=null,decrypted=null;
             byte[] tmp=null;
             try {
                 this.client.startClientAuthenticationProcedure(in_client, out_server);
                 AES aes=this.client.getAES();
               
                 //send a regular message after authentication
                decrypted=new String("Message from Client "+this.client.getUsername());
                tmp=aes.encrypt(decrypted.getBytes());
                encrypted=new String(tmp);
                write.writeUTF(encrypted);
                
                
                 
                 
                 
             } catch (RSAAuthenticationException ex) {
                 System.out.println("ClientThread:RSAAuthenticationException:"+ex.getMessage());
             } catch (Exception ex) {
                 System.out.println("ClientThread:Exception:"+ex.getMessage());
             }
        }
    
    }
    
    @Test
    public void testServerClientRSAAuthenticationPerStream() throws Exception {
        System.out.println("\ntestServerClientRSAAuthenticationPerStream:\n");
        System.out.println(ServerkeyDirectory);
        InputStream input1 = new ByteArrayInputStream(("23456\n").getBytes());
        InputStream input2 = new ByteArrayInputStream(("12345\n").getBytes());
        /*Communication between Server and Client over Streamconnection*/ 
        /*instead of socket stream we use PipedInputOutputSTream*/
       
        PipedInputStream  in_client = new PipedInputStream();
        PipedOutputStream out_client= new PipedOutputStream(in_client);
        PipedInputStream  in_server = new PipedInputStream();
        PipedOutputStream out_server= new PipedOutputStream(in_server);
        
        try{
            //Initialize RSA SERVER
            System.out.println("Create RSA Server instance....");
            System.setIn(input1);
            RSAServer server = new RSAServer("auction-server",ClientkeyDirectory,
                    ServerkeyDirectory);
            
            //Initialize RSA CLIENT
            System.out.println("Create RSA Client instance....");
            System.setIn(input2);//USER ********ALICE********
            RSAClient client = new RSAClient("alice","1234567","auction-server",ClientkeyDirectory,
                    ServerkeyDirectory);
            
            //start all threads
            ClientThread clientthread = new ClientThread(client,
                    out_server,in_client);
            
            ServerThread serverthread=new ServerThread(server,
                    out_client,in_server);
            
            Thread cl=new Thread(clientthread);
            Thread se=new Thread(serverthread);
            
            se.start();
            cl.start();
            
            se.join();
            cl.join();
            
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
