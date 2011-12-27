/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Diamond;

import Diamond.SourceUI.OrganizerType;
import java.util.HashMap;
import java.util.HashSet;
import org.apache.log4j.Logger;
import sagex.phoenix.factory.ConfigurableOption.ListValue;
import sagex.phoenix.factory.IConfigurable;

/**
 *
 * @author jusjoken
 */
public class PresentationOrg {
    static private final Logger LOG = Logger.getLogger(PresentationOrg.class);
    private String thisFlowName = "";
    private String PropLocation = "";
    private String Name = "";
    private HashSet<ConfigOption> ConfigOptions = new HashSet<ConfigOption>();
    private OrganizerType thisType = null;
    //HasContent is set to true if properties are found for this organizer
    private Boolean HasContent = Boolean.FALSE;

    public PresentationOrg(String FlowName, Integer Level, OrganizerType Type){
        thisFlowName = FlowName;
        thisType = Type;
        IConfigurable tOrganizer = null;
        PropLocation = SourceUI.GetPropertyLocation(FlowName, Type.toString(), Level);
        String tName = SourceUI.GetOrgValue(FlowName, Type.toString(), Level, "Name");
        if (!tName.equals(SourceUI.OptionNotSet)){
            this.Name = tName;
            this.HasContent = Boolean.TRUE;
            if (Type.equals(OrganizerType.GROUP)){
                tOrganizer = phoenix.umb.CreateGrouper(this.Name);
            }else{
                tOrganizer = phoenix.umb.CreateSorter(this.Name);
            }
            //LOG.debug(myType() + ": '" + this.Name + "' OptionsList '" + tOrganizer.getOptionNames() + "'");
            for (String tOpt: tOrganizer.getOptionNames()){
                ConfigOption tConfig = new ConfigOption(PropLocation, tOrganizer.getOption(tOpt));
                ConfigOptions.add(tConfig);
            }
        }
    }
    public Boolean HasContent(){
        return HasContent;
    }
    public String Name(){
        return this.Name;
    }
    public String myType(){
        return thisType.toString();
    }
    public HashSet<ConfigOption> ConfigOptions(){
        //TODO: needs to be sorted by the Label
        return ConfigOptions;
    }
    public String LogMessage(){
        String tMess = myType() + "-";
        tMess = tMess + Name;
        for (ConfigOption tConfig: ConfigOptions){
            tMess = tMess + ":" + tConfig.getName() + "=" + tConfig.GetValue() + "(" + tConfig.GetValueLabel() + ")";
        }
        return tMess;
    }
    
}
