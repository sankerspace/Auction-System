/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import org.bouncycastle.util.encoders.Base64;

/**
 *
 * @author sanker
 */
public class Base64Encoder {
    
    
    public static byte[] encodeBase64(byte[] plaindata)
    {
        byte[] base64Message = Base64.encode(plaindata);
        return base64Message;
    }
    
    public static byte[] decodeBase64(byte[] base64Message)
    {
        byte[] plaindata = Base64.decode(base64Message);
        return plaindata;
    }
}
