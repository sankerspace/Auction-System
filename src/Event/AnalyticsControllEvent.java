/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Event;

/**
 *
 * @author sanker
 */
public class AnalyticsControllEvent extends Event {
    
    private long Id;
    
    public AnalyticsControllEvent(long ID,AnalyticsControllEvent.AnalyticsControllEventType type)
    {
        super(type.name());
        this.Id=ID;
   
    }
    
   public long getIDNumber()
   {
       return this.Id;
   }
   
     public static enum AnalyticsControllEventType
    {
        CLOSE_FILTER,
        CLOSE_DISTRIBUTOR,
        CLOSE_STATISTIC,
        ERROR_SUBSCRIPTION,
        ERROR_UNSUBSCRIPTION
        
    }
     
    public String toString()
    {
        String msg = null;
        if(super.getType().contains("CLOSE_FILTER"))
        {
            msg=""; 
        }else  if(super.getType().contains("ERROR_SUBSCRIPTION"))
        {
             msg="Error: Subscription of ID  "+this.getIDNumber()+" was unsuccessfull."; 
        }else  if(super.getType().contains("ERROR_UNSUBSCRIPTION"))
        {
             msg="Error: Unsubscription of ID  "+this.getIDNumber()+" was unsuccessfull."; 
        }
        return (msg);
    }
    
}
