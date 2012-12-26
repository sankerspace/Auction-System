/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair; 
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PasswordFinder;

/**
 *
 * @author sanker
 */

/*
 * RSA Authentication between Client and a Server
 * Client always starts authentication handshake protocoll
 * and the Server confirms this handshake and creates 
 * a shared secret key that wil be user for later communications 
 * between Client and User
 * 
 * 
 *  
 */
public class RSAAuthentication {
    
    private boolean protocolstatus;
    protected SecureRandom securerandom=null; 
    protected Cipher c1=null; //c1 cipher for outgoing messages
    protected Cipher c2=null;  //c2 cipher for incoming messages
    
    protected File ServerKeydirectory=null;
    protected File ClientKeydirectory=null;
    
    public RSAAuthentication(String ClientKeyDirectory
            ,String ServerKeyDirectory) throws RSAAuthenticationException
    {
        try {   
            
            ServerKeydirectory=new File(ServerKeyDirectory);
            if(!ServerKeydirectory.isDirectory())
                throw new RSAAuthenticationException("Constructor:Invalid Server Directory");
            ClientKeydirectory=new File(ClientKeyDirectory);
            if(!ClientKeydirectory.isDirectory())
                throw new RSAAuthenticationException("Constructor:Invalid Client Directory");
            
            

            //random=SecureRandom.getInstance("SHA1PRNG");
            securerandom=new SecureRandom();
            c1=Cipher.getInstance("RSA/NONE/OAEPWithSHA256AndMGF1Padding","BC");
            c2=Cipher.getInstance("RSA/NONE/OAEPWithSHA256AndMGF1Padding","BC");
            //if something in the handshakeprotocol goes wrong protocolstatus=false;
            protocolstatus=true;
            
        } catch (NoSuchProviderException ex) {
            throw new RSAAuthenticationException("Constructor:NoSuchProviderException:"+
                   ex.getMessage());
        } catch (NoSuchPaddingException ex) {
           throw new RSAAuthenticationException("Constructor:NoSuchPaddingException:"+
                   ex.getMessage());
        } catch (NoSuchAlgorithmException ex) {
           throw new RSAAuthenticationException("Constructor:NoSuchAlgorithmException:"+
                   ex.getMessage());
        }
        
    }
    
    PublicKey getPublicKey(String file)throws RSAAuthenticationException
    {//!!Directory anagaben gehn unter unix und Linux????
        PEMReader in = null;
        PublicKey key=null;
        try {
            
            in = new PEMReader(new FileReader(file));
            key = (PublicKey) in.readObject();
            
        } catch (IOException ex) {
           throw new RSAAuthenticationException("getPublicKeyfromUser:IOException:"+
                   ex.getMessage());
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                 throw new RSAAuthenticationException("getPublicKeyfromUser:cannot close file reader:"+
                   ex.getMessage());
            }
        }
        
        return key;
    }
    
    
    PrivateKey getPrivateKey(String file)throws RSAAuthenticationException
    {//!!Directory anagaben gehn unter windows und Linux????
        PEMReader in = null;
        PrivateKey key=null;
        try {
            PasswordFinder pwFinder=new PasswordFinder()
            {//implementation of interface PasswortFinder()
                @Override 
                public char[] getPassword() {
                
                    // reads the password from standard input for decrypting the private key
                    System.out.println("Enter pass phrase:");
                    char[] array=null;
                    String line=null;
                    BufferedReader input=null;
                    try {
                        input = new BufferedReader(new InputStreamReader(System.in));
                        while((line=input.readLine())!=null)
                        {
                            break;
                        }
                        array = line.toCharArray();
                    } catch (IOException ex) {
                       
                    }finally
                    {
                        try {
                            input.close();
                        } catch (IOException ex) {
                            
                        }
                    }
                    return array;
    
                };
            };
            
            in = new PEMReader(new FileReader(file),pwFinder);
            KeyPair keyPair = (KeyPair)in.readObject();
            key=keyPair.getPrivate();
            
        } catch (IOException ex) {
           throw new RSAAuthenticationException("getPrivateKeyfromUser:IOException:"+
                   ex.getMessage());
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                 throw new RSAAuthenticationException("getPublicKeyfromUser:cannot close file reader:"+
                   ex.getMessage());
            }
        }
        
        return key;
    }
    
    String getServerKeyDirectorypath()
    {
        return this.ServerKeydirectory.getPath();
    }
    
    String getClientKeyDirectorypath()
    {
        return this.ClientKeydirectory.getPath();
    }
   
}
