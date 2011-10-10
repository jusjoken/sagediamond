/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sagediamond;

/**
 *
 * @author jusjoken
 * - 10/10/2011 - class for all Flow related calls - for 4.x this will be replaced with a Flow class to encapsulate Flow as an object
 * 
 */
public class Flow {
    
   
    public static String GetFlowBaseProp(String FlowName){
        return Const.BaseProp + Const.PropDivider + FlowName;
    }
    
    public static String GetInstantSearchMode(String FlowName){
        String InstantSearchModeProp = GetFlowBaseProp(FlowName) + Const.PropDivider + Const.InstantSearchMode;
        return util.GetProperty(InstantSearchModeProp, InstantSearch.InstantSearchMode.JUMPTO.toString());
    }
    
    public static void SetInstantSearchMode(String FlowName, String Value){
        String InstantSearchModeProp = GetFlowBaseProp(FlowName) + Const.PropDivider + Const.InstantSearchMode;
        String SetValue = InstantSearch.InstantSearchMode.JUMPTO.toString();
        for (InstantSearch.InstantSearchMode val:InstantSearch.InstantSearchMode.values()){
            if (val.toString().equals(Value)){
                SetValue = val.toString();
                break;
            }
        }
        util.SetProperty(InstantSearchModeProp, SetValue);
    }
    
}
