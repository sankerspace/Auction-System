package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Dave
 */
public interface BillingServerInterface extends Remote {

    public BillingServerSecure login(String username, String password) throws RemoteException;
    public BillingServerSecure getBillingServerSecure() throws RemoteException;
}
