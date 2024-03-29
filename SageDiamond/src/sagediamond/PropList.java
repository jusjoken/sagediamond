/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sagediamond;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author jusjoken
 * - 10/10/2011 - class to handle properties that are a short list of values
 */
//public class PropList<String,Property> extends LinkedHashMap<String,sagediamond.Property> {
public class PropList extends LinkedHashMap<String,sagediamond.Property> {
    
    //private List<String> DisplayNames = new ArrayList<String>();
    public PropList(){
        
    }
    
    //returns the Property with the Default set or the first Property if no Default set
    public Property GetDefault(){
        for(sagediamond.Property Item:this.values()){
            if(Item.IsDefault()){
                return Item;
            }
        }
        return Const.NotFoundProp;
    }
    
    //returns the Property with the same Boolean Value
    public Property GetBoolean(Boolean InValue){
        for(sagediamond.Property Item:this.values()){
            if(Item.BoolValue().equals(InValue)){
                return Item;
            }
        }
        return Const.NotFoundProp;
    }
    //returns the Name of the Property with the same Boolean Value
    public String GetBooleanName(Boolean InValue){
        for(sagediamond.Property Item:this.values()){
            if(Item.BoolValue().equals(InValue)){
                return Item.DisplayName();
            }
        }
        return Const.NotFoundProp.DisplayName();
    }
}
