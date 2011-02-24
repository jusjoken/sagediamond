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

    public static int GetViewStyle(String View) {
        if (View.contains("MovieWall")) {
            return 1;
        }
        if (View.contains("ListFlow")) {
            return 2;
        } else {
            return 3;
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
    return name.split("&&")[0];}

    public static String GetViewType(String name){
    return name.split("&&")[1];}

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
        String CurrElements = sagex.api.Configuration.GetProperty(PropName, "");
        String ElementRemoved = null;
        String result = null;
        if (CurrElements.contains(Element + ";")) {

            ElementRemoved = CurrElements.replace(Element + ";", "");

            sagex.api.Configuration.SetProperty(PropName, ElementRemoved);
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
