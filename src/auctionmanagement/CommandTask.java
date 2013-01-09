package auctionmanagement;

import communication.Client;

/**
 *
 * @author sanker
 */
public class CommandTask {

    private StringBuffer StrRepr = null;
    List list = null;
    Login login = null;
    Create create = null;
    Logout logout = null;
    Bid bid = null;
    End end = null;
    Dummy dummy = null;
    ClientList clientList = null;
    CloseConnection closeConnection = null;
    GroupBid groupbid=null;
    ConfirmBid confirmbid=null;

    public CommandTask(List list) {
        this.list = list;
        StrRepr = new StringBuffer(this.list.toString());
    }
    
    public CommandTask(GroupBid groupbid) {
        this.groupbid = groupbid;
        StrRepr = new StringBuffer(this.groupbid.toString());
    }
     public CommandTask(ConfirmBid confirmbid) {
        this.confirmbid = confirmbid;
        StrRepr = new StringBuffer(this.confirmbid.toString());
    }
    public CommandTask(ClientList clientList) {
        this.clientList = clientList;
        StrRepr = new StringBuffer(this.clientList.toString());
    }

    public CommandTask(Login login) {
        this.login = login;
        StrRepr = new StringBuffer(this.login.toString());
    }

    public CommandTask(Create create) {
        this.create = create;
        StrRepr = new StringBuffer(this.create.toString());
    }

    public CommandTask(Logout logout) {
        this.logout = logout;
        StrRepr = new StringBuffer(this.logout.toString());
    }

    public CommandTask(Bid bid) {
        this.bid = bid;
        StrRepr = new StringBuffer(this.bid.toString());
    }

    public CommandTask(End end) {
        this.end = end;
        StrRepr = new StringBuffer(this.end.toString());
    }

    public CommandTask(Dummy dummy) {
        this.dummy = dummy;
        StrRepr = new StringBuffer(this.dummy.toString());
    }

    public CommandTask(CloseConnection closeConnection) {
        this.closeConnection = closeConnection;
        StrRepr = new StringBuffer(this.closeConnection.toString());
    }

    public String toString() {
        return StrRepr.toString();
    }

    public static class ClientList {

        Client client = null;
        StringBuffer StrRepr = null;
        String name = null;

        public ClientList(Client client, String name) {
            this.client = client;
            this.name = name;
            StrRepr = new StringBuffer("ClientList:client:host" + this.client.getDestinationHost()
                    + "\nClientList:client:port:" + this.client.getDestinationPort()
                    + "\nClientList:client:name:" + this.name);
        }

        public String toString() {
            return StrRepr.toString();
        }
    }

    public static class List {

        Client client = null;
        StringBuffer StrRepr = null;

        public List(Client client) {
            this.client = client;
            StrRepr = new StringBuffer("List:client:host" + this.client.getDestinationHost()
                    + "\nList:client:port:" + this.client.getDestinationPort());

        }

        public String toString() {
            return StrRepr.toString();
        }
    }

    public static class Login {

        StringBuffer StrRepr = null;
        Client client = null;
        String user = null;
        int udpPort;

        public Login(Client client, String user, int udpPort) {
            this.client = client;
            this.user = user;
            this.udpPort = udpPort;

            StrRepr = new StringBuffer("Login:client:host" + this.client.getDestinationHost()
                    + "\nLogin:client:port:" + this.client.getDestinationPort());

            StrRepr.append("\nLogin:user:" + this.user);
            StrRepr.append("\nLogin:udpPort:" + this.udpPort);

        }

        public String toString() {
            return StrRepr.toString();
        }
    }

    public static class Create {

        StringBuffer StrRepr = null;
        Client client = null;
        String user = null;
        long expire;
        String description;

        public Create(Client client, String user, long expire, String description) {
            this.client = client;
            this.description = description;
            this.expire = expire;
            this.user = user;

            StrRepr = new StringBuffer("Create:client:host" + this.client.getDestinationHost()
                    + "\n" + "Create:client:port:" + this.client.getDestinationPort());
            StrRepr.append("\n" + "Create:user;" + this.description);
            StrRepr.append("\n" + "Create:description:" + this.description);
            StrRepr.append("\n" + "Create:user:" + this.user);
        }

        public String toString() {
            return StrRepr.toString();
        }
    }

    public static class Logout {

        StringBuffer StrRepr = null;
        Client client = null;
        String user;

        public Logout(Client client, String user) {
            this.client = client;
            this.user = user;

            StrRepr = new StringBuffer("Logout:client:host" + this.client.getDestinationHost()
                    + "\n" + "Logout:client:port:" + this.client.getDestinationPort());
            StrRepr.append("\n" + "Logout:user;" + this.user);

        }

        public String toString() {
            return StrRepr.toString();
        }
    }

    public static class Bid {

        StringBuffer StrRepr = null;
        Client client = null;
        String user = null;
        String firstconfirmer=null;
        String secondconfirmer=null;
        int id;
        double amount;
        boolean isGroupBid;

        public Bid(Client client, String user, int id, double amount) {
            this.client = client;
            this.amount = amount;
            this.id = id;
            this.user = user;
            this.isGroupBid=false;

            StrRepr = new StringBuffer("Bid:client:host" + this.client.getDestinationHost()
                    + "\n" + "Bid:client:port:" + this.client.getDestinationPort());
            StrRepr.append("\n" + "Bid:NormalBid:");
            StrRepr.append("\n" + "Bid:user:" + this.user);
            StrRepr.append("\n" + "Bid:id:" + this.id);
            StrRepr.append("\n" + "Bid:amount:" + this.amount);
        }
        
         public Bid(Client client, String user,String firstconfirm,String secondconfirm, int id, double amount) {
            this.client = client;
            this.amount = amount;
            this.id = id;
            this.user = user ;
            this.isGroupBid=true;
            this.firstconfirmer=firstconfirm;
            this.secondconfirmer=secondconfirm;
            
            StrRepr = new StringBuffer("Bid:client:host" + this.client.getDestinationHost()
                    + "\n" + "Bid:client:port:" + this.client.getDestinationPort());
            StrRepr.append("\n" + "Bid:GroupBid:" + this.user);
            StrRepr.append("\n" + "Bid:user;" + this.user);
            StrRepr.append("\n" + "Bid:FistConfirmer;" + this.firstconfirmer);
             StrRepr.append("\n" + "Bid:SecondConfirmer;" + this.secondconfirmer);
            StrRepr.append("\n" + "Bid:id:" + this.id);
            StrRepr.append("\n" + "Bid:amount:" + this.amount);
        }

        
        public String toString() {
            return StrRepr.toString();
        }
    }

    public static class GroupBid {

        StringBuffer StrRepr = null;
        Client client = null;
        String user = null;
        int id;
        double amount;

        public GroupBid(Client client, String user, int id, double amount) {
            this.client = client;
            this.amount = amount;
            this.id = id;
            this.user = user;

            StrRepr = new StringBuffer("GroupBid:client:host" + this.client.getDestinationHost()
                    + "\n" + "GroupBid:client:port:" + this.client.getDestinationPort());
            StrRepr.append("\n" + "GroupBid:user;" + this.user);
            StrRepr.append("\n" + "GroupBid:id:" + this.id);
            StrRepr.append("\n" + "GroupBid:amount:" + this.amount);
        }

        public String toString() {
            return StrRepr.toString();
        }
    }

    public static class ConfirmBid {

        StringBuffer StrRepr = null;
        Client client = null;
        String user = null;
        int id;
        double amount;

        public ConfirmBid(Client client, String user, int id, double amount) {
            this.client = client;
            this.amount = amount;
            this.id = id;
            this.user = user;

            StrRepr = new StringBuffer("ConfirmBid:client:host" + this.client.getDestinationHost()
                    + "\n" + "ConfirmBid:client:port:" + this.client.getDestinationPort());
            StrRepr.append("\n" + "ConfirmBid:user;" + this.user);
            StrRepr.append("\n" + "ConfirmBid:id:" + this.id);
            StrRepr.append("\n" + "ConfirmBid:amount:" + this.amount);
        }

        public String toString() {
            return StrRepr.toString();
        }
    }

    
    public static class End {

        Client client = null;
        StringBuffer StrRepr = null;

        public End(Client client) {
            this.client = client;

            StrRepr = new StringBuffer("End:client:host" + this.client.getDestinationHost()
                    + "\n" + "End:client:port:" + this.client.getDestinationPort());

        }

        public String toString() {
            return StrRepr.toString();
        }
    }

    public static class Dummy {

        Client client = null;
        StringBuffer StrRepr = null;

        public Dummy(Client client) {
            this.client = client;

            StrRepr = new StringBuffer("Dummy:client:host" + this.client.getDestinationHost()
                    + "\n" + "Dummy:client:port:" + this.client.getDestinationPort());

        }

        public String toString() {
            return StrRepr.toString();
        }
    }
    
    
    public static class CloseConnection {

        StringBuffer StrRepr = null;

        public CloseConnection() {
            StrRepr = new StringBuffer("CloseConnection");
        }

        public String toString() {
            return StrRepr.toString();
        }
    }
}
