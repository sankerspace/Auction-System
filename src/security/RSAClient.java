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
import javax.crypto.spec.SecretKeySpec;

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
    private static org.apache.log4j.Logger logger=org.apache.log4j.Logger.getLogger(RSAClient.class.getSimpleName());
    private PublicKey publicKeyServer=null;
    private PrivateKey privateKeyClient=null;
   
    
    public RSAClient(String user,String servername,String ClientKeyDirectory
        ,String ServerKeyDirectory)throws RSAAuthenticationException
    {
        super(ClientKeyDirectory,ServerKeyDirectory);
        this.clientname=user;
        this.servername=servername;   
       
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
            throw new RSAAuthenticationException("RSAClient:NoSuchAlgorithmException:"+
               ex.getMessage());
        }catch (Exception ex) {
            throw new RSAAuthenticationException("RSAClient:Exception:"+
               ex.getMessage());
        }
    }
    
    
    
    //first message of client
    //userinfo contains the tcpPort information, but business logic
    //has no place in the security package
    //the entire message is NOT Base64 encoded, this is outside work
    public byte[] ClientToServerHandshakeProtocol(String user,String userinfo)throws RSAAuthenticationException
    {
        /**
         * msg=!login +user +userinfo   +Base64(clientchallenge) 
         * return RSA(msg)
         */
        
        try {
            if((user==null)||(userinfo==null))
                throw new RSAAuthenticationException("Invalid Arguments.");
            else if( ((user.split(" ").length)==1) || ((userinfo.split(" ").length)==1) )
                 throw new RSAAuthenticationException("Invalid Arguments.");
            String message = "!login"+" "+user+" "+userinfo+" ";
            byte[] encrypted=null;
            logger.info("Send Client Challenge: "+new String(clientChallenge));
            //encode Client Challenge with Base64 and add it to message
            byte[] Base64ClientChallenge=Base64Encoder.encodeBase64(this.clientChallenge);
            message+=(new String(Base64ClientChallenge));
           
            encrypted=c1.doFinal(message.getBytes());
      
            return encrypted;
            
        } catch (IllegalBlockSizeException ex) {
            throw new RSAAuthenticationException("ClientToServerHandshakeProtocol:IllegalBlockSizeException:"+ex.getMessage());
        } catch (BadPaddingException ex) {
            throw new RSAAuthenticationException("ClientToServerHandshakeProtocol:BadPaddingException:"+ex.getMessage());
        }catch(Exception ex)
        {
            throw new RSAAuthenticationException("ClientToServerHandshakeProtocol:Exception:"+ex.getMessage());
        }
    }
    
    public boolean FromServerToClientHandshakeProtocol(byte[] encrypted)throws RSAAuthenticationException
    {
        /**
             * msg=RSA(!ok +Base64(clientchallenge)+Base64(serverchallenge)
             *         +Base64(SecretKey)      +Base64(IV parameter) ) 
             * 
             * */
        try {
            //decrypt RSA
            String[] msg=(new String(c2.doFinal(encrypted))).split(" ");
            if(msg[0].contains("!ok")&&(msg.length==5))
            {
               //get clientchallenge and compare with own challenge
                byte[] tmp=Base64Encoder.decodeBase64(msg[1].getBytes());
                if(!this.compareChallenges(tmp, clientChallenge))
                {
                    logger.error("clientChallenge from Server does not correspond to our challenge.");
                    return false;
                }
                
                //get serverchallenge
                this.serverChallenge=Base64Encoder.decodeBase64(msg[2].getBytes());
                
                //get session keypair
                //get SecretKey
                tmp=Base64Encoder.decodeBase64(msg[3].getBytes());
                this.Secretkey=new SecretKeySpec(tmp,"AES");
                //get IV Parameter
                this.IVParameter=Base64Encoder.decodeBase64(msg[4].getBytes());
                
            }else
                return false;    
            
            
        } catch (IllegalBlockSizeException ex) {
            
            throw new RSAAuthenticationException("ClientToServerHandshakeProtocol:Exception:"+ex.getMessage());
        } catch (BadPaddingException ex) {
            throw new RSAAuthenticationException("ClientToServerHandshakeProtocol:Exception:"+ex.getMessage());
        }
 
        return true;
    }
    
    
    
}
