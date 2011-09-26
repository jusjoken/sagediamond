/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SDGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.apache.log4j.Logger;

/**
 *
 * @author SBANTA
 * @author JUSJOKEN
 * - 9/29/2011 - minor code flow updating, log message changes for testing
 */
public class Groups {

    static private final Logger LOG = Logger.getLogger(Groups.class);


//
//    public static List<Object> GetAllShowsGroupedBySeason(List<Object> Shows){
//    List<Object> GroupedList = new ArrayList<Object>();
//    HashMap<String,Object> AllShowsByTitle = sagex.api.Database.GroupByMethod(Shows, null)
//    List<Object> NewestList = (List<Object>) sagex.api.Database.SortLexical(Shows, false, "sageeasytv_MetadataCalls_GetOriginalAirDate");
//    Collections.sort(Shows,null);
//



//
//
//    }

    public static void main(String[] args){

        Object[] Test=(Object[]) GetAllVidsByTitle("Testnman","GettingFiles",null,null);
   
        for(Object Curr:Test){
            System.out.println("Curr="+sagex.api.ShowAPI.GetShowTitle(Curr));
        }
        System.out.println("Test Size"+Test.length);

        Object tester =sagex.api.Database.GroupByArrayMethod(Test,"sagediamond_MetadataCalls_GetMediaTitle");

        System.out.println("done"+tester.getClass());

    }
    
    public static Object GetAllVidsByTitle(String PropertyAdder){
        return GetAllVidsByTitle(PropertyAdder,"none",null,null);
    }

    public static Object GetAllVidsByTitle(String PropertyAdder, String FilterMethod,Object PassValue,Object[] AllVids){
        System.out.println("SDGroup: Grouping Files started for Property='" + SortMethods.PropertyPrefix + "'");

        //save the time to determine the elapsed time for method
        Long Time = System.currentTimeMillis();

        if(FilterMethod.equals("none")||FilterMethod.equals("GettingFiles")){
            Boolean IncludeTV=GetPropertyBoolean("IncludeTV","true");
            Boolean IncludeMovies=GetPropertyBoolean("IncludeMovies","true");
            Object[] AllTV=null;
            Object[] AllMovies=null;
     
            if(IncludeTV){
                LOG.debug("Including TV getting all movie types.");
                AllTV=sagex.api.MediaFileAPI.GetMediaFiles(GetFileTypes("TV"));
                AllTV=(Object[]) sagex.api.Database.FilterByBoolMethod(AllTV,"sagediamond_MetadataCalls_IsMediaTypeTV",true);
            }

            if(IncludeMovies){
                LOG.debug("Including Movies getting all movie types.");
                AllMovies=sagex.api.MediaFileAPI.GetMediaFiles(GetFileTypes("Movies"));
            }
            AllMovies=(Object[]) sagex.api.Database.FilterByBoolMethod(AllMovies,"sagediamond_MetadataCalls_IsMediaTypeTV",false);


            //Check and run Folder Filter if necessary
            AllTV=RunFolderFilter(AllTV,PropertyAdder);
            AllMovies=RunFolderFilter(AllMovies,PropertyAdder);

            //Check for Unscraped Included and filter if needed
            if(!GetPropertyBoolean("IncludeUnScraped","true")){
                LOG.debug("Not Including UnScraped filterting them out");
   
                AllMovies =(Object[]) sagex.api.Database.FilterByMethod(AllMovies, "phoenix_metadata_GetMediaType", "Movie",true);
            }

            //Check for Watch Included and filter if needed
            if(!GetPropertyBoolean("IncludeWatched","true")){
                LOG.debug("Not Including Watched filterting them out");
                AllTV =(Object[]) sagex.api.Database.FilterByBoolMethod(AllTV, "IsWatched", false);
                AllMovies =(Object[]) sagex.api.Database.FilterByBoolMethod(AllMovies, "IsWatched", false);
            }

            if(IncludeTV){
                LOG.debug("Running TV Filters");
                if(!GetPropertyBoolean("IncludeImportedTV","true")){
                    LOG.debug("Not Including ImportedTV filterting them out");
                    AllTV=(Object[]) sagex.api.Database.FilterByBoolMethod(AllTV,"sagediamond_MetadataCalls_IsImportedTV", false);
                }
                if(!GetPropertyBoolean("IncludeImportedTV","true")&&GetPropertyBoolean("RecordedTV","true")){
                    LOG.debug("Not Including RecordedTV filterting them out");
                    AllTV=(Object[]) sagex.api.Database.FilterByBoolMethod(AllTV,"sagediamond_MetadataCalls_IsRecordedTV", false);
                }
                if(!GetPropertyBoolean("IncludePlayonTV","true")){
                    LOG.debug("Not Including PlayonTV filterting them out");
                    AllTV = (Object[]) sagex.api.Database.FilterByBoolMethod(AllTV,"sagediamond_MetadataCalls_IsPlayonFile", false);
                }
            }

            if(IncludeMovies){
                LOG.debug("Running Movie Filters");
                if(GetPropertyBoolean("IncludeRecordedMovies","true")){
                    LOG.debug("Including Recorded Movies add them.");
                    Object[] RecordedMovies = (Object[]) sagex.api.Database.FilterByMethod(sagex.api.MediaFileAPI.GetMediaFiles("T"), "UserCategories", "Movie,Film", true );
                    Vector test=sagex.api.Database.DataUnion(AllMovies, RecordedMovies);
                    AllMovies=test.toArray();
                }
                if(!GetPropertyBoolean("IncludePlayonMovies","true")){
                    LOG.debug("Not Including Playon movies filterting them out");
                    AllMovies = (Object[]) sagex.api.Database.FilterByBoolMethod(AllMovies,"sagediamond_MetadataCalls_IsPlayonFile", false);
                }
            }
            System.out.println("done");
            Vector Test=sagex.api.Database.DataUnion(AllMovies,AllTV);
            LOG.debug("VectorSize="+Test.size());
            AllVids=Test.toArray();

     
     
            if(FilterMethod.equals("GettingFiles")){
                return AllVids;
            }
        }
        else{

            LOG.debug("Building a custom group filter that method");
            AllVids=(Object[]) sagex.api.Database.FilterByMethod(AllVids,FilterMethod,PassValue,true);
        }

        HashMap<String,Object> presorted = new HashMap<String,Object>();
        if(AllVids.length>0){
            Dividers.SageClass=AllVids[0].getClass();
            AllVids = (Object[]) sagex.api.Database.SortLexical(AllVids,false,SortMethods.GetMainSortMethod());
            HashMap<String,Vector> tester = (HashMap<String,Vector>) sagex.api.Database.GroupByArrayMethod(AllVids, "sagediamond_MetadataCalls_GetMediaTitle");
    
            Iterator keys = tester.keySet().iterator();
            String EpisodeSortMethod =SortMethods.GetEpisodeSortMethod();
            LOG.debug("Episode Sort Method="+EpisodeSortMethod);
            Boolean ShowDividers =Boolean.parseBoolean(sagex.api.Configuration.GetProperty(SortMethods.PropertyPrefix+"ShowDividers","false"));
            LOG.debug("Dividers on ="+ShowDividers);
            Boolean ShowSeasons = Boolean.parseBoolean(sagex.api.Configuration.GetProperty(SortMethods.PropertyPrefix+"ShowSeasons","true"));
            LOG.debug("Show Sesaons="+ShowSeasons);
            int i=00;

            while(keys.hasNext()){
                String currentkey= keys.next().toString();
                Vector current =tester.get(currentkey);
                LOG.trace("CurrentKey="+currentkey);
                Object[] Seasonsorted = (Object[]) sagex.api.Database.Sort(current, false,EpisodeSortMethod);
                LOG.trace("Done sorting Episodes for="+currentkey);
                if(EpisodeSortMethod.equals("sagediamond_MetadataCalls_GetSeasonNumber")&&ShowSeasons){
                    SeasonHandler.BuildSeasonHashMap(Seasonsorted,String.format("%02d",i)+"&&"+currentkey);
                    presorted.put(String.format("%02d",i)+"&&"+currentkey,sagex.api.Database.GroupByMethod(Seasonsorted,"sagediamond_MetadataCalls_GetEpisodeTitle"));

                }
                else if(ShowDividers){
                    LOG.trace("Show Dividers on go ahead and get them");
                    presorted.put(String.format("%02d",i)+"&&"+currentkey,sagex.api.Database.GroupByMethod(Dividers.AddDividers(Seasonsorted,EpisodeSortMethod),"sagediamond_MetadataCalls_GetEpisodeTitle"));
                }
                else{
                    LOG.trace("Show Dividers off just add sorted list without dividers");
                    presorted.put(String.format("%02d",i)+"&&"+currentkey,sagex.api.Database.GroupByMethod(Seasonsorted,"sagediamond_MetadataCalls_GetEpisodeTitle"));
                }

                i++;
                LOG.debug("CurrentKeyAdded="+currentkey);
            }
        }
        //check the current time and determine elapsed
        Long Time2 = System.currentTimeMillis()- Time;
        LOG.debug("Done getting datafiles. Grouping/Sorting/Filter done in "+Time2+"ms");
        return  sagex.api.Database.Sort(presorted, false,"SDGroup_SortMethods_GetFinalSortMethod");
    }

    public static List<String> GetAllSeasons(List<Object> currshows){
        List<String> Seasons= new ArrayList<String>();

        for(Object curr:currshows){
            if(!Dividers.IsDivider(curr)){
                String currseason = sagediamond.MetadataCalls.GetSeasonNumberPad(curr);
                if(!Seasons.contains(currseason)){
                    Seasons.add(currseason);
                }
            }
        }
        return Seasons;
    }

    public static List<Object> GetAllSeasons(Object[] currshows){
        List<Object> Seasons= new ArrayList<Object>();

        for(Object curr:currshows){
            if(!Dividers.IsDivider(curr)){
                String currseason = sagediamond.MetadataCalls.GetSeasonNumberPad(curr);
                if(!Seasons.contains(currseason)){
                    Seasons.add(currseason);
                }   
            }
        }
        return Seasons;
    }

    public static Object GetSeasonLocationForFocus(List<Object> shows,Integer season){
        int Location= sagex.api.Utility.FindElementIndex(shows, season)+1;
        return shows.get(Location);
    }

    public static String GetFileTypes(String Type){

        String Types = "L";
        if(GetPropertyBoolean(Type+"IncludeVideos","true")){
            Types = Types+"V";
        }
        if(GetPropertyBoolean(Type+"IncludeDVD","true")){
            Types=Types+"D"; 
        }
        if(GetPropertyBoolean(Type+"IncludeBluRay","true")){
            Types=Types+"B"; 
        }
        if(Type.equals("TV")){
            Types = Types+"T";
        }

        LOG.debug("Type of videos = "+Types);
        return Types;
    }

    public static boolean IsIncludingPlayon(){
        return Boolean.parseBoolean(sagex.api.Configuration.GetProperty(SortMethods.PropertyPrefix+"OnlineTV","false"));
    }

    public static HashMap<String,Vector> GetPlayonFiles(){
        Object[] AllPlayonTV=sagex.api.MediaFileAPI.GetMediaFiles("TVDBL");
        AllPlayonTV=(Object[]) sagex.api.Database.FilterByBoolMethod(AllPlayonTV,"sagediamond_MetadataCalls_IsMediaTypeTV", true);
        AllPlayonTV = (Object[]) sagex.api.Database.FilterByBoolMethod(AllPlayonTV,"sagediamond_MetadataCalls_IsPlayonFile", true);
        AllPlayonTV = (Object[]) sagex.api.Database.SortLexical(AllPlayonTV,false,SortMethods.GetMainSortMethod());
        HashMap<String,Vector> tester = (HashMap<String,Vector>) sagex.api.Database.GroupByArrayMethod(AllPlayonTV, "sagediamond_MetadataCalls_GetMediaTitle");
        return tester;
    }

    public static HashMap<String,Vector> FilterInPlayonFiles(HashMap<String,Vector> AllVids){
        HashMap<String,Vector> PlayonTV = GetPlayonFiles();
        Iterator Playon = PlayonTV.keySet().iterator();
        while(Playon.hasNext()){

            String currkey = (String) Playon.next();
            if(!AllVids.containsKey(currkey)){
                AllVids.put(currkey, PlayonTV.get(currkey));
            }
            else{
                Vector currplay=PlayonTV.get(currkey);
                Vector currtv=AllVids.get(currkey);
                for(Object curr:currplay){
                    String Episode=sagediamond.MetadataCalls.GetEpisodeTitle(curr);
                    int NumMatchFound = 0;
                    for(Object cuurtv:currtv){
                        String TVEpisode = sagediamond.MetadataCalls.GetEpisodeTitle(cuurtv);
                        if(TVEpisode.equals(Episode)){
                            NumMatchFound++;
                        }
                    }
                    if(NumMatchFound>1){
                        LOG.debug("More than one show found with episode Title go ahead and remove="+Episode+";"+curr.toString());
                        currtv.remove(curr);
                    }
                }
                AllVids.put(currkey, currtv);

            }

        }
        return AllVids;
    }

    public static Object[] RunFolderFilter(Object[] MediaFiles,String CurrView){
        if(sagediamond.FolderExclusion.GetAllFolderRestrictions(CurrView).size()>0){
            LOG.debug("Folder Filters needed running now.");
            MediaFiles=sagediamond.FolderExclusion.RunFolderFilter(MediaFiles, CurrView);
        }
        return MediaFiles;
    }

    public static Boolean GetPropertyBoolean(String Prop,String Default){
        return Boolean.parseBoolean(sagex.api.Configuration.GetProperty(SortMethods.PropertyPrefix+Prop,Default));
    }

}

