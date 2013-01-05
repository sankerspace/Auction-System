/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import utils.EasySecure;

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
        
        private static org.apache.log4j.Logger logger=org.apache.log4j.Logger.getLogger(RSAServer.class.getSimpleName());
        private PublicKey   publicKeyClient=null;
        private PrivateKey  privateKeyServer=null;
        private String      userinfo=null;
        final private int KEYSIZE=256;//Keysize of the secret Key for AES Cipher
        private String lastReceivedMessage=null;
        
        public RSAServer(String servername,String ClientKeyDirectory
            ,String ServerKeyDirectory)throws RSAAuthenticationException
        {
            super(ClientKeyDirectory,ServerKeyDirectory);
            this.servername=servername;
            
            try {
                String path=this.ServerKeydirectory.getPath()+File.separator
                        +servername+".pem";               
                File privateServerKeyFile = new File(path);
                privateKeyServer=this.getPrivateKey(privateServerKeyFile.getPath());
                logger.info("Initialized server private key.");
                
                 if(privateKeyServer!=null)
                    c2.init(Cipher.DECRYPT_MODE, privateKeyServer);
                 else 
                    throw new RSAAuthenticationException("Client Private Key is null!");
                 
                 logger.info("Initialized cypher c2.");
                
            } catch (InvalidKeyException ex) {
                throw new RSAAuthenticationException("RSAServer:NoSuchAlgorithmException:"+
                   ex.getMessage());
            }catch (Exception ex) {
                throw new RSAAuthenticationException("RSAServer:Exception:"+
                    ex.getMessage());
                }
        }
        
        public String getUserInfo()
        {
            return this.userinfo;
        }
        
        
        private void setUser(String user)throws RSAAuthenticationException
        {
            try {
               logger.info("Find Public Key of user "+user); 
               String path=this.ClientKeydirectory.getPath()+File.separator
                       +user+".pub.pem";
               File publicClientKeyFile = new File(path);
               publicKeyClient=this.getPublicKey(publicClientKeyFile.getPath());
               logger.info("Initialized client public key.");
               if(publicKeyClient!=null)
                   c1.init(Cipher.ENCRYPT_MODE, publicKeyClient);
                else 
                   throw new RSAAuthenticationException("Key is not valid.");

                logger.info("Initialized cypher c1.");
               this.clientname=user;
            } catch (InvalidKeyException ex) {
                throw new RSAAuthenticationException("setUser:"+ex.getMessage());
            } catch (Exception ex) {
                throw new RSAAuthenticationException("Exception:"+ex.getMessage());
            }
        }
        //Session key contains two keys-Secret Key and IV byte array
        private void createSessionKey()throws RSAAuthenticationException
        {
            try { 
                //create SecretKey
                KeyGenerator generator = KeyGenerator.getInstance("AES");
                generator.init(KEYSIZE);
                 Secretkey = generator.generateKey();
                //create IV value
                IVParameter=new byte[16];
                securerandom.nextBytes(IVParameter);
                
            } catch (NoSuchAlgorithmException ex) {
                throw new RSAAuthenticationException("NoSuchAlgorithmException:"+ex.getMessage());
            } catch (Exception ex) {
                throw new RSAAuthenticationException("Exception:"+ex.getMessage());
            }
        
        }
        
        
        
        public boolean HandshakeProtocolMessageOneHandle(byte[] encrypted)throws RSAAuthenticationException
        {
            /**
             * encrypted=Base64(msg)
             * msg=RSA(!login +user +userinfo   +Base64(clientchallenge)) 
             * 
             */
            try {
                logger.info("Read Request of the first message from Client");
                //decode Base64
                byte[] tmp=Base64Encoder.decodeBase64(encrypted);
               //decrypt RSA message
                byte[] message=c2.doFinal(tmp);
                //get message components
                String[] msg=(new String(message)).split(" ");
                if(msg.length<4)
                {
                    logger.error("Less then 4 arguments from Client.");
                    return false;
                }
                logger.debug("message length from client:"+msg.length);
                try{
                    if(msg[0].contains("!login"))
                    {
                       logger.info("'!login' recognized ");
                       this.setUser(msg[1]);                
                       logger.debug("User "+this.clientname+" requested connection.");
                       int counter=2;
                       this.userinfo=msg[counter++];
                       while(counter < (msg.length-1))
                       {
                           this.userinfo+=msg[counter];
                           this.userinfo+=" ";
                           counter++;
                       }
                       logger.info("UserInfo from "+this.clientname+" is "+userinfo);
                       byte[] challengeClient=msg[(msg.length-1)].getBytes();
                       
                       this.clientChallenge=Base64Encoder.decodeBase64(challengeClient);
                       logger.debug("received Client Challenge: "+EasySecure.convertBytetoStringofDigits(clientChallenge));

                    }else
                        return false;
                }catch(RSAAuthenticationException ex)
                {
                   logger.error("Client has violated Handshake protocol. ");
                   return false; 
                }
                return true;
            } catch (IllegalBlockSizeException ex) {
               throw new RSAAuthenticationException("FromClientToServerHandshakeProtocol:IllegalBlockSizeException:"+ex.getMessage());
            } catch (BadPaddingException ex) {
               throw new RSAAuthenticationException("FromClientToServerHandshakeProtocol:BadPaddingException:"+ex.getMessage());
            }catch (Exception ex) {
               throw new RSAAuthenticationException("FromClientToServerHandshakeProtocol:Exception:"+ex.getMessage());
            }
        }
        
        
        public byte[] HandshakeProtocolMessageTwoCreate()throws RSAAuthenticationException
        {
            /**
             * msg=!ok +Base64(clientchallenge)+Base64(serverchallenge)
             *         +Base64(SecretKey)      +Base64(IV parameter)  
             * return Base64( RSA(msg) )
             * */
            //Base64(clientchallenge)
            logger.info("Create second responsemessage for Handshake Protocol.");
            String msg="!ok"+" "+new String(Base64Encoder.encodeBase64(clientChallenge))+" ";
            logger.info("Client Challenge is Base64 encoded");
            ////Base64(serverchallenge)
            serverChallenge=new byte[32];
            securerandom.nextBytes(serverChallenge);
            msg+=new String((Base64Encoder.encodeBase64(serverChallenge)));
            msg+=" ";
            //create SessionKeypair
            try {
                this.createSessionKey();
                logger.info("Session Key created.");
            } catch (RSAAuthenticationException ex) {
                throw new RSAAuthenticationException("ServerToClientHandshakeProtocol:createSessionKey:"+ex.getMessage());
            }
            //Base64(SecretKey)
            msg+=new String(Base64Encoder.encodeBase64(this.Secretkey.getEncoded()));
            msg+=" ";
            
            //Base64(IV parameter
            msg+=new String(Base64Encoder.encodeBase64(this.IVParameter));
            //last argument in the message
            byte[] encrypted=null;
            byte[] Base64andencrypted=null;
            try {
                encrypted=this.c1.doFinal(msg.getBytes());
            } catch (IllegalBlockSizeException ex) {
                throw new RSAAuthenticationException("ServerToClientHandshakeProtocol:createSessionKey:"+ex.getMessage());
            } catch (BadPaddingException ex) {
                throw new RSAAuthenticationException("ServerToClientHandshakeProtocol:createSessionKey:"+ex.getMessage());
            }
            //encode base64 and return
            Base64andencrypted=Base64Encoder.encodeBase64(encrypted);
            logger.debug("Summary of the Second HANDSHAKEPROTOCOLL message created on Server side:");
            logger.debug("ClientChallenge:"+EasySecure.convertBytetoStringofDigits(clientChallenge));
            logger.debug("ServerChallenge:"+EasySecure.convertBytetoStringofDigits(serverChallenge));
            logger.debug("SecretKey:"+EasySecure.convertBytetoStringofDigits(Secretkey.getEncoded()));
            logger.debug("IVParameter:"+EasySecure.convertBytetoStringofDigits(IVParameter));
            return Base64andencrypted;
        }
        
        
        public boolean HandshakeProtocolMessageThreeHandle(byte[] encrypted)throws RSAAuthenticationException
        {
            /**
             * encrypted=Base64(AES(Base64(serverChallenge)))
             * 
             * 
             */
            //Base64 decode
            
            byte[] challenge=null;
            byte[] tmp=null;
            try {
                //create AES object to decode message and for further commmunication
                aes=new AES(this.Secretkey,this.IVParameter);
                //decrypt AES and decode base64
                tmp=aes.decrypt(encrypted);
                challenge= Base64Encoder.decodeBase64(tmp);
            } catch (AESException ex) {
               throw new RSAAuthenticationException("AESException:"+ex.getMessage());
            }
            logger.debug("Summary of the Third HANDSHAKEPROTOCOLL message send from CLIENT");
            logger.debug("ServerChallenge:"+EasySecure.convertBytetoStringofDigits(challenge));
            return compareChallenges(challenge, this.serverChallenge);
        
        }
        
        public void startServerAuthenticationProcedure(InputStream in,OutputStream out) throws RSAAuthenticationException
        {
            
             
            DataOutputStream w = new DataOutputStream(out);
            DataInputStream r = new DataInputStream(in);
            String send=null,read=null,error=this.getErrorMessage();
            boolean b;
            try {
                
                //first message read
                read=r.readUTF();
                this.lastReceivedMessage=new String(read);
                byte[] firstmessage=read.getBytes();
                b=this.HandshakeProtocolMessageOneHandle(firstmessage);
                if( !b )
                    throw new RSAAuthenticationException("Authentication failed.");
               


                //second message send
                byte[] secondmessage = this.HandshakeProtocolMessageTwoCreate();
                send=new String(secondmessage);
                w.writeUTF(send);



                //third message receive
                read=r.readUTF();
                this.lastReceivedMessage=new String(read);
                if(read.contains(error))
                    throw new RSAAuthenticationException("Authentication failed at Client side.");
                byte[] thirdmessage=read.getBytes();
                b=this.HandshakeProtocolMessageThreeHandle(thirdmessage);
                if( !b )
                    throw new RSAAuthenticationException("Authentication failed.");


            } catch (RSAAuthenticationException ex) {
                try {
                    w.writeUTF(error);
                } catch (IOException ex1) {  }
               throw new RSAAuthenticationException(ex.getMessage());
            } catch (IOException ex) {
                try {
                    w.writeUTF(error);
                } catch (IOException ex1) {  }
               throw new RSAAuthenticationException("IOException:"+ex.getMessage());
            }catch(Exception ex)
            {
                try {
                    w.writeUTF(error);
                } catch (IOException ex1) { }
                throw new RSAAuthenticationException("Exception:"+ex.getMessage());
            }

            
        }
        
        public String getLastMessage()
        {
            return this.lastReceivedMessage;
        }
        
        
        
    }
