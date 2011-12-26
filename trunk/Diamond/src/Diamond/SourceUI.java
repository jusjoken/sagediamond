/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Diamond;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import sagex.phoenix.factory.ConfigurableOption.ListValue;
import sagex.phoenix.factory.IConfigurable;

/**
 *
 * @author jusjoken
 */
public class SourceUI {
    static private final Logger LOG = Logger.getLogger(SourceUI.class);
    private String thisFlowName = "";
    private Boolean thisHasPresentation = Boolean.FALSE;
    private Integer thisLevels = 0;
    private SortedMap<Integer,PresentationUI> thisUIList = new TreeMap<Integer,PresentationUI>();
    public static enum OrganizerType{GROUP,SORT};
    
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
        thisHasPresentation = !thisUIList.isEmpty();
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
        return Flow.GetOptionName(thisFlowName, tProp, Boolean.TRUE.toString());
    }
    public String PruneSingleItemFolders(){
        String tProp = Const.FlowSourceUI + Const.PropDivider + "PruneSingleItemFolders";
        return Flow.GetOptionName(thisFlowName, tProp, Boolean.TRUE.toString());
    }
    
    //Presentation specific settings
    public Boolean HasPresentation(){
        return thisHasPresentation;
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
        return Flow.GetOptionName(FlowName, tProp, util.OptionNotFound);
    }
    public static void SetOrgValue(String FlowName, String OrgType, Integer Level, String Option, String NewValue){
        String tProp = GetPresentationProp(Level) + OrgType + Const.PropDivider + Option;
        Flow.SetOption(FlowName, tProp, NewValue);
    }
    //Expose UI functions to Sage
    public static Integer GetUILevel(PresentationUI inUI){
        return inUI.Level();
    }
    
//    public class UI{
//
//        private Organizer thisGroupBy = null;
//        private Organizer thisSortBy = null;
//        private Integer thisLevel = 0;
//        //HasContent is set to true if properties are found for this UI
//        private Boolean HasContent = Boolean.FALSE;
//        
//        public UI(String FlowName, Integer Level){
//            this.thisLevel = Level;
//            this.thisGroupBy = new Organizer(FlowName,Level,OrganizerType.GROUP);
//            this.thisSortBy = new Organizer(FlowName,Level,OrganizerType.SORT);
//            if (thisGroupBy.HasContent() || thisSortBy.HasContent()){
//                HasContent = Boolean.TRUE;
//            }
//        }
//        public Boolean HasContent(){
//            return HasContent;
//        }
//        public Organizer Group(){
//            return this.thisGroupBy;
//        }
//        public Organizer Sort(){
//            return this.thisSortBy;
//        }
//        public Integer Level(){
//            return this.thisLevel;
//        }
//        public String LogMessage(){
//            String tMess = "Level-";
//            tMess = tMess + thisLevel + "|" + thisGroupBy.LogMessage() + "|" + thisSortBy.LogMessage();
//            return tMess;
//        }
//
//        public class Organizer{
//
//            private String Name = "";
//            private String optIgnoreAll = "";
//            private String optIgnoreThe = "";
//            private String optEmptyFoldername = "";
//            private String optPruneSingleItemGroups = "";
//            private HashMap<String,String> optList = new HashMap<String, String>();
//            private OrganizerType thisType = null;
//            //HasContent is set to true if properties are found for this organizer
//            private Boolean HasContent = Boolean.FALSE;
//
//            public Organizer(String FlowName, Integer Level, OrganizerType Type){
//                thisType = Type;
//                IConfigurable tOrganizer = null;
//                String tName = GetOrgValue(FlowName, Type.toString(), Level, "Name");
//                if (!tName.equals(util.OptionNotFound)){
//                    this.Name = tName;
//                    this.HasContent = Boolean.TRUE;
//                    if (Type.equals(OrganizerType.GROUP)){
//                        tOrganizer = phoenix.umb.CreateGrouper(this.Name);
//                    }else{
//                        tOrganizer = phoenix.umb.CreateSorter(this.Name);
//                    }
//                    //LOG.debug(myType() + ": '" + this.Name + "' OptionsList '" + tOrganizer.getOptionNames() + "'");
//                    for (String tOpt: tOrganizer.getOptionNames()){
//                        String tValue = GetOrgValue(FlowName, Type.toString(), Level, tOpt);
//                        if (!tValue.equals(util.OptionNotFound)){
//                            optList.put(tOpt, tValue);
//                        }
//                        
//                    }
//                }
////                if (Type.equals(OrganizerType.GROUP)){
////                    if (Level==0){
////                        this.Name = "show";
////                        this.HasContent = Boolean.TRUE;
////                        optList.put("empty-foldername", "EMPTY");
////                        optList.put("prune-single-item-groups", "true");
////                    }else if (Level==1){
////                        this.Name = "season";
////                        this.HasContent = Boolean.TRUE;
////                    }
////                    if (this.HasContent){
////                        tOrganizer = phoenix.umb.CreateGrouper(this.Name);
////                    }
////                }else{
////                    if (Level==0 || Level==1){
////                        this.Name = "title";
////                        this.HasContent = Boolean.TRUE;
////                        optList.put("ignore-all", "true");
////                    }
////                    if (this.HasContent){
////                        tOrganizer = phoenix.umb.CreateSorter(this.Name);
////                    }
////                }
//                if (this.HasContent){
//                    LOG.debug(myType() + ": '" + this.Name + "' OptionsList '" + tOrganizer.getOptionNames() + "'");
//                    for (String tOpt: tOrganizer.getOptionNames()){
//                        if (tOrganizer.getOption(tOpt).isList()){
//                            for (ListValue Item: tOrganizer.getOption(tOpt).getListValues()){
//                                LOG.debug(myType() + ": Option '" + tOpt + "' Name '" + Item.getName() + "' Value '" + Item.getValue() + "'");
//                            }
//                        }else{
//                            LOG.debug(myType() + ": Option '" + tOpt + "' Not a list");
//                        }
//                    }
//                }
//            }
//            public Boolean HasContent(){
//                return HasContent;
//            }
//            public String Name(){
//                return this.Name;
//            }
//            public String myType(){
//                return thisType.toString();
//            }
//            public HashMap<String,String> optList(){
//                return optList;
//            }
//            public String LogMessage(){
//                String tMess = myType() + "-";
//                tMess = tMess + Name;
//                for (String tOpt:optList().keySet()){
//                    tMess = tMess + ":" + tOpt + "=" + optList.get(tOpt);
//                }
//                return tMess;
//            }
//        }
//
//    }

}
