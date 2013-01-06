package auctionmanagement;



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sanker
 */
import auctionmanagement.AuctionClient;
import auctionmanagement.AuctionClientException;
import MyLogger.Log;
import MyLogger.Log;
import java.io.PrintWriter;



public class PartBClient {

    private  String[] arguments=null;
    private  String host=null;
    private  int tcpPort=0;
    private  int udpPort=0;
    private  String ServerPublicKeyFilename=null;
    private  String ClientKeyDirectoryname=null;
    private  String ServerKeyDirectoryname=null;
    private  AuctionClient auction=null;
    private  Log output=null;
    
    public PartBClient(String[] args) {
       
        arguments=args;
         
         output=new Log(new PrintWriter(System.out),0); 
    }
    
   public boolean checkandgetArguments()
    {
        if(arguments.length != 6)
               return false;
            host = this.arguments[0];
       try{
            tcpPort=Integer.parseInt(this.arguments[1]);
            udpPort=Integer.parseInt(this.arguments[2]);
            ServerPublicKeyFilename=this.arguments[3];
            ClientKeyDirectoryname=this.arguments[4];
            ServerKeyDirectoryname=this.arguments[5];
            
            if(ServerPublicKeyFilename.contains(".pub.pem"))
                ServerPublicKeyFilename=ServerPublicKeyFilename.replace(".pub.pem","");
            if(!ClientKeyDirectoryname.endsWith("/"))
                ClientKeyDirectoryname.concat("/");
            if(!ServerKeyDirectoryname.endsWith("/"))
                ServerKeyDirectoryname.concat("/");
            
            return true;
        }catch(NumberFormatException e)
        {
            return false;
        }
        
      
    }
    
    
    public void printUsage()
    {
        System.err.println("usage: "
                +  "<run-client>  -host  -tcpPort  -udpPort"
                + "\n\t"+"-host:       host name or IP of the auction server"
                + "\n\t"+"-tcpPort:    TCP Connection port on which the auction server is listening for incoming connections"
                + "\n\t"+"-udpPort:    port is for handling UDP notofocations from the auctions server"
                + "\n\t"+"-Server.pub: Server public key file name   "
                + "\n\t"+"-ClientKeyDirectry: Directory of all Client keys   "
                 );
    }
    
    
   public int run()
    {
       
       output.output("Running PartB Client...",2);
         if(!this.checkandgetArguments())
        {
            this.printUsage();
            return -1;
        }
        
        try {
            output.output("Creating AuctionClient Object...",2);
            auction = new AuctionClient(this.host,this.tcpPort,this.udpPort,
                    this.ServerPublicKeyFilename,
                    this.ClientKeyDirectoryname,
                    this.ServerKeyDirectoryname,this.output);
            
            auction.run();
           
        } catch (AuctionClientException e) {
            output.output("AuctionClientException:"+e.getMessage());
            auction.close();
            output.close();
            
            return -1;
            
        }
         output.output("Closing PartB Client...",2);
        auction.close();
        output.close();
        return 0;
    }
    
    
}
