/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import security.AES;
import security.AESException;
import security.RSAAuthenticationException;
import security.RSAClient;
import security.RSAServer;

/**
 *
 * @author sanker
 */
public class OperationSecure implements Operation{
    
    private Client client = null;
    private OutputStream out = null;
    private InputStream in = null;
    
    /*Secure parameters*/
   
    private AES securechannel=null;
    private String user=null;
    private String userinfo=null;
    private String lastReceivedMessageAfterRSAServerFailure=null;
    /*
     * Konstruktor for client side
     */
    public OperationSecure(Client c,String clientname,String userinfo,String servername,
            String serverkeydirectory,String clientkeydirectory) throws OperationException, RSAAuthenticationException
    {   
        try
        {
            client = c;
            in = client.getInputStream();
            out = client.getOutputStream();
            this.user=clientname;
            this.userinfo=userinfo;
            
           
            RSAClient rsaclient=new RSAClient(clientname,userinfo,servername,
                    clientkeydirectory,serverkeydirectory);
            rsaclient.startClientAuthenticationProcedure(in, out);
            this.securechannel=rsaclient.getAES();
            
        }catch(ClientException e)
        {
            throw new OperationException("ClientException::"+e.getMessage());
        }
        
    }
    /*
     * Konstruktor for server side
     */
    public OperationSecure(Client c,String servername,
            String serverkeydirectory,String clientkeydirectory) throws OperationException,RSAAuthenticationException
    {   
        RSAServer server=null;
        try
        {
            client = c;
            in = client.getInputStream();
            out = client.getOutputStream();
           
           server = new RSAServer(servername,clientkeydirectory,
                    serverkeydirectory);
           server.startServerAuthenticationProcedure(in, out);
           this.user=server.getUsername();
           this.userinfo=server.getUserInfo();
           this.securechannel=server.getAES();
            
        }catch(ClientException e)
        {
            throw new OperationException("ClientException::"+e.getMessage());
        }catch(RSAAuthenticationException e)
        {
            this.lastReceivedMessageAfterRSAServerFailure=server.getLastMessage();
            throw e;
        }
        
    }
    
     public OperationSecure(OperationSecure op)
     {
         this.client=op.client;
         this.in=op.in;
         this.lastReceivedMessageAfterRSAServerFailure=op.lastReceivedMessageAfterRSAServerFailure;
         this.out=op.out;
         this.securechannel=this.securechannel;
         this.user=op.user;
         this.userinfo=op.userinfo;
         
     }
    
    /*
     * Methode 
     */
    public void writeString(String s)throws OperationException
    {
        String encrypted=null;
        byte[] tmp=null;
        DataOutputStream w=null;
       try
        {
            w = new DataOutputStream(out);
            tmp=this.securechannel.encrypt(s.getBytes());
            encrypted=new String(tmp);
            w.writeUTF(encrypted);
            
        } catch (AESException e) {
           throw new OperationException("AESException::"+e.getMessage());
        }catch(IOException e)
        {
            throw new OperationException("IOException::"+e.getMessage());
        }
        
       
    }
    
    
     public String readString()throws OperationException
    {
       String decrypted=null,s=null;
       byte[] tmp=null;
       DataInputStream r =null;
       try
        {   
            r= new DataInputStream(in);
            s=r.readUTF();
            tmp=this.securechannel.decrypt(s.getBytes());
            decrypted=new String(tmp);
            return decrypted;
            
        } catch (AESException e) {
            throw new OperationException("AESException::"+e.getMessage());
        }catch(IOException e)
        {
            throw new OperationException("IOException::"+e.getMessage());
        }
    }
     
    public String getUserInfo()
    {
        return this.userinfo;
    }
    
    public String getUserName()
    {
        return this.user;
    }
            
    
    public String getLastMessageAfterRSAServerFailure()
    {
        return this.lastReceivedMessageAfterRSAServerFailure;
    }
}
