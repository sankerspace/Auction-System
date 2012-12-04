/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagementClients;

import RMI.ManagementClientCallBackInterface;
import auctionmanagement.Notification;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentLinkedQueue;


import org.apache.log4j.Logger;


/**
 *
 * @author Dave
 */
public class ManagementClientCallBackInterfaceImpl extends UnicastRemoteObject implements ManagementClientCallBackInterface{
    
    private long id;
    private boolean mode=false;
    private ConcurrentLinkedQueue<String> eventbuffer=null;
    private static Logger logger=Logger.getLogger(ManagementClientCallBackInterfaceImpl.class.getSimpleName());
    private boolean init=false;
    
    public ManagementClientCallBackInterfaceImpl() throws RemoteException {
        super();
    }

    
  
    @Override 
    public void processEvent(String msg) throws RemoteException
    {
        boolean b;
        if(init)
        {
            if(mode)   
                System.out.print(msg+"\n>");
            else 
            {
                b=eventbuffer.offer(msg);
                if(!b)
                    throw new RemoteException("ManagementClientCallBackInterfaceImpl:processEvent:Buffer is full.");
            }
        }
    }
    
    @Override 
    public long getManagementClientID()throws RemoteException
    {
         return id;
    }
    @Override 
    public void setManagementClientID(long ClientID)throws RemoteException
    {
        this.id=ClientID;
    }
    
    
    public void initializeManagementClient()
    {
        if(!init)
        {
           
            this.id=-1;
            mode=false;
            eventbuffer= new ConcurrentLinkedQueue<String>();
            init=true;
        }
        
    }
    
    public void initializeManagementClientinAutoMode()
    {
        if(!init)
        {
           
            this.id=-1;
            mode=true;
            eventbuffer= null;
            init=true;
        }
        
    }
    
    public long getLocalClientID()
    {
        return this.id;
    }
    
    public void reset()
    {
        eventbuffer.clear();
        mode=false;
    }
    
    // mode=true : auto
    //mode false : hide
    public synchronized void  setEventPrintMode(boolean mode)
    {
        if(this.mode!=mode)
        {
            this.mode=mode;
            if(mode)
            {
                String[] s=clearBuffer();
                for(int i=0;i<s.length;i++)
                    System.out.println(">"+s[i]);
            }
        }
            
    }
    
    public void printBuffer()
    {
        if(!mode)
        {
            String[] s =clearBuffer();
            for(int i=0;i<s.length;i++)
                System.out.println(">"+s[i]);
        }
    }
    
    public boolean getMode()
    {
        return this.mode;
    }
    
    
    
    public String[] clearBuffer()
    {
        String[] s=this.eventbuffer.toArray(new String[0]);
        this.eventbuffer.clear();
        return s;
        
        
    }
    
    
}
