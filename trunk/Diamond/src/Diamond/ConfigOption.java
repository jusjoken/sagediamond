/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Diamond;

import java.util.List;
import sagex.phoenix.factory.ConfigurableOption;

/**
 *
 * @author jusjoken
 */
public class ConfigOption extends ConfigurableOption {

    private String thisFlowName = "";
    public ConfigOption(String FlowName, String name, String value) {
        super(name, value);
        thisFlowName = FlowName;
    }

    public ConfigOption(String FlowName, String name, String label, String value, DataType dataType, boolean isList, ListSelection listSelection, String listValues) {
        super(name, label, value, dataType, isList, listSelection, listValues);
        thisFlowName = FlowName;
    }

    public ConfigOption(String FlowName, String name, String label, String value, DataType dataType, boolean isList, ListSelection listSelection, List<ListValue> listValues) {
        super(name, label, value, dataType, isList, listSelection, listValues);
        thisFlowName = FlowName;
    }

    public ConfigOption(String FlowName, String name, String label, String value, DataType dataType) {
        super(name, label, value, dataType);
        thisFlowName = FlowName;
    }

    public ConfigOption(String FlowName, String name) {
        super(name);
        thisFlowName = FlowName;
    }

    public ConfigOption(String FlowName, ConfigurableOption inOpt) {
        super(inOpt.getName(), inOpt.getLabel(), "", inOpt.getDataType(), inOpt.isList(), inOpt.getListSelection(), inOpt.getListValues());
        thisFlowName = FlowName;
    }

    public String GetValue(){
        //get the value from the properties file
        //if not found then use NotSet
        return "testing";
    }
    public void SetNext(){
        if (isList()){
            //change the value to the next value in the list or NotSet if at the last entry already
        }
    }
    
}
