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
import communication.OperationSecure;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import security.RSAAuthenticationException;



/**
 *
 * @author sanker
 */
public class AuctionServer implements Runnable{
    private AuctionManagementSystem ams=null;
    private Server server=null;
    private final ExecutorService pool;
    private LinkedBlockingQueue<CommandTask> queue=null;//communication to AMS
    private Log output=null;
    //Alle Server.Handler schreiben auf die queue, nur das ActionmanagementSystem darf
    //von der queue lesen [blockierend]
    
    
    public AuctionServer(int tcpPort,String analytic, String billing,
            String ServerPrivateKeyFilename,String ClientKeyDirectoryname,
            String ServerKeyDirectoryname,Log output)throws AuctionServerException
    {
        try {
           this.output=output;
           OperationSecure op=new OperationSecure(
                   ServerPrivateKeyFilename,
                   ClientKeyDirectoryname,
                   ServerKeyDirectoryname);
           //communication between ServerSocketHandleThread and AMSHandlerThread
           queue = new LinkedBlockingQueue<CommandTask>();
           pool = Executors.newCachedThreadPool();
           ams= new AuctionManagementSystem(analytic, billing, queue,pool,output);
           output.output("AuctionServer Port:"+tcpPort+"", 3);
           Server.Handler serversocketHandle=new ServerSocketHandleThread(queue,
                   pool,op,output);
           pool.execute(ams);
        
           server=new Server(tcpPort,serversocketHandle,pool,output);
           pool.execute(server);
        } catch (ServerException e) {
            throw (new AuctionServerException("ServerException:"+e.getMessage()));
        }catch (OperationException e) {
            throw (new AuctionServerException("OperationException:"+e.getMessage()));
        }catch (RSAAuthenticationException e) {
            throw (new AuctionServerException("RSAAuthenticationException:"+e.getMessage()));
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
                output.output("\n:");
                while((line=in.readLine())!=null)
                {
                    //this.queue.offer(Commandtask); nur für !closeconnection
                    Thread.currentThread().interrupt();
                    //server.shutdown();
                    //nach paar sekunden
                    /*
                    Server.Handler serversocketHandle=new ServerSocketHandleThread(queue,
                        pool,ServerPrivateKeyFilename,
                        ClientKeyDirectoryname,ServerKeyDirectoryname,output);
      
                    try {
                        server=new Server(tcpPort,serversocketHandle,pool,output);
                        pool.execute(server);
                    } catch (ServerException e) {
                        throw (new AuctionServerException("ServerException:"+e.getMessage()));
                    }* */
                                
                    break;
                  
                }//while
                } catch (IOException e) {
                            
                    this.output.output("AuctionServerThread:IOException:"+e.getMessage()); 
                        
                }
           
        } //while(!Thread.
                
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
        private OperationSecure op=null;
        
       public ServerSocketHandleThread(LinkedBlockingQueue<CommandTask> queue,
               ExecutorService pool,
               OperationSecure op,
               Log output)
       {
           this.pool=pool;
           this.queue=queue;
           this.output=output;
           this.op=op;
       }
       
       
       public void run()
       {
           AuctionTCPReadHandler handler = new AuctionTCPReadHandler(
                    this.queue,
                    this.getClient(),
                    op,
                    this.output);
           this.pool.execute(handler);
       
       }

    }
    
    
    
    
    
}
