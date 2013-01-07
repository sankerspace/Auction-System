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
import javax.crypto.SecretKey;
import org.apache.log4j.xml.DOMConfigurator;
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
    
    //private boolean protocolstatus;
    private static org.apache.log4j.Logger log=org.apache.log4j.Logger.getLogger(RSAAuthentication.class.getSimpleName());
    protected SecureRandom securerandom=null; 
    protected Cipher    c1=null; //c1 cipher for outgoing messages
    protected Cipher    c2=null;  //c2 cipher for incoming messages
    protected File      ServerKeydirectory=null;
    protected File      ClientKeydirectory=null;
    protected String    servername=null;
    protected String    clientname=null;
    protected byte[]    clientChallenge=null;
    protected byte[]    serverChallenge=null;
    protected SecretKey Secretkey=null;
    protected byte[]    IVParameter=null;
    
    protected AES aes=null;
    
    public RSAAuthentication(String ClientKeyDirectory
            ,String ServerKeyDirectory) throws RSAAuthenticationException
    {
        try {   
            
            DOMConfigurator.configure("./src/log4j.xml"); 
            
            ServerKeydirectory=new File(ServerKeyDirectory);
            if(!ServerKeydirectory.isDirectory())
                throw new RSAAuthenticationException("RSAAuthentication:Invalid Server Directory");
            ClientKeydirectory=new File(ClientKeyDirectory);
            if(!ClientKeydirectory.isDirectory())
                throw new RSAAuthenticationException("RSAAuthentication:Invalid Client Directory");
           log.info("Initialized key directories.");
            

            //random=SecureRandom.getInstance("SHA1PRNG");
            securerandom=new SecureRandom();
            c1=Cipher.getInstance("RSA/NONE/OAEPWithSHA256AndMGF1Padding","BC");
            c2=Cipher.getInstance("RSA/NONE/OAEPWithSHA256AndMGF1Padding","BC");
            //if something in the handshakeprotocol goes wrong protocolstatus=false;
             log.info("Initialized ciphers.");
            
        } catch (NoSuchProviderException ex) {
            throw new RSAAuthenticationException("RSAAuthentication:NoSuchProviderException:"+
                   ex.getMessage());
        } catch (NoSuchPaddingException ex) {
           throw new RSAAuthenticationException("RSAAuthentication:NoSuchPaddingException:"+
                   ex.getMessage());
        } catch (NoSuchAlgorithmException ex) {
           throw new RSAAuthenticationException("RSAAuthentication:NoSuchAlgorithmException:"+
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
                    BufferedReader input_=null;
                    try {
                        input_ = new BufferedReader(new InputStreamReader(System.in));
                        while((line=input_.readLine())!=null)
                        {
                            break;
                        }
                        array = line.toCharArray();
                    } catch (IOException ex) {
                       log.error(ex.getMessage());
                    }catch (Exception ex) {
                        log.error(ex.getMessage());
                    }finally
                    {/*
                        try {
                            input_.close();
                        } catch (IOException ex) {
                            
                        }*/
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
    
   protected boolean compareChallenges(byte[] challenge1,byte[] challenge2) 
   {
       int size=challenge1.length;
       if(size !=challenge2.length)
           return false;
       
       for(int i=0;i<size;i++)
       {
           if(challenge1[i]!=challenge2[i])
               return false;
       }

       return true;
   }
    
   public String getServerKeyDirectorypath()
    {
        return this.ServerKeydirectory.getPath();
    }
    
   public String getClientKeyDirectorypath()
    {
        return this.ClientKeydirectory.getPath();
    }
   
   public String getUsername()
    {
        return this.clientname;
    }
    
    public String getServername()
    {
        return this.servername;
    }
    
    
    public String getErrorMessage()
    {
        return "!denied";
    }
    
    public AES getAES()
    {
        return this.aes;
    }
    
}
