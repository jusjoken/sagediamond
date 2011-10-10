/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sagediamond;

import java.util.LinkedHashMap;

/**
 *
 * @author jusjoken
 * - 10/10/2011 - class to handle properties that are a short list of values
 */
//public class PropList<String,Property> extends LinkedHashMap<String,sagediamond.Property> {
public class PropList extends LinkedHashMap<String,sagediamond.Property> {
    
    public PropList(){
        
    }
    
    //returns the Property with the Default set or the first Property if no Default set
    public Property GetDefault(){
        sagediamond.Property FirstItem = null;
        Integer counter = 0;
        for(sagediamond.Property Item:this.values()){
            counter++;
            if(counter==1){
                FirstItem = Item;
            }
            if(Item.IsDefault()){
                return Item;
            }
        }
        return FirstItem;
    }
}
