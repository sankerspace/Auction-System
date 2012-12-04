package BillingServer;

import RMI.BillingServerInterface;
import RMI.RMIRegistry;
import RMI.RMIRegistryException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.concurrent.Executor;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 *
 * @author Dave
 */
public class BillingsServer implements Runnable {

    private static Logger logger = Logger.getLogger(BillingsServer.class.getSimpleName());
    private RMIRegistry registry = null;
    //private BillingServerInterface bsi = null;
    //private Executor pool = null;
    
   
    
    private BillingServerInterfaceImpl BSII=null;
    private BillingServerSecureImpl BSSI=null;
    
    public BillingsServer(String propertyFile, String billingBindingName) {
        try {
           
             DOMConfigurator.configure("./src/log4j.xml"); 
            
            
            registry = new RMIRegistry(propertyFile);
            if (registry.getRegistry() == null) {
                registry.startRegistry();
                logger.info("Registry successfully started by BillingsServer");
            }
            BSII=new BillingServerInterfaceImpl();
            BSSI=new BillingServerSecureImpl();
            
            BSII.initialize(BSSI);
            
            registry.registerObject(billingBindingName, BSII);
            logger.info("BillingServerInterface was successfully registered to the Registry");
            
        } catch (RemoteException ex) { // java 7 wird bei der abgabe nicht unterstützt
            logger.error("RemoteException:"+ex.getMessage());
        } catch (RMIRegistryException ex) {
            logger.error("RMIRegistryException:"+ex.getMessage());
        }
    }
    
    public void run() {
        logger.info("BillingServer started...");
        String line = null;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while ((line = in.readLine()) != null) {
                    Thread.currentThread().interrupt();
                    break;
                }
            } catch (IOException e) {
                logger.fatal("Billing Server Main is not running.");
            }
        }
        logger.info("BillingServer closed...");
    }
}
