/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package auctionmanagement;

import communication.OperationTCP;
import communication.OperationException;
import java.lang.Thread;
import java.io.PrintWriter;
import java.util.concurrent.LinkedBlockingQueue;
import MyLogger.Log;
import communication.Client;
import communication.ClientException;
import communication.ClientSecure;
import communication.Operation;
import communication.OperationSecure;

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
        Client client=null;
        Answer r=null;
        boolean b;
        while(!Thread.currentThread().isInterrupted())
        {
            try {
                
                
                r=outgoinganswers.take();
                client=r.getClient();
                b=client.getClientType().contains("clientsecure");
                if(b)
                {   
                    ClientSecure clsec=(ClientSecure)client;
                    if(clsec.isInSecuredMode())
                        op=new OperationSecure(clsec);
                    else
                        op = new OperationTCP(client);
                    
                }else{
                    op = new OperationTCP(client);
                }
                
                op.writeString(r.getMessage());
                
               errorlog.output("AuctionTCPMessageServerThread send a message:\n"+r.getMessage(), 3);  
            } catch (ClientException ex) {
               errorlog.output("AuctionTCPMessageServerThread:ClientException"+ex.getMessage());
            }catch (InterruptedException ex) {
               Thread.currentThread().interrupt();
            } catch(OperationException e)
            {
                errorlog.output("AuctionTCPMessageServerThread:OperationException"+e.getMessage());
            }  catch(Exception e)
            {
                errorlog.output("AuctionTCPMessageServerThread:Exception"+e.getMessage());
            }           
        }
        errorlog.output("AuctionTCPMessageServerThread end...", 2);
        
    }
            
}
