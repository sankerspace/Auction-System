/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package auctionmanagement;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author sanker
 */



/**
     * Restrictions:
     * 
     * 1) A auctionID must be unique , because it is the key
     *  and several groupBids on one auction are not allowed 
     * 2)FirstConfirmUser must be unique
     *  a user cannot be multiple blocked 
     * 3)groupBidUser must be unique,
     * only one groupBid allowed for one user because of
     * Starvation prevention of auctionsystem  
     * 4)view on all tentaiveBids must be a subset of all active auctions
     * 5)grouBidUser is  allowed to be also a FirstConfirmUser,
     * but vice vers it is NOT allowed
     * 5a)If a user is in blocked Mode[a FirstConfirmuser]he can not enter a groupbid
     * 5b)if a user has set a groupBid he is allowed to confirm a groupbid elsewhere,
     * but not in the same groupbid before
     * 
     */
public class TentativeBids {
    //<AuctionID,TentativeBidEntry>
    private ConcurrentHashMap<Long,TentativeBidEntry> map=null;
    
    
    
    TentativeBids()
    {
        map=new ConcurrentHashMap<Long,TentativeBidEntry>();
    }
    
    
    public  boolean insertnewTentativeBid(long auctionID,
            String groupBidUser,
            double amount)
    {   //1) and 3) and 2)
        //check for Keyduplicate
        if(this.isalreadyinsertedAuctionID(auctionID))
            return false;
        //check for same groupBidUser
        TentativeBidEntry tbe=null;
        Set<Entry<Long,TentativeBidEntry>> entrySet=map.entrySet();
        Iterator<Entry<Long,TentativeBidEntry>> iter =entrySet.iterator();
        String FirstConfirmUser=null;
        while(iter.hasNext())
        {
            Entry<Long,TentativeBidEntry> entry = iter.next();
            tbe=entry.getValue();
            /* 3) */
            if(tbe.getgroupBidUser().contentEquals(groupBidUser))
                return false;
            /*5a) FirstConfirmuser should be in blocked Mode   */
            FirstConfirmUser=tbe.getFirstConfirmUser();
            if(FirstConfirmUser!=null)
            {
                if(FirstConfirmUser.contentEquals(groupBidUser))
                    return false;
            }
            
            
        }

       TentativeBidEntry entry = new  TentativeBidEntry(auctionID,
               groupBidUser,amount);
       map.put(auctionID, entry);
       
       
        return true;
        
    }
    
    public int getNumberofTentativeBids()
    {
        return this.map.size();
    }
    
    public boolean isalreadyinsertedAuctionID(long auctionID)
    {
        boolean b=map.containsKey(new Long(auctionID)); 
        return b;
    }
    public boolean AuctionhasOneConfirmer(long auctionID)
    {
        
        TentativeBidEntry tbe=this.map.get(auctionID);
        if(tbe==null)
            return false;
        if(tbe.getFirstConfirmUser()!=null)
            return true;
        else
            return false;
    }
    
    public  boolean isalreadyregistratedgroupBidUser(String user)
    {
        TentativeBidEntry tbe=null;
        Set<Entry<Long,TentativeBidEntry>> entrySet=map.entrySet();
        Iterator<Entry<Long,TentativeBidEntry>> iter =entrySet.iterator();
        while(iter.hasNext())
        {
            Entry<Long,TentativeBidEntry> entry = iter.next();
            tbe=entry.getValue();
            if(tbe.getgroupBidUser().contentEquals(user))
                return true;
            
        }
        
        return false;
    }
    
    public  boolean isalreadyregistratedAsFirstConfirmedUser(String user)
    {
        TentativeBidEntry tbe=null;
        String FirstConfirmUser=null;
        Set<Entry<Long,TentativeBidEntry>> entrySet=map.entrySet();
        Iterator<Entry<Long,TentativeBidEntry>> iter =entrySet.iterator();
        while(iter.hasNext())
        {
            Entry<Long,TentativeBidEntry> entry = iter.next();
            tbe=entry.getValue();
            FirstConfirmUser=tbe.getFirstConfirmUser();
            if(FirstConfirmUser!=null)
            {
                if(FirstConfirmUser.contentEquals(user))
                    return true;
            }
            
        }
        
        return false;
    }
    
   public  boolean isvalidConfirm(long AuctionID,double amount)
   {
       TentativeBidEntry tbe=getEntry(new Long(AuctionID));
       if(tbe==null)
           return false;
       if(tbe.getBid()!=amount)
           return false;
       return true;
       
   }
    
    
    private  TentativeBidEntry getEntry(Long AuctionID)
    {
  
        TentativeBidEntry tbe=this.map.get(AuctionID);
        return tbe;
    }
    
    
    /**
     * if user is the first who confirms on a TentativeBid
     * and is not registrated anywhere [2)],than
     * the user is registrated as FirstConfirmUser
     * return true and set TentativeBidEntry to null
     * 
     * if user is the second who confirms on a TentativeBid
     * and is not registrated anywhere [2)],than
     * the user is registrated as SecondConfirmUser
     * return true and set a reference to the TentativeBidEntry of the actual groupBid
     * and remove the TentativeBidEntry fron the List of all TentativeBids
     * 
     * 
     */
    public  TentativeBidEntry  confirm(String user,long AuctionID)
    {
        TentativeBidEntry tmp=null;
        Long key=new Long(AuctionID);
        tmp=this.getEntry(key);
        if(tmp.getFirstConfirmUser()==null)
            tmp.setFirstConfirmUser(user);
            
        else
        {
            tmp.setSecondConfirmUser(user);
            this.map.remove(key);
            return tmp;
        }
        return null;
    }
    
    public TentativeBidEntry remove(long AuctionID)
    {
        return this.map.remove(new Long(AuctionID));
    }
      
}



