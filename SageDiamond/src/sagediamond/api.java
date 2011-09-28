/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sagediamond;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import sagex.UIContext;

/**
 *
 * @author SBANTA
 * @author JUSJOKEN
 * - 9/26/2011 - version number changed to 3.401
 * - 9/27/2011 - added LOG4J setup and Load method
 */
public class api {

    public static Logger LOG=null;

    public static void main(String[] args){

        Load();
    }
    
    //load any SageDiamond settings that need to load at application start
    public static void Load(){
        //initialize the Logging 
        System.out.println("Load: setting up logger");
        LOG = Logger.getLogger(api.class);
        System.out.println("Load: setting up logger - 2");
        String log4jfile = "STVs" + File.separator + "SageDiamond" + File.separator + "Configuration" + File.separator + "SageDiamond.log4j.properties";
        String log4jfullpath = sagex.api.Utility.GetWorkingDirectory(new UIContext(sagex.api.Global.GetUIContextName())) + File.separator + log4jfile;
        //check if the log4j property file exists and use defaults if it does not
        Boolean FileExists = (new File(log4jfullpath)).exists();
        System.out.println("Load: fileExists = '" + FileExists + "'");
        if (FileExists){
            PropertyConfigurator.configure(log4jfullpath);
        }else{
            //configure manually
            Properties log4jProps = new Properties();
            log4jProps.put("log4j.rootCategory", "debug, Log");
            log4jProps.put("log4j.additivity.SageDiamond", "false");
            log4jProps.put("log4j.appender.SageDiamond", "org.apache.log4j.RollingFileAppender");
            log4jProps.put("log4j.appender.SageDiamond.File", "logs/SageDiamond.log");
            log4jProps.put("log4j.appender.SageDiamond.layout", "org.apache.log4j.PatternLayout");
            log4jProps.put("log4j.appender.SageDiamond.layout.ConversionPattern", "%d{EEE M/d HH:mm:ss.SSS} [%t] %-5p %c - %m%n");
            log4jProps.put("log4j.appender.SageDiamond.MaxBackupIndex", "5");
            log4jProps.put("log4j.appender.SageDiamond.MaxFileSize", "10000KB");
            log4jProps.put("log4j.appender.SageDiamond.Threshold", "debug");
            log4jProps.put("log4j.additivity.Sage", "false");
            log4jProps.put("log4j.appender.Sage", "org.apache.log4j.ConsoleAppender");
            log4jProps.put("log4j.appender.Sage.layout", "org.apache.log4j.PatternLayout");
            log4jProps.put("log4j.appender.Sage.layout.ConversionPattern", "%d{EEE M/d HH:mm:ss.SSS} [%t] %-5p %c - %m%n");
            log4jProps.put("log4j.appender.Sage.Threshold", "info");
            log4jProps.put("log4j.logger.SDGroup", "debug,SageDiamond,Sage");
            log4jProps.put("log4j.logger.sagediamond", "debug,SageDiamond,Sage");
            PropertyConfigurator.configure(log4jProps);
        }
        LOG.info("Logger for SageDiamond created successfully!");
//        LOG.debug("Test Log Message - debug");
//        LOG.info("Test Log Message - info");
//        LOG.warn("Test Log Message - warn");
//        LOG.error("Test Log Message - error");
//        LOG.fatal("Test Log Message - fatal");
   }

    public static String Version = "3.401";


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
