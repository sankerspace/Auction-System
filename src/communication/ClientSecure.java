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
    
    ClientSecure(String url,int port) throws ClientException
    {
        super(url,port);
        this.cypher=null;
        this.user=null;
        this.userinfo=null; 
        this.type="clientsecure";
    }
    
    
    public ClientSecure(Socket sock) 
    {
           
       super(sock); 
       this.cypher=null;
       this.user=null;
       this.userinfo=null;  
       this.type="clientsecure";
    }
    
    public ClientSecure(Client client) 
    {
         
        super(client);
        this.cypher=null;
        this.user=null;
        this.userinfo=null;  
        this.type="clientsecure";
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
    public boolean isInSecuredMode()
    {
        return this.mode;
    }
    
    public void setSecuredChannel(AES cypher,String user,String userinfo)
    {
        this.mode=true;
        this.cypher=cypher;
        this.user=user;
        this.userinfo=userinfo;
    }
    
    public void setUnsecuredChannel()
    {
        this.mode=false;
        this.cypher=null;
        this.user=null;
        this.userinfo=null;
    
    }
}
