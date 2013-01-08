/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 *
 * @author sanker
 */
public class AES {
    
    private Cipher c1=null;
    private Cipher c2=null;
    
    public AES(SecretKey secretkey,byte[] IVParameter) throws AESException
    {
        try {
            c1= Cipher.getInstance("AES/CTR/NoPadding","BC");
            c2= Cipher.getInstance("AES/CTR/NoPadding","BC");
            
            c1.init(Cipher.ENCRYPT_MODE, secretkey, new IvParameterSpec(IVParameter));
            c2.init(Cipher.DECRYPT_MODE, secretkey, new IvParameterSpec(IVParameter));
            
        } catch (InvalidKeyException ex) {
            throw new AESException("InvalidKeyException:"+ex.getMessage());
        } catch (InvalidAlgorithmParameterException ex) {
             throw new AESException("InvalidAlgorithmParameterException:"+ex.getMessage());
        } catch (NoSuchAlgorithmException ex) {
            throw new AESException("NoSuchAlgorithmException:"+ex.getMessage());
        } catch (NoSuchProviderException ex) {
            throw new AESException("NoSuchProviderException:"+ex.getMessage());
        } catch (NoSuchPaddingException ex) {
            throw new AESException("NoSuchPaddingException:"+ex.getMessage());
        }
    
    }
    
    public byte[] encrypt(byte[] msg) throws AESException
    {
        
        /**
         * return Base64(AES(msg))
         * 
         */
        byte[] tmp=null;
        try {
            //tmp=AES(msg)
            tmp=c1.doFinal(msg);
            
        } catch (IllegalBlockSizeException ex) {
             throw new AESException("IllegalBlockSizeException:"+ex.getMessage());
        } catch (BadPaddingException ex) {
             throw new AESException("BadPaddingException:"+ex.getMessage());
        }
        //Base64(tmp)
        return Base64Encoder.encodeBase64(tmp);
    }
    
    
    public byte[] decrypt(byte[] msg) throws AESException
    {
        
        /**
         * msg = Base64(AES(plaintext))
         * return plaintext
         */
        //base64 decode
        byte[] tmp=Base64Encoder.decodeBase64(msg);
        try {
            //AES decrypt
            tmp=c2.doFinal(tmp);
            
        } catch (IllegalBlockSizeException ex) {
             throw new AESException("IllegalBlockSizeException:"+ex.getMessage());
        } catch (BadPaddingException ex) {
             throw new AESException("BadPaddingException:"+ex.getMessage());
        }
        return tmp;
    }
  
}
