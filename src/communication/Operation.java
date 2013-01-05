/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

/**
 *
 * @author sanker
 */
public interface Operation {
    
    public void   writeString(String s) throws OperationException;
    public String readString() throws OperationException;
}
