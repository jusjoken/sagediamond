/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SDGroup;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
/**
 *
 * @author SBANTA
 * @author JUSJOKEN
 * - 9/29/2011 - minor code flow updating, log message changes for testing
 */
public class PloxeeAPI {

    //public static Logger LOG=null;
    static private final Logger LOG = Logger.getLogger(PloxeeAPI.class);

//    public static void Load(String PropertyAdder){
//        //    Log4jConfigurator.configureQuietly("ploxeetv", this.getClass().getClassLoader());
//        //LOG = Logger.getLogger(PloxeeAPI.class);
//        // original //PropertyConfigurator.configure("plugins/SageDiamond/SageDiamond.log4j.properties");
//        PropertyConfigurator.configure("STVs/SageDiamond/Configuration/SageDiamond.log4j.properties");
//
//        LOG.info("Logger created successfully!");
//        LOG.debug("Test Log Message - debug");
//        LOG.info("Test Log Message - info");
//        LOG.warn("Test Log Message - warn");
//        LOG.error("Test Log Message - error");
//        LOG.fatal("Test Log Message - fatal");
//        SortMethods.PropertyAdder=PropertyAdder;
//        SortMethods.PropertyPrefix=SortMethods.PropertyDefault+PropertyAdder+"/";
//        //    LOG.info("Loading Theme");
//        //    ThemeProperties.LoadThemeProperties();
//        //    Theme.SetTheme();
//        //    LOG.info("Done loading Theme");
//        //
//   }

    public static void LoadView(String CurrView){
        SortMethods.PropertyPrefix=SortMethods.PropertyDefault+CurrView+"/";
    }

    public static void ResetVariables(){
        ThemeProperties.LoadThemeProperties();
        Theme.SetTheme();
    }

    public static String GetTitle(String Title) {
        return Title.split("&&")[1];
    }

    public static Object GetTitle(GroupObject Title){
        return GroupHandler.GetGroupName(Title);
    }

    public static Object GetAllVideosGrouped(String PropertyAdder,String Method){
        if(Method.equals("none")){
            return Groups.GetAllVidsByTitle(PropertyAdder);
        }
        else{
            return GroupBuilder.GetAllVideosGrouped(PropertyAdder, Method);
        }
    }

    public static void TestMe(String PropertyAdder,String Method){
        LOG.info("TestMe: started");
        Object[] Test=null;
        if(Method.equals("GettingFiles")){
            LOG.info("TestMe: ByTitle");
            Test=(Object[]) Groups.GetAllVidsByTitle(PropertyAdder, Method,null,null);
        }
        else{
            LOG.info("TestMe: ByGroup");
            Test = (Object[]) GroupBuilder.GetAllVideosGrouped(PropertyAdder, Method);
        }

        for(Object Curr:Test){
            LOG.info("TestMe: looping");
            System.out.println("TestMe: Curr="+sagex.api.ShowAPI.GetShowTitle(Curr));
        }
        System.out.println("TestMe: Test Size"+Test.length);
        LOG.info("TestMe: Test Size"+Test.length);

        Object tester =sagex.api.Database.GroupByArrayMethod(Test,"sagediamond_MetadataCalls_GetMediaTitle");

        System.out.println("TestMe: " + tester.getClass() + " - done");
        LOG.info("TestMe: " + tester.getClass() + " - done");

    }
    
    
}
