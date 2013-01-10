/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package auctionmanagement;

/**
 *
 * @author sanker
 */
public class TentativeBidEntry {
    long auctionID=0;
    String groupBidUser=null;
    String FirstConfirmUser=null;
    String SecondConfirmUser=null;
    double amount;
    TentativeBidEntry(long auctionID,
            String groupBidUser,
            double amount){
        this.amount=amount;
        this.auctionID=auctionID;
        this.groupBidUser=groupBidUser;        
    }
    
    TentativeBidEntry(){
        this.amount=0;
        this.auctionID=0;
        this.groupBidUser=null;        
    }
    
    public synchronized long getAuctionID()
    {
        return this.auctionID;
    }
    
    public synchronized  String getFirstConfirmUser()
    {
        return this.FirstConfirmUser;
    }
    
    public  synchronized  String getSecondConfirmUser()
    {
        return this.SecondConfirmUser;
    }
    
    public synchronized   String getgroupBidUser()
    {
        return this.groupBidUser;
    }
     
    public synchronized   double getBid()
    {
        return this.amount;
    }
    
    public synchronized   void setSecondConfirmUser(String user)
    {
        this.SecondConfirmUser=user;
    }
    
    public synchronized   void setFirstConfirmUser(String user)
    {
        this.FirstConfirmUser=user;
    }
    
    
            
    
   
}