/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package auctionmanagement;

import communication.Client;

/**
 *
 * @author sanker
 */
public class CommandTask {
     
    private StringBuffer StrRepr=null;
     List   list=null;
     Login  login=null;
     Create create=null;
     Logout logout=null;
     Bid    bid=null;
     End    end=null;
    
    public CommandTask(List list){this.list=list;StrRepr=new StringBuffer(this.list.toString());}
    public CommandTask(Login login){this.login=login;StrRepr=new StringBuffer(this.login.toString());}
    public CommandTask(Create create){this.create=create;StrRepr=new StringBuffer(this.create.toString());}
    public CommandTask(Logout logout){this.logout=logout;StrRepr=new StringBuffer(this.logout.toString());}
    public CommandTask(Bid bid){this.bid=bid;StrRepr=new StringBuffer(this.bid.toString());}
    public CommandTask(End end){this.end=end;StrRepr=new StringBuffer(this.end.toString());}
 
    public String toString(){
        return StrRepr.toString();
    }
    
    
    
    
    
    
    
    
    
    public static class List
    {
        Client client=null;
        StringBuffer StrRepr=null;
        public List(Client client)
         {
             this.client=client;
             StrRepr= new StringBuffer("List:client:host"+this.client.getDestinationHost()
                     +"\nList:client:port:"+this.client.getDestinationPort());
                     
         }
        public String toString()
        {
            return StrRepr.toString();
        }
    }
    
    public static class Login
    {
         StringBuffer StrRepr=null;
         Client client=null;
         String user=null;
         int udpPort;
        
         public Login(Client client,String user,int udpPort)
         {
             this.client=client;
             this.user=user;
             this.udpPort=udpPort;
             
              StrRepr= new StringBuffer("Login:client:host"+this.client.getDestinationHost()
                     +"\nLogin:client:port:"+this.client.getDestinationPort());
              
              StrRepr.append("\nLogin:user:"+this.user);
              StrRepr.append("\nLogin:udpPort:"+this.udpPort);
         
         }
         
         public String toString()
        {
            return StrRepr.toString();
        }
        
    }
    
    public static class Create
    {
         StringBuffer StrRepr=null;
         Client client=null;
         String user=null;
         long expire;
         String description;
         
         public Create(Client client,String user,long expire,String description)
         {
             this.client=client;
             this.description=description;
             this.expire=expire;
             this.user=user;
             
             StrRepr= new StringBuffer("Create:client:host"+this.client.getDestinationHost()
                     +"\n"+"Create:client:port:"+this.client.getDestinationPort());
               StrRepr.append("\n"+"Create:user;"+this.description);
              StrRepr.append("\n"+"Create:description:"+this.description);
              StrRepr.append("\n"+"Create:user:"+this.user);
         }
         
        public String toString()
        {
            return StrRepr.toString();
        }
        
        
    }
    
    public static class Logout
    { 
         StringBuffer StrRepr=null;
         Client client=null;
         String user;
        
        public Logout(Client client,String user)
        {
            this.client=client;
            this.user=user;
            
            StrRepr= new StringBuffer("Logout:client:host"+this.client.getDestinationHost()
                     +"\n"+"Logout:client:port:"+this.client.getDestinationPort());
               StrRepr.append("\n"+"Logout:user;"+this.user);
              
        }
        public String toString()
        {
            return StrRepr.toString();
        }
        
    }
    
    public static class Bid
    {
        StringBuffer StrRepr=null;
        Client client=null;
        String user=null;
        int id;
        double amount;
        
        public Bid(Client client,String user,int id,double amount)
        {
            this.client=client;
            this.amount=amount;
            this.id=id;
            this.user=user;
            
            StrRepr= new StringBuffer("Bid:client:host"+this.client.getDestinationHost()
                     +"\n"+"Bid:client:port:"+this.client.getDestinationPort());
               StrRepr.append("\n"+"Bid:user;"+this.user);
              StrRepr.append("\n"+"Bid:id:"+this.id);
              StrRepr.append("\n"+"Bid:amount:"+this.amount);
        }
        public String toString()
        {
            return StrRepr.toString();
        }
    }
    
    public static class End
    {
        
        Client client=null;
        StringBuffer StrRepr=null;
        
        public End(Client client)
         {
             this.client=client;
             
             StrRepr= new StringBuffer("End:client:host"+this.client.getDestinationHost()
                     +"\n"+"End:client:port:"+this.client.getDestinationPort());
                     
         }
         public String toString()
        {
            return StrRepr.toString();
        }
    }
    
    
}
