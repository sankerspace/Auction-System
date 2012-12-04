/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package auctionmanagement;

import communication.Client;

/**
 *
 * @author sanker
 */
public class Answer {
    
    //private String user=null;
    private Client client=null;
    private String message=null;
    
    public Answer(String message,Client client)
    {
        this.client=client;
        this.message=message;
    
    }
    
    public Client getClient()
    {
        return this.client;    
    }
    
    public String getMessage()
    {
        return this.message;
    }
    
}
