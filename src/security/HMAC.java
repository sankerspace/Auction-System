package security;

import java.io.FileInputStream;
import java.io.IOException;
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

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(HMAC.class.getSimpleName());
    private String plaintextWithHmac = null;
    Mac hMac = null;
    Key secretKey = null;

    public HMAC() throws HMACException, NoSuchAlgorithmException {
        secretKey = getKey("keys/Clients/alice.key");
        hMac = Mac.getInstance("HmacSHA256");
    }

    /**
     * Generates an Hmac from a message. Also a string will be created
     * containing the message itself and the hash.
     *
     * @return Mac of the given message
     * @param message Message to which the hMac will be added
     */
    public byte[] generateHmac(String message) throws HMACException {
        try {
            hMac.init(secretKey);
            byte[] messageBytes = message.getBytes();
            hMac.update(messageBytes);

            byte[] hmacByte = hMac.doFinal();
            String h = new String(Base64Encoder.encodeBase64(hmacByte));
            plaintextWithHmac = message + " " + h;
            return hmacByte;
        } catch (InvalidKeyException ex) {
            throw new HMACException("HMAC:InvalidKeyException:" + ex.getMessage());
        }
    }

    /**
     * Extracts the plaintext and hash from a string. Generates a new hMac of
     * the plaintext and compares it to the received hMac.
     *
     * @param plaintextWithHash String containing the plaintext and the hash in
     * form of a string.
     * @return true if byte arrays are equal. false otherwise.
     */
    public boolean validateHMac(String plaintextWithHash) throws HMACException {
        String[] splitted = plaintextWithHash.split(" ");
        String plaintext = splitted[0];
        byte[] receivedHash = Base64Encoder.decodeBase64(splitted[1].getBytes());
        byte[] computedHash = generateHmac(plaintext);
        return Arrays.equals(receivedHash, computedHash);
    }

    private Key getKey(String path) throws HMACException {
        try {
            FileInputStream fis = new FileInputStream(path);
            byte[] keyBytes = new byte[1024];
            fis.read(keyBytes);
            fis.close();
            byte[] input = Hex.decode(keyBytes);
            return new SecretKeySpec(input, "HmacSHA256");
        } catch (IOException ex) {
            throw new HMACException("HMAC:IOException:" + ex.getMessage());
        }
    }

    public String getPlaintextWithHmac() {
        return plaintextWithHmac;
    }
}
