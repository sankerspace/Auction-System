/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import java.io.IOException;
import java.net.InetAddress;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.UnknownHostException;
import MyLogger.Log;

/**
 *
 * @author sanker
 */
public class ClientUDP {
    
   private DatagramSocket udpsocket=null;
  // private DatagramPacket receivepacket=null;
  // private DatagramPacket sendpacket=null;
   private InetAddress    destinationURL=null;
   private int            destinationPORT=0; 
    
    /*
     * The client uses a constructor that does not require a port number. 
     * This constructor just binds the DatagramSocket to any available local port.      
     */
    public ClientUDP()throws ClientUDPException
    {
        try {
            udpsocket=new DatagramSocket();
        } catch (SocketException e) {
            throw new  ClientUDPException("SocketException::",e);
        }
    }
    
    public ClientUDP(String url,int port)throws ClientUDPException
    {
        try {
          
            udpsocket=new DatagramSocket();
            destinationURL = InetAddress.getByName(url);
            destinationPORT=port;
        } catch (SocketException e) {
            throw new  ClientUDPException("SocketException::",e);
        }catch (UnknownHostException e) {
            throw new  ClientUDPException("UnknownHostException::",e);
        }
    }
    
    public ClientUDP(InetAddress urladdress,int port)throws ClientUDPException
    {
        try {
          
            udpsocket=new DatagramSocket();
            destinationURL = urladdress;
            destinationPORT=port;
        } catch (SocketException e) {
            throw new  ClientUDPException("SocketException::",e);
        }
    }
    
    
    public ClientUDP(DatagramSocket socket) 
    {
           
      udpsocket = socket; 
        
    }
    
    void sendPacket(DatagramPacket packet)throws ClientUDPException
    {
        try {
            this.udpsocket.send(packet);
        } catch (IOException e) {
            throw new  ClientUDPException("IOException::",e);
        }
    }
    
    void receivePacket(DatagramPacket packet)throws ClientUDPException
    {
        try {
            this.udpsocket.receive(packet);
        } catch (IOException e) {
            throw new  ClientUDPException("IOException::",e);
        }
    }
    
    public void setDestinationHost(String URL,int port)throws ClientUDPException
    {
        try {
            destinationURL = InetAddress.getByName(URL);
        } catch (UnknownHostException e) {
            throw new  ClientUDPException("UnknownHostException::",e);
        }
        destinationPORT=port;
    }
    
    
    public InetAddress getDestinationHostAddress()
    {
        return this.destinationURL;
    }
    
    public String getDestinationHostUrl()
    {
        return this.destinationURL.getHostName();
    }
    
    public int getDestinationHostPort()
    {
        return this.destinationPORT;
    }
    
    
    
     public boolean connectionEstablished()
     {
         return udpsocket.isConnected();
     }
    
     public void closeSocket()
     {
         if(!udpsocket.isClosed())
            udpsocket.close();
     
     }
    
}
