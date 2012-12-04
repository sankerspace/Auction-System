/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MyLogger;

import java.io.PrintWriter;
/**
 * TEST
 * @author sanker
 */
public class Log {
    private PrintWriter out=null;
    private boolean level_1;
    private boolean level_2;
    private boolean level_3;
    
    public Log(PrintWriter out)
    {
        level_1=true;
        this.out=out;
    }
    
    public Log(PrintWriter out,int level)
    {
        if(level==3)
        {   
            level_1=true;
            level_2=true;
            level_3=true;
        }else if(level==2)
        {
            level_1=true;
            level_2=true;
        }else
             level_1=true;
        this.out=out;
    }
    
    public synchronized void output(String s)
    {  
        if(level_1)
        {
            out.println(s);
            out.flush();
            System.out.flush();
        }
    }
    
     public synchronized void out(String s)
    {  
        if(level_1)
        {
            out.print(s);
            out.flush();
             System.out.flush();
        }
    }
    
    public synchronized void output(String s,int level)
    {  
        if((level==3)&&level_3)
        {
            out.println(s);
            out.flush();
             System.out.flush();
	    
        }else if((level==2)&&level_2)
        {
            out.println(s);
            out.flush();
             System.out.flush();
        }else if((level==1)&&level_1)
        {
            out.println(s);
            out.flush();
             System.out.flush();
        }
    }
    
    public  synchronized void setLevel(int level)
    {
        if(level==3)
        {   
            level_1=true;
            level_2=true;
            level_3=true;
        }else if(level==2)
        {
            level_1=true;
            level_2=true;
        }else
             level_1=true;
    }
    
    public synchronized void close()
    {
        out.close();
    }
}
