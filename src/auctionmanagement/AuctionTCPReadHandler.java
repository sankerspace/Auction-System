/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package auctionmanagement;

import MyLogger.Log;
import communication.Client;
import communication.ClientException;
import communication.OperationTCP;
import communication.OperationException;
import communication.OperationSecure;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sanker
 */
public class AuctionTCPReadHandler implements Runnable{
    
    private LinkedBlockingQueue<CommandTask> queue=null;
    private Client client=null;
    private Log logger=null;
    private boolean secureconnectionestablished;
    
    public AuctionTCPReadHandler(LinkedBlockingQueue<CommandTask> queue,Client client,Log logger)
    {
        this.client=client;
        this.queue=queue;
        this.logger=logger;
        secureconnectionestablished=false;
        logger.output("AuctionTCPReadHandler created....", 2);
    }
    
    
    
    
        private CommandTask  ExtractInfo(Request r)
        {   
            String command=r.getCommandName();
            CommandTask c=null;
            if(command==null)
            {
                
                logger.output("AuctionTCPReadHandler:ExtractInfo:Command points to null!");
            }
            
            
            if(command.contains("list"))
            {   
                CommandTask.List l = new CommandTask.List(r.getClient());
                c= new CommandTask(l);
                
            }else if(command.contains("login"))
            {
                CommandTask.Login l = new CommandTask.Login(r.getClient(),r.getUserName(),r.getUDPPort());
                c= new CommandTask(l);
                
            }else if(command.contains("create"))
            {
               CommandTask.Create l = new CommandTask.Create(r.getClient(),
                       r.getUserName(),r.getParameter().createTime,r.getParameter().createDesc);
               c= new CommandTask(l);
            
            }else if(command.contains("logout"))
            {
                CommandTask.Logout l = new CommandTask.Logout(r.getClient(),r.getUserName());
                c= new CommandTask(l);
            
            }else if(command.contains("bid"))
            {
                
               CommandTask.Bid l = new CommandTask.Bid(r.getClient(),
                       r.getUserName(),r.getParameter().bidId,r.getParameter().bidValue);
               c= new CommandTask(l);
            
            }
            /*
            else if(command.contains("end"))
            {
                CommandTask.End l = new CommandTask.End(r.getClient());
                c= new CommandTask(l);
            }
            */
            return c;
        }
                
        
        public void run()
        {
            logger.output("ServerSocketHandleThread started....", 2);
            String message=null;
            //OperationTCP op=null;
            OperationSecure opSec=null;
            CommandTask com=null;
            try {
                //op=new OperationTCP(this.client);
                
                while(!Thread.currentThread().isInterrupted())
                {   
                  if(this.secureconnectionestablished)
                  {
                      //message=op.readString();
                      if(message!=null)
                      {
                          if(message.contains("end"))break;
                          com = this.ExtractInfo(new Request(this.client,message));               
                          this.queue.offer(com); 
                           logger.output("ServerSocketHandleThread received"
                                   +" message and forwarded a CommandTask Object to AMSThread:\n"
                                   +"CommandTask[ServerSocketHandlThread]:"+"\n"
                                   +com.toString(), 3);
                      }else
                          logger.output("ServerSocketHandleThread received a null message.");
                  }else
                  {
                    //opSec= new OperationSecure();
                  }
        
       
       

                }
 
           } /*catch (OperationException ex) {
                
                    CommandTask.End l = new CommandTask.End(this.client);
                    CommandTask c= new CommandTask(l);
                    this.queue.offer(c); 
                    
                   this.logger.output("ServerSocketHandleThread:OperationException:"+ex.getMessage(),2);
                   Thread.currentThread().interrupt();
             }*/catch (Exception ex) {
                
                   
                   this.logger.output("ServerSocketHandleThread:Exception:"+ex.getMessage());
                   Thread.currentThread().interrupt();
             }finally{
                try {
                    this.logger.output("ServerSocketHandleThread:finally:Close Socket",2);
                    this.client.closeSocket();
                } catch (ClientException ex) {
                    this.logger.output("ServerSocketHandleThread:finally:ClientException:"+ex.getMessage());
                }
            }
            logger.output("ServerSocketHandleThread end....", 2);
           
            
        }
    
}
