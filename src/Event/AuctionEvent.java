/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Event;

/**
 *
 * @author Marko
 */
public class AuctionEvent extends  Event {
    private long auctionID=0;
    private long time=0;
    
    public AuctionEvent(AuctionEvent.AuctionType type,long auctionID)
    {
        super(type.name());
        this.auctionID=auctionID;
        
    }
  
    public AuctionEvent(AuctionEvent e)
    {
        super(e);
        this.auctionID=e.auctionID;
        this.time=e.time;
    }
    
    public long getAuctionID()
    {
        return this.auctionID;
    }
    
    public long getAuctionTime()
    {
        return this.time;
    }
    
    public void setTime(long time)
    {
        if(super.getType().contains(AuctionType.AUCTION_ENDED.name()) )
        {
           this.time=time; 
        }
    }
    
    public static enum AuctionType
    {
        AUCTION_STARTED,
        AUCTION_ENDED
    }
    
    public String toString()
    {
        String msg = null;
        if(super.getType().contains("AUCTION_STARTED"))
        {
            msg="auction with ID "+this.getID()+" has started."; 
        }else  if(super.getType().contains("AUCTION_ENDED"))
        {
             msg="auction with ID "+this.getID()+" finished."; 
        }
        return (super.toString() + msg );
    }


    
}

