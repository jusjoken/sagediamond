/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Diamond;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.log4j.Logger;

/**
 *
 * @author jusjoken
 */
public class SourceUI {
    static private final Logger LOG = Logger.getLogger(SourceUI.class);
    private String thisFlowName = "";
    private Integer thisLevels = 0;
    private SortedMap<Integer,PresentationUI> thisUIList = new TreeMap<Integer,PresentationUI>();
    public static enum OrganizerType{GROUP,SORT};
    public static final String OptionNotSet = "NotSet";
    
    public SourceUI(String FlowName){
        this.thisFlowName = FlowName;
        //based on the FlowName load the settings into this class from the properties file
        Refresh();
    }
    
    public void Refresh(){
        thisUIList.clear();
        Integer counter = 0;
        PresentationUI tUI = null;
        do {
            tUI = new PresentationUI(thisFlowName,counter);
            counter++;
            if (tUI.HasContent()){
                thisUIList.put(counter, tUI);
            }
        } while (tUI.HasContent());
        thisLevels = thisUIList.size();
        LOG.debug(LogMessage());
    }
    
    public String Source(){
        return Flow.GetFlowSource(thisFlowName);
    }
    public String Name(){
        return "source:" + thisFlowName;
    }
    public String Label(){
        return Flow.GetFlowName(thisFlowName);
    }
    public String IsFlat(){
        String tProp = Const.FlowSourceUI + Const.PropDivider + "IsFlat";
        return Flow.GetOptionName(thisFlowName, tProp, OptionNotSet);
    }
    public String PruneSingleItemFolders(){
        String tProp = Const.FlowSourceUI + Const.PropDivider + "PruneSingleItemFolders";
        return Flow.GetOptionName(thisFlowName, tProp, OptionNotSet);
    }
    
    //Presentation specific settings
    public Boolean HasPresentation(){
        if (!thisUIList.isEmpty()){
            return Boolean.TRUE;
        }else{
            if (IsSet(IsFlat()) || IsSet(PruneSingleItemFolders())){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
    public void ClearPresentation(){
        String tProp = Flow.GetFlowBaseProp(thisFlowName) + Const.PropDivider + Const.FlowSourceUI;
        util.RemovePropertyAndChildren(tProp);
        Refresh();
    }
    public Integer Levels(){
        return thisLevels;
    }
    public HashSet<PresentationUI> UIList(){
        LinkedHashSet<PresentationUI> tList = new LinkedHashSet<PresentationUI>();
        for (PresentationUI tUI:thisUIList.values()){
            tList.add(tUI);
        }
        
        return tList;
    }
    public String LogMessage(){
        String tMess = Label() + "-'" + Source() + "'Flat'" + IsFlat() + "'Prune'" + PruneSingleItemFolders() + "'Levels'" + thisLevels + "'-";
        for (PresentationUI tUI: UIList()){
            tMess = tMess + "[" + tUI.LogMessage() + "]";
        }
        return tMess;
    }
    
    public static String GetPresentationProp(Integer Level){
        return Const.FlowSourceUI + Const.PropDivider + Const.FlowPresentation + Const.PropDivider + Level.toString() + Const.PropDivider;
    }
    public static String GetOrgValue(String FlowName, String OrgType, Integer Level, String Option){
        String tProp = GetPresentationProp(Level) + OrgType + Const.PropDivider + Option;
        return Flow.GetOptionName(FlowName, tProp, OptionNotSet);
    }
    public static void SetOrgValue(String FlowName, String OrgType, Integer Level, String Option, String NewValue){
        String tProp = GetPresentationProp(Level) + OrgType + Const.PropDivider + Option;
        Flow.SetOption(FlowName, tProp, NewValue);
    }
    public static Boolean IsSet(String Value){
        if (Value.equals(OptionNotSet)){
            return Boolean.FALSE;
        }else{
            return Boolean.TRUE;
        }
    }

}
