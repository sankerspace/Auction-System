/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LoadTest;

import auctionmanagement.Auction;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dave
 */
public  class ParseClientInput {

    /*
     * Parse Methods
     */
    public static boolean parseLogin(String message) {
        if (message.contains("Succesfully logged in")) {
            return true;
        }
        return false;
    }

    public static boolean parseLogout(String message) {
        if (message.contains("Succesfully logged out")) {
            return true;
        }
        return false;
    }

    public static boolean parseCreate(String message) {
        if (message.contains("has been created")) {
            return true;
        }
        return false;
    }

    public static boolean parseBid(String message) {
        if (message.contains("You succesfully bid")) {
            return true;
        }
        return false;
    }

    
    /*
     * Example: <ungeordnet aich möglich>
     *  15. 'zweite auktion der welt' marko 07.12.2012 02:16:03 0.0 none
     *  14. 'erste auktion der welt' marko 07.12.2012 02:14:11 0.0 none 
     *                             ||    |          |        |   |    | 
 splitted2[0]       splitted2[1]            splitted2[2]  
 *                       splitted3[0]
 *                             splitted3[1]
 *                                        splitted3[2]
 *                                                splitted3[3]
 *                                                     splitted3[4]
 *                                                          splitted3[5]
     */
    public  static  HashMap<Long, Auction> parseList(String message, String clientOwner) {
        if(message.isEmpty())
            return null;
        
        
        if (message.contains(".") && message.contains("'")) {
            
            String[] listItems = message.split("\n");
            HashMap<Long, Auction> list = new HashMap<Long, Auction>();
            for (String s : listItems) {
                try {
                    //String[] splitted = s.split(" ");
                    String[] splitted2 = s.split("\'");
                    String[] splitted3=splitted2[2].split(" ");
                    String owner = splitted3[1];
                    if (!owner.equals(clientOwner)) {
                        String highestBidder = splitted3[5];
                        double highestBid = Double.parseDouble(splitted3[4]);
                        long id = Long.parseLong(splitted2[0].replace(". ", ""));
                        //String description = s.substring(s.indexOf('\'', 0),s.lastIndexOf('\''));
                        String description=splitted2[1];
                       // int positionbeforeDate=(s.indexOf(splitted[2]))+(splitted[2].length()+1);
                       // int positionafterDate=(s.indexOf(splitted[splitted.length-3])+splitted[splitted.length-3].length());
                        // extract date
                        Date date=null;
                       //String Date_=s.substring(positionbeforeDate, positionafterDate);
                        String Date_= splitted3[2]+" "+ splitted3[3];
                        DateFormat df = DateFormat.getDateTimeInstance( DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.GERMAN );
                        date=df.parse(Date_);
                        long timenow = new Date().getTime();
                        long expires = (date.getTime()-timenow)/1000;
                        if(expires>0)
                        {
                            Auction a = new Auction(owner, expires, description);
                            a.setnewBid(highestBidder, highestBid);
                            list.put(id, a); 
                        }
                    }
                    
                    
                } catch (ParseException ex) {
                    return null;
                }catch (NumberFormatException ex) {
                    return null;
                }
               
                
                
            }
            return list;
        }
        return null;
    }
}
