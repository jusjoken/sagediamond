/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PloxeeTV;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author SBANTA
 */
public class SortMethods {

static private final Logger LOG = Logger.getLogger(SortMethods.class);

    public static String Title = "PloxeeTV_MetadataCalls_GetSortTitle";
    public static String AiringDate="PloxeeTV_MetadataCalls_GetOriginalAirDate";
    public static String RecordingDate="GetAiringStartTime";
    public static String WatchedDate = "GetRealWatchedEndTime";
    public static String Seasons = "PloxeeTV_MetadataCalls_GetSeasonNumber";
    public static String EpisodeTitle = "PloxeeTV_MetadataCalls_GetEpisodeTitle";

    public static String PropertyAdder ="Default";
    public static String PropertyDefault ="ortus/ploxee/tv/";


    public static String PropertyPrefix= "ortus/ploxee/tv/"+PropertyAdder;

    public static void main(String[] args){
    String Value=ClassFromString.GetSortMethod("AiringDate");
    System.out.println(Value);

    }

    public static String GetMainSortMethod(){
    LOG.trace("Getting Main sort Method");
    String CurrentMethod = sagex.api.Configuration.GetProperty(PropertyPrefix+"MainSortMethod","Title");
    LOG.info("Main sort method property="+CurrentMethod);
    String Return = ClassFromString.GetSortMethod(CurrentMethod);
    LOG.debug("Main sort ClassFromString="+Return);
    return Return;

    }

    public static String GetEpisodeSortMethod(){
    LOG.trace("Getting Episode sort Method");
    String CurrentMethod = sagex.api.Configuration.GetProperty(PropertyPrefix+"EpisodeSortMethod","EpisodeTitle");
    LOG.info("Episode sort method property="+CurrentMethod);
    String Return = ClassFromString.GetSortMethod(CurrentMethod);
    LOG.debug("Episode sort ClassFromString="+Return);
    return Return;

    }

    public static boolean IsTitleType(String SubSort){
    return SubSort.contains("Title");
    }

    public static List<String> GetAllSortMethods(String Type){
    List<String> Sorts = new ArrayList<String>();
        if(Type.contains("Main")){
            Sorts.add("Title");
            Sorts.add("AiringDate");
            Sorts.add("RecordingDate");
            Sorts.add("WatchedDate");
    }
    else{
        Sorts.add("EpisodeTitle");
            Sorts.add("AiringDate");
            Sorts.add("RecordingDate");
            Sorts.add("WatchedDate");
            Sorts.add("Seasons");
    }
    return Sorts;

    }

    public static int GetFinalSortMethod(String sortname){
    String[] FullTitle =sortname.split("&&");
    String Location = FullTitle[0];
    return Integer.parseInt(Location);
    }

}
