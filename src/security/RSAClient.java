/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import java.io.File;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.Cipher;

/**
 *
 * @author sanker
 */
public class RSAClient extends RSAAuthentication{
    
    
        /**
         * 
         * Client c1 (Encryption/Decryption)  c2 Server
         * Client c2 (Decryption/Encryption)  c1 Server
         * 
         * 
         **/
    
    private PublicKey publicKeyServer=null;
    private PrivateKey privateKeyClient=null;
    
    
        public RSAClient(String user,String servername,String ClientKeyDirectory
            ,String ServerKeyDirectory)throws RSAAuthenticationException
        {
            super(ClientKeyDirectory,ServerKeyDirectory);
            
            try {

                File publicServerKeyFile = new File(this.ServerKeydirectory.getPath()+File.pathSeparator+
                    servername+".pub.pem");
                publicKeyServer=this.getPublicKey(publicServerKeyFile.getPath());
                
                 File privateClientKeyFile = new File(this.ClientKeydirectory.getPath()+File.pathSeparator+
                    user+".pem");
                privateKeyClient=this.getPrivateKey(privateClientKeyFile.getPath());
               
                 
                 
                 if(publicKeyServer!=null)
                    c1.init(Cipher.ENCRYPT_MODE, publicKeyServer);
                 else 
                    throw new RSAAuthenticationException("Server Public Key is null!");
                 
                 if(privateKeyClient!=null)
                    c2.init(Cipher.DECRYPT_MODE, privateKeyClient);
                 else 
                    throw new RSAAuthenticationException("Client Private Key is null!");
                
            } catch (InvalidKeyException ex) {
                throw new RSAAuthenticationException("RAS_Server:NoSuchAlgorithmException:"+
                   ex.getMessage());
            }
        }
    
    }
