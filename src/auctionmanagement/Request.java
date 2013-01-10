package auctionmanagement;

import communication.Client;

/**
 *
 * @author sanker
 */
public class Request {

    private String user = "none";
    private String user_2 = "none";
    private String command = null;
    private Parameter parameter = null;
    private int udpPort;
    private Client client = null;

    public Request(String message) throws RequestException {

        CheckRequest check = new CheckRequest(message, true);
        if (!check.getStatus()) {
            throw (new RequestException("Message is invalid."));
        }
        command = check.getCommand();
        String error = null;
        // if(!checkUserStatus(error))throw (new RequestException(error));
        parameter = check.getParam();
        if (parameter != null) {
            if (parameter.loginuser != null) {
                user = parameter.loginuser;
            }
            
            if (parameter.loginUdpPort != -1) {
                udpPort = parameter.loginUdpPort;
            } else {
                udpPort = -1;
            }
        }
    }

    public Request(String message, String user) throws RequestException {

        CheckRequest check = new CheckRequest(message, true);
        if (!check.getStatus()) {
            throw (new RequestException("Message is invalid."));
        }
        command = check.getCommand();
        //if(command.contains("!login"))throw (new RequestException("Already logged in."));
        String error = null;
        //if(!checkUserStatus(error))throw (new RequestException(error));
        this.user = user;
        parameter = check.getParam();
        if (parameter != null) {
            if (parameter.loginUdpPort != -1) {
                udpPort = parameter.loginUdpPort;
            } else {
                udpPort = -1;
            }
        }
    }

    public Request(Client client, String message) throws RequestException {
        CheckRequest check = new CheckRequest(message, false);
        if (!check.getStatus()) {
            throw (new RequestException("Message is invalid."));
        }
        this.client = client;
        this.command = check.getCommand();
        this.parameter = check.getParam();
        if (parameter != null) {
            if (parameter.loginUdpPort != -1) {
                udpPort = parameter.loginUdpPort;
            } else {
                udpPort = -1;
            }
            if (parameter.loginuser != null) {
                this.user = parameter.loginuser;
            }
        }
    }
    /* 
     private boolean checkUserStatus(String error) 
     {
     if(user.equals("none"))
     {
     if((command.contains("!list") || command.contains("!login")|| command.contains("!end")))
     {
     return true;
                
     }else 
     error=new String("You have to login first!");
     }else if(command.contains("!end"))
     {
     error=new String("You have to logout first!");
     }else
     return true;
        
     return false;
     }*/

    public String createRequestStringforServer() {
        if (this.command.contains("!list")) {
            return new String("!list");
        } else if (this.command.contains("!login")) {
            if (!(user.contains("none"))) {
                if (this.udpPort != -1) {
                    return new String("!login" + " " + user + " " + udpPort);
                }
            }

        } else if (this.command.contains("!create")) {
            return new String("!create" + " " + this.user + " " + parameter.createTime + " " + parameter.createDesc);
        } else if (this.command.contains("!logout")) {
            return new String("!logout" + " " + this.user);
        } else if (this.command.contains("!bid")) {
            return new String("!bid" + " " + this.user + " " + parameter.bidId + " " + parameter.bidValue);
        } else if (this.command.contains("!end")) {
            return new String("!end");
        } else if (this.command.contains("!getClientList")) {
            return new String("!getClientList" + " " + this.user);
        } else if (this.command.contains("!groupBid")) {
            return new String("!groupBid" + " " + this.user + " " + parameter.bidId + " " + parameter.bidValue);
        } else if (this.command.contains("!confirm")) {
            return new String("!confirm" + " " + this.user + " " + parameter.bidId + " " + parameter.bidValue);
        } else if (this.command.contains("!signedBid")) {
            return new String("!signedBid" + " " + this.user + " " + parameter.bidId + " " + parameter.bidValue + 
                    " " + parameter.user_1 + ":" + parameter.timestamp_1 + ":" + parameter.signature_1 + 
                    " " + parameter.user_2 + ":" + parameter.timestamp_2 + ":" + parameter.signature_2);
        }
        return null;
    }

    public void setUdpPort(int port) {
        this.udpPort = port;
    }

    public String getUserName() {
        return this.user;
    }

    public String getCommandName() {
        return this.command;
    }

    public Request.Parameter getParameter() {
        return this.parameter;
    }

    public Client getClient() {
        return this.client;
    }

    public int getUDPPort() {
        return this.udpPort;
    }

    public static class Parameter {
        public String command = null;
        public long createTime = -1;
        public String createDesc = null;
        public double bidValue = 0;
        public int bidId = -1;
        public String loginuser = null;
        public int loginUdpPort = -1;
        public String user_1 = null;
        public String user_2 = null;
        public String timestamp_1 = null;
        public String signature_1 = null;
        public String timestamp_2 = null;
        public String signature_2 = null;
    }
}
