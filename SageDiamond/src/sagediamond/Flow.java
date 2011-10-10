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
        String tProp = GetFlowBaseProp(FlowName) + Const.PropDivider + Const.InstantSearchMode;
        return util.GetProperty(tProp, api.InstantSearchModes.GetDefault().Key());
    }
    
    public static void SetInstantSearchMode(String FlowName, String Value){
        String tProp = GetFlowBaseProp(FlowName) + Const.PropDivider + Const.InstantSearchMode;
        if (api.InstantSearchModes.containsKey(Value)){
            util.SetProperty(tProp, Value);
        }else{
            //use the default
            util.SetProperty(tProp, api.InstantSearchModes.GetDefault().Key());
        }
    }
    
    public static String GetInstantSearchExecuteMode(String FlowName){
        String tProp = GetFlowBaseProp(FlowName) + Const.PropDivider + Const.InstantSearchExecuteMode;
        return util.GetProperty(tProp, api.InstantSearchExecuteModes.GetDefault().Key());
    }
    
    public static void SetInstantSearchExecuteMode(String FlowName, String Value){
        String tProp = GetFlowBaseProp(FlowName) + Const.PropDivider + Const.InstantSearchExecuteMode;
        if (api.InstantSearchExecuteModes.containsKey(Value)){
            util.SetProperty(tProp, Value);
        }else{
            //use the default
            util.SetProperty(tProp, api.InstantSearchExecuteModes.GetDefault().Key());
        }
    }
    
}
