/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SDGroup;


import java.util.HashMap;

import sun.rmi.runtime.Log;
 import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
/**
 *
 * @author SBANTA
 */
public class PloxeeAPI {

    public static Logger LOG=null;



    public static void Load(String PropertyAdder){
//    Log4jConfigurator.configureQuietly("ploxeetv", this.getClass().getClassLoader());
     LOG = Logger.getLogger(PloxeeAPI.class);
    PropertyConfigurator.configure("plugins/SageDiamond/SageDiamond.log4j.properties");

    LOG.info("Logger created successfully!");
    SortMethods.PropertyAdder=PropertyAdder;
    SortMethods.PropertyPrefix=SortMethods.PropertyDefault+PropertyAdder;
//    LOG.info("Loading Theme");
//    ThemeProperties.LoadThemeProperties();
//    Theme.SetTheme();
//    LOG.info("Done loading Theme");
//

   }

    public static void LoadView(String CurrView){
    SortMethods.PropertyPrefix=SortMethods.PropertyDefault+CurrView+"/";}

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
    return Groups.GetAllVidsByTitle(PropertyAdder);}
    else{
    return GroupBuilder.GetAllVideosGrouped(PropertyAdder, Method);}}

}
