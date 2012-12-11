/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LoadTest;

import MyLogger.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;




/*
 * 
 * static int clients = Integer.parseInt(EasyProperties.getProperty("./src/loadtest.properties", "clients"));
    static int auctionsPerMin = Integer.parseInt(EasyProperties.getProperty("./src/loadtest.properties", "auctionsPerMin"));
    static int auctionDuration = Integer.parseInt(EasyProperties.getProperty("./src/loadtest.properties", "auctionDuration"));
    static int updateIntervalSec = Integer.parseInt(EasyProperties.getProperty("./src/loadtest.properties", "updateIntervalSec"));
    static int bidsPerMin = Integer.parseInt(EasyProperties.getProperty("./src/loadtest.properties", "bidsPerMin"));
    
 * 
 * 
 * 
 */

/**
 *
 * @author sanker
 */
public class AuctionTest 
{
    private Log logger=null;
    private ExecutorService pool;
    private int clients;
    private String host=null;
    private int port;
    private Properties prop=null;
    private String AnalyticBindingName=null;
    private ReentrantLock controlManagementClientLock=null;
    
    
            
    public AuctionTest(String host,int port,String AnalyticBindingName,
            int clients,
            int auctionsPerMin,
            int auctionDuration,
            int updateIntervalSec,
            int bidsPerMin,
            Log logger) throws AuctionTestException
    {
        if( (clients<1) || (auctionsPerMin < 1) || (auctionDuration<1) 
                || (updateIntervalSec<1) || (bidsPerMin<1) || (logger==null)
                || (host==null) || (port<1) || (AnalyticBindingName==null))
            throw new AuctionTestException("Illegal parameter passed.");
        
        pool = Executors.newCachedThreadPool();
        this.logger=logger;
        this.clients=clients;
        this.host=host;
        this.port=port;
        this.AnalyticBindingName=AnalyticBindingName;
        this.prop=new Properties(
                auctionsPerMin,
                auctionDuration,
                updateIntervalSec,
                bidsPerMin);
        
        
        
    }

    
    
    public void run() throws AuctionTestException
    {
        logger.output("AuctionTest:run:Start AuctionTest",2);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        AuctionTestClient[] auctionclient=new AuctionTestClient[clients];
        logger.output("AuctionTest:run:Create Lock for ManagementClient.",3);
        controlManagementClientLock=new ReentrantLock();
        controlManagementClientLock.lock();
    
        //start ManagementClient 
        ManagementClientForLoadTest managementclient=null;
        managementclient=new ManagementClientForLoadTest("./src/registry.properties",
                this.AnalyticBindingName,controlManagementClientLock);
        //pool.execute(managementclient);
       
        
        int i=0;
        //initialize and start all clients
        try{
            if(!managementclient.getInitStatus())
                throw new Exception("ManagementClient for Load Test not ready.");
           
            //managementclient.run();
            
            for (i = 0; i < clients; i++) 
            {
                 logger.output("AuctionTest:run:Create AuctionTestClient "+i+".",2);
                 auctionclient[i]= new AuctionTestClient(i,host,
                        port,
                        prop,                   
                        pool,
                        logger);
                  pool.execute(auctionclient[i]);

                
             }
            logger.output("AuctionTest:run:Wait for Input.",3);
            //wait for input to exit program
            String line=in.readLine();
            
        }catch(AuctionTestClientException ex)
        {
            throw new AuctionTestException("AuctionTestClientException:"+ex);
        }catch(IOException ex)
        {
            throw new AuctionTestException("IOException:"+ex);
        }catch(Exception ex)
        {
            throw new AuctionTestException("Exception:"+ex);
        }finally
        {
            logger.output("AuctionTest:run:finally:Free ressources.",2);
            this.controlManagementClientLock.unlock();
            if(i>0)
            {
                for (int j = 0; j < i; j++) 
                {
                    if(auctionclient[j]!=null)
                    {
                        logger.output("AuctionTest:run:finally:"
                                +"Close auctionclient "+j+".",3);
                        auctionclient[j].getWaitingRoom().callingfromWaitingRoom();
                        //wakes up the client to proceed to death
                    }
                }
                    
            }
            this.shutdownPool();
        } 
         logger.output("AuctionTest:run:Exit AuctionTest",2);
    }//run
    
     private  void shutdownPool()
    {
       
        if(!pool.isShutdown())
        {
            pool.shutdown(); // Disable new tasks from being submitted

            try {
                // Wait a while for existing tasks to terminate

                if (!pool.awaitTermination(3, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled      
                }
            }catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
            }
        } 
        
    
    }
    
    
    
    public class Properties
    {
        int auctionsPerMin; 
        int auctionDuration; 
        int updateIntervalSec;
        int bidsPerMin; 
    
        
          
        public Properties(int auctionsPerMin,int auctionDuration
                ,int updateIntervalSec,int bidsPerMin)
        {
          this.auctionDuration=auctionDuration;
          this.auctionsPerMin=auctionsPerMin;
          this.bidsPerMin=bidsPerMin;
          this.updateIntervalSec=updateIntervalSec;
        }
    }
    
 }