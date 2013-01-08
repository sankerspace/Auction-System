/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marko
 */
public class AESwithHMAC {
    
   
    AES aes=null;
    HMAC hmac=null;
    
    public AESwithHMAC(AES aes,File key)throws AESwithHMACException
    {
        try {
            
            this.aes=aes;
            this.hmac=new HMAC(key);
        } catch (HMACException ex) {
            throw new AESwithHMACException("HMACException:"+ex.getMessage());
        }
    }
    
    public String createMessage(String plaintext)throws AESwithHMACException
    {
        try {
            String encryptedAES=null;
            String integrityHMAC=null;
            String result=null;
            byte[] resultAES=this.aes.encrypt(plaintext.getBytes());
            byte[] resultHMAC=this.hmac.generateHmac(plaintext);
            encryptedAES=new String(resultAES);
            integrityHMAC=new String(resultHMAC);
            result=(encryptedAES+" "+integrityHMAC);
            
            return result;
        } catch (HMACException ex) {
            throw new AESwithHMACException("HMACException:"+ex.getMessage());
        } catch (AESException ex) {
            throw new AESwithHMACException("AESException:"+ex.getMessage());
        }
    }
    
    public boolean validateMessage(String encryptedAESwithHMAC,StringBuffer plainResult)throws AESwithHMACException
    {
        try {
            String[] msg=encryptedAESwithHMAC.split(" ");
            byte[] resultfromAES=this.aes.decrypt(msg[0].getBytes());
            plainResult.append(new String(resultfromAES));
            boolean result=this.hmac.validateHMac(plainResult.toString(),msg[1]);
            return result;
        } catch (HMACException ex) {
            throw new AESwithHMACException("HMACException:"+ex.getMessage());
        } catch (AESException ex) {
            throw new AESwithHMACException("AESException:"+ex.getMessage());
        }
 
    }
}
