package BillingServer;

import RMI.BillingServerInterface;
import RMI.BillingServerSecure;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.apache.log4j.Logger;
import utils.EasyProperties;
import utils.UtilsException;

/**
 *
 * @author Dave
 */
public class BillingServerInterfaceImpl extends UnicastRemoteObject implements BillingServerInterface {
     static Logger logger = Logger.getLogger(BillingsServer.class.getSimpleName()+"."
             +BillingServerInterfaceImpl.class);
   
    BillingServerSecure bss = null;

    public BillingServerInterfaceImpl() throws RemoteException {
        super();
    }

    public void initialize(BillingServerSecureImpl BSSI)
    {
        this.bss=BSSI;
    }
    @Override
    public BillingServerSecure login(String username, String password) throws RemoteException {
        logger.debug("BillingServerInterface Login Method started!");
        
        try{
            if (EasyProperties.login("./src/user.properties", username, password)) {
                return bss;
            } else {
                return null;
            }  
        }catch(UtilsException ex)
        {
            throw new RemoteException("UtilsException:"+ex.getMessage());
        }
    }

    @Override
    public BillingServerSecure getBillingServerSecure() {
        if (bss != null) {
            return bss;
        }
        return null;
    }
}
