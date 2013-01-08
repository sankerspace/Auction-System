/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import java.net.Socket;
import security.AES;

/**
 *
 * @author Marko
 */
public class ClientSecure extends Client {
    
    private AES cypher=null;
    private String user=null;
    private String userinfo=null;
    private boolean mode=false;
    private String ClientKeyDirectory=null;
    ClientSecure(String url,int port) throws ClientException
    {
        super(url,port);
        this.cypher=null;
        this.user=null;
        this.userinfo=null; 
        this.mode=false;
        this.type="clientsecure";
        this.ClientKeyDirectory=null;
    }
    
    
    public ClientSecure(Socket sock) 
    {
           
       super(sock); 
       this.cypher=null;
       this.user=null;
       this.userinfo=null;  
       this.mode=false;
       this.type="clientsecure";
        this.ClientKeyDirectory=null;
    }
    
    public ClientSecure(Client client) 
    {
         
        super(client);
        if(!client.getClientType().contains("clientsecure"))
        {
        this.cypher=null;
        this.user=null;
        this.userinfo=null;
        this.mode=false;
        }else
        {
            ClientSecure cl = (ClientSecure)client;
            this.cypher=cl.cypher;
            this.mode=cl.mode;
            this.user=cl.user;
            this.userinfo=cl.userinfo;
             this.ClientKeyDirectory=cl.ClientKeyDirectory;
        }
        this.type="clientsecure";
        
    }
    protected void setClientKeyDirectory(String ClientKeyDirectory)
    {
        this.ClientKeyDirectory=ClientKeyDirectory;
    }
    protected AES getAES()
    {
        return this.cypher;
    }
   
    protected String getUser()
    {
        return this.user;
    }
    protected String getUserinfo()
    {
        return this.userinfo;
    }
    
    protected String getClientKeyDirectory()
    {
        return this.ClientKeyDirectory;
    }
    public boolean isInSecuredMode()
    {
        return this.mode;
    }
    
    public void setSecuredChannel(AES cypher,String user,String userinfo,String ClientKeyDirectory)
    {
        this.mode=true;
        this.cypher=cypher;
        this.user=user;
        this.userinfo=userinfo;
        this.ClientKeyDirectory=ClientKeyDirectory;
    }
    
    public void setUnsecuredChannel()
    {
        this.mode=false;
        this.cypher=null;
        this.user=null;
        this.userinfo=null;
    
    }
}
