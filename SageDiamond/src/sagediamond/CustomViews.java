/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sagediamond;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import sagex.UIContext;

/**
 *
 * @author SBANTA
 * @author JUSJOKEN
 * - 10/17/2011 - added logging and handled null for ViewType
 */
public class CustomViews {

    static private final Logger LOG = Logger.getLogger(CustomViews.class);
    public static final String PropName="JOrton/CustomViews";
    public static final String PathFiltersPropName = "JOrton/PathFilters";

//    public static String GetViewStyle(String View) {
//        if (View==null){
//            LOG.debug("GetViewStyle: request for null View returned NotFound");
//            return util.OptionNotFound;
//        }
//        String ViewType = View;
//        if (ViewType.contains("&&")) {
//            ViewType = GetViewType(ViewType);
//            LOG.debug("GetViewStyle ERROR: ("+View+")("+ViewType+")");
//        }
//
//        if (ViewType.equals("Wall Flow") || ViewType.equals("MovieWall")) {  // Needed for backwards compatability
//            return "Wall Flow";
//        } else if (ViewType.equals("List Flow") || ViewType.equals("ListFlow")) { // Needed for backwards compatability
//            return "List Flow";
//        } else if (ViewType.equals("Cover Flow") || ViewType.equals("CoverFlow")) { // Needed for backwards compatability
//            return "Cover Flow";
//        } else if(ViewType.equals("Category Flow")) {
//            return "Category Flow";
//        } else if(ViewType.equals("360 Flow")) {
//            return "360 Flow";
//        } else if(ViewType.equals("SideWays Flow")) {
//            return "SideWays Flow";
//        }
//        else {
//            return ViewType;
//        }
//    }

    public static Object GetCustomViews(){
        String views=sagex.api.Configuration.GetProperty(PropName,"");
        if(views.contains(";")){	
            return views.split(";");
        }
        return views;
    }

    public static String GetViewName(String name){
        if (name==null){
            LOG.debug("GetViewName: request for null name returned NotFound");
            return util.OptionNotFound;
        }
        //first see if the Flow has a FlowName Property and use it
        String FlowNameProp = Flow.GetFlowBaseProp(name) + Const.PropDivider + Const.FlowName;
        String tFlowName = Const.FlowNameNotFound;
        if (util.HasProperty(FlowNameProp)){
            tFlowName = util.GetProperty(FlowNameProp, Const.FlowNameNotFound);
            LOG.debug("GetViewName: '" + name + "' FlowName = '" + tFlowName + "'");
        }else{
            //convert to using the new structure
            tFlowName = ConvertFlowNames(name, Boolean.FALSE);
        }
        return tFlowName;
        
    }
    
    //used the 1st time an old name or type is found to convert the flow to using the new naming structure
    public static String ConvertFlowNames(String name, Boolean ReturnType){
        String ElementNew = util.GenerateRandomName();
        String FlowNameProp = Flow.GetFlowBaseProp(ElementNew) + Const.PropDivider + Const.FlowName;
        String tFlowName = Const.FlowNameNotFound;
        String FlowTypeProp = Flow.GetFlowBaseProp(ElementNew) + Const.PropDivider + Const.FlowType;
        String tFlowType = Const.FlowTypeDefault;
        String CurrElements = util.GetProperty(PropName, "");
        //see if this is a Custom Flow
        if (CurrElements.contains(name + ";")) {
            
        }
        
        String[] SplitString = name.split("&&");
        if (SplitString.length == 2) {
            tFlowName = SplitString[0];
            tFlowType = StandardizeViewType(SplitString[1]);
            //convert all the properties to a new name
            

            String OldCVPropName = "JOrton/" + name;
            String NewCVPropName = "JOrton/" + ElementNew;
            String[] AllProps = sagex.api.Configuration.GetSubpropertiesThatAreLeaves(new UIContext(sagex.api.Global.GetUIContextName()),OldCVPropName);
            String ElementRenamed = null;
            String result = null;
            if (CurrElements.contains(name + ";")) {

                //replace the old name with the new name in the customviews list
                ElementRenamed = CurrElements.replace(name + ";", ElementNew + ";");
                util.SetProperty(PropName, ElementRenamed);

                //iterate through all the old ViewName properties and set them to the new one
                for (String curr:AllProps){
                    util.SetProperty(NewCVPropName + "/" + curr, util.GetProperty(OldCVPropName + "/" + curr,""));
                }
                //remove the old ViewName properties
                util.RemovePropertyAndChildren(OldCVPropName);

                //copy over existing pathfilters and remove old one
                util.SetProperty(PathFiltersPropName + "/" + ElementNew, util.GetProperty(PathFiltersPropName + "/" + name,""));
                util.RemoveProperty(PathFiltersPropName + "/" + name);

                result = "1";
            } else {
                //this is a Default Flow - so do not restructure it
                ElementNew = name;
                FlowNameProp = Flow.GetFlowBaseProp(name) + Const.PropDivider + Const.FlowName;
                FlowTypeProp = Flow.GetFlowBaseProp(name) + Const.PropDivider + Const.FlowType;
                result = "0";
            }
            
            //write the new name and type properties
            util.SetProperty(FlowNameProp, tFlowName);
            util.SetProperty(FlowTypeProp, tFlowType);
            LOG.debug("ConvertFlowNames: old '" + name + "' new '" + ElementNew + "' FlowName = '" + tFlowName + "' FlowType = '" + tFlowType + "'");

        } else {
            LOG.debug("ConvertFlowNames: failed for name = '" + name + "'");
        }
        if (ReturnType){
            return tFlowType;
        }else{
            return tFlowName;
        }
        
    }

    // Make sure View Type matches a valid type
    public static String StandardizeViewType(String View) {
        if (View==null){
            LOG.debug("StandardizeViewType: request for null View returned NotFound");
            return util.OptionNotFound;
        }
        String ViewType = View;
        if (ViewType.contains("&&")) {
            ViewType = GetViewType(ViewType);
            LOG.debug("StandardizeViewType ERROR: ("+View+")("+ViewType+")");
        }
        if (ViewType.equals("Wall Flow") || ViewType.equals("MovieWall")) {  // Needed for backwards compatability
            return "Wall Flow";
        } else if (ViewType.equals("List Flow") || ViewType.equals("ListFlow")) { // Needed for backwards compatability
            return "List Flow";
        } else if (ViewType.equals("Cover Flow") || ViewType.equals("CoverFlow")) { // Needed for backwards compatability
            return "Cover Flow";
        } else if(ViewType.equals("Category Flow")) {
            return "Category Flow";
        } else if(ViewType.equals("360 Flow")) {
            return "360 Flow";
        } else if(ViewType.equals("SideWays Flow")) {
            return "SideWays Flow";
        }
        else {
            return Const.FlowTypeDefault;
        }
    }
	
    public static String GetViewType(String name){
        if (name==null){
            LOG.debug("GetViewType: request for null name returned NotFound");
            return util.OptionNotFound;
        }
        //first see if the Flow has a FlowType Property and use it
        String FlowTypeProp = Flow.GetFlowBaseProp(name) + Const.PropDivider + Const.FlowType;
        String tFlowType = Const.FlowTypeDefault;
        if (util.HasProperty(FlowTypeProp)){
            tFlowType = util.GetProperty(FlowTypeProp, Const.FlowTypeDefault);
            LOG.debug("GetViewType: '" + name + "' FlowType = '" + tFlowType + "'");
        }else{
            //convert to using the new structure
            tFlowType = ConvertFlowNames(name, Boolean.TRUE);
        }
        return tFlowType;
    }

    public static void SetViewType(String name, String FlowType){
        if (name==null){
            LOG.debug("SetViewType: request for null name returned NotFound");
        }
        String FlowTypeProp = Flow.GetFlowBaseProp(name) + Const.PropDivider + Const.FlowType;
        //String tFlowType = Const.FlowTypeDefault;
        util.SetProperty(FlowTypeProp, FlowType);
    }

    //    public static Boolean HasView(String ViewName, String ViewType) {
//        if (ViewName==null){
//            LOG.debug("HasView: request for null name returned FALSE");
//            return Boolean.FALSE;
//        }
//        String Element = ViewName + "&&" + ViewType;
//        if (util.GetProperty(PropName, "").contains(Element + ";")) {
//            return Boolean.TRUE;
//        } else {
//            return Boolean.FALSE;
//        }
//    }

    public static String SaveView(String ViewName, String ViewType) {
        if (ViewName==null){
            LOG.debug("SaveView: request for null name returned 0");
            return "0";
        }
        //String Element = ViewName + "&&" + ViewType;
        String Element = util.GenerateRandomName();
        String CurrentElements = util.GetProperty(PropName, "");
        String result = null;
        if (CurrentElements.contains(Element + ";")) {
            result = "0";
        } else {
            String NewElements = CurrentElements + Element + ";";
            util.SetProperty(PropName, NewElements);
            //Save the Name and Type for the Flow
            String FlowNameProp = Flow.GetFlowBaseProp(Element) + Const.PropDivider + Const.FlowName;
            util.SetProperty(FlowNameProp, ViewName);
            String FlowTypeProp = Flow.GetFlowBaseProp(Element) + Const.PropDivider + Const.FlowType;
            util.SetProperty(FlowTypeProp, ViewType);
            result = "1";
        }
        return result;
    }

    public static String RemoveView(String Element) {
        if (Element==null){
            LOG.debug("RemoveView: request for null name returned 0");
            return "0";
        }
        //String Element =ViewName + "&&" + ViewType;
        String OldCVPropName = "JOrton/" + Element;
        String CurrElements = util.GetProperty(PropName, "");
        String ElementRemoved = null;
        String result = null;
        if (CurrElements.contains(Element + ";")) {
            ElementRemoved = CurrElements.replace(Element + ";", "");
            util.SetProperty(PropName, ElementRemoved);

            //remove the old ViewName properties
            util.RemovePropertyAndChildren(OldCVPropName);

            //remove old pathfilters
            util.RemoveProperty(PathFiltersPropName + "/" + Element);
            result = "1";
        } else {
            result = "0";
        }
        return result;
    }

    public static String RenameView(String Element, String NewViewName) {
        String FlowNameProp = Flow.GetFlowBaseProp(Element) + Const.PropDivider + Const.FlowName;
        util.SetProperty(FlowNameProp, NewViewName);
        return "1";
    }

    public static String ChangeViewType(String Element, String NewViewType) {
        String FlowTypeProp = Flow.GetFlowBaseProp(Element) + Const.PropDivider + Const.FlowType;
        util.SetProperty(FlowTypeProp, NewViewType);
        return "1";
    }
        
//    private static String ChangeView(String Element, String NewViewName, String NewViewType) {
//        if (Element==null){
//            LOG.debug("ChangeView: request for null name returned 0");
//            return "0";
//        }
//        //String Element =ViewName + "&&" + ViewType;
//        //TODO: need to change this to only change the new property with the name or type and not the Key (Element)
//        String ElementNew =NewViewName + "&&" + NewViewType;
//        String OldCVPropName = "JOrton/" + Element;
//        String NewCVPropName = "JOrton/" + ElementNew;
//        String[] AllProps = sagex.api.Configuration.GetSubpropertiesThatAreLeaves(new UIContext(sagex.api.Global.GetUIContextName()),OldCVPropName);
//        String CurrElements = util.GetProperty(PropName, "");
//        String ElementRenamed = null;
//        String result = null;
//        if (CurrElements.contains(Element + ";")) {
//
//            //replace the old name with the new name in the customviews list
//            ElementRenamed = CurrElements.replace(Element + ";", ElementNew + ";");
//            util.SetProperty(PropName, ElementRenamed);
//
//            //iterate through all the old ViewName properties and set them to the new one
//            for (String curr:AllProps){
//                util.SetProperty(NewCVPropName + "/" + curr, util.GetProperty(OldCVPropName + "/" + curr,""));
//            }
//            //remove the old ViewName properties
//            util.RemovePropertyAndChildren(OldCVPropName);
//
//            //copy over existing pathfilters and remove old one
//            util.SetProperty(PathFiltersPropName + "/" + ElementNew, util.GetProperty(PathFiltersPropName + "/" + Element,""));
//            util.RemoveProperty(PathFiltersPropName + "/" + Element);
//
//            result = "1";
//        } else {
//            result = "0";
//        }
//        return result;
//    }

    public static ArrayList<String> AllViewsInOrder(){
        String[] AllViews= (String[]) GetCustomViews();
        ArrayList AllViewsOrder=new ArrayList<String>();
        for (String curr:AllViews){
                AllViewsOrder.add(curr);}

        return AllViewsOrder;
    }

    public static ArrayList<String> SetElementLocation(ArrayList<String> Views,String Element,int Location){
        Views.remove(Element);
        Views.add(Location, Element);
        return Views;
    }

    public static void SaveViewList(ArrayList<String> Views){
        util.SetProperty(PropName,"");
        String NewElements = "";
        for(String element:Views){
            NewElements = NewElements + element + ";";
        }
        LOG.debug("SaveViewList: writing new Flows list = '" + NewElements + "'");
        util.SetProperty(PropName, NewElements);
    }

    public static void main(String[] args){
        ArrayList<String> test=new ArrayList<String>();
        test.add("1");
        test.add("2");
        test.add("3");
        SetElementLocation(test,"3",0);
        for(String curr:test){
            LOG.debug(curr);}
    }

}
