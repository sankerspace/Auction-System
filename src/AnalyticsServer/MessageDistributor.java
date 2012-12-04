/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalyticsServer;

import Event.AnalyticsControllEvent;
import Event.Event;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

/**
 *
 * @author Marko
 */
public class MessageDistributor implements Runnable{
    private static long counter=0;
    private LinkedBlockingQueue<Event> incomingchannel=null;   
    private ConcurrentHashMap<Long,LinkedBlockingQueue<Event>> outcomingdistributor=null;
    private static Logger logger=Logger.getLogger(AnalyticsServer.class.getSimpleName()
                    +"."+MessageDistributor.class.getSimpleName());
    public MessageDistributor(LinkedBlockingQueue<Event> incomingchannel)
    {   

        this.incomingchannel=incomingchannel;
        outcomingdistributor=new ConcurrentHashMap<Long,LinkedBlockingQueue<Event>>();
    }
    
    public void run()
    {
      
        Event event=null;
        while(!Thread.currentThread().isInterrupted())
        {
            try {
                
               event=incomingchannel.take();
               if(event.getType().contains(AnalyticsControllEvent.AnalyticsControllEventType.CLOSE_DISTRIBUTOR.name()))
               {
                   Thread.currentThread().interrupt();
                   break;
               }
               if(!this.outcomingdistributor.isEmpty())
               {
                   
                   Iterator<Map.Entry<Long,LinkedBlockingQueue<Event>>> iter = outcomingdistributor.entrySet().iterator();
                   while(iter.hasNext())
                   {
                        Map.Entry<Long,LinkedBlockingQueue<Event>> entry = iter.next(); 
                        entry.getValue().offer(event);
                   }
                   logger.debug("Distributed event: "+event.getType()+" to all MClientHandler.");
               }
                                           
              
            } catch (InterruptedException ex) {
              logger.error("InterruptedException:"+ex.getMessage());
              Thread.currentThread().interrupt();
            }catch (Exception ex) {
              logger.error("Exception:"+ex.getMessage());
              Thread.currentThread().interrupt();
            }             
        
        }
     this.close();
    }
   
    public  Long registerOutcomingMember(long channelID,LinkedBlockingQueue<Event> lbq)throws MessageDistributorException
    {
        //ClientID=FILTERID=ChannelID
        Long id=new Long(channelID);
        this.outcomingdistributor.put(id, lbq);
        logger.debug("Registered a new channel in the distributor List with Channel ID"+channelID);
        if(!this.outcomingdistributor.containsKey(id))
        {
            logger.error("Hashmap registration unsuccesfull.");
            throw new MessageDistributorException("Hashmap registration unsuccesfull.");
        }
        logger.trace("Added new queue to outcomingdistributor.");
        return id;
        
        
    }
    
    public  void deregisterOutcomingMember(Long id)
    {
        if(outcomingdistributor.containsKey(id))
        {
            LinkedBlockingQueue<Event> out=this.outcomingdistributor.remove(id);
            out.offer(new AnalyticsControllEvent(id.longValue(),
                    AnalyticsControllEvent.AnalyticsControllEventType.CLOSE_FILTER));
            logger.debug("Removed queue from outcomingdistributor "
                    +"and send close message to Handler.");
        }
    }
    
   
    
    private Long getID()
    {
        return new Long(counter++);
    }
    
    
    public void close()
    {
        logger.debug("Closing MessageDistributor and all ressources.");
        if(!this.outcomingdistributor.isEmpty())
        {
            Event event=null;    
           Iterator<Map.Entry<Long,LinkedBlockingQueue<Event>>> iter = outcomingdistributor.entrySet().iterator();
           while(iter.hasNext())
           {
                Map.Entry<Long,LinkedBlockingQueue<Event>> entry = iter.next(); 
                event=new AnalyticsControllEvent(entry.getKey().longValue(),
                    AnalyticsControllEvent.AnalyticsControllEventType.CLOSE_FILTER);
                entry.getValue().offer(event);
           }
           logger.debug("Send kill message to al MCClientHandler");
        }
       
        
         this.outcomingdistributor.clear();
         this.incomingchannel=null;
         this.outcomingdistributor=null;
         
          logger.trace("Closing  MessageDistributor finished.");
         
    }
}