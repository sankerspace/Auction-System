/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package auctionmanagement;

import communication.Operation;
import communication.OperationException;
import java.lang.Thread;
import java.io.PrintWriter;
import java.util.concurrent.LinkedBlockingQueue;
import MyLogger.Log;

/**
 *
 * @author sanker
 */
public class AuctionTCPMessageServer implements Runnable{
    
     private  LinkedBlockingQueue<Answer> outgoinganswers=null;
     private Log errorlog=null;
     
     public AuctionTCPMessageServer(LinkedBlockingQueue<Answer> outgoinganswers, Log error)
     {
         this.outgoinganswers= outgoinganswers ;
         errorlog=error;
     }
    
    public void run()
    {
        errorlog.output("AuctionTCPMessageServerThread started...", 2);
        Operation op=null;
        Answer r=null;
        while(!Thread.currentThread().isInterrupted())
        {
            try {
                
                
                r=outgoinganswers.take();
                op = new Operation(r.getClient());
                op.writeString(r.getMessage());
                
               errorlog.output("AuctionTCPMessageServerThread send a message:\n"+r.getMessage(), 3);  
            } catch (InterruptedException ex) {
               Thread.currentThread().interrupt();
            }catch(OperationException e)
            {
                errorlog.output("AuctionTCPMessageServerThread:"+e.getMessage());
            }           
        }
        errorlog.output("AuctionTCPMessageServerThread end...", 2);
        
    }
            
}
