/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Event;

/**
 *
 * @author Marko
 */


public class UserEvent extends  Event {
    private String userName=null; 
    private long time=0;
    
    public UserEvent(String userName,UserEvent.UserEventType type)
    {
        super(type.name());
        this.userName=userName;

    }
    public UserEvent(UserEvent e)
    {
        super(e);
        this.userName=e.userName;
        this.time=e.time;
    }
    
    public String getUserName()
    {
        return this.userName;
    }
    
    public long getUserTime()
    {
        return this.time;
    }
    
    public void setTime(long time)
    {
        if(super.getType().contains(UserEventType.USER_LOGOUT.name())||
             super.getType().contains(UserEventType.USER_DISCONNECTED.name()) )
        {
           this.time=time; 
        }
    }
    public static enum UserEventType
    {
        USER_LOGIN,
        USER_LOGOUT,
        USER_DISCONNECTED
    }

    public String toString()
    {
        String msg = null;
        if(super.getType().contains("USER_LOGIN"))
        {
            msg="user "+this.userName+" logged in."; 
        }else  if(super.getType().contains("USER_LOGOUT"))
        {
             msg="user "+this.userName+" logged out."; 
        }else  if(super.getType().contains("USER_DISCONNECTED"))
             msg="user "+this.userName+" is disconnected."; 
        
        return (super.toString() + msg );
               
               
    }
   
}

