/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import Event.Event;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Dave
 */
public interface AnalyticsServerInterface extends Remote{
    
   
    public long subscribe(ManagementClientCallBackInterface mclient,String filter) throws RemoteException;
    
    public void processEvents(Event e) throws RemoteException;
    
    public boolean unsubscribe(long subsciptionID) throws RemoteException;
}