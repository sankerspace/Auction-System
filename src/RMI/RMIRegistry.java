/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import utils.EasyProperties;
import utils.UtilsException;

/**
 *
 * @author sanker
 */
public class RMIRegistry {
    
    
     private static Logger logger = Logger.getLogger(RMIRegistry.class.getSimpleName());
     private Registry registry=null;
     private String properties=null;
     private String registryHost=null;
     private int registryPort;
    
    public RMIRegistry(String propertyFile)
    {
         DOMConfigurator.configure("./src/log4j.xml"); 
        
        String[] str=null;
        this.properties = propertyFile;
     
        
        try {
            this.registryHost = EasyProperties.getProperty(propertyFile,"registry.host");
            this.registryPort = Integer.parseInt(EasyProperties.getProperty(propertyFile,"registry.port"));
            
            
            this.registry = LocateRegistry.getRegistry(this.registryHost,this.registryPort);
            str=this.registry.list();
            logger.info("Array of the names bound in this registry:length:"+str.length);
            //this.registry.
        } catch (RemoteException ex) {
            logger.debug("RMIRegistry:constructor:RemoteException:"+ex.getMessage());
            this.registry=null;
            //java.util.logging.Logger.getLogger(RMIRegistry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }catch(UtilsException ex)
        {
            logger.error("RMIRegistry:constructor:UtilsException:"+ex.getMessage());
            this.registry=null;
        }catch(Exception ex)
        {
            logger.error("RMIRegistry:constructor:Exception:"+ex.getMessage());
            this.registry=null;
        }
        
    }
    
    public Registry startRegistry() throws RMIRegistryException {
       
        
        
        try{
           
            int port = Integer.parseInt(EasyProperties.getProperty(this.properties,"registry.port"));
            registry = java.rmi.registry.LocateRegistry.createRegistry(port);
            
        }catch(RemoteException ex)
        {
             logger.error("RemoteException:"+ex.getMessage());
             throw new RMIRegistryException("RemoteException:",ex);
         
        }catch(IOException ex)
        {
             logger.error("IOException:"+ex.getMessage());
            throw new RMIRegistryException("IOException:",ex);
        }catch(Exception ex)
        {
             logger.error("Exception:"+ex.getMessage());
             throw new RMIRegistryException("Exception:",ex);
        }
      
        return registry;
    }
    
    public Registry getRegistry()
    {
        return this.registry;
    }

    
    public  void registerObject(String name, Remote remoteObj) throws RMIRegistryException  {
        
        try{   
            if(registry==null)
                throw new RMIRegistryException("No registry located.");
            registry.rebind(name, remoteObj);
            //System.out.println("Registered: " + name + " -> " + remoteObj.getClass().getName() + "[" + remoteObj + "]");
            String msg = "RMIRegistry:RegisterObject: " + name + " -> " + remoteObj.getClass().getName() + "[" + remoteObj + "]";
            logger.debug(msg);
        }catch(RemoteException ex)
        {
             logger.error("RemoteException:"+ex.getMessage());
            throw new RMIRegistryException("RemoteException:",ex);
        }
        
    }
    
    public void deregisterObject(String name)throws RMIRegistryException
    {
        if(registry==null)
                throw new RMIRegistryException("No registry located.");
        try{   
            registry.unbind(name);
        }catch(NotBoundException ex)
        {
            logger.error("NotBoundException:"+ex.getMessage());
           throw new RMIRegistryException("NotBoundException:",ex);
        }catch(RemoteException ex)
        {
              logger.error("RemoteException:"+ex.getMessage());
             throw new RMIRegistryException("RemoteException:",ex);
            
        }
    
    }
    
    
    
    
    public AnalyticsServerInterface getAnalyticsInterface(String analyticBindingName) throws RMIRegistryException 
    {
        if(registry==null)
                throw new RMIRegistryException("No registry located.");
        
        AnalyticsServerInterface analytic=null;
        try {

            analytic=(AnalyticsServerInterface) registry.lookup(analyticBindingName);
       
        } catch (RemoteException ex) {
            logger.error("RMIRegistry:getAnalyticsInterface:RemoteException:"+ex.getMessage());
             throw new RMIRegistryException("RemoteException:",ex);
        } catch (NotBoundException ex) {
            logger.debug("RMIRegistry:getAnalyticsInterface:NotBoundException:"+ex.getMessage());
            throw new RMIRegistryException("NotBoundException:",ex);
        } 
        
        return analytic;
    }
    
    public BillingServerInterface getBillingInterface(String billingBindingName) throws RMIRegistryException 
    {
        if(registry==null)
                throw new RMIRegistryException("No registry located.");
        
        BillingServerInterface billing=null;
        try {

            billing=(BillingServerInterface) registry.lookup(billingBindingName);
       logger.debug("billingInterface was returned successfully!");
        } catch (RemoteException ex) {
            logger.error("RMIRegistry:getBillingInterface:RemoteException:"+ex.getMessage());
            throw new RMIRegistryException("RemoteException:",ex);
        } catch (NotBoundException ex) {
            logger.debug("RMIRegistry:getBillingInterface:NotBoundException:"+ex.getMessage());
            throw new RMIRegistryException("NotBoundException:",ex);
        } 
        
        return billing;
    }
    
    
    
    
    
}
