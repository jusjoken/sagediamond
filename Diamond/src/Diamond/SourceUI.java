/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Diamond;

import java.util.HashMap;
import java.util.HashSet;
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
    private HashSet<UI> thisUIList = new HashSet<UI>();
    public static enum OrganizerType{GROUP,SORT};
    
    public SourceUI(String FlowName){
        this.thisFlowName = FlowName;
        //based on the FlowName load the settings into this class from the properties file
        thisLevels = 2;
        for (Integer i=0;i<thisLevels;i++){
            thisUIList.add(new UI(i));
        }
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
    public Integer Levels(){
        return thisLevels;
    }
    public HashSet<UI> UIList(){
        return thisUIList;
    }
    public String LogMessage(){
        String tMess = Label() + "-'" + Source() + "'Flat'" + IsFlat() + "'Prune'" + PruneSingleItemFolders() + "'-";
        for (SourceUI.UI tUI: UIList()){
            tMess = tMess + "[" + tUI.LogMessage() + "]";
        }
        return tMess;
    }
    
    public class UI{

        private Organizer thisGroupBy = null;
        private Organizer thisSortBy = null;
        private Integer thisLevel = 0;
        //HasContent is set to true if properties are found for this UI
        private Boolean HasContent = Boolean.FALSE;
        
        public UI(Integer Level){
            this.thisLevel = Level;
            this.thisGroupBy = new Organizer(Level,OrganizerType.GROUP);
            this.thisSortBy = new Organizer(Level,OrganizerType.SORT);
            if (thisGroupBy.HasContent() || thisSortBy.HasContent()){
                HasContent = Boolean.TRUE;
            }
        }
        public Boolean HasContent(){
            return HasContent;
        }
        public Organizer Group(){
            return this.thisGroupBy;
        }
        public Organizer Sort(){
            return this.thisSortBy;
        }
        public Integer Level(){
            return this.thisLevel;
        }
        public String LogMessage(){
            String tMess = "Level-";
            tMess = tMess + thisLevel + "|" + thisGroupBy.LogMessage() + "|" + thisSortBy.LogMessage();
            return tMess;
        }

        public class Organizer{

            private String Name = "";
            private String optIgnoreAll = "";
            private String optIgnoreThe = "";
            private String optEmptyFoldername = "";
            private String optPruneSingleItemGroups = "";
            private HashMap<String,String> optList = new HashMap<String, String>();
            private OrganizerType thisType = null;
            //HasContent is set to true if properties are found for this organizer
            private Boolean HasContent = Boolean.FALSE;

            public Organizer(Integer Level, OrganizerType Type){
                thisType = Type;
                IConfigurable tOrganizer = null;
                if (Type.equals(OrganizerType.GROUP)){
                    if (Level==0){
                        this.Name = "show";
                        this.HasContent = Boolean.TRUE;
                        optList.put("empty-foldername", "EMPTY");
                        optList.put("prune-single-item-groups", "true");
                    }else{
                        this.Name = "season";
                        this.HasContent = Boolean.TRUE;
                    }
                    tOrganizer = phoenix.umb.CreateGrouper(this.Name);
                }else{
                    this.Name = "title";
                    this.HasContent = Boolean.TRUE;
                    optList.put("ignore-all", "true");
                    tOrganizer = phoenix.umb.CreateSorter(this.Name);
                }
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

    }

}
