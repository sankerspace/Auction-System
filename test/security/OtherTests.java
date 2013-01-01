/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package security;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 * @author sanker
 */
public class OtherTests {
    String pathtoServerKeys = null;
    String pathtoClientKeys = null;
    private Console console=null;
    public OtherTests() {
         pathtoServerKeys = "./keys/Server/";
         pathtoClientKeys = "./keys/Clients/";
         
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
     * Test of getPublicKey method, of class RSAAuthentication.
     */
    @Test
    public void testFindServerKeyDirectory() throws Exception {
        System.out.println("Test if a path to the server key directory is valid(linux and windows):");
        System.out.println("path to Server keys(from outside the test enviromnemt):"+
                "./keys/Server");
       
        File path=new File(pathtoServerKeys);
        assertEquals(true, path.isDirectory());
    }
    @Test
    public void testFindClientKeyDirectory() throws Exception {
        System.out.println("Test if a path to the client key directory is valid(linux and windows):");
        System.out.println("path to Client keys(from outside the test enviromnemt):"+
                "./keys/Clients");
        
        File path=new File(pathtoClientKeys);
        assertEquals(true, path.isDirectory());
    }
     @Test
     public void testFindClientKeyFile() throws Exception {
        System.out.println("Test if a path to the client key public file is valid(linux and windows):");
        System.out.println("path to Client file(from outside the test enviromnemt):"+
                "./keys/Clients/alice.public.pem");
        
        File publicKey=new File(pathtoClientKeys+"alice.pub.pem");
        assertEquals(true, publicKey.isFile());
    }
     
    // @Test
     public void testConsole() throws Exception {
       console=System.console();
       System.out.println("Console test:");
       console.printf("This is a output from a console");
       String s=this.console.readLine();
       System.out.println("Console input:"+s);
    } 
     
     @Test
     public void testSystemInBypass() throws Exception {
         System.out.println("\ntestSystemInBypass:");
       //Pipes and read writer
       PipedInputStream inputstream=new PipedInputStream();
       PipedOutputStream outputstream =new PipedOutputStream(inputstream);
      
       System.setIn(inputstream);
       //BufferedWriter out =new BufferedWriter(new OutputStreamWriter(outputstream));
       
       
       PrintWriter print=new PrintWriter(outputstream,true);
       BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
       
       String s1="Hallo",s2="wie gehts",s3="uns heute",s4="Danke mir geht es sehr gut.";
       String s =null;
       
       System.out.println("standard Input is bypassed by a PipedInputStream.");
       System.out.println("All read Methods will be performed on the new System.in");
       
       System.out.println("Write new Line with:'"+s1+"' to the propper PipedOutputStream:");
       print.println(s1);print.flush();
       System.out.println("Read String from System.in with a readLine method.");
       s=input.readLine();
       System.out.println("Read from System.in:"+s);
       Assert.assertArrayEquals("Strings are not equal", s.toCharArray(), s1.toCharArray());
       
       System.out.println("Write new Line with:'"+s2+"' to the propper PipedOutputStream:");
       print.println(s2);print.flush();
       System.out.println("Read String from System.in with a readLine method.");
       s=input.readLine();
       System.out.print("Read from System.in:"+s);
       Assert.assertArrayEquals("Strings are not equal", s.toCharArray(), s2.toCharArray());
       
       System.out.println("Write new Line with:'"+s3+"' to the propper PipedOutputStream:");
       print.println(s3);print.flush();
       System.out.println("Read String from System.in with a readLine method.");
       s=input.readLine();
       System.out.print("Read from System.in:"+s);
       Assert.assertArrayEquals("Strings are not equal", s.toCharArray(), s3.toCharArray());
       
       System.out.println("Write new Line with:'"+s4+"' to the propper PipedOutputStream:");
       print.println(s4);print.flush();
       System.out.println("Read String from System.in with a readLine method.");
       s=input.readLine();
       System.out.print("Read from System.in:"+s);
       Assert.assertArrayEquals("Strings are not equal", s.toCharArray(), s4.toCharArray());
       
    } 
     
   
    
    
}
