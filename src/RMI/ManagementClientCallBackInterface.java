/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Dave
 */
public interface ManagementClientCallBackInterface extends Remote{
    
    void processEvent(String msg)throws RemoteException;
    
    public long getManagementClientID()throws RemoteException;
    
    public void setManagementClientID(long ClientID)throws RemoteException;
}
