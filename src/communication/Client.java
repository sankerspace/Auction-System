/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import MyLogger.Log;
/**
 *
 * @author sanker
 */
public class Client {
    private Socket clientsocket = null;
    protected String type="client";

    /*
     * Konstruktor
     */
    public Client(String url,int port) throws ClientException
    {
        try{
            
            clientsocket = new Socket(url,port);   
        
        }catch(UnknownHostException e)
        {
            throw new  ClientException("UnknownHostException::",e);
            
        }catch(IOException e)
        {
            throw new  ClientException("IOException::",e);
        }
    
    }
    
    public Client(Socket sock) 
    {
           
       clientsocket = sock; 
         
    }
    
    public Client(Client client) 
    {
           
       clientsocket = client.clientsocket; 
         
    }
    
    public String getClientType()
    {
        return this.type;
    }
    
    
    /*
     * Methode
     */
    public  OutputStream getOutputStream() throws ClientException
    {
        try{
            
            return clientsocket.getOutputStream();
            
        }catch(IOException e)
        {
            throw new  ClientException("IOException::",e);
        }
    }
    
    /*
     * Methode
     */
    public  InputStream getInputStream() throws ClientException
    {
        try{
            
            return clientsocket.getInputStream();
            
        }catch(IOException e)
        {
            throw new  ClientException("IOException::",e);
        }
    }
    
    
 
    public boolean connectionEstablished()
    {
        return clientsocket.isConnected();
    }
  
    
    public String getDestinationHost()
    {
       return clientsocket.getInetAddress().getHostAddress();
    }
    
    public int getDestinationPort()
    {
        return clientsocket.getPort();
    }
    
    
    public void closeSocket()throws ClientException
    {
        try{
            if(!clientsocket.isClosed())
                clientsocket.close();
            
        }catch(IOException e)
        {
            throw new  ClientException("IOException::",e);
        }
       
    
    }
}
