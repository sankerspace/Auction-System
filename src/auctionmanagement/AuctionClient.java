package auctionmanagement;

import MyLogger.Log;
import communication.Client;
import communication.ClientException;

import communication.OperationTCP;

import communication.ServerUDP;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;

import MyLogger.Log;
import auctionmanagement.CheckRequest.checkAuctionAnswer;
import communication.Operation;
import communication.OperationException;
import communication.OperationSecure;
import java.util.concurrent.Executors;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sanker
 */
public class AuctionClient {

    private final ExecutorService pool;
    private ServerUDP serverUDP = null;
    private int udpPort;
    private AuctionClientUDPHandler handleUDP = null;
    private AuctionClientTCPHandler handleTCP = null;
    private Client clientTCP = null;
    private Log errorlog = null;
    private ClientStatus userstatus = null;

    public AuctionClient(String host, int tcpPort, int udpPort, Log output) throws AuctionClientException {
        this.errorlog = output;
        userstatus = new ClientStatus("none");
        try {

            //this.handleUDP=new AuctionClientUDPHandler(output);
            this.udpPort = udpPort;
            this.clientTCP = new Client(host, tcpPort);
            this.handleTCP = new AuctionClientTCPHandler(this.clientTCP, output);
            //this.serverUDP=new ServerUDP(udpPort,handleUDP,output);
            // this.serverUDP.setErrorLog(output);
            pool = Executors.newCachedThreadPool();
            // pool.execute(serverUDP);
            pool.execute(handleTCP);

            
        } catch (OperationException e) {
            throw (new AuctionClientException(":OperationException:",e));

        } catch (ClientException e) {
            throw (new AuctionClientException(":ClientException:", e));
        }  /* catch (ServerUDPException e) {
         throw (new AuctionClientException(":ServerUDPException:",e));
         } */
    }

    public AuctionClient(String host, int tcpPort, int udpPort, ExecutorService pool, Log output) throws AuctionClientException {
        this.errorlog = output;
        userstatus = new ClientStatus("none");
        try {

            //this.handleUDP=new AuctionClientUDPHandler(output);
            this.udpPort = udpPort;
            this.clientTCP = new Client(host, tcpPort);
            this.handleTCP = new AuctionClientTCPHandler(this.clientTCP, output);
            //this.serverUDP=new ServerUDP(udpPort,handleUDP,output);
            // this.serverUDP.setErrorLog(output);
            this.pool = pool;
            //pool.execute(serverUDP);
            pool.execute(handleTCP);

            
        } catch (OperationException e) {
           throw (new AuctionClientException(":OperationException:",e));

        } catch (ClientException e) {
            throw (new AuctionClientException(":ClientException:", e));
        }/* catch (ServerUDPException e) {
         throw (new AuctionClientException(":ServerUDPException:",e));
         } */
    }

    public void run() {
        this.errorlog.output("AuctionClient is running..", 2);
        Request req = null;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        String line;
        String msg;
        this.errorlog.out(">");
        try {

            OperationTCP op = new OperationTCP(this.clientTCP);
           
            while((line=in.readLine())!=null)
            {
                this.errorlog.output("AuctionClient input:"+line,3);
                try{
                   
                   if(this.userstatus.noUser()) 
                   {
                        if(line.length()<4)continue;

                        //this.errorlog.out(">");
                        req = new Request(line);
                        if (req.getCommandName().contains("!end")) {
                            break;
                        } else if (req.getCommandName().contains("!login")) {
                            this.userstatus.setUser(req.getUserName());
                            req.setUdpPort(this.udpPort);
                        } else if (!req.getCommandName().contains("!list")) {
                            throw (new RequestException("You must be logged in!\n>"));
                        } else if (!req.getCommandName().contains("!getClientList")) {  //TODO Stage4:test this line
                            throw (new RequestException("You must be logged in!\n>"));
                        }
                    } else {
                        if (line.length() < 4) {
                            continue;
                        }
                        // this.errorlog.out(this.userstatus.getUser()+">");
                        req = new Request(line, this.userstatus.getUser());
                        if (req.getCommandName().contains("!end")) {
                            throw (new RequestException("You must be logged out!" + "\n"
                                    + this.userstatus.getUser() + ">"));
                        } else if (req.getCommandName().contains("!logout")) {
                            this.userstatus.resetUser();
                        } else if (req.getCommandName().contains("!login")) {
                            throw (new RequestException("You must log out!" + "\n"
                                    + this.userstatus.getUser() + ">"));
                        }
                    }
                    msg = req.createRequestStringforServer();
                    this.errorlog.output("createRequestStringforServer():" + msg, 3);
                    if (msg != null) {
                        op.writeString(msg);
                    } else {
                        throw (new RequestException("Cannot generate message!"));
                    }

                } catch (RequestException e) {

                    this.errorlog.output(e.getMessage(), 2);

                }
                if (userstatus.noUser()) {
                    errorlog.output(">");
                } else {
                    this.errorlog.output(this.userstatus.getUser() + ">");
                }
                this.errorlog.output("AuctionClient wait for input..", 3);

            }
        } catch (IOException e) {
            this.errorlog.output("AuctionClientThread:" + e.getMessage());
        } catch (OperationException e) {
            this.errorlog.output("AuctionClientThread:" + e.getMessage());
        }
        this.errorlog.output("AuctionClient ended..", 2);
        //this.shutdown();

    }

    public void close() {
        this.shutdownPool();
        try {
            this.clientTCP.closeSocket();
        } catch (ClientException e) {
            this.errorlog.output("ActionClientThread:close():" + e.getMessage());
        }
    }

    private void shutdownPool() {
        if (!pool.isShutdown()) {
            pool.shutdown(); // Disable new tasks from being submitted
        }
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(3, TimeUnit.SECONDS)) {
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

    private class AuctionClientUDPHandler extends ServerUDP.Handler {

        private Log out = null;

        public AuctionClientUDPHandler(Log out) {
            this.out = out;
            this.out.output("Constructor:Create AuctionClientUDPHandler", 2);
        }

        public String checkIncomingMessage(String msg) {
            String newMessage = null;
            newMessage = CheckRequest.checkAuctionAnswer.checkandget(msg,
                    userstatus.getUser());
            if (newMessage == null) {
                return msg;
            } else {
                return newMessage;
            }
            // userstatus
        }

        public void handle(String msg) {
            out.output("AuctionClientUDPHandlerThread running..", 2);
            String checkedmessage = this.checkIncomingMessage(msg);
            out.output(checkedmessage);
            out.output("AuctionClientUDPHandlerThread finished..", 2);
        }
    }

    private class AuctionClientTCPHandler implements Runnable {

        
        private Log out=null;
        private Client client=null;
        boolean switchToSecureChannel=false;
        OperationSecure opSecure=null;
        OperationTCP op=null;
        
        public AuctionClientTCPHandler(Client client,Log out) throws OperationException
        {
                this.out=out;
                this.client=client;
                out.output("Constructor:Create AuctionClientTCPHandler", 2);
                setRegularChannel();
        }
        
        

        public void run()
        {
            String msg=null;

            while(!Thread.currentThread().isInterrupted())
            {
                out.output("AuctionClientTCPHandlerThread is running..", 2);
                try {
                    if(!switchToSecureChannel)
                    {
                        
                        msg = op.readString();
                        if(msg.contains("!dummy"))
                        {
                            while((opSecure==null))
                            { 
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException ex) {
                                   out.output("Wait after dummy messages finished.", 3);
                                }
                            }
                        }else
                            out.output(msg);
                        
                    }else{
                        msg = opSecure.readString();
                        out.output(msg);
                    }


                } catch (OperationException ex) {
                    out.output("AuctionClientTCPHandlerThread: OperationException");
                    Thread.currentThread().interrupt();

                }
            }
            out.output("AuctionClientTCPHandlerThread finished..", 2);

            
        } 
        
        public void setSecureChannel(Operation op)
        {
           this.switchToSecureChannel=true; 
           this.opSecure=new OperationSecure((OperationSecure)op);
           this.op=null;
        }
        
        public void setRegularChannel() throws OperationException
        {
            this.switchToSecureChannel=false;
            this.opSecure=null;
            this.op = new OperationTCP(this.client);
        }

    }

    private class ClientStatus {

        private String user = null;
        private String reset = null;

        public ClientStatus(String name) {
            this.reset = name;
            this.user = name;
        }

        public String getUser() {
            return this.user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public void resetUser() {
            this.user = this.reset;
        }

        public boolean sameUser(String user1, String user2) {
            return user1.contains(user2);
        }

        public boolean noUser() {
            return this.user.contains(this.reset);
        }
    }
}
