/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LoadTest;

import auctionmanagement.Auction;
import java.util.TimerTask;

/**
 *
 * @author sanker
 */
public class AuctionTestTimerTasksforLoadTest {
    
    
    public class perMinuteTask extends TimerTask {

        private Long auction_id;
        private Auction auc = null;
       
        public perMinuteTask() {
          

        }

        public void run() {
            
        }
        
    }
    
    
    
     public class updateListTask extends TimerTask {

        private Long auction_id;
        private Auction auc = null;
       
        public updateListTask() {
          

        }

        public void run() {
            
        }
        
    }
     
     public class endAuctionafterDurationTask extends TimerTask {

        private Long auction_id;
        private Auction auc = null;
       
        public endAuctionafterDurationTask() {
          

        }

        public void run() {
            
        }
        
    }
    
}
