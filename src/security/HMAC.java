/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.util.encoders.Hex;

/**
 *
 * @author Dave
 */
public class HMAC {
    
    private String plaintextWithHmac = null;
    
    /**
     * Generates an Hmac from a message. Also a string will be created containing the message itself and the hash.
     *
     * @return Mac of the given message
     * @param message Message to which the hMac will be added
     */
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

    /**
     * Extracts the plaintext and hash from a string. Generates a new hMac of the plaintext
     * and compares it to the received hMac.
     * @param plaintextWithHash String containing the plaintext and the hash in form of a string.
     * @return true if byte arrays are equal. false otherwise.
     */
    public boolean validateHMac(String plaintextWithHash){
        String[] splitted = plaintextWithHash.split(" ");
        String plaintext = splitted[0];
        byte[] receivedHash = convertStringofDigitstoByte(splitted[1]);
        byte[] computedHash = generateHmac(plaintext);
        return Arrays.equals(receivedHash, computedHash);
    }
    
    private Key getKey(String path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            byte[] keyBytes = new byte[1024];
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

    public static String convertBytetoStringofDigits(byte[] b) {
        BigInteger big = new BigInteger(1, b);
        return big.toString(16);
    }

    public static byte[] convertStringofDigitstoByte(String s) {
        BigInteger big = new BigInteger(s, 16);
        return big.toByteArray();

    }
}
