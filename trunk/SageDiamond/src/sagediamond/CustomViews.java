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

    public static String GetViewStyle(String View) {
        if (View==null){
            LOG.debug("GetViewStyle: request for null View returned NotFound");
            return util.OptionNotFound;
        }
        String ViewType = View;
        if (ViewType.contains("&&")) {
            ViewType = GetViewType(ViewType);
            LOG.debug("GetViewStyle ERROR: ("+View+")("+ViewType+")");
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
            return ViewType;
        }
    }

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
        String[] SplitString = name.split("&&");
        LOG.debug("GetViewName: '"+name+"'");
        if (SplitString.length == 2) {
            return SplitString[0];
        } else {
            LOG.debug("GetViewName ERROR:("+name+")");
            return "Error";
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
            return "Cover Flow";
        }
    }
	
    public static String GetViewType(String name){
        if (name==null){
            LOG.debug("GetViewType: request for null name returned NotFound");
            return util.OptionNotFound;
        }
        String[] SplitString = name.split("&&");
        if (SplitString.length == 2){
            return StandardizeViewType(SplitString[1]);
        } else {
            System.out.println("ERROR: GetViewType("+name+")");
            return StandardizeViewType(name);
	}
    }

    public static Boolean HasView(String ViewName, String ViewType) {
        if (ViewName==null){
            LOG.debug("HasView: request for null name returned FALSE");
            return Boolean.FALSE;
        }
        String Element = ViewName + "&&" + ViewType;
        if (util.GetProperty(PropName, "").contains(Element + ";")) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public static String SaveView(String ViewName, String ViewType) {
        if (ViewName==null){
            LOG.debug("SaveView: request for null name returned 0");
            return "0";
        }
        String Element = ViewName + "&&" + ViewType;
        String CurrentElements = util.GetProperty(PropName, "");
        String result = null;
        if (CurrentElements.contains(Element + ";")) {
            result = "0";
        } else {
            String NewElements = CurrentElements + Element + ";";
            util.SetProperty(PropName, NewElements);
            result = "1";
        }
        return result;
    }

    public static String RemoveView(String ViewName, String ViewType) {
        if (ViewName==null){
            LOG.debug("RemoveView: request for null name returned 0");
            return "0";
        }
        String Element =ViewName + "&&" + ViewType;
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

    public static String RenameView(String ViewName, String ViewType, String NewViewName) {
        return ChangeView(ViewName, ViewType, NewViewName, ViewType);
    }

    public static String ChangeViewType(String ViewName, String ViewType, String NewViewType) {
        return ChangeView(ViewName, ViewType, ViewName, NewViewType);
    }
        
    private static String ChangeView(String ViewName, String ViewType, String NewViewName, String NewViewType) {
        if (ViewName==null){
            LOG.debug("ChangeView: request for null name returned 0");
            return "0";
        }
        String Element =ViewName + "&&" + ViewType;
        String ElementNew =NewViewName + "&&" + NewViewType;
        String OldCVPropName = "JOrton/" + Element;
        String NewCVPropName = "JOrton/" + ElementNew;
        String[] AllProps = sagex.api.Configuration.GetSubpropertiesThatAreLeaves(new UIContext(sagex.api.Global.GetUIContextName()),OldCVPropName);
        String CurrElements = util.GetProperty(PropName, "");
        String ElementRenamed = null;
        String result = null;
        if (CurrElements.contains(Element + ";")) {

            //replace the old name with the new name in the customviews list
            ElementRenamed = CurrElements.replace(Element + ";", ElementNew + ";");
            util.SetProperty(PropName, ElementRenamed);

            //iterate through all the old ViewName properties and set them to the new one
            for (String curr:AllProps){
                util.SetProperty(NewCVPropName + "/" + curr, util.GetProperty(OldCVPropName + "/" + curr,""));
            }
            //remove the old ViewName properties
            util.RemovePropertyAndChildren(OldCVPropName);

            //copy over existing pathfilters and remove old one
            util.SetProperty(PathFiltersPropName + "/" + ElementNew, util.GetProperty(PathFiltersPropName + "/" + Element,""));
            util.RemoveProperty(PathFiltersPropName + "/" + Element);

            result = "1";
        } else {
            result = "0";
        }
        return result;
    }

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

    public static void SetArrayAsProperty(ArrayList<String> Views){
        util.SetProperty(PropName,"");
        for(String element:Views){
            String View=GetViewName(element);
            String Type=GetViewType(element);
            SaveView(View,Type);
        }
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
