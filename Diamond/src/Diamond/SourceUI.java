/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Diamond;

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
    
    public SourceUI(String FlowName){
        this.thisFlowName = FlowName;
        //based on the FlowName load the settings into this class from the properties file
        thisHasPresentation = Boolean.TRUE;
        thisLevels = 2;
        
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
        //TODO:read from properties
        return Boolean.TRUE.toString();
    }
    public String PruneSingleItemFolders(){
        //TODO:read from properties
        return Boolean.TRUE.toString();
    }
    
    //Presentation specific settings
    public Boolean HasPresentation(){
        return thisHasPresentation;
    }
    public Integer Levels(){
        return thisLevels;
    }
    
    public class GroupBy{
        public GroupBy(){
            
        }
    }
    
}
