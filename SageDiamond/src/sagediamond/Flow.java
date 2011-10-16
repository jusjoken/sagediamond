/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sagediamond;

import org.apache.log4j.Logger;

/**
 *
 * @author jusjoken
 * - 10/10/2011 - class for all Flow related calls - for 4.x this will be replaced with a Flow class to encapsulate Flow as an object
 * 
 */
public class Flow {
    
    static private final Logger LOG = Logger.getLogger(Flow.class);
   
    public static String GetFlowBaseProp(String FlowName){
        return Const.BaseProp + Const.PropDivider + FlowName;
    }
    
    public static String GetInstantSearchMode(String FlowName){
        String tProp = GetFlowBaseProp(FlowName) + Const.PropDivider + Const.InstantSearchMode;
        return util.GetProperty(tProp, api.InstantSearchModes.GetDefault().Key());
    }
    
    public static String GetInstantSearchModeName(String FlowName){
        String tProp = GetFlowBaseProp(FlowName) + Const.PropDivider + Const.InstantSearchMode;
        return api.InstantSearchModes.get(util.GetProperty(tProp, api.InstantSearchModes.GetDefault().Key())).DisplayName();
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
    
    public static String GetInstantSearchExecuteModeName(String FlowName){
        String tProp = GetFlowBaseProp(FlowName) + Const.PropDivider + Const.InstantSearchExecuteMode;
        return api.InstantSearchExecuteModes.get(util.GetProperty(tProp, api.InstantSearchExecuteModes.GetDefault().Key())).DisplayName();
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
    
    public static Boolean GetInstantSearchIsNumericListener(String FlowName){
        String tProp = GetFlowBaseProp(FlowName) + Const.PropDivider + Const.InstantSearchIsNumericListener;
        return util.GetPropertyAsBoolean(tProp, Boolean.FALSE);
    }
    
    public static void SetInstantSearchIsNumericListener(String FlowName, Boolean Value){
        String tProp = GetFlowBaseProp(FlowName) + Const.PropDivider + Const.InstantSearchIsNumericListener;
        util.SetProperty(tProp, Value.toString());
    }
    
    public static Double GetFocusedPosterScale(String FlowName){
        String tProp = GetFlowBaseProp(FlowName) + Const.PropDivider + Const.FocusedPosterScale;
        return util.GetPropertyAsDouble(tProp, 1.0);
    }
    
    public static void SetFocusedPosterScale(String FlowName, Double Value){
        String tProp = GetFlowBaseProp(FlowName) + Const.PropDivider + Const.FocusedPosterScale;
        util.SetProperty(tProp, Value.toString());
    }
    
}
