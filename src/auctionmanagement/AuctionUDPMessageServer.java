/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package auctionmanagement;

import communication.OperationUDP;
import communication.OperationUDPException;
import java.util.concurrent.LinkedBlockingQueue;
import MyLogger.Log;

/**
 *
 * @author sanker
 */
public class AuctionUDPMessageServer  implements Runnable {
    
    private  LinkedBlockingQueue<Notification> notifications=null;
    private Log errorlog=null;
     
    public AuctionUDPMessageServer(LinkedBlockingQueue<Notification> notifications,Log error){
         
         this.notifications=notifications;
         errorlog=error;
         
         errorlog.output("AuctionUDPMessageServerThread created..,", 2);  
        
     }
    
    
    public void run()
    {
        Notification n=null;
        OperationUDP op=null;
        errorlog.output("AuctionUDPMessageServerThread started..,", 2);
        while(!Thread.currentThread().isInterrupted())
        {
            try {
                
                n = notifications.take();
                op=new OperationUDP(n.getClient());
                op.writeString(n.getMessage());
                
                errorlog.output("AuctionUDPMessageServerThread send a message:\n"+n.getMessage(), 3);  
            } catch (InterruptedException ex) {
              Thread.currentThread().interrupt();
            }catch(OperationUDPException e)
            {
                errorlog.output("AuctionUDPMessageServerThread:"+e.getMessage());
            }
        }
         errorlog.output("AuctionUDPMessageServerThread ended..,", 2);
       
         
    }
}
