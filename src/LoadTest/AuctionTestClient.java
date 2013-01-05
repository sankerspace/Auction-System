/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LoadTest;

import MyLogger.Log;
import auctionmanagement.Auction;
import communication.Client;
import communication.ClientException;
import communication.OperationTCP;
import communication.OperationException;
import java.util.HashMap;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sanker
 */
public class AuctionTestClient implements Runnable {

    private int id;
    private final ExecutorService pool;
    private AuctionTCPHandlerForAuctiontest handleTCP = null;
    private Client clientTCP = null;
    private Log logger = null;
    private AuctionTest.Properties properties;
    
    private LinkedBlockingQueue<String> messaging = null;
    
    private HashMap<Long, Auction> activeForeignAuctions=null;
    private ReentrantLock lockforactiveForeignAuctions=null;

    private AuctionTestClient.WaitingRoom waiting=null;
    
    
    private Timer timer=null;
   

    public AuctionTestClient(int id, String host,
            int tcpPort,
            AuctionTest.Properties properties,
            ExecutorService pool,
            Log logger) throws AuctionTestClientException {

        try {
            logger.output("Client "+id+" inizialize.",3);
            //Initializations
            this.id = id;
            this.pool = pool;
            this.properties=properties;
            this.logger=logger;
            this.clientTCP = new Client(host, tcpPort);
            this.messaging = new LinkedBlockingQueue<String>();
            this.handleTCP = new AuctionTCPHandlerForAuctiontest(this.id, this.clientTCP, this.messaging, logger);
            
            this.activeForeignAuctions=new HashMap<Long, Auction>();
            this.lockforactiveForeignAuctions=new ReentrantLock();
            
            waiting=new AuctionTestClient.WaitingRoom(logger);
            
            
            
            pool.execute(handleTCP);
            logger.output("Client "+id+" is starting his AuctionTCPHandlerForAuctiontest.",3);
        } catch (ClientException e) {
            throw (new AuctionTestClientException("id:" + id + ":ClientException:", e));
        } catch (Exception e) {
            throw (new AuctionTestClientException("id:" + id + ":Exception:", e));
        }
    }
    
    public AuctionTestClient.WaitingRoom getWaitingRoom()
    {
        return this.waiting; 
    }
    

    public void run() {
        try {
            logger.output("Client "+id+" is running.",3);
            timer = new Timer(true);
            AuctionTestTimerTasksforLoadTest.PerMinuteTask minuteTask=null;
            AuctionTestTimerTasksforLoadTest.UpdateListTask updateTask=null;
            
            minuteTask = new AuctionTestTimerTasksforLoadTest.PerMinuteTask(
                    clientTCP,  
                    messaging,
                    ("client"+id),
                    properties,
                    activeForeignAuctions,                         
                    lockforactiveForeignAuctions,
                    waiting,
                    logger       
                    );
            updateTask=new AuctionTestTimerTasksforLoadTest.UpdateListTask(
                    clientTCP,
                    messaging,
                    ("client"+id),
                    activeForeignAuctions,
                    lockforactiveForeignAuctions,//for mutual exclusion   
                    waiting,
                    logger
                    );
            /**********************Login*******************/
            //start registration of the client at the AuctionServer
            OperationTCP op = new OperationTCP(this.clientTCP);
            //udpPort will be set to a fake
            op.writeString("!login" +" "+("client"+id)+" "+0);//udpPort=0
            if(!ParseClientInput.parseLogin(this.messaging.take()))
                throw new Exception("Cannot login to AuctionServer.");
            logger.output("Client "+id+" logged succesfully to the AuctionServer.",2);
            
            
            //START TIMER
            timer.scheduleAtFixedRate(updateTask, 0, properties.updateIntervalSec*1000);
            timer.scheduleAtFixedRate(minuteTask, 0, 60000);
          
            logger.output("Client "+id+" registered all schedules.",3);
            //Thread waites for notification from outside
            logger.output("Client "+id+" is wating to be closed.",3);
            /**********************waiting*******************/
            
            this.waiting.enterWaitingRoom();            
           
            /**********************Logout*******************/
            op.writeString("!logout"+" "+"client"+id);
            if(!ParseClientInput.parseLogout(this.messaging.take()))
                throw new Exception("Logout of client unsuccesfully.");
            
            
            logger.output("Client "+id+" is closing all his ressources an will end.",3);
            this.close();
        } //run
        catch (InterruptedException ex) {
            logger.output("Client "+id+":InterruptedException:"+ex.getMessage(),2);
        }catch (OperationException ex) {
            logger.output("Client "+id+":OperationException:"+ex.getMessage());
        }catch (Exception ex) {
            logger.output("Client "+id+":Exception:"+ex.getMessage());
        }finally        
        {
            close();
        }
        
    }//run

    public void close() {

        try {
            if(timer!=null)
            {
                this.timer.cancel();
                logger.output("Timer of Client "+id+" is canceled.",3);
            }
            if(clientTCP!=null)
            {
                this.clientTCP.closeSocket();
                logger.output("Closed socket of Client "+id+".",3);
            }
            
            
        } catch (ClientException e) {
            this.logger.output("ActionClientThread:close():" + e.getMessage());
        }

    }

 
    public class AuctionTCPHandlerForAuctiontest implements Runnable {

        private Log out = null;
        private Client client = null;
        private int id;
        private LinkedBlockingQueue<String> messaging = null;

        public AuctionTCPHandlerForAuctiontest(int id, Client client, LinkedBlockingQueue<String> messaging, Log out) {
            this.out = out;
            this.client = client;
            this.id = id;
            this.messaging = messaging;
            out.output("Constructor:Create AuctionClientTCPHandler " + id, 3);
        }

        public void run() {
            String msg = null;
            OperationTCP op = null;
            try {
                op = new OperationTCP(this.client);
            } catch (OperationException ex) {
                out.output("AuctionClientTCPHandlerThread " + id + ": OperationException");
                Thread.currentThread().interrupt();
            }
            out.output("AuctionClientTCPHandlerThread is running..", 3);
            while (!Thread.currentThread().isInterrupted()) {
               
                try {


                    msg = op.readString();
                    messaging.add(msg);

                } catch (OperationException ex) {
                    out.output("AuctionClientTCPHandlerThread " + id + ": OperationException", 2);
                    Thread.currentThread().interrupt();

                }
            }
            out.output("AuctionClientTCPHandlerThread finished " + id, 3);

        }
    }
    
    /*
     * A Thread can enter the waiting Room and will be suspended.
     * Only one Thread can enter the room.
     * Every other Thread can remove a thread from this room.
     * The suspended Thread is going in active mode.
     */
    public  class WaitingRoom
    {
        private boolean iswaiting;
        private Log logger=null;
        
        public WaitingRoom(Log logger)
        {
            this.iswaiting=false;
            this.logger=logger;
        }
        
        public synchronized void enterWaitingRoom()
        {
            if(!this.iswaiting)
                this.iswaiting=true;
            logger.output("AuctionTestClient:WaitingRoom:Client "+id+" enters waiting room.", 2);
            try {
                this.wait();
            } catch (InterruptedException ex) {
               
            }
            logger.output("AuctionTestClient:WaitingRoom:Client "+id+" exits the waiting room.", 2);
        }
        
        
        public synchronized void callingfromWaitingRoom()
        {
            if(this.iswaiting)
            {
                this.iswaiting=false;
                this.notify();
                logger.output("AuctionTestClient:WaitingRoom:Client "+id+" will be removed from Waiting Room.", 2);
            }
        }
        
        
    }
}
