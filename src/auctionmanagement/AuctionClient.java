package auctionmanagement;

import MyLogger.Log;
import communication.Client;
import communication.ClientException;
import communication.Operation;
import communication.OperationException;
import communication.OperationSecure;
import communication.OperationSecurewithHmac;
import communication.OperationTCP;
import communication.ServerUDP;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import security.RSAAuthenticationException;

/**
 *
 * @author sanker
 */
public class AuctionClient {

    private final ExecutorService pool;
    private ServerUDP serverUDP = null;
    private int udpPort;
    private boolean isLoginTry = false;
    private String ServerPublicKeyFilename = null;
    private String ClientKeyDirectoryname = null;
    private String ServerKeyDirectoryname = null;
    private AuctionClientUDPHandler handleUDP = null;
    private AuctionClientTCPHandler handleTCP = null;
    private Client clientTCP = null;
    private Log errorlog = null;
    private ClientStatus userstatus = null;
    private Operation operation = null;
    private LinkedList<LightAccount> clientList = null; //Stage4:List of clients, filled by !getClientList command
    private ReentrantLock lockForSendingSocket = null; //for synced communication between AuctionClient and AuctionTCPHandler
    private TimerTask checkServerConnection = null;

    public AuctionClient(final String host, final int tcpPort, int udpPort,
            String ServerPublicKeyFilename,
            String ClientKeyDirectoryname,
            String ServerKeyDirectoryname,
            Log output) throws AuctionClientException {
        this.clientList = new LinkedList<LightAccount>();
        this.errorlog = output;
        userstatus = new ClientStatus("none");
        this.ServerPublicKeyFilename = ServerPublicKeyFilename;
        this.ClientKeyDirectoryname = ClientKeyDirectoryname;
        this.ServerKeyDirectoryname = ServerKeyDirectoryname;
        lockForSendingSocket = new ReentrantLock();
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

            //TimerTask
            checkServerConnection = new TimerTask() {
                @Override
                public void run() {
                    try {
                        clientTCP = new Client(host, tcpPort);
                    } catch (ClientException ex) {
                    }
                }
            };
        } catch (OperationException e) {
            startOutageProcess();
            throw (new AuctionClientException(":OperationException:", e));
        } catch (ClientException e) {
            throw (new AuctionClientException(":ClientException:", e));
        }
    }

    public AuctionClient(final String host, final int tcpPort, int udpPort,
            String ServerPublicKeyFilename,
            String ClientKeyDirectoryname,
            String ServerKeyDirectoryname, ExecutorService pool, Log output) throws AuctionClientException {
        this.clientList = new LinkedList<LightAccount>();
        this.errorlog = output;
        userstatus = new ClientStatus("none");
        try {
            //this.handleUDP=new AuctionClientUDPHandler(output);
            this.udpPort = udpPort;
            this.ServerPublicKeyFilename = ServerPublicKeyFilename;
            this.ClientKeyDirectoryname = ClientKeyDirectoryname;
            this.ServerKeyDirectoryname = ServerKeyDirectoryname;
            this.clientTCP = new Client(host, tcpPort);
            //Übergabe von Lock
            this.handleTCP = new AuctionClientTCPHandler(this.clientTCP, output);
            //this.serverUDP=new ServerUDP(udpPort,handleUDP,output);
            // this.serverUDP.setErrorLog(output);
            this.pool = pool;
            //pool.execute(serverUDP);
            pool.execute(handleTCP);
            //TimerTask
            checkServerConnection = new TimerTask() {
                @Override
                public void run() {
                    try {
                        clientTCP = new Client(host, tcpPort);
                    } catch (ClientException ex) {
                    }
                }
            };
        } catch (OperationException e) {
            startOutageProcess();
            throw (new AuctionClientException(":OperationException:", e));
        } catch (ClientException e) {
            throw (new AuctionClientException(":ClientException:", e));
        }
    }

    public void run() {
        this.errorlog.output("AuctionClient is running..", 2);
        Request req = null;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line;
        String msg;
        this.errorlog.out(">");
        try {
            //without login only regular channel to the server is established
            operation = new OperationTCP(this.clientTCP);

            while ((line = in.readLine()) != null) {
                this.errorlog.output("AuctionClient input:" + line, 3);
                try {
                    if (this.userstatus.noUser()) {
                        if (line.length() < 4) {
                            continue;
                        }

                        //this.errorlog.out(">");
                        req = new Request(line);
                        if (req.getCommandName().contains("!end")) {
                            break;
                        } else if (req.getCommandName().contains("!login")) {
                            this.isLoginTry = true;
                            lockForSendingSocket.lock();
                            operation.writeString("!dummy");
                            lockForSendingSocket.unlock();
                            String user = req.getUserName();
                            operation = new OperationSecure(clientTCP,
                                    user,
                                    (new Integer(this.udpPort)).toString(),
                                    ServerPublicKeyFilename,
                                    this.ServerKeyDirectoryname,
                                    this.ClientKeyDirectoryname);
                            this.handleTCP.setSecureChannel(operation);
                            this.userstatus.setUser(req.getUserName());
                            req = null;
                            //req.setUdpPort(this.udpPort);
                        } else if (req.getCommandName().contains("!getClientList")) {  //TODO Stage4:test this line
                            throw (new RequestException("You must be logged in!\n>"));
                        } else if (!(req.getCommandName().contains("!list"))) {
                            throw (new RequestException("You must be logged in!\n>"));
                        } else if (!(req.getCommandName().contains("!groupBid"))) {
                            throw (new RequestException("You must be logged in!\n>"));
                        } else if (!(req.getCommandName().contains("!confirm"))) {
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
                            lockForSendingSocket.lock();
                            operation.writeString("!dummy");
                            lockForSendingSocket.unlock();
                            this.userstatus.resetUser();
                        } else if (req.getCommandName().contains("!login")) {
                            throw (new RequestException("You must log out!" + "\n"
                                    + this.userstatus.getUser() + ">"));
                        }
                    }
                    //if a request object is avaible, send the created String to the server
                    if (req != null) {
                        msg = req.createRequestStringforServer();
                        this.errorlog.output("createRequestStringforServer():" + msg, 3);
                        if (msg != null) {
                            lockForSendingSocket.lock();
                            operation.writeString(msg);
                            lockForSendingSocket.unlock();
                        } else {
                            throw (new RequestException("Cannot generate message!"));
                        }
                    }

                } catch (RequestException e) {

                    this.errorlog.output(e.getMessage(), 2);

                } catch (RSAAuthenticationException e) {
                    this.errorlog.output("Error:Authentication failed");
                    this.errorlog.output(e.getMessage(), 2);
                } finally {
                    isLoginTry = false;
                    if (lockForSendingSocket.isHeldByCurrentThread()) {
                        lockForSendingSocket.unlock();
                    }
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

    private void startOutageProcess() {
        //1. Start timer, which checks for server aviability
        Timer timer = new Timer();
        timer.schedule(this.checkServerConnection, 0, 5000);

        //2. Create timestamps
        //TODO
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

        private Log out = null;
        private Client client = null;
        boolean switchToSecureChannel = false;
        OperationSecurewithHmac opSecurewithHmac = null;
        OperationTCP op = null;
        boolean isSecondAttemptforListCommand = false;

        public AuctionClientTCPHandler(Client client, Log out) throws OperationException {
            this.out = out;
            this.client = client;
            out.output("Constructor:Create AuctionClientTCPHandler", 2);
            setRegularChannel();
        }

        public void run() {
            String msg = null;
            out.output("AuctionClientTCPHandlerThread is running..", 2);
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if (!switchToSecureChannel) {/*UNSECURE communication*/
                        msg = op.readString();
                        if (msg.contains("!denied")) {
                            out.output("AuctionClientTCPHandlerThread:message:'!denied'", 3);
                            while ((opSecurewithHmac == null) && isLoginTry) {
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException ex) {
                                }
                            }
                            out.output("AuctionClientTCPHandlerThread:OpSecure initialized.", 3);
                        } else if (msg.contains("list")) {
                            msg = (msg.split("%"))[1];
                            out.output(msg);
                        } else {
                            out.output(msg);
                        }

                    } else {
                        /*SECURE communication*/
                        msg = opSecurewithHmac.readString();
                        if (msg.contains("!dummy")) {
                            this.setRegularChannel();
                            this.opSecurewithHmac = null;
                        } else if (msg.contains("list")) {
                            String[] s = msg.split("%");
                            if (!this.opSecurewithHmac.getLastVerificationStatus()) {
                                out.output("Error:Message Integrity of the last received message was corrupted.");
                                if (!isSecondAttemptforListCommand) {
                                    isSecondAttemptforListCommand = true;
                                    lockForSendingSocket.lock();
                                    op.writeString("!list");
                                    lockForSendingSocket.unlock();
                                }
                            }
                            out.output(s[1]);
                        } else if (msg.contains("Active Clients:")) {
                            String clientListString = msg.replace("Active Clients:\n","");
                            
                            String[] splitted = clientListString.split("\n");
                            for(String client : splitted) {
                                String[] sub_1 = client.split(" "); //<Host:port> <-> <Name>
                                String[] sub_2 = sub_1[0].split(":"); //<Host> <port>
                                LightAccount la = new LightAccount();
                                la.host = sub_2[0];
                                la.udpPort = Integer.parseInt(sub_2[1]);
                                la.name = sub_1[2];
                                clientList.add(la);
                            }
                            out.output(msg);
                        }  else {
                            isSecondAttemptforListCommand = false;
                            out.output(msg);
                        }
                    }
                } catch (OperationException ex) {
                    out.output("AuctionClientTCPHandlerThread:OperationException" + ex.getMessage());
                    Thread.currentThread().interrupt();

                } catch (Exception ex) {
                    out.output("AuctionClientTCPHandlerThread:Exception" + ex.getMessage());
                    Thread.currentThread().interrupt();

                } finally {
                    if (lockForSendingSocket.isHeldByCurrentThread()) {
                        lockForSendingSocket.unlock();
                    }
                }
            }
            out.output("AuctionClientTCPHandlerThread finished..", 2);
        }

        public void setSecureChannel(Operation op) throws OperationException {
            this.switchToSecureChannel = true;
            this.opSecurewithHmac = new OperationSecurewithHmac((OperationSecure) op);
            //this.op=null;
        }

        public void setRegularChannel() throws OperationException {
            this.switchToSecureChannel = false;
            //this.opSecure=null;
            this.op = new OperationTCP(this.client);
            isLoginTry = false;
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

    private class LightAccount {
        public String name;
        public String host;
        public int udpPort;
    }
}
