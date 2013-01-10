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
    private ConcurrentHashMap<Integer,TentativeBidEntry> map=null;
    private int blockedUser=0;
    
    
    TentativeBids()
    {
        map=new ConcurrentHashMap<Integer,TentativeBidEntry>();
    }
    
    
    public synchronized boolean insertnewTentativeBid(int auctionID,
            String groupBidUser,
            double amount)
    {   //1) and 3) and 2)
        //check for Keyduplicate
        if(this.isalreadyinsertedAuctionID(auctionID))
            return false;
        //check for same groupBidUser
        TentativeBidEntry tbe=null;
        Set<Entry<Integer,TentativeBidEntry>> entrySet=map.entrySet();
        Iterator<Entry<Integer,TentativeBidEntry>> iter =entrySet.iterator();
        while(iter.hasNext())
        {
            Entry<Integer,TentativeBidEntry> entry = iter.next();
            tbe=entry.getValue();
            /* 3) */
            if(tbe.getgroupBidUser().contentEquals(groupBidUser))
                return false;
            /*5a) FirstConfirmuser should be in blocked Mode   */
            if(tbe.getFirstConfirmUser().contentEquals(groupBidUser))
                return false;
            
            
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
    
    public boolean isalreadyinsertedAuctionID(int auctionID)
    {
        boolean b=map.containsKey(new Integer(auctionID)); 
        return b;
    }
    public boolean AuctionhasOneConfirmer(int auctionID)
    {
        TentativeBidEntry tbe=this.map.get(auctionID);
        if(tbe.getFirstConfirmUser()!=null)
            return true;
        else
            return false;
    }
    
    public boolean isalreadyregistratedgroupBidUser(String user)
    {
        TentativeBidEntry tbe=null;
        Set<Entry<Integer,TentativeBidEntry>> entrySet=map.entrySet();
        Iterator<Entry<Integer,TentativeBidEntry>> iter =entrySet.iterator();
        while(iter.hasNext())
        {
            Entry<Integer,TentativeBidEntry> entry = iter.next();
            tbe=entry.getValue();
            if(tbe.getgroupBidUser().contentEquals(user))
                return true;
            
        }
        
        return false;
    }
    
    public boolean isalreadyregistratedAsFirstConfirmedUser(String user)
    {
        TentativeBidEntry tbe=null;
        String FirstConfirmUser=null;
        Set<Entry<Integer,TentativeBidEntry>> entrySet=map.entrySet();
        Iterator<Entry<Integer,TentativeBidEntry>> iter =entrySet.iterator();
        while(iter.hasNext())
        {
            Entry<Integer,TentativeBidEntry> entry = iter.next();
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
    
    private TentativeBidEntry getEntry(Integer AuctionID)
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
    public synchronized boolean confirm(String user,int AuctionID,double amount,TentativeBidEntry tbe)
    {
        tbe=null;
        TentativeBidEntry tmp=null;
        Integer key=new Integer(AuctionID);
        if(this.isalreadyregistratedAsFirstConfirmedUser(user))
            return false;
        if(!this.map.containsKey(key))
            return false;
        tmp=this.getEntry(key);
        if(tmp.getBid()!=amount)
            return false;
        if(tmp.getFirstConfirmUser()==null)
            tmp.setFirstConfirmUser(user);
        else
        {
            tmp.setSecondConfirmUser(user);
            tbe.copy(tbe);
            this.map.remove(key);
            
        }
        return true;

    }
      
}



