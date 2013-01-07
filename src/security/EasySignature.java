/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PasswordFinder;


/**
 *
 * @author Marko
 */
public class EasySignature {
    //private boolean protocolstatus;
    private static org.apache.log4j.Logger log=org.apache.log4j.Logger.getLogger(RSAAuthentication.class.getSimpleName());

    private PublicKey   publicKey=null;
    private PrivateKey  privateKey=null; 
    private Signature signature=null;
    boolean mode;
   /*
    *   true:Private and Public Key
    *   false:only use Public key to decrypt
    */
    public EasySignature(String KeyDirectory,String username,boolean mode) throws EasySignatureException
    {
        try {
            signature=Signature.getInstance("SHA1withDSA");
            this.mode=mode;
            File Keydirectory=new File(KeyDirectory);
            if(!Keydirectory.isDirectory())
                throw new EasySignatureException("SignatureException:Invalid Server Directory");
            
            
            if(mode)
            {

                String path_second=Keydirectory.getPath()+File.separator+username+".pem";
                File privateClientKeyFile = new File(path_second);
                privateKey=this.getPrivateKey(privateClientKeyFile.getPath());
                signature.initSign(privateKey);
                
                
            }else{
                String path = Keydirectory.getPath()+File.separator+username+".pub.pem";
                File publicServerKeyFile = new File(path);
                publicKey=this.getPublicKey(publicServerKeyFile.getPath());
                signature.initVerify(publicKey);
            }
        } catch (InvalidKeyException ex) {
           throw new EasySignatureException("InvalidKeyException:"+
                   ex.getMessage());
        } catch (NoSuchAlgorithmException ex) {
            throw new EasySignatureException("NoSuchAlgorithmException:"+
                   ex.getMessage());
        }
        
    }
    
    
    public String sign(String s)throws EasySignatureException
    {
        String encrypted=null;
        if(!mode)
            throw new EasySignatureException("Instance is created for signing messages only.");
        
        
        
        return encrypted;
    }
    
     public String verify(String s)throws EasySignatureException
    {
        try {
            String decrypted=null;
            if(mode)
                throw new EasySignatureException("Instance is created to verify messages only.");
            this.signature.update(s.getBytes());
            return decrypted;
        } catch (SignatureException ex) {
           throw new EasySignatureException("SignatureException:"+ex.getMessage());
        }
    }
 
    
    private PublicKey getPublicKey(String file)throws EasySignatureException
    {//!!Directory anagaben gehn unter unix und Linux????
        PEMReader in = null;
        PublicKey key=null;
        try {
            
            in = new PEMReader(new FileReader(file));
            key = (PublicKey) in.readObject();
            
        } catch (IOException ex) {
           throw new EasySignatureException("getPublicKeyfromUser:IOException:"+
                   ex.getMessage());
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                 throw new EasySignatureException("getPublicKeyfromUser:cannot close file reader:"+
                   ex.getMessage());
            }
        }
        
        return key;
    }
    
    
    private PrivateKey getPrivateKey(String file)throws EasySignatureException
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
           throw new EasySignatureException("getPrivateKeyfromUser:IOException:"+
                   ex.getMessage());
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                 throw new EasySignatureException("getPublicKeyfromUser:cannot close file reader:"+
                   ex.getMessage());
            }
        }
        
        return key;
    }
    
    
    
}
