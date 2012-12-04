/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LoadTest;

/**
 *
 * @author sanker
 */


import ManagementClients.ManagementClientCallBackInterfaceImpl;
import RMI.AnalyticsServerInterface;

import RMI.RMIRegistry;
import RMI.RMIRegistryException;

import java.rmi.RemoteException;
import java.util.concurrent.locks.ReentrantLock;



/**
 *
 * @author Dave
 */
public class ManagementClientForLoadTest implements Runnable {

   
  
    private RMIRegistry registry = null;
    private ManagementClientCallBackInterfaceImpl mccbi = null;
    private AnalyticsServerInterface analytic = null;
    private boolean init = true;
    private boolean analyticIsAvaible = false;
    private ReentrantLock waitforlock=null;

    public ManagementClientForLoadTest(String propertyFile, String analyticBindingName,ReentrantLock lock) {
        try {
           
           
            /**
             * Get or start Registry(Naming Server)
             *
             */
            this.waitforlock=lock;
            registry = new RMIRegistry(propertyFile);

            /**
             * ***AnalyticServer Part*******
             */
            try {
                analytic = registry.getAnalyticsInterface(analyticBindingName);
                mccbi = new ManagementClientCallBackInterfaceImpl();
                mccbi.initializeManagementClientinAutoMode();
                analyticIsAvaible = true;
            } catch (RMIRegistryException ex) {
               
                init=false;
                analyticIsAvaible = false;
            }
           
        } catch (Exception ex) {
          
            init = false;
        }

    }

    public void run() {
        
      try{
        analytic.subscribe(mccbi,".*");
      
        waitforlock.lock();
  
      }catch(RemoteException ex)
      {
          init=false;
      }finally        
      {
          waitforlock.unlock();
      
      }
      
      System.out.println("ManagementClient for Load test closed.");

    }
    
    public boolean getInitStatus()
    {
        return this.init;
    }
        
}

  
   