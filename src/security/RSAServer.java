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

    //c1 cypher for outgoing messages
    //c2 cipher for incoming messages
    public class RSAServer extends RSAAuthentication{
        
        /**
         * 
         * Client c1 (Encryption/Decryption)  c2 Server
         * Client c2 (Decryption/Encryption)  c1 Server
         * 
         * 
         **/
        
        
        private PublicKey publicKeyClient=null;
        private PrivateKey privateKeyServer=null;
        
        
        public RSAServer(String user,String servername,String ClientKeyDirectory
            ,String ServerKeyDirectory)throws RSAAuthenticationException
        {
            super(ClientKeyDirectory,ServerKeyDirectory);
            try {
                
                File privateServerKeyFile = new File(this.ServerKeydirectory.getPath()+File.pathSeparator+
                    servername+".pem");
                privateKeyServer=this.getPrivateKey(privateServerKeyFile.getPath());
                
                 File publicClientKeyFile = new File(this.ClientKeydirectory.getPath()+File.pathSeparator+
                    user+".pub.pem");
                publicKeyClient=this.getPublicKey(publicClientKeyFile.getPath());
                

                if(publicKeyClient!=null)
                    c1.init(Cipher.ENCRYPT_MODE, publicKeyClient);
                 else 
                    throw new RSAAuthenticationException("Server Public Key is null!");
                 
                 if(privateKeyServer!=null)
                    c2.init(Cipher.DECRYPT_MODE, privateKeyServer);
                 else 
                    throw new RSAAuthenticationException("Client Private Key is null!");
                
            } catch (InvalidKeyException ex) {
                throw new RSAAuthenticationException("RAS_Server:NoSuchAlgorithmException:"+
                   ex.getMessage());
            }
        }
        
        
        
    }
