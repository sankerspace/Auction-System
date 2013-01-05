/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.BufferedInputStream;

/**
 *
 * @author sanker
 */
public class OperationTCP implements Operation{
    
    private Client client = null;
    private OutputStream out = null;
    private InputStream in = null;
    /*
     * Konstruktor
     */
    public OperationTCP(Client c) throws OperationException
    {   try
        {
            client = c;
            in = client.getInputStream();
            out = client.getOutputStream();
            
        }catch(ClientException e)
        {
            throw new OperationException("ClientException::"+e.getMessage());
        }
        
    }
    
    /*
     * Methode 
     */
    public void writeString(String s)throws OperationException
    {
       try
        {
            DataOutputStream w = new DataOutputStream(out);
            w.writeUTF(s);
            
        }catch(IOException e)
        {
            throw new OperationException("IOException::"+e.getMessage());
        }
        
       
    }
    
    
     public String readString()throws OperationException
    {
        
       try
        {
            DataInputStream r = new DataInputStream(in);
            return r.readUTF();
            
        }catch(IOException e)
        {
            throw new OperationException("IOException::"+e.getMessage());
        }
    }
   
    /*
    public void writeLine(String s)throws OperationException
    {
       try
        {
            PrintWriter w = new PrintWriter(out);
            w.println(s);
            
        }catch(Exception e)
        {
            throw new OperationException("Exception::"+e.getMessage());
        }
        
       
    }
    
    
  
    public String readLine()throws OperationException
    {
       try
        {
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            return r.readLine();
            
        }catch(Exception e)
        {
            throw new OperationException("Exception::"+e.getMessage());
        }
    }
    
    
    
    
    
    
    public void writeByteArray(byte[] b)throws OperationException
    {
        try
        {
            out.write(b);
           
            
            
        }catch(Exception e)
        {
            throw new OperationException("Exception::"+e.getMessage());
        }
    }
    */
    
}
