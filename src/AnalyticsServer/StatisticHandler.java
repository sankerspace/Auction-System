/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalyticsServer;

import Event.AnalyticsControllEvent;
import Event.AuctionEvent;
import Event.BidEvent;
import Event.Event;
import Event.StatisticsEvent;
import Event.UserEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

/**
 *
 * @author sanker
 */
public class StatisticHandler implements Runnable{
    private final long periode=60000;
    
    private LinkedBlockingQueue<Event> inputchannel=null;
    private LinkedBlockingQueue<Event> outputchannel=null;
    private Logger logger=Logger.getLogger(AnalyticsServer.class.getSimpleName()
                +"."+StatisticHandler.class.getSimpleName());
    private Timer timer = null;
    
    /*Statistic tracking values**/
    private double count_Bid_Won=0;
    private double count_Auctions=0;
    private double bid_max=0;
    private double count_Bidding=0;
    private double user_sessiontime_max=0;
    private double user_sessiontime_min=0;
    private double user_sessiontime_avg=0;
    private double sum_user_sessiontime=0;
    private double count_user_sessions=0;
   /*Statistic time values [ms]**/
    private long sum_AllAuctionTime; //AuctionTime1 + AuctionTime2+..+AuctionTimeN
    
   private final Lock lock =new ReentrantLock();;
    
    public StatisticHandler(ExecutorService pool,LinkedBlockingQueue<Event> inputchannel
            ,LinkedBlockingQueue<Event> outputchannel)
    {
      
       this.inputchannel=inputchannel;
       this.outputchannel=outputchannel;
       this.timer = new Timer(true);
       
    }
       
    
    public void run()
    {
        
   
       logger.info("Start timer with a periode of 60 seconds.");
       timer.scheduleAtFixedRate(new StatisticHandler.Task(lock),0, periode);
       
        Event event=null;
        while(!Thread.currentThread().isInterrupted())
        {
            try {
                
               event=inputchannel.take();
               if(event.getType().contains(AnalyticsControllEvent.AnalyticsControllEventType.CLOSE_STATISTIC.name()))
               {
                   logger.debug("Event:CLOSE_STATISTIC:Close StatisticHandler.");
                   Thread.currentThread().interrupt();
                   break;
               }
               else if( (event.getType().contains(UserEvent.UserEventType.USER_LOGOUT.name()))
                       || (event.getType().contains(UserEvent.UserEventType.USER_DISCONNECTED.name()) ))
               {
                   logger.debug("Incoming Event:"+event.getType());
                 //track USER LOGOUTS
                   UserEvent userevent=(UserEvent)event;
                   long usertime=userevent.getUserTime();
                   sum_user_sessiontime+=usertime;
                   count_user_sessions++;
                   user_sessiontime_avg=sum_user_sessiontime/count_user_sessions;//USER_SESSION_AVG
                   if(count_user_sessions==1)//user_session_min=user_session_max
                       user_sessiontime_min=usertime;
                   if(user_sessiontime_min>usertime)
                       user_sessiontime_min=usertime;//USER_SESSION_MIN
                   if(user_sessiontime_max<usertime)
                       user_sessiontime_max=usertime;//USER_SESSION_MAX
                    StatisticsEvent sevent= new StatisticsEvent(user_sessiontime_min,
                           StatisticsEvent.StatisticsEventType.USER_SESSIONTIME_MIN);
                    this.outputchannel.offer(sevent);
                    logger.debug("Send new StatisticalEvent of Typ:"+
                           StatisticsEvent.StatisticsEventType.USER_SESSIONTIME_MIN.name()
                             +" and value:"+user_sessiontime_min);
                    sevent= new StatisticsEvent(user_sessiontime_max,
                           StatisticsEvent.StatisticsEventType.USER_SESSIONTIME_MAX);
                    this.outputchannel.offer(sevent);
                    logger.debug("Send new StatisticalEvent of Typ:"+
                           StatisticsEvent.StatisticsEventType.USER_SESSIONTIME_MAX.name()
                            +" and value:"+user_sessiontime_max);
                     sevent= new StatisticsEvent(user_sessiontime_avg,
                           StatisticsEvent.StatisticsEventType.USER_SESSIONTIME_AVG);
                    this.outputchannel.offer(sevent);
                    logger.debug("Send new StatisticalEvent of Typ:"+
                           StatisticsEvent.StatisticsEventType.USER_SESSIONTIME_AVG.name()
                            +" and value:"+user_sessiontime_avg);
                   
                   
               }else if(event.getType().contains(BidEvent.BidEventType.BID_WON.name()))
               {
                   logger.debug("Incoming Event:"+event.getType());
                   BidEvent bidevent =(BidEvent)event;
                   if(this.bid_max<bidevent.getPrice())
                   {
                       this.bid_max=bidevent.getPrice();//BID_PRICE_MAX
                   }
                 
                   
                   count_Bid_Won++;
                   
                    StatisticsEvent sevent= new StatisticsEvent(bid_max,
                           StatisticsEvent.StatisticsEventType.BID_PRICE_MAX);
                    logger.debug("Send new StatisticalEvent of Typ:"+
                           StatisticsEvent.StatisticsEventType.BID_PRICE_MAX.name()
                            +" and value:"+bid_max);
                   this.outputchannel.offer(sevent);
                   
               }else if(event.getType().contains(BidEvent.BidEventType.BID_PLACED.name()))
               {
                   logger.debug("Incoming Event:"+event.getType());
                 //for bid_COUNT_PER_MINUTE
                    try{
                        lock.lock();
                        count_Bidding++;
                    }finally
                    {
                        lock.unlock();
                    }
                   
                   
                   
               }else if(event.getType().contains(AuctionEvent.AuctionType.AUCTION_ENDED.name()))
               {
                    logger.debug("Incoming Event:"+event.getType());
                 //for AUCTION_TIME_AVG
                   AuctionEvent auctionevent = (AuctionEvent)event;
                   count_Auctions++;
                   sum_AllAuctionTime+=auctionevent.getAuctionTime();
                   double auction_time_avg=sum_AllAuctionTime/count_Auctions;//for AUCTION_TIME_AVG
                   double success_ratio=count_Bid_Won/count_Auctions;//for AUCTION_SUCCESS_RATIO
		   logger.debug("count_Auctions:"+count_Auctions);
		   logger.debug("count_Bid_Won:"+count_Bid_Won);
                   logger.debug("success_ratio:"+success_ratio);
                   
                   
                   StatisticsEvent sevent1= new StatisticsEvent(auction_time_avg,
                           StatisticsEvent.StatisticsEventType.AUCTION_TIME_AVG);
                  
                   StatisticsEvent sevent2= new StatisticsEvent(success_ratio,
                           StatisticsEvent.StatisticsEventType.AUCTION_SUCCESS_RATIO);
                   
                   this.outputchannel.offer(sevent1);
                   logger.debug("Send new StatisticalEvent of Typ:"+
                           StatisticsEvent.StatisticsEventType.AUCTION_TIME_AVG.name()
                           +" and value:"+auction_time_avg);
                    this.outputchannel.offer(sevent2);
                    logger.debug("Send new StatisticalEvent of Typ:"+
                           StatisticsEvent.StatisticsEventType.AUCTION_SUCCESS_RATIO.name()
                            +" and value:"+success_ratio);
               }
                                           
              
            } catch (InterruptedException ex) {
              logger.error("InterruptedException:"+ex.getMessage());
              Thread.currentThread().interrupt();
            }catch (Exception ex) {
              logger.error("Exception:"+ex.getMessage());
              Thread.currentThread().interrupt();
            }             
        
        }
     
     
        
        
    }
    
    
     public class Task extends TimerTask {

        private Lock lock; 
         private  Logger logger=null;
        public Task(Lock lock) {
            this.lock=lock;
            logger=Logger.getLogger(AnalyticsServer.class.getSimpleName()
                    +"."+StatisticHandler.class.getSimpleName()
                    +"."+Task.class.getSimpleName());
           
           
            
        }
        
        public void run() 
        {
            double temp;
            try{
                lock.lock();
                temp=count_Bidding;
                count_Bidding=0;
            }finally
            {
                lock.unlock();
            }
            StatisticsEvent sevent= new StatisticsEvent(temp,
                          StatisticsEvent.StatisticsEventType.BID_COUNT_PER_MINUTE);
                   
            outputchannel.offer(sevent);
                   logger.debug("Send new StatisticalEvent of Typ:"+
                           StatisticsEvent.StatisticsEventType.BID_COUNT_PER_MINUTE.name()
                            +" and value:"+temp);
            
        }
      
     }//Task
     

}
