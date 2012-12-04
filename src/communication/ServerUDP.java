/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;


import MyLogger.Log;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;



/**
 *
 * @author sanker
 */
public class ServerUDP implements Runnable{
    
    private DatagramSocket udpserversocket=null;
    private ClientUDP serverclient=null;
    private OperationUDP op=null;
    private ServerUDP.Handler handler=null;
    boolean stopServer;
    private Log logger=null;
   
    
    public ServerUDP(int port,ServerUDP.Handler myhandler,Log logger) throws ServerUDPException
    {
        try {
            udpserversocket=new DatagramSocket(port);
            serverclient = new ClientUDP(udpserversocket);
            op = new OperationUDP(serverclient);
            this.handler=myhandler;
            this.handler.setOperationUDP(op);
       
            stopServer=false;
            this.logger=logger;
            logger.output("UDPServer created....", 2);
        } catch (SocketException e) {
            throw new  ServerUDPException("SocketException::",e);
        }
    }
    
    private void shutdown () 
    {
        
         if(!udpserversocket.isClosed())
             udpserversocket.close();
        stopServer=true;
        logger.output("UDPServer closed....", 2);
    }
    
    public void run()
    {
        boolean t=false;
        logger.output("UDPServer started....", 2);
        while(!(t=Thread.currentThread().isInterrupted()) && (!stopServer))
         {
                
            try{
                if(this.handler==null)
                    throw (new ServerUDPException("Handler not initialized."));
             
               this.handler.handle(op.readString());

            }catch(OperationUDPException e)
            {
                logger.output("UDPServer error:"+e.getMessage());
                Thread.currentThread().interrupt();
            }catch(ServerUDPException e)
            {
                logger.output("UDPServer error:"+e.getMessage());
                Thread.currentThread().interrupt();
            }
         
         }
        this.shutdown();
    }
    
    
    
    
    public static class Handler 
    {
        
        OperationUDP handlerop=null;
        public Handler()
        {
            
        }
       protected void setOperationUDP(OperationUDP op)
       {
           this.handlerop=op;
       }
        
       
       
        public void handle(String msg)
        {
        
        }
        
       
       

    }
}
