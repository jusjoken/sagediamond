/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Diamond;

import Diamond.SourceUI.OrganizerType;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import sagex.phoenix.factory.IConfigurable;

/**
 *
 * @author jusjoken
 */
public class PresentationOrg {
    static private final Logger LOG = Logger.getLogger(PresentationOrg.class);
    private String thisFlowName = "";
    private String PropLocation = "";
    private String Name = SourceUI.OptionNotSet;
    private String Label = SourceUI.OptionNotSet;
    private Integer thisLevel = 0;
    private IConfigurable thisOrganizer = null;
    private SortedMap<String,ConfigOption> ConfigOptionsList = new TreeMap<String,ConfigOption>();
    private OrganizerType thisType = null;
    //HasContent is set to true if properties are found for this organizer
    private Boolean HasContent = Boolean.FALSE;

    public PresentationOrg(String FlowName, Integer Level, OrganizerType Type){
        thisFlowName = FlowName;
        thisType = Type;
        thisLevel = Level;
        PropLocation = SourceUI.GetPropertyLocation(FlowName, Type.toString(), Level);
        String tName = SourceUI.GetOrgValue(FlowName, Type.toString(), Level, "Name");
        if (!tName.equals(SourceUI.OptionNotSet)){
            this.Name = tName;
            Refresh();
        }
    }
    public void Refresh(){
        this.HasContent = Boolean.TRUE;
        if (thisType.equals(OrganizerType.GROUP)){
            thisOrganizer = phoenix.umb.CreateGrouper(this.Name);
        }else{
            thisOrganizer = phoenix.umb.CreateSorter(this.Name);
        }
        this.Label = Source.GetOrganizerName(this.Name, thisType.toString());
        ConfigOptionsList.clear();
        for (String tOpt: thisOrganizer.getOptionNames()){
            ConfigOption tConfig = new ConfigOption(PropLocation, thisOrganizer.getOption(tOpt));
            ConfigOptionsList.put(tConfig.getLabel(), tConfig);
        }
    }
    public void SetOrg(String NewName){
        //TODO: may want to clear old properties for this Org based on the old Name before changing it
        SourceUI.SetOrgValue(thisFlowName, thisType.toString(), thisLevel, "Name", NewName);
        this.Name = NewName;
        Refresh();
    }
    public Boolean HasContent(){
        return HasContent;
    }
    public String Name(){
        return this.Name;
    }
    public String Label(){
        return this.Label;
    }
    public String myType(){
        return thisType.toString();
    }
    public Boolean HasConfigOptions(){
        return !ConfigOptionsList.isEmpty();
    }
    public Boolean HasConfigOptionsSet(){
        for (ConfigOption tConfig: ConfigOptionsList.values()){
            if (tConfig.IsSet()){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
    public HashSet<ConfigOption> ConfigOptions(){
        LinkedHashSet<ConfigOption> tList = new LinkedHashSet<ConfigOption>();
        for (ConfigOption tConfig:ConfigOptionsList.values()){
            tList.add(tConfig);
        }
        return tList;
    }
    public String LogMessage(){
        String tMess = myType() + "-";
        tMess = tMess + Name;
        for (ConfigOption tConfig: ConfigOptionsList.values()){
            tMess = tMess + ":" + tConfig.getName() + "=" + tConfig.GetValue() + "(" + tConfig.GetValueLabel() + ")";
        }
        return tMess;
    }
    
}
