/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package auctionmanagement;


import communication.ClientUDP;

/**
 *
 * @author sanker
 */
public class Notification {
    
    //private String user=null;
    private ClientUDP clientUDP=null;
    private String message=null;
    
    public Notification(String message,ClientUDP client)
    {
        this.clientUDP=client;
        this.message=message;
    
    }
    
    public ClientUDP getClient()
    {
        return this.clientUDP;    
    }
    
    public String getMessage()
    {
        return this.message;
    }
    
}

