/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LoadTest;

import auctionmanagement.Auction;
import java.util.HashMap;

/**
 *
 * @author Dave
 */
public class ParseClientInput {

    /*
     * Parse Methods
     */
    public boolean parseLogin(String message) {
        if (message.contains("Succesfully logged in")) {
            return true;
        }
        return false;
    }

    public boolean parseLogout(String message) {
        if (message.contains("Succesfully logged out")) {
            return true;
        }
        return false;
    }

    public boolean parseCreate(String message) {
        if (message.contains("has been created")) {
            return true;
        }
        return false;
    }

    public boolean parseBid(String message) {
        if (message.contains("You succesfully bid")) {
            return true;
        }
        return false;
    }

    // 0. 'description' Owner 22.3.2012 667.8 HighesBidder
    public HashMap<Long, Auction> parseList(String message, String clientOwner) {
        if (message.contains(".") && message.contains("'")) {
            System.out.println("DEBUG: Insided parseList");
            String[] listItems = message.split("\n");
            HashMap<Long, Auction> list = new HashMap<Long, Auction>();
            for (String s : listItems) {
                System.out.println("DEBUG: Insided loop");
                String[] splitted = s.split(" ");
                System.out.println("DEBUG: List Item contains: " + splitted[0] + " : " + splitted[1] + " : " + splitted[2] + " : " + splitted[3] + " : " + splitted[4] + " : " + splitted[5] + " : ");
                String owner = splitted[2];
                if (!owner.equals(clientOwner)) {
                    System.out.println("DEBUG: Owner of auction is not clientOwner.");
                    Auction a = new Auction(owner, Long.parseLong(splitted[3]), splitted[2]);
                    list.put(Long.parseLong(splitted[0].replace(".", "")), a); //Hier nimmt er sich die Long id aus dem String (zB 0. und macht draus eine 0)
                    System.out.println("DEBUG: Put auction with ID " + a.getID() + " into list success!");
                }
            }
            return list;
        }
        return null;
    }
}
