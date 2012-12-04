/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalyticsServer;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import Event.AnalyticsControllEvent;
import Event.Event;
import RMI.ManagementClientCallBackInterface;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import java.util.concurrent.LinkedBlockingQueue;





/**
 *
 * @author sanker
 */
public class AnalyticsManagementSystem implements Runnable {
    
    private ExecutorService pool=null;
     private static Logger logger=Logger.getLogger(AnalyticsServer.class.getSimpleName()+
                "."+AnalyticsManagementSystem.class.getSimpleName());
    /*maps*/
    private ConcurrentHashMap<Long,Filter> mclient_map=null;
    private ConcurrentHashMap<Long,Filter> subscription_map=null;
    /*message objects*/
    private LinkedBlockingQueue<Task> toamsincomechannel=null;//from rmi or an Client Handler
    private LinkedBlockingQueue<Task.RESULT> amstormisoutcomechannel=null;
    private LinkedBlockingQueue<Event> distributorincomechannel=null;
     private LinkedBlockingQueue<Event> statisticsincomechannel=null;
    /*Handler*/
    private MessageDistributor MD =null;
    private StatisticHandler   SH =null;
    
    public AnalyticsManagementSystem(ExecutorService pool,
            LinkedBlockingQueue<Task> toamsincomechannel,
            LinkedBlockingQueue<Task.RESULT> amstormisoutcomechannel,
            LinkedBlockingQueue<Event> distributorincomechannel,
            LinkedBlockingQueue<Event> statisticsincomechannel)
    {
        
       
        
        
        this.pool=pool;
        //ManagementClient ID and filter
        this.mclient_map=new ConcurrentHashMap<Long,Filter>();
        this.subscription_map=new ConcurrentHashMap<Long,Filter>();
        this.toamsincomechannel=toamsincomechannel;
        this.amstormisoutcomechannel=amstormisoutcomechannel;
        this.distributorincomechannel=distributorincomechannel;
        this.statisticsincomechannel=statisticsincomechannel;
        
        MD=new MessageDistributor(this.distributorincomechannel);
        SH=new StatisticHandler(this.pool,this.statisticsincomechannel,this.distributorincomechannel);
        
    }
    
    public void run()
    {
        //start Distributor
        pool.execute(MD);
        logger.debug("Message Distributor started.");
        //start StatisticcHandler
        pool.execute(SH);
        logger.debug("StatisticHandler started.");
       
        
        Task task;
        logger.trace("AnalyticsManagemenSystem Handler is started...");
        while(!Thread.currentThread().isInterrupted())
        {
            Task.RESULT result=null;//used in subscriber or unsubscriber part
            long subscriptionID=0;//used in subscriber or unsubscriber part
            
            try {
               logger.trace("Wait for incoming Task from RMI.");
               task=this.toamsincomechannel.take();
               //get a remoterror task from a client handler
               if(task.isClose())
               {
                   logger.trace("Incoming Task is a CLOSE Request.");
                   Thread.currentThread().interrupt();
                   //get out from while loop
  
               }else if(task.isRemoteError())
               {
                   logger.trace("Incoming Task is a RemoteErrorType.");
                   Task.REMOTEERROR error = task.getRemoteError();
                   
                   
                   this.handleRemoteException(error.ClientID);
                   
                
               }
               else if(task.isSubscriber())
               {
                   /************SUBSCRIBER****************/
                   logger.trace("Incoming Task is a Subscriber Type.");
                   Long clientId=null;
                   Task.SUBSCRIBER subscriber=task.getSubscriber();
                   subscriptionID=subscriber.subscribeID;
                   try{ 
                       
                        //cannot subscribe with already defined ID
                        if(subscription_map.containsKey(subscriptionID))
                               throw new Exception("Subscription ID "
                                       +subscriber.subscribeID
                                       +"already defined.");
                       
                       logger.debug("Taken Subscriber Task from RMI.");
                       Filter filter=null;
                       
                       //get ClientID
                       clientId=subscriber.mccbi.getManagementClientID();
                       
                         
                       //find handler for ClientID to handle this subscription or create on
                       if(this.mclient_map.containsKey(clientId))
                       {
                           
                           //EXISTING HANDLER
                           //a handler already exist for this managementclient
                           logger.debug("Found Handler with ClientID "
                                   +clientId.longValue()
                                   +" for handling subscription ID "
                                   +subscriber.subscribeID);
                           

                           filter=this.mclient_map.get(clientId);
                           filter.subscribeRegex(subscriber.subscribeID,subscriber.regex);
                           subscription_map.put(subscriber.subscribeID, filter);
                           

                       }else
                       {    //CREATE NEW HANDLER
                           //is this subscription from a new ManagementClient
                           //a new handler must be created for this managementclient
                           //filter ID and Client ID are the same
                           //because only one filter exist for only one managemenclient
                           logger.trace("Subscribtion is from unknown Client,create ClientHandler.");
                           filter=new Filter();
                           clientId=filter.getFilterID();
                           logger.trace("ClientID "+clientId+" for the new ManagementClient.");
                           //the managementclient must get his new ID for easier identification
                           subscriber.mccbi.setManagementClientID(clientId);
                           logger.trace("Add Subscriber Regex '"+subscriber.regex
                                   +"' into the new filter ID "+filter.getFilterID());
                           filter.subscribeRegex(subscriber.subscribeID,subscriber.regex);
                           subscription_map.put(subscriber.subscribeID, filter);
                           logger.trace("Add Client ID "+clientId+" and Filter ID "
                                   +"into mclient_map");
                           
                           mclient_map.put(clientId, filter);
                           //create incomingChannel for new ManagementClient Handler
                           LinkedBlockingQueue<Event> lbq=new LinkedBlockingQueue<Event>();
                           //the new incoming channel must be registered in the distributor
                           //all new events will be put on this new channel
                           logger.trace("Add new Channel for the new ClientHandler inside Distributor.");
                           this.MD.registerOutcomingMember(clientId,lbq);
                           logger.trace("Start the new MClienHandler.");
                           MClientHandler handler=new MClientHandler(clientId
                                   ,filter
                                   ,subscriber.mccbi
                                   ,lbq
                                   ,toamsincomechannel);
                           //execute handler
                           pool.execute(handler);
                       }
                      logger.debug("The result from the Subscriber task is true");
                      result = new Task.RESULT(true,subscriptionID);
                      /***********END SUBSCRIBER****************/  
                   }catch(RemoteException ex)
                   {
                       this.handleRemoteException(clientId);
                       logger.error("AnalyticsManagementSystem:RemoteException:"+ex.getMessage());
                       logger.trace("The result from the Subscriber task is false");
                       result = new Task.RESULT(false,subscriptionID);
                       
                   }catch(Exception ex)
                   {
                       logger.error("Failure in Subcription of ID "+clientId+" :Exception:"+ex.getMessage());
                       logger.trace("The result from the Subscriber task is false");
                       result = new Task.RESULT(false,subscriptionID);
                       
                   }
                   logger.debug("AnalyticManagementSystem send success result "
                            +result.success
                            +" for subscriptionID "+result.subscriptionID+" back to RMI:");
                   //Subscriber send result back
                   this.amstormisoutcomechannel.offer(result);
                   
                   
                }else if(task.isUnsubscriber())
                {
                    /************UNSUBSCRIBER****************/
                   
                   try{
                       
                       Task.UNSUBSCRIBER unsubscriber=task.getUnSubscriber();
                       subscriptionID=unsubscriber.subscriptionID;
                       logger.debug("Taken Unsubscriber Task for Subcription ID"
                               +subscriptionID+" from RMI."); 
                                        
                      
                       if(!subscription_map.containsKey(subscriptionID))
                           throw new Exception("Subscription ID"
                                   +" does not exist.");
                      
                      logger.trace("Remove SubscriptionID "+subscriptionID
                              +" from subscription_map.");
                      Filter filter=subscription_map.remove(subscriptionID);
                      long filter_id=filter.getFilterID();
                      if(subscription_map.containsKey(subscriptionID))
                          throw new Exception("Subscription ID was not properly deleted.");
                      //filter_id=client_id
                      //always one filter exist for one management client
                      logger.trace("Search in the mclient_map for Client_ID "+filter_id);
                      if(!mclient_map.containsKey(filter_id))
                          throw new Exception("No Filter exists for "
                                  +"propper Subcsription ID "
                                  +subscriptionID
                                  +" .");
                      logger.trace("Unsubscribe SubscriptionID "+subscriptionID
                              +" from Filter ID "+filter.getFilterID());
                      filter.unsubscribeRegex(subscriptionID);
                      if(filter.getSubscriberSize()==0)
                      {
                         logger.trace("The Filter ID "+filter.getFilterID()
                                 +" has no Subscription Rgexes anymore.");
                         mclient_map.remove(filter_id);
                         logger.trace("Client ID "+filter_id
                                 +" removed from client_map.");
                         //will remove incomechannel from handler 
                         //and send the handler a kill message
                         MD.deregisterOutcomingMember(filter_id);
                         logger.trace("The event Channel for  Client ID "
                                 +filter_id+" is removed from the Distributor.");



                      }
                      result = new Task.RESULT(true,subscriptionID);
                      /***********END UNSUBSCRIBER****************/  
                   }catch(Exception ex)
                   {
                       logger.error("Failure in Unsubscription of ID "+subscriptionID+" :Exception:"+ex.getMessage());
                       logger.trace("The result from the Unsubscriber task is false");
                       result = new Task.RESULT(false,subscriptionID);
                   
                   }
                    logger.debug("AnalyticManagementSystem send success result "
                            +result.success
                            +" for subscriptionID "+result.subscriptionID+" back to RMI:");
                    this.amstormisoutcomechannel.offer(result);

                }

                //return succes status back to rmi
                //this.amstormisoutcomechannel.offer(result);
            } catch (InterruptedException ex) {
               Thread.currentThread().interrupt();
              logger.error("InterruptedException:"+ex.getMessage());
              Thread.currentThread().interrupt();
            } catch (Exception ex) {
             logger.error("Exception:"+ex.getMessage());
              Thread.currentThread().interrupt();
            }  
           
            
        }
        this.close();
        logger.debug("AnalyticsManagemenSystem Handler closed...");
       
    }
    
     public void close()
     {  
         logger.debug("Closing AnalyticManagementSystem and all his resources.");
         AnalyticsControllEvent closeStatistic=new AnalyticsControllEvent(0,
                 AnalyticsControllEvent.AnalyticsControllEventType.CLOSE_STATISTIC);
         
         AnalyticsControllEvent closeDistributor=new AnalyticsControllEvent(0,
                 AnalyticsControllEvent.AnalyticsControllEventType.CLOSE_DISTRIBUTOR);
         
         this.statisticsincomechannel.offer(closeStatistic);
         this.distributorincomechannel.offer(closeDistributor);
         
         this.amstormisoutcomechannel.clear();
         this.toamsincomechannel.clear();
         this.mclient_map.clear();
         this.statisticsincomechannel.clear();
         this.subscription_map.clear();
         this.distributorincomechannel.clear();
         
         this.statisticsincomechannel=null;
         this.distributorincomechannel=null;
         this.amstormisoutcomechannel=null;
         this.toamsincomechannel=null;
         this.mclient_map=null;
         this.statisticsincomechannel=null;
         this.subscription_map=null;
         logger.trace("Closing AnalyticManagementSystem finished");
         
         
         
     }
     
     private void handleRemoteException(long ClientID)
     {
          //subscription_map muss komplett durchgegangen werden
         logger.debug("A RemoteException from Client ID "+ClientID
                +"leads to deletion of all subscriptions of the former Client.");
         Iterator<Map.Entry<Long,Filter>> iter = subscription_map.entrySet().iterator();
         while(iter.hasNext())
         {
             Map.Entry<Long,Filter> entry = iter.next();
             long filterID=entry.getValue().getFilterID();
             if(filterID==ClientID)
                 iter.remove();
             
         }

         if(mclient_map.containsKey(ClientID)) 
            mclient_map.remove(ClientID);

         //will remove incomechannel from handler 
         //and send the handler a kill message
         MD.deregisterOutcomingMember(ClientID);
         logger.debug("All subscriptions and messagequeues of Client ID "+ClientID+" deleted ");

     }
   
}