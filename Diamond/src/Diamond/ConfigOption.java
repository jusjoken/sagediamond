/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Diamond;

import java.util.List;
import org.apache.log4j.Logger;
import sagex.phoenix.factory.ConfigurableOption;

/**
 *
 * @author jusjoken
 */
public class ConfigOption extends ConfigurableOption {

    private String PropLocation = "";
    static private final Logger LOG = Logger.getLogger(ConfigOption.class);
    public ConfigOption(String PropertyLocation, String name, String value) {
        super(name, value);
        PropLocation = PropertyLocation;
    }

    public ConfigOption(String PropertyLocation, String name, String label, String value, DataType dataType, boolean isList, ListSelection listSelection, String listValues) {
        super(name, label, value, dataType, isList, listSelection, listValues);
        PropLocation = PropertyLocation;
    }

    public ConfigOption(String PropertyLocation, String name, String label, String value, DataType dataType, boolean isList, ListSelection listSelection, List<ListValue> listValues) {
        super(name, label, value, dataType, isList, listSelection, listValues);
        PropLocation = PropertyLocation;
    }

    public ConfigOption(String PropertyLocation, String name, String label, String value, DataType dataType) {
        super(name, label, value, dataType);
        PropLocation = PropertyLocation;
    }

    public ConfigOption(String PropertyLocation, String name) {
        super(name);
        PropLocation = PropertyLocation;
    }

    public ConfigOption(String PropertyLocation, ConfigurableOption inOpt) {
        super(inOpt.getName(), inOpt.getLabel(), inOpt.value().getValue(), inOpt.getDataType(), inOpt.isList(), inOpt.getListSelection(), inOpt.getListValues());
        PropLocation = PropertyLocation;
    }

    public String GetValue(){
        //get the value from the properties file
        //if not found then use NotSet
        String tReturn = util.GetProperty(PropLocation + getName(), SourceUI.OptionNotSet);
        if (isList() && !tReturn.equals(SourceUI.OptionNotSet)){
            //check that this is a valid value
            Boolean found = Boolean.FALSE;
            for (ListValue tItem: getListValues()){
                if (tItem.getValue().equals(tReturn)){
                    found = Boolean.TRUE;
                    break;
                }
            }
            if (!found){
                LOG.debug("GetValue: for '" + getName() + "' stored value '" + tReturn + "' is not a valid value. Returning NotSet. PropertyLocation '" + PropLocation + "'");
                tReturn = SourceUI.OptionNotSet;
            }
        }
        return tReturn;
    }
    public String GetValueLabel(){
        //get the Label for the List Item
        //if not found then use NotSet
        String tReturn = util.GetProperty(PropLocation + getName(), SourceUI.OptionNotSet);
        if (isList() && !tReturn.equals(SourceUI.OptionNotSet)){
            //check that this is a valid value and get the Label
            Boolean found = Boolean.FALSE;
            for (ListValue tItem: getListValues()){
                if (tItem.getValue().equals(tReturn)){
                    found = Boolean.TRUE;
                    tReturn = tItem.getName();
                    break;
                }
            }
            if (!found){
                LOG.debug("GetValue: for '" + getName() + "' stored value '" + tReturn + "' is not a valid value. Returning NotSet. PropertyLocation '" + PropLocation + "'");
                tReturn = SourceUI.OptionNotSet;
            }
        }
        return tReturn;
    }
    public void SetNext(){
        if (isList()){
            //change the value to the next value in the list or NotSet if at the last entry already
        }
    }
    
}
