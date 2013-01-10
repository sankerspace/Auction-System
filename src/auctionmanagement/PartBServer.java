
package auctionmanagement;



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sanker
 */

import auctionmanagement.AuctionServer;
import auctionmanagement.AuctionServerException;
import MyLogger.Log;
import java.io.PrintWriter;


public class PartBServer {
    
     String[] arguments=null;
     int tcpPort=0;
     AuctionServer auction=null;
     String billing = null;
     String analytic = null;
     String ServerPrivateKeyFilename=null;
     String ClientKeyDirectoryname=null;
     String ServerKeyDirectoryname=null;
     
    public PartBServer(String[] args)
    {
        arguments=args;
    }
    
    
   public boolean checkandgetArguments()
    {
        if(arguments.length != 5)
                  return false;
        tcpPort=Integer.valueOf(arguments[0]);
        analytic = arguments[1];
        billing=arguments[2];
        String s=arguments[3];
        String[] spl=s.split("/");
        if(spl.length>1)
        {
            ServerPrivateKeyFilename=spl[spl.length-1];
            ServerKeyDirectoryname=s.replace(ServerPrivateKeyFilename, "");
        }else
        {
          ServerPrivateKeyFilename=spl[0];
          ServerKeyDirectoryname="./";
        }
        
        
        ClientKeyDirectoryname=arguments[4];
        
        
        
        if(ServerPrivateKeyFilename.contains(".pem"))
            ServerPrivateKeyFilename=ServerPrivateKeyFilename.replace(".pem", "");
       
       return true;
    }
    
    
    public void printUsage()
    {
        System.err.println("usage: <run-server>  -tcpPort -analyticBindingName -billingBindingName"
                + "\n\t"+"tcpPort:  TCP connection port on which the auction server will "
                + "\n\t\t"+"receive incomming (command) messages from clients."
             
                 );
    }
    
    
   public int run()
    {
        
        
      if(!this.checkandgetArguments()) 
        {
            this.printUsage();
            return -1;
        }
       Log output=new Log(new PrintWriter(System.out)); 
       
       
        try {
            
            auction = new AuctionServer(this.tcpPort,analytic,billing,
                    this.ServerPrivateKeyFilename,
                    this.ClientKeyDirectoryname,
                    this.ServerKeyDirectoryname,
                    output);
            auction.run();
        } catch (AuctionServerException e) {
            output.output("AuctionServerException:"+e.getMessage());
            if(auction!=null)
                auction.close();
            output.close();
            return 0;
            
        }
        auction.close();
        output.close();
        return 0;
    }
}