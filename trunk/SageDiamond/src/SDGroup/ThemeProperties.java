/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SDGroup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author SBANTA
 */
public class ThemeProperties {
    static private final Logger LOG = Logger.getLogger(ThemeProperties.class);

    public static HashMap<String,Properties> CurrProps = new HashMap<String,Properties>();





     public static void LoadThemeProperties(){
    File ThemePropFile = new File(Theme.DefaultThemeFolder+Theme.GetThemeName()+"\\PloxeeTheme.properties");
    Properties Themeprops = new Properties();
    FileInputStream PropFile = null;
        try {
            PropFile = new FileInputStream(ThemePropFile);
        } catch (FileNotFoundException ex) {
            LOG.fatal("ProblemLoadingProperties "+ThemeProperties.class.getName()+ ex);
        }
        try {
            Themeprops.load(PropFile);
        } catch (IOException ex) {
            LOG.fatal("ProblemLoadingProperties "+ThemeProperties.class.getName()+ex);
        }
        try {
            PropFile.close();
        } catch (IOException ex) {
           LOG.fatal("ProblemLoadingProperties "+ThemeProperties.class.getName()+ ex);
        }
       CurrProps.put(sagex.api.Global.GetUIContextName(), Themeprops);


    }

     public static String GetProperty(String PropertyName, String PropertyDefault){
     return CurrProps.get(sagex.api.Global.GetUIContextName()).getProperty(PropertyName,PropertyDefault);}

}
