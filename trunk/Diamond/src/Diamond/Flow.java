/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Diamond;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import sagex.UIContext;

/**
 *
 * @author jusjoken
 * - 10/10/2011 - class for all Flow related calls - for 4.x this will be replaced with a Flow class to encapsulate Flow as an object
 * 
 */
public class Flow {
    
    static private final Logger LOG = Logger.getLogger(Flow.class);
   
    public static String GetFlowsBaseProp(){
        return Const.BaseProp + Const.PropDivider + Const.FlowProp + Const.PropDivider;
    }
    public static String GetFlowBaseProp(String FlowName){
        return GetFlowsBaseProp() + FlowName;
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
    
    public static String GetFlowName(String name){
        if (name==null){
            LOG.debug("GetViewName: request for null name returned NotFound");
            return util.OptionNotFound;
        }
        String FlowNameProp = Flow.GetFlowBaseProp(name) + Const.PropDivider + Const.FlowName;
        return util.GetProperty(FlowNameProp, Const.FlowNameNotFound);
    }
    
    public static String GetFlowType(String name){
        if (name==null){
            LOG.debug("GetViewType: request for null name returned NotFound");
            return util.OptionNotFound;
        }
        String FlowTypeProp = Flow.GetFlowBaseProp(name) + Const.PropDivider + Const.FlowType;
        return util.GetProperty(FlowTypeProp, Const.FlowTypeDefault);
    }
    
    public static String GetFlowTypeShortName(String name){
        if (name==null){
            LOG.debug("GetViewType: request for null name returned NotFound");
            return util.OptionNotFound;
        }
        String FlowTypeProp = Flow.GetFlowBaseProp(name) + Const.PropDivider + Const.FlowType;
        String tFlow = util.GetProperty(FlowTypeProp, Const.FlowTypeDefault);
        tFlow = tFlow.replaceAll("Flow", "");
        return tFlow.trim();
    }

    public static void SetFlowType(String name, String FlowType){
        if (name==null){
            LOG.debug("SetViewType: request for null name returned NotFound");
        }
        String FlowTypeProp = Flow.GetFlowBaseProp(name) + Const.PropDivider + Const.FlowType;
        util.SetProperty(FlowTypeProp, FlowType);
    }

    public static Boolean GetTrueFalseOption(String PropSection, String PropName, Boolean DefaultValue){
        return util.GetTrueFalseOptionBase(Boolean.TRUE, PropSection, PropName, DefaultValue);
    }
    
    public static String GetTrueFalseOptionName(String PropSection, String PropName, String TrueValue, String FalseValue){
        return util.GetTrueFalseOptionNameBase(Boolean.TRUE, PropSection, PropName, TrueValue, FalseValue, Boolean.FALSE);
    }
    public static String GetTrueFalseOptionName(String PropSection, String PropName, String TrueValue, String FalseValue, Boolean DefaultValue){
        return util.GetTrueFalseOptionNameBase(Boolean.TRUE, PropSection, PropName, TrueValue, FalseValue, DefaultValue);
    }

    public static void SetTrueFalseOptionNext(String PropSection, String PropName){
        util.SetTrueFalseOptionNextBase(Boolean.TRUE, PropSection, PropName, Boolean.FALSE);
    }
    public static void SetTrueFalseOptionNext(String PropSection, String PropName, Boolean DefaultValue){
        util.SetTrueFalseOptionNextBase(Boolean.TRUE, PropSection, PropName, DefaultValue);
    }
    
    public static String GetOptionName(String PropSection, String PropName, String DefaultValue){
        return util.GetOptionNameBase(Boolean.TRUE, PropSection, PropName, DefaultValue);
    }
    
    public static void SetOption(String PropSection, String PropName, String NewValue){
        util.SetOptionBase(Boolean.TRUE, PropSection, PropName, NewValue);
    }
    
    public static String GetListOptionName(String PropSection, String PropName, String OptionList, String DefaultValue){
        return util.GetListOptionNameBase(Boolean.TRUE, PropSection, PropName, OptionList, DefaultValue);
    }
    
    public static void SetListOptionNext(String PropSection, String PropName, String OptionList){
        util.SetListOptionNextBase(Boolean.TRUE, PropSection, PropName, OptionList);
    }

    public static Collection<String> GetFlows(){
        LOG.debug("GetFlows: started");
        String[] FlowItems = sagex.api.Configuration.GetSubpropertiesThatAreBranches(new UIContext(sagex.api.Global.GetUIContextName()),GetFlowsBaseProp());
        LOG.debug("GetFlows: Items = '" + FlowItems.length + "'");
        if (FlowItems.length>0){
            //Add the flows in Sort Order
            TreeMap<Integer,String> tSortedList = new TreeMap<Integer,String>();
            Integer counter = 0;
            for (String tFlow:FlowItems){
                counter++;
                LOG.debug("GetFlows: processing '" + tFlow + "'");
                Integer thisSort = GetFlowSort(tFlow);
                LOG.debug("GetFlows: thisSort '" + thisSort + "'");
                if (thisSort==0){
                    while(tSortedList.containsKey(counter)){
                        counter++;
                    }
                    thisSort = counter;
                    LOG.debug("GetFlows: Sort for '" + tFlow + "' adjusted to '" + thisSort + "'");
                }
                tSortedList.put(thisSort, tFlow);
                LOG.debug("GetFlows: '" + tFlow + "' added at '" + thisSort + "'");
            }
            return tSortedList.values(); 
        }else{
            return new ArrayList<String>();
        }
    }
    
    public static Integer GetFlowSort(String Element){
        return util.GetPropertyAsInteger(GetFlowBaseProp(Element) + Const.PropDivider + Const.FlowSort, 0);
    }
    
    public static void SetFlowSort(String Element, Integer iSort){
        util.SetProperty(GetFlowBaseProp(Element) + Const.PropDivider + Const.FlowSort, iSort.toString());
    }
    
//    public static ArrayList<String> AllFlowsInOrder(){
//        //TODO:need to retain the order of the Flows
////        String[] AllViews= (String[]) GetFlows();
////        ArrayList AllViewsOrder=new ArrayList<String>();
////        for (String curr:AllViews){
////                AllViewsOrder.add(curr);}
//        return GetFlows();
//    }

//    public static ArrayList<String> SetElementLocation(ArrayList<String> Views,String Element,int Location){
//        Views.remove(Element);
//        Views.add(Location, Element);
//        return Views;
//    }

    public static String SaveFlow(String ViewName, String ViewType) {
        if (ViewName==null){
            LOG.debug("SaveView: request for null name returned 0");
            return "0";
        }
        String Element = util.GenerateRandomName();
        Integer NewSort = GetFlows().size();
        //Save the Name and Type for the Flow
        String FlowNameProp = GetFlowBaseProp(Element) + Const.PropDivider + Const.FlowName;
        util.SetProperty(FlowNameProp, ViewName);
        String FlowTypeProp = GetFlowBaseProp(Element) + Const.PropDivider + Const.FlowType;
        util.SetProperty(FlowTypeProp, ViewType);
        //Save the sort order
        SetFlowSort(Element, NewSort);
        return Element;
    }
  
    public static String RemoveFlow(String Element) {
        if (Element==null){
            LOG.debug("RemoveView: request for null name returned 0");
            return "0";
        }
        String OldCVPropName = GetFlowBaseProp(Element);
        util.RemovePropertyAndChildren(OldCVPropName);
        //TODO: need to deal with path filters
        //util.RemoveProperty(PathFiltersPropName + "/" + Element);
        return "1";
    }

    public static String RenameFlow(String Element, String NewViewName) {
        String FlowNameProp = Flow.GetFlowBaseProp(Element) + Const.PropDivider + Const.FlowName;
        util.SetProperty(FlowNameProp, NewViewName);
        return "1";
    }

    public static String ChangeFlowType(String Element, String NewViewType) {
        String FlowTypeProp = Flow.GetFlowBaseProp(Element) + Const.PropDivider + Const.FlowType;
        util.SetProperty(FlowTypeProp, NewViewType);
        return "1";
    }
    
    
}
