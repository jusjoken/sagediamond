/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Diamond;

import Diamond.SourceUI.OrganizerType;
import java.util.HashMap;
import org.apache.log4j.Logger;
import sagex.phoenix.factory.ConfigurableOption.ListValue;
import sagex.phoenix.factory.IConfigurable;

/**
 *
 * @author jusjoken
 */
public class PresentationOrg {
    static private final Logger LOG = Logger.getLogger(PresentationOrg.class);
    private String Name = "";
    private String optIgnoreAll = "";
    private String optIgnoreThe = "";
    private String optEmptyFoldername = "";
    private String optPruneSingleItemGroups = "";
    private HashMap<String,String> optList = new HashMap<String, String>();
    private OrganizerType thisType = null;
    //HasContent is set to true if properties are found for this organizer
    private Boolean HasContent = Boolean.FALSE;

    public PresentationOrg(String FlowName, Integer Level, OrganizerType Type){
        thisType = Type;
        IConfigurable tOrganizer = null;
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
                String tValue = SourceUI.GetOrgValue(FlowName, Type.toString(), Level, tOpt);
                if (!tValue.equals(SourceUI.OptionNotSet)){
                    optList.put(tOpt, tValue);
                }

            }
        }
        if (this.HasContent){
            LOG.debug(myType() + ": '" + this.Name + "' OptionsList '" + tOrganizer.getOptionNames() + "'");
            for (String tOpt: tOrganizer.getOptionNames()){
                if (tOrganizer.getOption(tOpt).isList()){
                    for (ListValue Item: tOrganizer.getOption(tOpt).getListValues()){
                        LOG.debug(myType() + ": Option '" + tOpt + "' Name '" + Item.getName() + "' Value '" + Item.getValue() + "'");
                    }
                }else{
                    LOG.debug(myType() + ": Option '" + tOpt + "' Not a list");
                }
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
    public HashMap<String,String> optList(){
        return optList;
    }
    public String LogMessage(){
        String tMess = myType() + "-";
        tMess = tMess + Name;
        for (String tOpt:optList().keySet()){
            tMess = tMess + ":" + tOpt + "=" + optList.get(tOpt);
        }
        return tMess;
    }
    
}
