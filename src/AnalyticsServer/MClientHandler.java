/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalyticsServer;

import Event.AnalyticsControllEvent;
import Event.Event;
import RMI.ManagementClientCallBackInterface;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

/**
 *
 * @author sanker
 */
public class MClientHandler implements Runnable{
    
    private long ClientID;
    private Filter filter=null;
    private ManagementClientCallBackInterface mccbi=null;
    private LinkedBlockingQueue<Event> lbq=null;
    private LinkedBlockingQueue<Task> toamsincomechannel=null;
    private Logger logger=Logger.getLogger(AnalyticsServer.class.getSimpleName()
                    +"."+MClientHandler.class.getSimpleName());
    
    public MClientHandler(long ClientID,
            Filter filter,
            ManagementClientCallBackInterface mccbi,
            LinkedBlockingQueue<Event> lbq,
            LinkedBlockingQueue<Task> toamsincomechannel)
    {
        this.ClientID=ClientID;
        this.filter=filter;
        this.mccbi=mccbi;
        this.lbq=lbq;
        this.toamsincomechannel=toamsincomechannel;

    }
    
    public void run()
    {
        
        Event event=null;
        logger.debug("A new ManagementClient handler is running with ClientID:"
                +ClientID
                +" and FilterID "
                +filter.getFilterID()
                );
        while(!Thread.currentThread().isInterrupted())
        {
            try {
                
              event=lbq.take();
              logger.debug("MClientHandler:Event message type "+event.getType()+" taken.");
              if(event.getType().contains(AnalyticsControllEvent.AnalyticsControllEventType.CLOSE_FILTER.name()))
              {
                  AnalyticsControllEvent asce=(AnalyticsControllEvent)event;
                  if(asce.getIDNumber()==this.ClientID)
                  {
                    logger.debug("Event message leads to closing this Thread. ");
                    Thread.currentThread().interrupt();
                    break;
                  }
                  
              }else if(filter.checkMessage(event.getType()))
              {
                 
                 mccbi.processEvent(event.toString()); 
                 logger.debug("Send Event: "+event.getType()+" to ManagementClient with Client ID "
                         +this.ClientID+" .");
             
              }
                                           
              
            } catch (InterruptedException ex) {
              //message queue was close from outside
              logger.error("MClientHandler:InterruptedException:"+ex.getMessage());
              Thread.currentThread().interrupt();
            }catch (RemoteException ex) {
               //managementclient is not available anymore
               Task.REMOTEERROR error=new Task.REMOTEERROR(this.ClientID,
                       this.filter,
                       this.mccbi);
              this.toamsincomechannel.offer(new Task(error));
              logger.error("MClientHandler:RemoteException:"+ex.getMessage());
              Thread.currentThread().interrupt();
            } catch (Exception ex) {
              //a unknown exception
               Task.REMOTEERROR error=new Task.REMOTEERROR(this.ClientID,
                       this.filter,
                       this.mccbi);
              this.toamsincomechannel.offer(new Task(error));
              logger.error("MClientHandler:Exception:"+ex.getMessage());
              Thread.currentThread().interrupt();
            }             
        
        }
     this.close();
    
    }
    
    
    public void close()
    {
        
        //close ressources if avaible
        logger.debug("MClientHandler with filter ID "+this.filter.getFilterID()
                +" is closing.");
        this.filter=null;
        this.lbq=null;
        this.mccbi=null;
        this.toamsincomechannel=null;
    }
            
}
