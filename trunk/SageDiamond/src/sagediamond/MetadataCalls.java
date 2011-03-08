/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sagediamond;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author SBANTA
 */
public class MetadataCalls {

//    static private final Logger LOG = Logger.getLogger(MetadataCalls.class);
    public static String PlayonDirectory = sagex.api.Configuration.GetServerProperty("PlayonPlayback/ImportDirectory", "/SageOnlineServicesEXEs\\UPnPBrowser\\PlayOn") + "\\TV\\";
    public static String HuluFile = "Quicktime[H.264/50Kbps 480x368@24fps]";
    public static String NetflixFile = "Quicktime[H.264/50Kbps 480x368@25fps]";

    public static Integer GetSeasonNumber(Object MediaObject) {
        String SN = sagex.api.MediaFileAPI.GetMediaFileMetadata(MediaObject, "SeasonNumber");
        if (SN.equals("")) {
            SN = "0";
        }
        return Integer.parseInt(SN);

    }

    public static String GetSeasonNumberDivider(Object MediaObject) {
        return "Season " + GetSeasonNumber(MediaObject);

    }

    public static String GetSortTitle(Object MediaObject) {
        String Title = GetMediaTitle(MediaObject);
        if (Title.equals("") || Title.equals(null)) {
            return "000";
        }
        return sagex.api.Database.StripLeadingArticles(Title.toLowerCase());

    }

    public static Integer GetEpisodeNumber(Object MediaObject) {
        String EN = sagex.api.MediaFileAPI.GetMediaFileMetadata(MediaObject, "EpisodeNumber");
        if (EN.equals("")) {
            EN = "0";
        }
        return Integer.parseInt(EN);
    }

    public static String GetEpisodeNumberPad(Object MediaObject) {
        int en = GetEpisodeNumber(MediaObject);


        return String.format("%02d", en);


    }

    public static String GetSeasonNumberPad(Object MediaObject) {
        int en = GetSeasonNumber(MediaObject);
        return String.format("%02d", en);
    }

    public static String GetMediaTitle(Object MediaObject) {
        String Title = sagex.api.MediaFileAPI.GetMediaFileMetadata(MediaObject, "MediaTitle");
        if (Title.equals("") || Title.equals(null)) {
            Title = sagex.api.MediaFileAPI.GetMediaTitle(MediaObject);
        }
         if (Title.equals("") || Title.equals(null)) {
        Title = sagex.api.AiringAPI.GetAiringTitle(MediaObject);
         }
        if (Title.equals("") || Title.equals(null)) {
            return "Unknown";
        }
        return Title;
    }

    public static int GetShowDuration(Object MediaObject) {
        long duration = sagex.api.ShowAPI.GetShowDuration(MediaObject);
        int durationint = (int) duration;
        if (durationint > 0) {
            durationint = durationint / 60000;
        }
        return durationint;

    }

    public static boolean IsImportedNotPlayon(Object MediaObject) {
        return !IsPlayonFile(MediaObject) && IsImportedTV(MediaObject);
    }

    public static boolean IsPlayonFile(Object MediaObject) {
//    System.out.println("Checking for playon type="+sagex.api.MediaFileAPI.GetParentDirectory(MediaObject)+"for value"+PlayonDirectory);
   return sagex.api.MediaFileAPI.GetParentDirectory(MediaObject).toString().contains(PlayonDirectory);
    }

    public static int GetPlayonFileType(Object MediaObject) {
        String Type = sagex.api.MediaFileAPI.GetMediaFileFormatDescription(MediaObject);
        if (Type.equals(HuluFile)) {
            return 1;
        }
        if (Type.equals(NetflixFile)) {
            return 2;
        } else {
            return 3;
        }

    }

//   public static String GetEpisodeTitle(Object MediaObject){
//      if(MediaObject.getClass().equals(Dividers.SageClass))
//	{
//       String Title  = sagex.api.ShowAPI.GetShowEpisode(MediaObject);
//       if(Title.equals("")){
//		return sagex.api.MediaFileAPI.GetMediaFileMetadata(MediaObject, "EpisodeTitle");}
//       return Title;
//	}
//      else{
//       return MediaObject.toString();}}
//     public static String GetEpisodeTitleDivider(Object MediaObject)
//	{
//         String EpisodeTitle=GetEpisodeTitle(MediaObject);
//         if(EpisodeTitle.equals(null)||EpisodeTitle.length()<1){
//         return "Unknown";}
//                 return GetEpisodeTitle(MediaObject).substring(0,1).toUpperCase();
//	}
    public static long GetOriginalAirDate(Object MediaObject) /*returns the OriginalAiringDate as a long (in java date format) gathered from the metadta
     * 0 if it does not exist or catches an error
     *
     * @param MediaObject, a sage Airing, Show, or MediaFile
     */ {
        String s1 = sagex.api.MediaFileAPI.GetMediaFileMetadata(MediaObject, "OriginalAirDate");
        //System.out.println("OriginalAirDateString = '" + OrigAirDateString + "'");

        if (s1.length() == 0) {
            //System.out.println("No Original Air Date");
            return 0;
        } //System.out.println("OriginalAirDateString = '" + OrigAirDateString + "'");
        else {
//                        LOG.debug("OriginalAiringDate="+s1);
            Long l1 = Long.parseLong(s1);

//			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return l1 + GetSeasonEpisodeNumber(MediaObject);
        }





    }

    public static int GetSeasonEpisodeNumber(Object MediaObject) /*
     * returns the SeasonEpisode Number as an integer
     * or 0 if Season Number does not exist
     * 101 (s01e01)
     * 201 (s02e01)
     * 1010 (s10e10)
     *
     * @param MediaObject, a sage Airing, Show, or MediaFile Object
     */ {
        int sn = GetSeasonNumber(MediaObject);
        int en = GetEpisodeNumber(MediaObject);

        if (sn == 0) {
            return 0;
        } else {
            return sn * 100 + en;
        }
    }

    public static String GetMediaType(Object MediaObject) {
        return sagex.api.MediaFileAPI.GetMediaFileMetadata(MediaObject, "MediaType");
    }

    public static boolean IsMediaTypeTV(Object MediaObject) {
        String Type = sagex.api.MediaFileAPI.GetMediaFileMetadata(MediaObject, "MediaType");
        if (Type.contains("TV") || sagex.api.MediaFileAPI.IsTVFile(MediaObject)) {
                        return true;
        } else {

            return false;
        }
    }

    public static boolean IsImportedTV(Object MediaObject) {

         if (IsMediaTypeTV(MediaObject) && !sagex.api.MediaFileAPI.IsTVFile(MediaObject)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean IsRecordedTV(Object MediaObject) {
        return sagex.api.MediaFileAPI.IsTVFile(MediaObject);
    }

    public static Long GetDateRecorded(Object MediaObject) {
        return sagex.api.AiringAPI.GetAiringStartTime(MediaObject);

    }




    public static String GetShowCategory(Object MediaObject) {
        String Cat = sagex.api.ShowAPI.GetShowCategory(MediaObject);
        if(Cat.startsWith("Movie")&&Cat.contains("/")){

        Cat=Cat.substring(Cat.indexOf("/"));
        }

        if (Cat.equals("")) {
            return "unknown";
        }
        if (Cat.contains("and")) {
            return Cat.substring(0, Cat.indexOf("and") - 1);
        }
        if (Cat.contains(",")) {

            return Cat.substring(0, Cat.indexOf(","));
        }
       return Cat;

    }

    public static ArrayList<String> GetAllShowCategories(Object MediaObject){
    String[] Cats=sagex.api.ShowAPI.GetShowCategory(MediaObject).split(",");
    ArrayList<String> AllCats = new ArrayList<String>();
    for(String curr:Cats){
    if(curr.contains("and")){
    curr=curr.trim();
    String[] andsplit = curr.split("and");
    String cat1=andsplit[0];
    cat1=cat1.trim();
    String cat2=andsplit[0];
    cat1=cat2.trim();
    System.out.print("Adding Category=="+cat1+"!!"+cat2+"!!");
    AllCats.add(cat1);
    AllCats.add(cat2);}
    else if(curr.equals("")){
    AllCats.add("unknown");}
    else if(curr.startsWith(" ")){
    AllCats.add(curr.substring(1));}
    else{
    System.out.println("Adding single categories="+curr+"!!");
    AllCats.add(curr);}}
    return AllCats;
    }
//     public static String GetTimeAdded(Object Title) {
//    // Check to see if date variables have been set
//    if(!DateConverter.IsDateVariableSet){
//    LOG.trace("DateVariablesNot set Go ahead and set them");
//    String First =sagex.api.Configuration.GetProperty(SortMethods.PropertyPrefix+"DateGroupFirstTime","10080");
//    String Second = sagex.api.Configuration.GetProperty(SortMethods.PropertyPrefix+"DateGroupSecondTime","20160");
//    String Third = sagex.api.Configuration.GetProperty(SortMethods.PropertyPrefix+"DateGroupThirdTime","43200");
//    String Fourth = sagex.api.Configuration.GetProperty(SortMethods.PropertyPrefix+"DateGroupFourthTime","86400");
//    Long LFirst = java.lang.Long.parseLong(First);
//    LFirst = LFirst *60*1000;
//    Long LSecond = java.lang.Long.parseLong(Second);
//    LSecond = LSecond*60*1000;
//    Long LThird = java.lang.Long.parseLong(Third);
//    LThird = LThird*60*1000;
//    Long LFourth = java.lang.Long.parseLong(Fourth);
//    LFourth = LFourth *60*1000;
//        Long CurrTime = System.currentTimeMillis();
//    DateConverter.FirstDateGroup =CurrTime-LFirst;
//    DateConverter.SecondDateGroup=CurrTime-LSecond;
//    DateConverter.ThirdDateGroup=CurrTime-LThird;
//    DateConverter.FourthDateGroup=CurrTime-LFourth;
//    DateConverter.FifthDateGroup =CurrTime-(Long.parseLong(sagex.api.Configuration.GetProperty(SortMethods.PropertyPrefix+"DateGroupFifthTime","86401"))*60*1000);
//    DateConverter.IsDateVariableSet=true;}
//    LOG.trace("DateVariables Set to="+DateConverter.FirstDateGroup+":");
//    LOG.trace("DateVariables Set to="+DateConverter.SecondDateGroup+":");
//    LOG.trace("DateVariables Set to="+DateConverter.ThirdDateGroup+":");
//    LOG.trace("DateVariables Set to="+DateConverter.FourthDateGroup+":");
//    LOG.trace("DateVariables Set to="+DateConverter.FifthDateGroup+":");
//
//    Long DateAdded = (Long) ClassFromString.GetDateClass("GetOriginalAirDate", Title);
//    if(DateAdded>=DateConverter.FirstDateGroup){
//    return sagex.api.Configuration.GetProperty(SortMethods.PropertyPrefix+"DateGroupFirstName","New");}
//    else if (DateAdded>=DateConverter.SecondDateGroup){
//    return sagex.api.Configuration.GetProperty(SortMethods.PropertyPrefix+"DateGroupSecondName","Last Week");}
//    else if (DateAdded>=DateConverter.ThirdDateGroup){
//    return sagex.api.Configuration.GetProperty(SortMethods.PropertyPrefix+"DateGroupThirdName","30 days");}
//   else if (DateAdded>=DateConverter.FourthDateGroup){
//    return sagex.api.Configuration.GetProperty(SortMethods.PropertyPrefix+"DateGroupFourthName","60 days");}
//   else{
//
//    return sagex.api.Configuration.GetProperty(SortMethods.PropertyPrefix+"DateGroupFifthName","Older");}
//
//        }
}
