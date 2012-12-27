/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

/**
 *
 * @author Dave
 */
public class HMAC {

    /**
     * Generates an Hmac to a message and returns the message with the hmac,
     * seperate by space.
     *
     * @return Mac of the given message
     * @param message Message to which the hMac will be added
     */
    
    private String plaintextWithHmac = null;
    public byte[] generateHmac(String message) {
        try {
            Key secretKey = getKey("keys/Clients/alice.key");
            Mac hMac = Mac.getInstance("HmacSHA256");
            hMac.init(secretKey);
            byte[] messageBytes = message.getBytes();
            hMac.update(messageBytes);
            
            byte[] hmacByte = hMac.doFinal();
            plaintextWithHmac = message+ " " + convertBytetoStringofDigits(hmacByte);
            return hmacByte;
        } catch (InvalidKeyException ex) {
            ex.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean validateHMac(String plaintextWithHash){
        String[] splitted = plaintextWithHash.split(" ");
        String plaintext = splitted[0];
        System.out.println("RECEIVED HASH " + splitted[1]);
        byte[] receivedHash = convertStringofDigitstoByte(splitted[1]);
        System.out.println("PLAINTEXT IS " + plaintext);
        byte[] computedHash = generateHmac(plaintext);
        System.out.println("CLIENT GENERATED HMAC " + convertBytetoStringofDigits(computedHash));
        return Arrays.equals(receivedHash, computedHash);
    }
    
    private Key getKey(String path) {
        try {
            FileInputStream fis = null;
            String pathToKey = path;
            byte[] keyBytes = new byte[1024];
            fis = new FileInputStream(pathToKey);
            fis.read(keyBytes);
            fis.close();
            byte[] input = Hex.decode(keyBytes);
            return new SecretKeySpec(input, "HmacSHA256");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String getPlaintextWithHmac() {
        return plaintextWithHmac;
    }
    
    /* Helping methods */
    
    private static String readFileAsString(String filePath) throws java.io.IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }

    public static String convertBytetoStringofDigits(byte[] b) {
        BigInteger big = new BigInteger(1, b);
        return big.toString(16);
    }

    public static byte[] convertStringofDigitstoByte(String s) {
        BigInteger big = new BigInteger(s, 16);
        return big.toByteArray();

    }
}
