/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalyticsServer;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 *
 * @author sanker
 */
public class Filter {
    private static long counter=0;
    private long filterID;
    private static Logger logger=Logger.getLogger(AnalyticsServer.class.getSimpleName()
                    +"."+Filter.class.getSimpleName());
    private ConcurrentHashMap<Long,Pattern> RegexFilterMap=null;
   
    
    public Filter()
    {
      
        filterID=getnewID();
        RegexFilterMap= new ConcurrentHashMap<Long,Pattern>();
    
    }
    
    public int getSubscriberSize()
    {
        return RegexFilterMap.size();
    }
           
    
    boolean checkMessage(String message)
    {
       Iterator<Map.Entry<Long,Pattern>> iter = this.RegexFilterMap.entrySet().iterator();
       logger.debug("Check message: '"+message+"' in the Filter ID "+this.filterID+".");
        while(iter.hasNext())
        {
            Map.Entry<Long,Pattern> entry = iter.next();
            
            Matcher match=entry.getValue().matcher(message);
            if(match.matches())
            {
                logger.debug("Filter System with ID "+this.filterID+" found a match.");
                     
                return true;
            }
                 
        }
        
        return false;
    }
    
    public long getFilterID()
    {
        return filterID;
    }
    
    public boolean subscribeRegex(Long id,Pattern pattern)
    {
        logger.debug("Subscribe Regex:'"+pattern.toString()+"' with id "+id+".");
        this.RegexFilterMap.put(id, pattern);
        if(RegexFilterMap.containsKey(id))
            return true;
        else
            return false;
    }
    
    public boolean subscribeRegex(Long id,String regex)
    {
        logger.debug("Subscribe Regex:'"+regex+"' with id "+id+".");
        this.RegexFilterMap.put(id, Pattern.compile(regex));
        if(RegexFilterMap.containsKey(id))
            return true;
        else
            return false;
     
    }
    
    public boolean  unsubscribeRegex(Long id)
    {
      logger.debug("Unsubscribe Subscription ID "+id+".");
      if((this.RegexFilterMap.remove(id))!=null)
         return true;
      else 
          return false;

    }
    
    private synchronized long getnewID()
    {
        return counter++;
    }

}