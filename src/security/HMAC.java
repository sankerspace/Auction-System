/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.util.encoders.Hex;

/**
 * 
 * @author Dave
 */
public class HMAC {
    
    String serverResponse = "Message from Server";
    
    public byte[] appendHmac() throws FileNotFoundException, IOException, NoSuchAlgorithmException, InvalidKeyException{
        Key secretKey = getKey();
        Mac hMac = Mac.getInstance("HmacSHA256");
        hMac.init(secretKey);
        byte[] message = serverResponse.getBytes();
        hMac.update(message);
        return hMac.doFinal();
    }
    
    private Key getKey() throws FileNotFoundException, IOException{
        String pathToKey = "template/keys/alice.key";
        byte[] keyBytes = new byte[1024];
        FileInputStream fis = new FileInputStream(pathToKey);
        fis.read(keyBytes);
        fis.close();
        
        byte[] input = Hex.decode(keyBytes);
        return new SecretKeySpec(input,"HmacSHA256");
    }
}
