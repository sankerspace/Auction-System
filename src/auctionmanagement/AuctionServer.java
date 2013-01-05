/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package auctionmanagement;

import auctionmanagement.AuctionManagementSystem;
import communication.OperationTCP;
import communication.OperationException;
import communication.Server;
import communication.Server.Handler;
import communication.ServerException;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import MyLogger.Log;
import communication.Client;
import communication.ClientException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;



/**
 *
 * @author sanker
 */
public class AuctionServer implements Runnable{
    private AuctionManagementSystem ams=null;
    private Server server=null;
    private final ExecutorService pool;
    private LinkedBlockingQueue<CommandTask> queue=null;
    private Log output=null;
    //Alle Server.Handler schreiben auf die queue, nur das ActionmanagementSystem darf
    //von der queue lesen [blockierend]
    public AuctionServer(int tcpPort,String analytic, String billing,Log output)throws AuctionServerException
    {
       this.output=output;
       //communication between ServerSocketHandleThread and AMSHandlerThread
       queue = new LinkedBlockingQueue<CommandTask>();
       pool = Executors.newCachedThreadPool();
       ams= new AuctionManagementSystem(analytic, billing, queue,pool,output);
       output.output("AuctionServer Port:"+tcpPort+"", 3);
       Server.Handler serversocketHandle=new ServerSocketHandleThread(queue,pool,output);
       pool.execute(ams);
        try {
            server=new Server(tcpPort,serversocketHandle,pool,output);
            pool.execute(server);
        } catch (ServerException e) {
            throw (new AuctionServerException("ServerException:"+e.getMessage()));
        }
        
        
       output.output("AuctionServer created..,", 2);
    }
    
    public void run()
    {
        output.output("AuctionServerThread started..,", 2);
        String line=null;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while(!Thread.currentThread().isInterrupted())
        {
            try {
                while((line=in.readLine())!=null)
                {
                    
                    Thread.currentThread().interrupt();
                    break;
                }
            } catch (IOException e) {
                this.output.output("AuctionServerThread:IOException:"+e.getMessage());
            }
        }  
        output.output("AuctionServerThread finished..,", 2);
  
    }
    
    private void shutdownPool()
    {
     if(!pool.isShutdown())
        pool.shutdown(); // Disable new tasks from being submitted
    try {
     // Wait a while for existing tasks to terminate
     if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
       pool.shutdownNow(); // Cancel currently executing tasks
       // Wait a while for tasks to respond to being cancelled
      
     }
        } catch (InterruptedException ie) {
     // (Re-)Cancel if current thread also interrupted
     pool.shutdownNow();
     // Preserve interrupt status
     Thread.currentThread().interrupt();
        } 
    }
    
    
    public void close()
    {
       
        this.shutdownPool();
        try {
            this.server.shutdown();
        } catch (ServerException ex) {
            this.output.output("AuctionServerThread:ServerException:"+ex.getMessage());
        }
        this.queue.clear();
        this.ams.close();
        this.output.output("AuctionServer closed...",2);
        
    }
    
    
    private static class ServerSocketHandleThread extends Server.Handler{
       
        private final ExecutorService pool;
        private LinkedBlockingQueue<CommandTask> queue=null;
        private Log output=null;
        
       public ServerSocketHandleThread(LinkedBlockingQueue<CommandTask> queue,ExecutorService pool,Log output)
       {
           this.pool=pool;
           this.queue=queue;
           this.output=output;
       }
       
       
       public void run()
       {
           AuctionTCPReadHandler handler = new AuctionTCPReadHandler(this.queue,
                   this.getClient(),this.output);
           this.pool.execute(handler);
       
       }

    }
    
    
    
    
    
}
