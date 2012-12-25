/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sanker
 */
public class EasySecureTest {
    
    public EasySecureTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of createUserandPass method, of class EasySecure.
     */
   @Test
    public void TestByteOperation()  {
        System.out.println("\nTestByteOperation:");
        System.out.println("makeByte('StanisicMarko:)'");
        byte[] array=EasySecure.makeBytes("StanisicMarko");
       
        String s=EasySecure.BytestoString(array);
         System.out.println("BytestoString():"+s);
        
        
    }
    
    @Test
    public void TestConvertOperation()  {
        System.out.println("\nTestConvertOperation:");
        
        byte[] array=EasySecure.convertStringofDigitstoByte("123456789");
        System.out.println("convert '123456789':"+new String(array));
        String s=EasySecure.convertBytetoStringofDigits(array);
        System.out.println("convert back:"+s);
        
        
    }
    
    @Test
    public void TestConvertOperation_2()  {
        System.out.println("\nTestConvertOperation_2:");
        String s1="Hallo",s2=null,s3=null,s4=null;
        //byte[] array=EasySecure.convertStringofDigitstoByte("123456789");
        System.out.println("Convert String in Digit Representation:"+s1);
        byte[] array = s1.getBytes();
        s2=EasySecure.convertBytetoStringofDigits(array);
        System.out.println("String representation of byte Array:"+s2);
        
        byte[] array_2=EasySecure.convertStringofDigitstoByte(s2);
        s4=new String(array_2);
        
        
        System.out.println("convert back to original String:"+s4);
        
        
    }
    
   @Test
    public void TestCodingOperation()  {
        String enc=null,dec=null;
        String load="A";
        String algo="MD5";
        System.out.println("\nTestCodingOperation:");
        System.out.println(new String(load.getBytes()));
        System.out.println("encode String:"+load);
        enc=EasySecure.encodeString(load, algo);
        System.out.println("result:"+enc);
        
       
      
  
    }
    
    
   
   
}
