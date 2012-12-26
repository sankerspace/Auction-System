/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import java.io.File;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

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
    
    private byte[] clientChallenge=null;
    private byte[] serverChallenge=null;
    
    private  byte[] SecretKey=null;
    private byte[]  IVParameter=null;
    
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
             
            clientChallenge=new byte[32];
            securerandom.nextBytes(clientChallenge);
             

        } catch (InvalidKeyException ex) {
            throw new RSAAuthenticationException("RAS_Server:NoSuchAlgorithmException:"+
               ex.getMessage());
        }
    }
    //first message of client
    //userinfo contains the tcpPort information, but business logic
    //has no place in the security package
    //the entire message is NOT Base64 encoded, this is outside work
    public byte[] createRequestHandshakeProtocol(String user,String userinfo)throws RSAAuthenticationException
    {
        try {
            
            String message = "!login"+" "+user+" "+userinfo+" ";
            byte[] encrypted=null;
            //encode Client Challenge with Base64 and add it to message
            byte[] Base64ClientChallenge=Base64Encoder.encodeBase64(this.clientChallenge);
            message+=Base64ClientChallenge;
           
            encrypted=c1.doFinal(message.getBytes());
      
            return encrypted;
            
        } catch (IllegalBlockSizeException ex) {
            throw new RSAAuthenticationException("createRequestHandshakeProtocol:IllegalBlockSizeException:"+ex.getMessage());
        } catch (BadPaddingException ex) {
            throw new RSAAuthenticationException("createRequestHandshakeProtocol:BadPaddingException:"+ex.getMessage());
        }catch(Exception ex)
        {
            throw new RSAAuthenticationException("createRequestHandshakeProtocol:Exception:"+ex.getMessage());
        }
    }
    
    public void analyzeAnswerHandshakeProtocol()
    {
        
    }
    
    
    
}
