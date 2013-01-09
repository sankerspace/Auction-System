/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import java.lang.Thread;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.io.OutputStream;
import java.io.PrintWriter;
import MyLogger.Log;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author sanker
 */
public class Server implements Runnable{
    
    private final ServerSocket serversocket;
    private final ExecutorService pool;
    private Server.Handler handler;
    private Log errorlog;
    
    
    public Server(int port,Server.Handler Handler,Log error)throws ServerException
    {
        try{
            serversocket = new ServerSocket(port);
            pool = Executors.newCachedThreadPool();
            handler=Handler;
            errorlog=error;
             errorlog.output("TCP Server created....", 2);
        }catch(IOException e){
            throw new  ServerException("IOException::",e);
        } 
    }
    
     public Server(int port,Server.Handler Handler,ExecutorService exservice,Log error)throws ServerException
    {
        try{
            serversocket = new ServerSocket(port);
            pool = exservice;
            handler=Handler;
            errorlog=error;
            errorlog.output("TCP Server created....", 2);
        }catch(IOException e){
            throw new  ServerException("IOException::",e);
        } 
    }
    
    
    public void shutdown()throws ServerException
    {
        try {
            if(!pool.isShutdown())
                pool.shutdown();
            if(!serversocket.isClosed())
                serversocket.close();
             errorlog.output("TCP Server closed....", 2);
        } catch (IOException e) {
            throw new  ServerException("IOException::",e);
        }
    
    }
   
    public void shutdownServerThread()
    {
        try {
            Thread.currentThread().interrupt();
            if(!serversocket.isClosed())
                serversocket.close();
        } catch (IOException ex) {
            
        }
    }
    
    
        
   
    
    public void run()
    {
         errorlog.output("TCPListenServerThread  started....", 2);
         while(!(Thread.currentThread().isInterrupted()))
         {
              
            try{
                if(this.handler==null)
                    throw (new ServerException("Handler not initialized."));
                    
                this.handler.setClient(serversocket.accept());
                pool.execute(this.handler);
            }catch(IOException e)
            {   Thread.currentThread().interrupt();
                this.errorlog.output("Server error:"+e.getMessage());
            }catch(ServerException e)
            {
                this.errorlog.output("Server error:"+e.getMessage());
                Thread.currentThread().interrupt();
            }
         
         }
         /*
        try {
            this.shutdown();
            errorlog.output("SocketServer is closed.");
        } catch (ServerException e) {
            this.errorlog.output("Server error:"+e.getMessage());
            errorlog.output("SocketServer is closed.");
        }
         */
      
    }    
    //A user defined Handler must inherit this class and must
    //override the run method
    //
     static public class Handler implements Runnable
    {
        private  Client client;
        
        public Handler()
        {
            //client must be initialized with the socket 
            //from established connection
            this.client=null;
            
        }
       
        
        @Override
        public void run()
        {
                        
        }
        
        protected void setClient(Socket sock)
        {
           
            this.client=new Client(sock);
            
        }
        
        protected void setClient(Client client)
        {
           
            this.client=client;
            
        }
        
        public Client getClient()
        {
            return this.client;
        }
        
        
      
    }
    
    
    
}


