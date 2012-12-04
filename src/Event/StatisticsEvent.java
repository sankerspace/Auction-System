/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Event;

/**
 *
 * @author Marko
 */


public class StatisticsEvent extends  Event {
   private double value=0;
    
    public StatisticsEvent(double value, StatisticsEvent.StatisticsEventType type)
    {        
        super(type.name());
        this.value=value;
    
    }
    
    public StatisticsEvent(StatisticsEvent e)
    {
        super(e);
        this.value=e.value;
    }
    
  
    
    public static enum StatisticsEventType
    {
        USER_SESSIONTIME_MIN,
        USER_SESSIONTIME_MAX,
        USER_SESSIONTIME_AVG,
        BID_PRICE_MAX,
        BID_COUNT_PER_MINUTE,
        AUCTION_TIME_AVG,
        AUCTION_SUCCESS_RATIO
    }
    
    public double getTypeValue()
    {
        return this.value;
    }

    public String toString()
    {
        String msg = null;
        if(super.getType().contains("USER_SESSIONTIME_MIN"))
        {
            msg="minimum sessiontime is "+(this.value/1000)+" sec."; 
        }else  if(super.getType().contains("USER_SESSIONTIME_MAX"))
        {
             msg="maximum sessiontime is "+(this.value/1000)+" sec.";
        }else  if(super.getType().contains("USER_SESSIONTIME_AVG"))
        {
            msg="average sessiontime is "+(this.value/1000)+" sec."; 
        }else  if(super.getType().contains("BID_PRICE_MAX"))
        {
             msg="maximum bid price seen so far is "+(this.value); 
        }else  if(super.getType().contains("AUCTION_TIME_AVG"))
        {
             msg="average auction time is "+(this.value/1000)+" sec."; 
        }else  if(super.getType().contains("AUCTION_SUCCESS_RATIO"))
        {
            msg="auction succes ratio over all last auctions is "+(this.value); 
        }else  if(super.getType().contains("BID_COUNT_PER_MINUTE"))
        {
            msg="number of bid per minute is "+(this.value); 
        }
        
        return (super.toString() + msg );
    }
   
}

