/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;


/**
 *
 * @author sanker
 */
public class EasySecure {
    
    
    
    
    
      
    public static byte[] makeBytes(String s) {
    try {
      ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
      DataOutputStream dataOut = new DataOutputStream(byteOut);
      dataOut.writeUTF(s);
      
      return byteOut.toByteArray();
    }
    catch (IOException e) {
     return null;
    }
    
  }
  
  
   public static String convertBytetoStringofDigits(byte[] b)
   {
       BigInteger big = new BigInteger(1,b);
       return big.toString(16);
   }
   
   public static byte[] convertStringofDigitstoByte(String s)
   {   
        BigInteger big = new BigInteger(s,16);
        return big.toByteArray();
   
   }
    
    
    
    
  public static String BytestoString(byte[] b) {
      String s=null;
    try {
      
      ByteArrayInputStream byteIn = new ByteArrayInputStream(b);
      DataInputStream dataIn = new DataInputStream(byteIn);
      
      s=dataIn.readUTF();
      
      
    }
    catch (IOException e) {
     
    }catch(Exception ex)
    {
          
    }
    
    return s;
  }  
 
  public static String encodeString(String s,String Algorithm) 
  {
      String enc=null;
      try{
            MessageDigest md = MessageDigest.getInstance(Algorithm);
            byte[] b=s.getBytes();
            md.reset(); 
            //md.update(b);
            //encoding byte array
            byte[] mdbytes = md.digest(b);
            /*
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < mdbytes.length; i++) {
             buffer.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
             
            }*/
           enc= EasySecure.convertBytetoStringofDigits(mdbytes);
           //Hex.encodeHex(resultByte) 
           //enc=new String()
      }catch(NoSuchAlgorithmException ex)
      {
          enc=null;
      }catch(Exception ex)
      {
         enc=null; 
      }
  
      return enc;
  }
  
  

    
}
    
  
    
    
    
    
    

