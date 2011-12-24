/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Diamond;

import java.util.HashMap;
import java.util.HashSet;
import org.apache.log4j.Logger;

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

        private GroupBy thisGroupBy = null;
        private SortBy thisSortBy = null;
        private Integer thisLevel = 0;
        
        public UI(Integer Level){
            this.thisLevel = Level;
            this.thisGroupBy = new GroupBy(Level);
            this.thisSortBy = new SortBy(Level);
        }
        public GroupBy Group(){
            return this.thisGroupBy;
        }
        public SortBy Sort(){
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

        public class GroupBy{

            private String Name = "";
            private String optIgnoreAll = "";
            private String optIgnoreThe = "";
            private String optEmptyFoldername = "";
            private String optPruneSingleItemGroups = "";
            private HashMap<String,String> optList = new HashMap<String, String>();

            public GroupBy(Integer Level){
                if (Level==0){
                    this.Name = "show";
                    optList.put("empty-foldername", "EMPTY");
                    optList.put("prune-single-item-groups", "true");
                }else{
                    this.Name = "season";
                }
            }
            public String Name(){
                return this.Name;
            }
            public HashMap<String,String> optList(){
                return optList;
            }
            public String LogMessage(){
                String tMess = "GroupBy-";
                tMess = tMess + Name;
                for (String tOpt:optList().keySet()){
                    tMess = tMess + ":" + tOpt + "=" + optList.get(tOpt);
                }
                return tMess;
            }
        }

        public class SortBy{

            private String Name = "";
            private String optIgnoreAll = "";
            private String optIgnoreThe = "";
            private String optEmptyFoldername = "";
            private String optPruneSingleItemGroups = "";
            private HashMap<String,String> optList = new HashMap<String, String>();

            public SortBy(Integer Level){
                this.Name = "title";
                optList.put("ignore-all", "true");
            }
            public String Name(){
                return this.Name;
            }
            public HashMap<String,String> optList(){
                return optList;
            }
            public String LogMessage(){
                String tMess = "SortBy-";
                tMess = tMess + Name;
                for (String tOpt:optList().keySet()){
                    tMess = tMess + ":" + tOpt + "=" + optList.get(tOpt);
                }
                return tMess;
            }
        }
    }

    
}
