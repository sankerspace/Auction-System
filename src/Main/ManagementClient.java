package Main;

/**
 *
 * @author sanker
 */
public class ManagementClient {
    
     public static void main(String[] args) {
       ManagementClients.ManagementClient mc= new ManagementClients.ManagementClient("./src/registry.properties",args[0],args[1]);
       mc.run();
       System.exit(0); 
    }
     
}
