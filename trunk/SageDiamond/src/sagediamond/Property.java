/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sagediamond;

/**
 *
 * @author jusjoken
 * - 10/10/2011 - class to handle properties that are a short list of values
 */
public class Property {
    private String Key = "";
    private String DisplayName = "";
    private Boolean Default = Boolean.FALSE;

    public Property(String Key, String DisplayName){
        this(Key,DisplayName,Boolean.FALSE);
    }
    public Property(String Key, String DisplayName, Boolean Default){
        this.Key = Key;
        this.DisplayName = DisplayName;
        this.Default = Default;
    }
    
    public Boolean IsDefault(){
        return Default;
    }
}
