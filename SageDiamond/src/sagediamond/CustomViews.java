/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sagediamond;

import java.util.ArrayList;

/**
 *
 * @author SBANTA
 */
public class CustomViews {

	public static final String PropName="JOrton/CustomViews";
        public static final String PathFiltersPropName = "JOrton/PathFilters";

	public static String GetViewStyle(String View) {
		String ViewType = View;
		if (ViewType.contains("&&")) {
			ViewType = GetViewType(ViewType);
			System.out.println("Error: GetViewStyle ("+View+")("+ViewType+")");
		}

		if (ViewType.equals("Wall Flow") || 
				ViewType.equals("MovieWall")) {  // Needed for backwards compatability
			return "Wall Flow";
		} else if (ViewType.equals("List Flow") || 
				ViewType.equals("ListFlow")) { // Needed for backwards compatability
			return "List Flow";
		} else if (ViewType.equals("Cover Flow") || 
				ViewType.equals("CoverFlow")) { // Needed for backwards compatability
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
		String[] SplitString = name.split("&&");
		System.out.println("GetViewName("+name+")");
		if (SplitString.length == 2) {
			return SplitString[0];
		} else {
			System.out.println("ERROR: GetViewName("+name+")");
			return "Error";
		}
	}

	// Make sure View Type matches a valid type
	public static String StandardizeViewType(String View) {
		String ViewType = View;
		if (ViewType.contains("&&")) {
			ViewType = GetViewType(ViewType);
			System.out.println("Error: StandardizeViewType ("+View+")("+ViewType+")");
		}

		if (ViewType.equals("Wall Flow") || 
				ViewType.equals("MovieWall")) {  // Needed for backwards compatability
			return "Wall Flow";
		} else if (ViewType.equals("List Flow") || 
				ViewType.equals("ListFlow")) { // Needed for backwards compatability
			return "List Flow";
		} else if (ViewType.equals("Cover Flow") || 
				ViewType.equals("CoverFlow")) { // Needed for backwards compatability
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
		String[] SplitString = name.split("&&");
		if (SplitString.length == 2){
			return StandardizeViewType(SplitString[1]);
		} else {
			System.out.println("ERROR: GetViewType("+name+")");
			return StandardizeViewType(name);
		}
	}

	public static boolean HasView(String ViewName, String ViewType) {
		String Element = ViewName + "&&" + ViewType;
		if (sagex.api.Configuration.GetProperty(PropName, "").contains(Element + ";")) {
			return true;
		} else {
			return false;
		}
	}

	public static String SaveView(String ViewName, String ViewType) {

		String Element = ViewName + "&&" + ViewType;
		String CurrentElements = sagex.api.Configuration.GetProperty(PropName, "");
		String result = null;
		if (CurrentElements.contains(Element + ";")) {
			result = "0";

		} else {

			String NewElements = CurrentElements + Element + ";";
			sagex.api.Configuration.SetProperty(PropName, NewElements);
			result = "1";
		}
		return result;
	}

	public static String RemoveView(String ViewName, String ViewType) {

		String Element =ViewName + "&&" + ViewType;
                String OldCVPropName = "JOrton/" + Element;
		String CurrElements = sagex.api.Configuration.GetProperty(PropName, "");
		String ElementRemoved = null;
		String result = null;
		if (CurrElements.contains(Element + ";")) {

			ElementRemoved = CurrElements.replace(Element + ";", "");
			sagex.api.Configuration.SetProperty(PropName, ElementRemoved);

                        //remove the old ViewName properties
                        sagex.api.Configuration.RemovePropertyAndChildren(OldCVPropName);

                        //remove old pathfilters
                        sagex.api.Configuration.RemoveProperty(PathFiltersPropName + "/" + Element);
                        
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

		String Element =ViewName + "&&" + ViewType;
		String ElementNew =NewViewName + "&&" + NewViewType;
                String OldCVPropName = "JOrton/" + Element;
                String NewCVPropName = "JOrton/" + ElementNew;
                String[] AllProps = sagex.api.Configuration.GetSubpropertiesThatAreLeaves(OldCVPropName);
		String CurrElements = sagex.api.Configuration.GetProperty(PropName, "");
		String ElementRenamed = null;
		String result = null;
		if (CurrElements.contains(Element + ";")) {

			//replace the old name with the new name in the customviews list
                        ElementRenamed = CurrElements.replace(Element + ";", ElementNew + ";");
			sagex.api.Configuration.SetProperty(PropName, ElementRenamed);
                        
                        //iterate through all the old ViewName properties and set them to the new one
                        for (String curr:AllProps){
                            sagex.api.Configuration.SetProperty(NewCVPropName + "/" + curr, sagex.api.Configuration.GetProperty(OldCVPropName + "/" + curr,""));
                        }
                        //remove the old ViewName properties
                        sagex.api.Configuration.RemovePropertyAndChildren(OldCVPropName);

                        //copy over existing pathfilters and remove old one
                        sagex.api.Configuration.SetProperty(PathFiltersPropName + "/" + ElementNew, sagex.api.Configuration.GetProperty(PathFiltersPropName + "/" + Element,""));
                        sagex.api.Configuration.RemoveProperty(PathFiltersPropName + "/" + Element);
                                                
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

		return AllViewsOrder;}

	public static ArrayList<String> SetElementLocation(ArrayList<String> Views,String Element,int Location){
		Views.remove(Element);
		Views.add(Location, Element);
		return Views;
	}

	public static void SetArrayAsProperty(ArrayList<String> Views){
		sagex.api.Configuration.SetProperty(PropName,"");
		for(String element:Views){
			String View=GetViewName(element);
			String Type=GetViewType(element);
			SaveView(View,Type);}
	}


	public static void main(String[] args){
		ArrayList<String> test=new ArrayList<String>();
		test.add("1");
		test.add("2");
		test.add("3");
		SetElementLocation(test,"3",0);
		for(String curr:test){
			System.out.println(curr);}
	}

}
