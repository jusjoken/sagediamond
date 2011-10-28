/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Diamond;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author jusjoken
 * - 10/10/2011 - class to handle properties that are a short list of values
 */
public class PropList extends LinkedHashMap<String,Diamond.Property> {
    
    //private List<String> DisplayNames = new ArrayList<String>();
    public PropList(){
        
    }
    
    //returns the Property with the Default set or the first Property if no Default set
    public Property GetDefault(){
        for(Diamond.Property Item:this.values()){
            if(Item.IsDefault()){
                return Item;
            }
        }
        return Const.NotFoundProp;
    }
    
    //returns the Property with the same Boolean Value
    public Property GetBoolean(Boolean InValue){
        for(Diamond.Property Item:this.values()){
            if(Item.BoolValue().equals(InValue)){
                return Item;
            }
        }
        return Const.NotFoundProp;
    }
    //returns the Name of the Property with the same Boolean Value
    public String GetBooleanName(Boolean InValue){
        for(Diamond.Property Item:this.values()){
            if(Item.BoolValue().equals(InValue)){
                return Item.DisplayName();
            }
        }
        return Const.NotFoundProp.DisplayName();
    }
}
