package auctionmanagement;

import communication.Client;
import communication.ClientException;
import communication.ClientSecure;
import communication.ClientUDP;
import communication.ClientUDPException;
import communication.OperationSecure;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sanker
 */
public class Account {

    private String name = null;
    private Queue<String> notifications = null;
    private Client TCPClient = null;
    private OperationSecure operationSecure = null;
    private String client_host = null;
    private int tcpport = -1;
    private int udpport = -1;
    private Date lastloginTimestamp = null;
    LinkedBlockingQueue<Notification> notificationchannel = null;

    public Account(String name, Client client, int udpPort, LinkedBlockingQueue<Notification> notificationchannel) throws AccountException {
        try {
            this.name = name;
            this.notifications = new ConcurrentLinkedQueue<String>();
            this.notificationchannel = notificationchannel;
            this.operationSecure = new OperationSecure((ClientSecure) client);
            try {
                this.activateAccount(client, udpPort);
            } catch (ClientUDPException ex) {
                throw (new AccountException("ClientUDPException:", ex));
            }
        } catch (ClientException ex) {
            throw (new AccountException("ClientException:", ex));
        }
    }

    public String getName() {
        return this.name;
    }

    public Client getClient() {
        return this.TCPClient;
    }

    public ClientUDP getClientUDP() throws ClientUDPException {
        if (this.isOnline()) {
            return new ClientUDP(this.client_host, this.udpport);
        } else {
            return null;
        }
    }

    public boolean isOnline() {
        return !(this.TCPClient == null);
    }

    //timestamp in ms
    public long getLastLoginTime() {
        return this.lastloginTimestamp.getTime();
    }

    public void activateAccount(Client client, int udpPort) throws ClientUDPException {
        this.TCPClient = client;
        this.client_host = client.getDestinationHost();
        this.tcpport = client.getDestinationPort();
        this.udpport = udpPort;
        this.lastloginTimestamp = new Date();
        if (this.hasNotifications()) {

            String[] not = this.consumeAllNotifications();
            for (int i = 0; i < not.length; i++) {
                Notification n = new Notification(not[i], this.getClientUDP());
                this.notificationchannel.offer(n);
            }
        }
    }

    public void activateAccountAfterClose(Client client) throws AccountException {
        try {
            this.TCPClient = client;
            this.client_host = client.getDestinationHost();
            this.tcpport = client.getDestinationPort();
            this.operationSecure.setClient((ClientSecure) client);
            if (this.hasNotifications()) {

                String[] not = this.consumeAllNotifications();
                for (int i = 0; i < not.length; i++) {
                    Notification n = new Notification(not[i], this.getClientUDP());
                    this.notificationchannel.offer(n);
                }
            }
        } catch (ClientUDPException ex) {
            throw (new AccountException("ClientUDPException:", ex));
        } catch (ClientException ex) {
            throw (new AccountException("ClientException:", ex));
        }
    }

    public void deactivateAccount() {
        this.TCPClient = null;
        this.client_host = null;
        this.tcpport = -1;
        this.udpport = -1;
    }

    public void deactivateAccountandCloseConnection() throws ClientException {
        try {
            if (this.TCPClient != null) {
                this.TCPClient.closeSocket();
                this.operationSecure.deleteClient();
            }

        } catch (ClientException ex) {
            throw new ClientException(ex.getMessage());
        } finally {
            this.TCPClient = null;
            this.client_host = null;
            this.tcpport = -1;
            //   this.udpport=-1;
        }
    }

    public boolean hasNotifications() {

        return (!notifications.isEmpty());
    }

    public void addNotification(String msg) {
        notifications.add(msg);

    }

    public String[] consumeAllNotifications() {
        String[] array = null;//new String[](notifications.toArray();
        //for(int k=0;k<i;k++)
        array = notifications.toArray(new String[notifications.size()]);
        notifications.clear();
        return array;
    }

    public int getUdpPort() {
        return udpport;
    }

    public String getHost() {
        return client_host;
    }
}
