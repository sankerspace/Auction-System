

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sanker
 */

import AnalyticsServer.AnalyticsServer;

public class AnalyticServer {
    
    public static void main(String[] args)
    {
        AnalyticsServer analyticserver=new AnalyticsServer("./src/registry.properties",args[0]);
        analyticserver.run();
  
        System.exit(0);
    }
    
    
}
