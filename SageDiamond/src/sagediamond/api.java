/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sagediamond;

import java.lang.reflect.InvocationTargetException;
import sagex.UIContext;

/**
 *
 * @author SBANTA
 */
public class api {


    public static String Version = "3.24";


    public static int GetSeasonEpisodeNumber(Object MediaObject) {
        return MetadataCalls.GetSeasonEpisodeNumber(MediaObject);
    }

    public static void AddStaticContext(String Context, Object Value) {
        sagex.api.Global.AddStaticContext(new UIContext(sagex.api.Global.GetUIContextName()), Context, Value);

    }

    public static void ExecuteWidgeChain(String UID) {
        String UIContext = sagex.api.Global.GetUIContextName();
        System.out.println("Getting Ready to execute widget chain for " + UIContext);
        System.out.println("Actual context " + sagex.api.Global.GetUIContextName());
        Object[] passvalue = new Object[1];
        passvalue[0] = sagex.api.WidgetAPI.FindWidgetBySymbol(new UIContext(UIContext), UID);
        try {
            sage.SageTV.apiUI(UIContext, "ExecuteWidgetChainInCurrentMenuContext", passvalue);
        } catch (InvocationTargetException ex) {
            System.out.println("error executing widget" + api.class.getName() + ex);
        }
//        sagex.api.WidgetAPI.ExecuteWidgetChain(new UIContext(UIContext),sagex.api.WidgetAPI.FindWidgetBySymbol(new UIContext(UIContext),UID));
        }

    public static String GetVersion() {
        return Version;
    }
}
