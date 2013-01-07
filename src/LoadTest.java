


import MyLogger.Log;
import auctionmanagement.AuctionClientException;
import LoadTest.AuctionTest;
import LoadTest.AuctionTestException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import utils.EasyProperties;
import utils.UtilsException;

/**
 *
 * @author Dave
 */
public class LoadTest {

    
    
    ExecutorService pool=null;
    

    public static void main(String[] args) throws AuctionClientException {
        try{
            
            int clients = Integer.parseInt(EasyProperties.getProperty("./src/loadtest.properties", "clients"));
            int auctionsPerMin = Integer.parseInt(EasyProperties.getProperty("./src/loadtest.properties", "auctionsPerMin"));
            int auctionDuration = Integer.parseInt(EasyProperties.getProperty("./src/loadtest.properties", "auctionDuration"));
            int updateIntervalSec = Integer.parseInt(EasyProperties.getProperty("./src/loadtest.properties", "updateIntervalSec"));
            int bidsPerMin = Integer.parseInt(EasyProperties.getProperty("./src/loadtest.properties", "bidsPerMin"));
            
            AuctionTest Test=null;
            
            Log logger=new Log(new PrintWriter(System.out)); 
            
            
            if(args.length==3)
            {
                String host = args[0];
                int port = Integer.parseInt(args[1]);
                String bindingName = args[2];
                
                Test=new AuctionTest(host,port,bindingName,
                        clients,
                        auctionsPerMin,
                        auctionDuration,
                        updateIntervalSec,
                        bidsPerMin,
                        logger);
                
                
                Test.run();
                
            }
            
           
        }catch(AuctionTestException ex)
        {
            System.out.println("LoadTest:AuctionTestException:"+ex.getMessage());
        }catch(NumberFormatException ex)
        {
            System.out.println("LoadTest:NumberFormatException:port number not correct.");
        }catch(UtilsException ex)
        {
            System.out.println("LoadTest:UtilsException:"+ex.getMessage());
        }catch(Exception ex)
        {
            System.out.println("LoadTest:Exception:"+ex.getMessage());
        }
        System.exit(0);
    }
}
