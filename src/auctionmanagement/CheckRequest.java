package auctionmanagement;

/**
 *
 * @author sanker
 */
public class CheckRequest {

    private String command = null;
    private Request.Parameter parameter = null;
    boolean StatusRequestCorrectnes;
    String[] tokenizedMessage = null;

    public CheckRequest(String msg, boolean user) {
        if (user) {
            StatusRequestCorrectnes = analyzeandextractTokensfromUser(msg);
        } else {
            StatusRequestCorrectnes = this.extractTokensfromMessageInput(msg);
        }

    }

    public boolean getStatus() {
        return this.StatusRequestCorrectnes;
    }

    public Request.Parameter getParam() {
        return parameter;
    }

    public String getCommand() {
        return this.command;
    }

    private boolean analyzeandextractTokensfromUser(String msg) {
        if (msg.charAt(0) != '!') {
            return false;
        }
        tokenizedMessage = msg.split(" ");

        command = tokenizedMessage[0];
        int num = tokenizedMessage.length;

        if (command.contains("!list")) {
            parameter = null;
            if (num == 1) {
                return true;
            }
        } else if (command.contains("!login")) {

            if (num != 2) {
                return false;
            }
            parameter = new Request.Parameter();
            parameter.loginuser = tokenizedMessage[1];
            return true;

        } else if (command.contains("!create")) {

            if ((tokenizedMessage.length) < 3) {
                return false;
            }
            parameter = new Request.Parameter();

            try {
                parameter.createTime = Long.valueOf(tokenizedMessage[1]).longValue();
                int position = msg.indexOf(tokenizedMessage[2], 0);
                parameter.createDesc = msg.substring(position);
                return true;

            } catch (NumberFormatException e) {
                return false;
            }

        } else if (command.contains("!logout")) {
            if (num != 1) {
                return false;
            }
            parameter = null;
            return true;
        } else if (command.contains("!bid")) {
            if ((tokenizedMessage.length) < 3) {
                return false;
            }
            parameter = new Request.Parameter();
            try {
                int id = Integer.parseInt(tokenizedMessage[1]);
                double amount = Double.parseDouble(tokenizedMessage[2]);
                parameter.bidId = id;
                parameter.bidValue = amount;
            } catch (NumberFormatException e) {
                return false;
            }
            return true;
        } else if (command.contains("!end")) {
            parameter = null;
            return true;
        } else if (command.contains("!getClientList")) {
            parameter = null;
            return true;
        } else if (command.contains("!groupBid")) {
            if ((tokenizedMessage.length) < 3) {
                return false;
            }
            parameter = new Request.Parameter();
            try {
                int id = Integer.parseInt(tokenizedMessage[1]);
                double amount = Double.parseDouble(tokenizedMessage[2]);
                parameter.bidId = id;
                parameter.bidValue = amount;
            } catch (NumberFormatException e) {
                return false;
            }
            return true;
        } else if (command.contains("!confirm")) {
            if ((tokenizedMessage.length) < 3) {
                return false;
            }
            parameter = new Request.Parameter();
            try {
                int id = Integer.parseInt(tokenizedMessage[1]);
                double amount = Double.parseDouble(tokenizedMessage[2]);
                parameter.bidId = id;
                parameter.bidValue = amount;
            } catch (NumberFormatException e) {
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean extractTokensfromMessageInput(String msg) {
        if (msg.charAt(0) != '!') {
            return false;
        }
        tokenizedMessage = msg.split(" ");

        command = tokenizedMessage[0];
        int num = tokenizedMessage.length;

        if (command.contains("!list")) {
            parameter = null;
            return true;
        } else if (command.contains("!login")) {

            if (num != 3) {
                return false;
            }
            parameter = new Request.Parameter();
            parameter.loginuser = tokenizedMessage[1];
            try {
                parameter.loginUdpPort = Integer.parseInt(tokenizedMessage[2]);
            } catch (NumberFormatException e) {
                return false;
            }

            return true;

        } else if (command.contains("!create")) {
            //!create <user> <expireTime> <description> 
            if ((tokenizedMessage.length) < 4) {
                return false;
            }
            parameter = new Request.Parameter();
            parameter.loginuser = tokenizedMessage[1];
            try {
                parameter.createTime = Long.valueOf(tokenizedMessage[2]).longValue();
                if (parameter.createTime < 1) {
                    return false;
                }
                int position = msg.indexOf(tokenizedMessage[3], 0);
                parameter.createDesc = msg.substring(position);

            } catch (NumberFormatException e) {
                return false;
            }
            return true;
        } else if (command.contains("!logout")) {
            if ((tokenizedMessage.length) < 2) {
                return false;
            }
            parameter = new Request.Parameter();
            parameter.loginuser = tokenizedMessage[1];
            return true;
        } else if (command.contains("!bid")) {
            //!bid <user>  <auction-id> <new-bid>
            if ((tokenizedMessage.length) < 4) {
                return false;
            }
            parameter = new Request.Parameter();
            parameter.loginuser = tokenizedMessage[1];
            try {
                int id = Integer.parseInt(tokenizedMessage[2]);
                double amount = Double.parseDouble(tokenizedMessage[3]);
                if (amount < 1) {
                    return false;
                }
                parameter.bidId = id;
                parameter.bidValue = amount;
            } catch (NumberFormatException e) {
                return false;
            }
            return true;
        } else if (command.contains("!end")) {
            parameter = null;
            return true;
        } else if (command.contains("!dummy")) {
            parameter = null;
            return true;
        } else if (command.contains("!getClientList")) {
            parameter = new Request.Parameter();
            if (tokenizedMessage.length != 2) {
                return false;
            }
            parameter.loginuser = tokenizedMessage[1];
            return true;
        } else if (command.contains("!groupBid")) {
            //!grouBid <user> <auction-id> <new-bid>
            if ((tokenizedMessage.length) < 4) {
                return false;
            }
            parameter = new Request.Parameter();
            parameter.loginuser = tokenizedMessage[1];
            try {
                int id = Integer.parseInt(tokenizedMessage[2]);
                double amount = Double.parseDouble(tokenizedMessage[3]);
                if (amount < 1) {
                    return false;
                }
                parameter.bidId = id;
                parameter.bidValue = amount;
            } catch (NumberFormatException e) {
                return false;
            }
            return true;
        } else if (command.contains("!confirm")) {
            //!confirm <user> <auction-id> <new-bid>
            if ((tokenizedMessage.length) < 4) {
                return false;
            }
            parameter = new Request.Parameter();
            parameter.loginuser = tokenizedMessage[1];
            try {
                int id = Integer.parseInt(tokenizedMessage[2]);
                double amount = Double.parseDouble(tokenizedMessage[3]);
                if (amount < 1) {
                    return false;
                }
                parameter.bidId = id;
                parameter.bidValue = amount;
            } catch (NumberFormatException e) {
                return false;
            }
            return true;
        } 
        return false;
    }

    public static class checkAuctionAnswer {

        public static String checkandget(String msg, String user) {

            String[] tokenized = msg.split(" ");

            if (tokenized[0].contains("!new-bid")) {
                int position = msg.indexOf(tokenized[1], 0);
                return new String(user + ">"
                        + "You have been overbid on" + " '"
                        + msg.substring(position) + "' ");

            } else if (tokenized[0].contains("!auction-ended")) {
                if (tokenized[1].contains(user)) {
                    int position = msg.indexOf(tokenized[3], 0);
                    return new String(user + ">"
                            + "The auction" + " '"
                            + msg.substring(position)
                            + "' " + "has ended." + " " + "You won with" + " "
                            + tokenized[2] + "!");
                } else {
                    int position = msg.indexOf(tokenized[3], 0);
                    return new String(user + ">"
                            + "The auction" + " '"
                            + msg.substring(position)
                            + "' " + "has ended." + " " + tokenized[1]
                            + " " + "won with" + " "
                            + tokenized[2] + ".");
                }
            }
            return null;
        }
    }
}
