/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PloxeeTV;


import java.util.HashMap;
import sagex.util.Log4jConfigurator;
import sagex.util.Log4jLog;
import sun.rmi.runtime.Log;
 import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
/**
 *
 * @author SBANTA
 */
public class api {

    public static Logger LOG=null;



    public static void Load(String PropertyAdder){
//    Log4jConfigurator.configureQuietly("ploxeetv", this.getClass().getClassLoader());
    PropertyConfigurator.configure("plugins/PloxeeTV/PloxeeTV.log4j.properties");
    LOG = Logger.getLogger(api.class);
    LOG.info("Logger created successfully!");
    SortMethods.PropertyAdder=PropertyAdder;
    SortMethods.PropertyPrefix=SortMethods.PropertyDefault+PropertyAdder;
    LOG.info("Loading Theme");
    ThemeProperties.LoadThemeProperties();
    Theme.SetTheme();
    LOG.info("Done loading Theme");
   

   }

    public static void ResetVariables(){
    ThemeProperties.LoadThemeProperties();
    Theme.SetTheme();
    }

    public static String GetTitle(String Title) {
    return Title.split("&&")[1];

    }

    public static Object GetTitle(GroupObject Title){
    return GroupHandler.GetGroupName(Title);}

    public static Object GetAllVideosGrouped(String PropertyAdder,String Method){
    if(Method.equals("none")){
    return Groups.GetAllTVByTitle(PropertyAdder);}
    else{
    return GroupBuilder.GetAllVideosGrouped(PropertyAdder, Method);}}

}
