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
    
    private ClientSecure client = null;
    private OutputStream out = null;
    private InputStream in = null;
    
    /*Secure parameters*/
    private RSAServer rsaserver=null;
    private RSAClient rsaclient=null;
    private AES securechannel=null;
    private String user=null;
    private String userinfo=null;
    private String lastReceivedMessageAfterRSAServerFailure=null;
    /*
     * Konstruktor for client side
     * secure connection etablished immediately
     */
    public OperationSecure(Client c,String clientname,String userinfo,String servername,
            String serverkeydirectory,String clientkeydirectory) throws OperationException, RSAAuthenticationException
    {   
        try
        {
            client = new ClientSecure(c);
            in = client.getInputStream();
            out = client.getOutputStream();
            this.user=clientname;
            this.userinfo=userinfo;
            
           
            RSAClient rsaclient=new RSAClient(clientname,userinfo,servername,
                    clientkeydirectory,serverkeydirectory);
            rsaclient.startClientAuthenticationProcedure(in, out);
            this.securechannel=rsaclient.getAES();
            
            client.setSecuredChannel(securechannel, user, userinfo);
            
        }catch(ClientException e)
        {
            throw new OperationException("ClientException::"+e.getMessage());
        }
        
    }
    /*
     * Konstruktor for server side
     * secure connection etablished immediately
     */
    public OperationSecure(Client c,String servername,
            String serverkeydirectory,String clientkeydirectory) throws OperationException,RSAAuthenticationException
    {   
        RSAServer server=null;
        try
        {
            client = new ClientSecure(c);
            in = client.getInputStream();
            out = client.getOutputStream();
           
           server = new RSAServer(servername,clientkeydirectory,
                    serverkeydirectory);
           server.startServerAuthenticationProcedure(in, out);
           this.user=server.getUsername();
           this.userinfo=server.getUserInfo();
           this.securechannel=server.getAES();
            
            client.setSecuredChannel(securechannel, user, userinfo);
        }catch(NullPointerException e)
        {
            throw new OperationException("NullPointerException::"+e.getMessage());
        }catch(ClientException e)
        {
            throw new OperationException("ClientException::"+e.getMessage());
        }catch(RSAAuthenticationException e)
        {
            this.lastReceivedMessageAfterRSAServerFailure=server.getLastMessage();
            throw e;
        }
        
    }
    /*
     * Konstruktor for server side
     * Server initialization without establishing a secured channel
     */
    public OperationSecure(String servername,
            String serverkeydirectory,
            String clientkeydirectory) 
            throws OperationException,RSAAuthenticationException
    {   
       
        try
        {
            
           
           this.rsaserver = new RSAServer(servername,clientkeydirectory,
                   serverkeydirectory);
           this.user=null;
           this.userinfo=null;
           this.securechannel=null;
            
            this.client=null;
        }catch(NullPointerException e)
        {
            throw new OperationException("NullPointerException::"+e.getMessage());
        }catch(Exception e)
        {
            throw new OperationException("Exception::"+e.getMessage());
        }
        
    }
    
    /**
      * Konstruktor on server or client side
      * connection already established
      * @param op
      * @throws ClientException 
      */
     public OperationSecure(OperationSecure op)
     {
         this.client=op.client;
         this.in=op.in;
         this.lastReceivedMessageAfterRSAServerFailure=op.lastReceivedMessageAfterRSAServerFailure;
         this.out=op.out;
         this.securechannel=this.securechannel;
         this.user=op.user;
         this.userinfo=op.userinfo;
         this.rsaclient=op.rsaclient;
         this.rsaserver=op.rsaserver;

     }
     
     /**
      * Konstruktor on server or client side
      * connection already established
      * @param client
      * @throws ClientException 
      */
     public OperationSecure(Client client) throws ClientException
     {
         try{
             if(client.getClientType().contentEquals("client"))
                 throw new Exception("Wrong client type.");
             this.client=(ClientSecure)client;
             this.in=this.client.getInputStream();
             this.lastReceivedMessageAfterRSAServerFailure=null;
             this.out=this.client.getOutputStream();
             this.securechannel=this.client.getAES();
             this.user=this.client.getUser();
             this.userinfo=this.client.getUserinfo();
             this.rsaclient=null;
             this.rsaserver=null;
            
         }catch(NullPointerException ex)
         {
             throw new ClientException("Wrong client type:"+ex.getMessage());
             
         }catch(Exception ex)
         {
             throw new ClientException("Exception:"+ex.getMessage());
         }

     }
    
     public void acceptConnection(Client client) throws OperationException,RSAAuthenticationException
     {
          try
        {
            
            if(client.getClientType().contentEquals("client"))
                 throw new OperationException("Wrong client type.");
            if(this.rsaserver==null)
                 throw new OperationException("Server not initialized.");
            ClientSecure clientsecure=(ClientSecure)client;
            in = clientsecure.getInputStream();
            out = clientsecure.getOutputStream();
           
           
           this.rsaserver.startServerAuthenticationProcedure(in, out);
           this.user=rsaserver.getUsername();
           this.userinfo=rsaserver.getUserInfo();
           this.securechannel=rsaserver.getAES();
            
           clientsecure.setSecuredChannel(securechannel, user, userinfo);
            
        }catch(NullPointerException e)
        {
            throw new OperationException("NullPointerException::"+e.getMessage());
        }catch(ClientException e)
        {
            throw new OperationException("ClientException::"+e.getMessage());
        }catch(RSAAuthenticationException e)
        {
            this.lastReceivedMessageAfterRSAServerFailure=rsaserver.getLastMessage();
            throw e;
        }
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
