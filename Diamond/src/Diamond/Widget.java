/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Diamond;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import org.apache.log4j.Logger;

/**
 *
 * @author jusjoken
 */
public class Widget {
    static private final Logger LOG = Logger.getLogger(Widget.class);
    public static final String WidgetProps = Const.BaseProp + Const.PropDivider + Const.WidgetProp + Const.PropDivider;
    public static enum WidgetSize{XL,L,M,S};
    public static List<String> InternalWidgetList = new ArrayList<String>();
    
    public static void AddWidgetType(String WidgetType){
        if (!InternalWidgetList.contains(WidgetType)){
            InternalWidgetList.add(WidgetType);
        }
    }

    public static ArrayList<String> GetWidgetList(){
        return GetWidgetList(Boolean.FALSE);
    }
    public static ArrayList<String> GetWidgetList(Boolean IncludeOff){
        
        if (InternalWidgetList.size()>0){
            //Add the widgets in Sort Order
            TreeMap<Integer,String> tSortedList = new TreeMap<Integer,String>();
            Integer counter = 0;
            for (String tWidget:InternalWidgetList){
                //make sure this is a real Flow entry with a name property
                if ((IncludeOff) || IncludeOff==Boolean.FALSE && ShowWidget(tWidget)){
                    counter++;
                    Integer thisSort = GetWidgetSort(tWidget);
                    if (thisSort==0){
                        thisSort = counter;
                    }
                    while(tSortedList.containsKey(thisSort)){
                        counter++;
                        thisSort = counter;
                    }
                    tSortedList.put(thisSort, tWidget);
                    //LOG.debug("GetFlows: '" + tFlow + "' added at '" + thisSort + "'");
                }
            }
            return new ArrayList<String>(tSortedList.values()); 
        }else{
            return new ArrayList<String>();
        }
    }
    
    public static String GetUseWidgetsName(){
        return util.GetTrueFalseOptionNameBase(Boolean.FALSE, WidgetProps + "WidgetsUse","", "On", "Off", Boolean.FALSE);
    }
    public static Boolean GetUseWidgets(){
        if (util.GetPropertyAsBoolean(WidgetProps + "WidgetsUse", Boolean.FALSE)){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }
    public static void SetUseWidgets(Boolean Value){
        util.SetProperty(WidgetProps + "WidgetsUse", Value.toString());
    }

    public static String GetLiveWidgetsName(){
        return util.GetTrueFalseOptionNameBase(Boolean.FALSE, WidgetProps + "WidgetsLive","", "On", "Off", Boolean.FALSE);
    }
    public static Boolean GetLiveWidgets(){
        if (util.GetPropertyAsBoolean(WidgetProps + "WidgetsLive", Boolean.FALSE)){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }
    public static void SetLiveWidgets(Boolean Value){
        util.SetProperty(WidgetProps + "WidgetsLive", Value.toString());
    }

    public static Boolean ShowWidgets(){
        //see if at least one of the 4 Widget Panels is not Off
        if (GetWidgetList().size()>0){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }
    
    public static Integer GetWidgetSort(String WidgetType){
        return util.GetPropertyAsInteger(WidgetProps + WidgetType + Const.PropDivider + "Sort", 0);
    }
    public static void SetWidgetSort(String WidgetType, Integer iSort){
        util.SetProperty(WidgetProps + WidgetType + Const.PropDivider + "Sort", iSort.toString());
    }
    
    public static Boolean ShowWidget(String WidgetType){
        if (GetSize(WidgetType).equals("Off")){
            return Boolean.FALSE;
        }else{
            return Boolean.TRUE;
        }
    }
    public static String GetSize(String WidgetType){
        return util.GetProperty(WidgetProps + WidgetType + Const.PropDivider + "Size","Off");
    }
    public static void SetSize(String WidgetType, String tSize){
        util.SetProperty(WidgetProps + WidgetType + Const.PropDivider + "Size",tSize);
    }
    
    public static Double GetForecastHeight(String WidgetType,Integer ForecastDay){
        String tSize = GetSize(WidgetType);
        if (tSize.equals(WidgetSize.XL.toString())){
            return 1.0/4;
        }else if (tSize.equals(WidgetSize.L.toString())){
            if (ForecastDay>3){
                return 0.0;
            }else{
                return 1.0/3;
            }
        }else if (tSize.equals(WidgetSize.M.toString())){
            if (ForecastDay>2){
                return 0.0;
            }else{
                return 1.0/2;
            }
        }else{
            if (ForecastDay>1){
                return 0.0;
            }else{
                return 1.0;
            }
        }
    }
    
//    public static String GetType(Integer WidgetNumber){
//        return util.GetProperty(WidgetProps + "WidgetPanel" + WidgetNumber,"Off");
//    }
//   
    public static Double GetAllHeights(){
        Double tHeight = 0.00;
        for (String tWidget:GetWidgetList()){
            tHeight = tHeight + GetHeightbyType(tWidget);
        }
        //LOG.debug("GetAllHeights: '" + tHeight.toString() + "'");
        return tHeight;
    }
    
    public static Double GetHeightbyType(String WidgetType){
        String tWidgetSize = GetSize(WidgetType);
        Double tRetVal = GetHeight(tWidgetSize);
        //LOG.debug("GetHeightbyType: for Widget '" + WidgetType + "' height = '" + tRetVal + "'");
        return tRetVal;
    }
    public static Double GetHeight(String tWidgetSize){
        Double tRetVal = StringtoDouble(util.GetProperty(WidgetProps + "WidgetHeight" + tWidgetSize,"0.00"));
        //LOG.debug("GetHeight: for WidgetSize '" + tWidgetSize + "' height = '" + tRetVal + "'");
        return tRetVal;
    }
    
    public static void SetHeight(String tWidgetSize, Double Height){
        //LOG.debug("SetHeight: for Widget Size = '" + tWidgetSize + "' height = '" + Height + "'");
        util.SetProperty(WidgetProps + "WidgetHeight" + tWidgetSize, Height.toString());
    }
    
    public static Double GetTitleHeightbyType(String WidgetType){
        String tWidgetSize = GetSize(WidgetType);
        return GetTitleHeight(tWidgetSize);
    }
    public static Double GetTitleHeight(String tWidgetSize){
        Double tRetVal = StringtoDouble(util.GetProperty(WidgetProps + "WidgetTitleHeight" + tWidgetSize,"0.00"));
        //LOG.debug("GetTitleHeight: for Widget Size = '" + tWidgetSize + "' height = '" + tRetVal + "'");
        return tRetVal;
    }
    
    public static void SetTitleHeight(String tWidgetSize, Double Height){
        util.SetProperty(WidgetProps + "WidgetTitleHeight" + tWidgetSize, Height.toString());
    }
    
    private static Double StringtoDouble(String s){
        try
        {
          Double d = Double.valueOf(s.trim()).doubleValue();
          return d;
        }
        catch (NumberFormatException nfe)
        {
            LOG.debug("StringtoDouble: Error converting '" + s + "' ERROR: '" + nfe + "'");
            return 0.00;
        }
    }
    
    public static Integer GetMaxListItems(String tWidgetSize){
        return util.GetPropertyAsInteger(WidgetProps + "WidgetMaxListItems" + tWidgetSize, 0);
    }
    public static void SetMaxListItems(String tWidgetSize, Integer MaxItems){
        util.SetProperty(WidgetProps + "WidgetMaxListItems" + tWidgetSize, MaxItems.toString());
    }
    
}
