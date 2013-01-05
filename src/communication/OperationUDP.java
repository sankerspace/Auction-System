/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;
import java.net.DatagramSocket;
import java.net.DatagramPacket;

/**
 *
 * @author sanker
 */
public class OperationUDP implements Operation{
    
    int standardBufferSize = 256;
    
    private ClientUDP client=null;
   
    
    public OperationUDP(ClientUDP client)
    {
        this.client=client;
        
        
    }
    
    
    public OperationUDP(ClientUDP client,int bufSize)
    {
        this.client=client;
        standardBufferSize=bufSize;
    }
    
    
    public void writeString(String s)throws OperationException
    {
        byte[] buf=s.getBytes();
        DatagramPacket packet = new DatagramPacket(buf,buf.length,
                client.getDestinationHostAddress(),client.getDestinationHostPort());
        try {
            client.sendPacket(packet);
        } catch (ClientUDPException e) {
           throw new  OperationException("ClientUDPException::",e);
        }
     
    }
    /*
     * @pre buffer size of the buffer parameter in Datagram constructor
     *      has a maximum of 256 bytes 
     * @post The address of source host who send the message 
     *       will be stored in the client
     */
    //standard buffer can read a maximum of 256 bytes
    public String readString()throws OperationException
    {
        byte[] buf=new byte[standardBufferSize];
        DatagramPacket packet = new DatagramPacket(buf,buf.length);
        try {
            client.receivePacket(packet);
            client.setDestinationHost(packet.getAddress().getHostAddress()
                    , packet.getPort());
            return (new String(buf,0,packet.getLength()));
        } catch (ClientUDPException e) {
           throw new  OperationException("ClientUDPException::",e);
        }
     
    }
    
    
}
