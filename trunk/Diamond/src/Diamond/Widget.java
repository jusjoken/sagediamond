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
public class Widget {
    static private final Logger LOG = Logger.getLogger(Widget.class);
    public static final String WidgetProps = Const.BaseProp + Const.PropDivider + Const.MainMenuProp + Const.PropDivider;
    public static enum WidgetSize{XL,L,M,S};
    
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
        for (Integer i=1;i<5;i++){
            if (!GetType(i).equals("Off")){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
    
    public static String GetSize(Integer WidgetNumber){
        return util.GetProperty(WidgetProps + "WidgetPanelLayoutW" + WidgetNumber,"Off");
    }
    
    public static String GetType(Integer WidgetNumber){
        return util.GetProperty(WidgetProps + "WidgetPanel" + WidgetNumber,"Off");
    }
    
    public static Double GetHeight(Integer WidgetNumber){
        String tWidgetSize = GetSize(WidgetNumber);
        Double tRetVal = StringtoDouble(util.GetProperty(WidgetProps + "WidgetHeight" + tWidgetSize,"0.00"));
        //LOG.debug("GetHeight: for Widget '" + WidgetNumber + "' Size = '" + tWidgetSize + "' height = '" + tRetVal + "'");
        return tRetVal;
    }
    
    public static void SetHeight(String tWidgetSize, Double Height){
        //LOG.debug("SetHeight: for Widget Size = '" + tWidgetSize + "' height = '" + Height + "'");
        util.SetProperty(WidgetProps + "WidgetHeight" + tWidgetSize, Height.toString());
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
