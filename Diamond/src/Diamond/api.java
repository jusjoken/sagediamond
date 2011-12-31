/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Diamond;

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
 * - 9/27/2011 - added LOG4J setup and Load method
 * - 10/1/2011 - implemented Load and InitLogger methods
 * - 10/28/2011 - Conversion to Diamond API for 4.x
 */
public class api {

    public static Logger LOG=null;

    public static String Version = "4.011";

    public static PropList YESNOList = new PropList();
    public static PropList ONOFFList = new PropList();
    public static PropList InstantSearchModes = new PropList();
    public static PropList InstantSearchExecuteModes = new PropList();
    public static enum YESNO{YES,NO};
    public static enum ONOFF{ON,OFF};
    public static enum InstantSearchMode{FILTERED,JUMPTO};
    public static enum InstantSearchExecuteMode{SELECT,AUTO};
    

    public static void main(String[] args){

        Load();
    }


    //load any Diamond settings that need to load at application start
    public static void Load(){
        //initialize the Logging 
        InitLogger();
        
        //generate symbols to be used for new names
        for (int idx = 0; idx < 10; ++idx)
            util.symbols[idx] = (char) ('0' + idx);
        for (int idx = 10; idx < 36; ++idx)
            util.symbols[idx] = (char) ('a' + idx - 10);
        
        //prep Property Lists
        YESNOList.put(YESNO.YES.toString(), new Property(YESNO.YES.toString(), "Yes",Boolean.FALSE,Boolean.TRUE));
        YESNOList.put(YESNO.NO.toString(), new Property(YESNO.NO.toString(), "No", Boolean.TRUE, Boolean.FALSE));
        ONOFFList.put(ONOFF.ON.toString(), new Property(ONOFF.ON.toString(), "On",Boolean.FALSE,Boolean.TRUE));
        ONOFFList.put(ONOFF.OFF.toString(), new Property(ONOFF.OFF.toString(), "Off", Boolean.TRUE, Boolean.FALSE));
        InstantSearchModes.put(InstantSearchMode.JUMPTO.toString(), new Property(InstantSearchMode.JUMPTO.toString(), "Jump to", Boolean.TRUE));
        InstantSearchModes.put(InstantSearchMode.FILTERED.toString(), new Property(InstantSearchMode.FILTERED.toString(), "Filtered"));
        InstantSearchExecuteModes.put(InstantSearchExecuteMode.AUTO.toString(), new Property(InstantSearchExecuteMode.AUTO.toString(), "Auto Filter as you type"));
        InstantSearchExecuteModes.put(InstantSearchExecuteMode.SELECT.toString(), new Property(InstantSearchExecuteMode.SELECT.toString(), "Press Select to Filter",Boolean.TRUE));
        
        //ensure the gemstone file location exists
        try{
            boolean success = (new File(util.UserDataLocation())).mkdirs();
            if (success) {
                LOG.debug("Load: Directories created for '" + util.UserDataLocation() + "'");
               }

            }catch (Exception ex){//Catch exception if any
                LOG.debug("Load: - error creating '" + util.UserDataLocation() + "'" + ex.getMessage());
            }
        
   }

    public static void InitLogger(){
        //initialize the Logging 
        System.out.println("InitLogger: setting up logger");
        LOG = Logger.getLogger(api.class);
        String log4jfile = "STVs" + File.separator + "Diamond" + File.separator + "Configuration" + File.separator + "Diamond.log4j.properties";
        String log4jfullpath = sagex.api.Utility.GetWorkingDirectory(new UIContext(sagex.api.Global.GetUIContextName())) + File.separator + log4jfile;
        //check if the log4j property file exists and use defaults if it does not
        Boolean FileExists = (new File(log4jfullpath)).exists();
        if (FileExists){
            System.out.println("InitLogger: using '" + log4jfullpath + "' for log properties");
            PropertyConfigurator.configure(log4jfullpath);
        }else{
            //configure manually
            System.out.println("InitLogger: using internal defaults for log properties. Properties file not found '" + log4jfullpath + "'");
            Properties log4jProps = new Properties();
            log4jProps.put("log4j.rootCategory", "debug, Log");
            log4jProps.put("log4j.additivity.Diamond", "false");
            log4jProps.put("log4j.appender.Diamond", "org.apache.log4j.RollingFileAppender");
            log4jProps.put("log4j.appender.Diamond.File", "logs/Diamond.log");
            log4jProps.put("log4j.appender.Diamond.layout", "org.apache.log4j.PatternLayout");
            log4jProps.put("log4j.appender.Diamond.layout.ConversionPattern", "%d{EEE M/d HH:mm:ss.SSS} [%t] %-5p %c - %m%n");
            log4jProps.put("log4j.appender.Diamond.MaxBackupIndex", "5");
            log4jProps.put("log4j.appender.Diamond.MaxFileSize", "10000KB");
            log4jProps.put("log4j.appender.Diamond.Threshold", "debug");
            log4jProps.put("log4j.additivity.Sage", "false");
            log4jProps.put("log4j.appender.Sage", "org.apache.log4j.ConsoleAppender");
            log4jProps.put("log4j.appender.Sage.layout", "org.apache.log4j.PatternLayout");
            log4jProps.put("log4j.appender.Sage.layout.ConversionPattern", "%d{EEE M/d HH:mm:ss.SSS} [%t] %-5p %c - %m%n");
            log4jProps.put("log4j.appender.Sage.Threshold", "debug");
            //log4jProps.put("log4j.logger.SDGroup", "debug,Diamond,Sage");
            log4jProps.put("log4j.logger.Diamond", "debug,Diamond,Sage");
            PropertyConfigurator.configure(log4jProps);
        }
        LOG.info("Logger for Diamond created successfully!");
//        LOG.debug("Test Log Message - debug");
//        LOG.info("Test Log Message - info");
//        LOG.warn("Test Log Message - warn");
//        LOG.error("Test Log Message - error");
//        LOG.fatal("Test Log Message - fatal");
   }

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
