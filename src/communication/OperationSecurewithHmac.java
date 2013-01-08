/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import security.AES;
import security.AESException;
import security.AESwithHMAC;
import security.AESwithHMACException;
import security.RSAAuthenticationException;
import security.RSAClient;
import security.RSAServer;

/**
 *
 * @author Marko
 */
public class OperationSecurewithHmac implements Operation{
    private ClientSecure client = null;
    private OutputStream out = null;
    private InputStream in = null;
    /*Secure parameters*/
    private AES securechannel = null;
    private AESwithHMAC aeswithhmac=null;
    private String user = null;
    private String userinfo = null;
    private boolean lastVerification;
    private String ClientKeyDirectory=null;
    
    /*
     * Konstruktor for client side
     * secure connection etablished immediately
     */
 public OperationSecurewithHmac(OperationSecure op) throws OperationException {
        try {
            this.client = op.client;
            this.in = op.in;
            this.out = op.out;
            this.user = op.user;
            this.userinfo = op.userinfo;
            this.ClientKeyDirectory=op.getClientKeyDirectory();
            File directory = new File(ClientKeyDirectory);
            if(!new File(ClientKeyDirectory).isDirectory())
                throw new OperationException("OperationException:Invalid Client Directory");
            String path_second=directory.getPath()+File.separator+user+".key";
            File KeyFile = new File(path_second);
            this.aeswithhmac=new AESwithHMAC(op.securechannel,KeyFile);
        } catch (AESwithHMACException ex) {
            throw new OperationException("AESwithHMACException"+ex.getMessage());
        }
        

    }
 
  public String readString() throws OperationException {
        String s = null;
        StringBuffer decrypted=new StringBuffer("");
        boolean integritystate;
        DataInputStream r = null;
        try {
            r = new DataInputStream(in);
            s = r.readUTF();
            integritystate =this.aeswithhmac.validateMessage(s,decrypted);
            return decrypted.toString();

        } catch (AESwithHMACException e) {
            throw new OperationException("AESException::" + e.getMessage());
        } catch (IOException e) {
            throw new OperationException("IOException::" + e.getMessage());
        }
    }
  
  
  /*
   * Methode 
   */
    public void writeString(String s) throws OperationException {
        String encrypted = null;
        byte[] tmp = null;
        DataOutputStream w = null;
        try {
            w = new DataOutputStream(out);
            encrypted = this.aeswithhmac.createMessage(s);
            w.writeUTF(encrypted);

        } catch (AESwithHMACException e) {
            throw new OperationException("AESException::" + e.getMessage());
        } catch (IOException e) {
            throw new OperationException("IOException::" + e.getMessage());
        }
    }
 
 
  public String getUserInfo() {
     return this.userinfo;
  }

  public String getUserName() {
      return this.user;
   }
}


