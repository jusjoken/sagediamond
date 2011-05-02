/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PloxeeTV;

import PloxeeTV.*;
import PloxeeTV.MetadataCalls;
import PloxeeTV.SeasonHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.apache.log4j.Logger;

/**
 *
 * @author SBANTA
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

    public static Object GetAllTVByTitle(String PropertyAdder){
    return GetAllTVByTitle(PropertyAdder,"none","none",null);}

    public static Object GetAllTVByTitle(String PropertyAdder, String FilterMethod,Object PassValue,Object[] AllTV){
       LOG.info("Grouping Files started");
       Long Time = System.currentTimeMillis();

     if(FilterMethod.equals("none")||FilterMethod.equals("GettingFiles")){

      
     String FileTypes = GetFileTypes();
     AllTV=sagex.api.MediaFileAPI.GetMediaFiles(FileTypes);
     if(!Boolean.parseBoolean(sagex.api.Configuration.GetProperty(SortMethods.PropertyPrefix+"Movies","true"))){
     LOG.debug("Not Including Movies filterting them out");
     AllTV=(Object[]) sagex.api.Database.FilterByBoolMethod(AllTV,"PloxeeTV_MetadataCalls_IsMediaTypeTV", true);}
     if(!Boolean.parseBoolean(sagex.api.Configuration.GetProperty(SortMethods.PropertyPrefix+"ImportedTV","true"))){
      LOG.debug("Not Including ImportedTV filterting them out");
     AllTV=(Object[]) sagex.api.Database.FilterByBoolMethod(AllTV,"PloxeeTV_MetadataCalls_IsImportedTV", false);}
//     if(Boolean.parseBoolean(sagex.api.Configuration.GetProperty(SortMethods.PropertyPrefix+"ImportedTV","true"))&&!Boolean.parseBoolean(sagex.api.Configuration.GetProperty(SortMethods.PropertyPrefix+"RecordedTV","true"))){
//     AllTV=(Object[]) sagex.api.Database.FilterByBoolMethod(AllTV,"PloxeeTV_MetadataCalls_IsRecordedTV", false);}
     if(!Boolean.parseBoolean(sagex.api.Configuration.GetProperty(SortMethods.PropertyPrefix+"RecordedTV","true"))){
      AllTV=(Object[]) sagex.api.Database.FilterByBoolMethod(AllTV,"PloxeeTV_MetadataCalls_IsRecordedTV", false);}
     if(!IsIncludingPlayon()){
      LOG.debug("Not Including Playon filterting them out");
     AllTV = (Object[]) sagex.api.Database.FilterByBoolMethod(AllTV,"PloxeeTV_MetadataCalls_IsPlayonFile", false);}
     if(FilterMethod.equals("GettingFiles")){
     return AllTV;}
     }
     else{

      LOG.debug("Building a custom group filter that method");
   AllTV=(Object[]) sagex.api.Database.FilterByMethod(AllTV,FilterMethod,PassValue,true);}

      HashMap<String,Object> presorted = new HashMap<String,Object>();
     if(AllTV.length>0){
     Dividers.SageClass=AllTV[0].getClass();
     AllTV = (Object[]) sagex.api.Database.SortLexical(AllTV,false,SortMethods.GetMainSortMethod());
     HashMap<String,Vector> tester = (HashMap<String,Vector>) sagex.api.Database.GroupByArrayMethod(AllTV, "PloxeeTV_MetadataCalls_GetMediaTitle");
   
     Iterator keys = tester.keySet().iterator();
     String EpisodeSortMethod =SortMethods.GetEpisodeSortMethod();
     LOG.info("Episode Sort Method="+EpisodeSortMethod);
     Boolean ShowDividers =Boolean.parseBoolean(sagex.api.Configuration.GetProperty(SortMethods.PropertyPrefix+"ShowDividers","true"));
     LOG.info("Dividers on ="+ShowDividers);
     Boolean ShowSeasons = Boolean.parseBoolean(sagex.api.Configuration.GetProperty(SortMethods.PropertyPrefix+"ShowSeasons","true"));
     LOG.info("Show Sesaons="+ShowSeasons);
     int i=00;

     while(keys.hasNext()){
     String currentkey= keys.next().toString();
     Vector current =tester.get(currentkey);
     LOG.trace("CurrentKey="+currentkey);
     Object[] Seasonsorted = (Object[]) sagex.api.Database.Sort(current, false,EpisodeSortMethod);
     LOG.trace("Done sorting Episodes for="+currentkey);
     if(EpisodeSortMethod.equals("PloxeeTV_MetadataCalls_GetSeasonNumber")&&ShowSeasons){
     SeasonHandler.BuildSeasonHashMap(Seasonsorted,String.format("%02d",i)+"&&"+currentkey);
    presorted.put(String.format("%02d",i)+"&&"+currentkey,sagex.api.Database.GroupByMethod(Seasonsorted,"PloxeeTV_MetadataCalls_GetEpisodeTitle"));

     }


     else if(ShowDividers){
     LOG.trace("Show Dividers on go ahead and get them");
      presorted.put(String.format("%02d",i)+"&&"+currentkey,sagex.api.Database.GroupByMethod(Dividers.AddDividers(Seasonsorted,EpisodeSortMethod),"PloxeeTV_MetadataCalls_GetEpisodeTitle"));}
     else{
     LOG.trace("Show Dividers off just add sorted list without dividers");
     presorted.put(String.format("%02d",i)+"&&"+currentkey,sagex.api.Database.GroupByMethod(Seasonsorted,"PloxeeTV_MetadataCalls_GetEpisodeTitle"));}

      i++;
      LOG.info("CurrentKeyAdded="+currentkey);
     }}
     
      Long Time2 = System.currentTimeMillis()-Time;
      LOG.debug("Done getting datafiles. Grouping/Sorting/Filter done in "+Time2+"ms");
     return  sagex.api.Database.Sort(presorted, false,"PloxeeTV_SortMethods_GetFinalSortMethod");


    }
  public static List<String> GetAllSeasons(List<Object> currshows){
   List<String> Seasons= new ArrayList<String>();

   for(Object curr:currshows){
   if(!Dividers.IsDivider(curr)){
   String currseason = MetadataCalls.GetSeasonNumberPad(curr);
   if(!Seasons.contains(currseason)){
   Seasons.add(currseason);}}
   }
   return Seasons;
    }


   public static List<Object> GetAllSeasons(Object[] currshows){
   List<Object> Seasons= new ArrayList<Object>();

   for(Object curr:currshows){
     if(!Dividers.IsDivider(curr)){
   String currseason = MetadataCalls.GetSeasonNumberPad(curr);
   if(!Seasons.contains(currseason)){
   Seasons.add(currseason);}}
   }
   return Seasons;
    }

   public static Object GetSeasonLocationForFocus(List<Object> shows,Integer season){
   int Location= sagex.api.Utility.FindElementIndex(shows, season)+1;
   return shows.get(Location);

   }

   public static String GetFileTypes(){
   String Types = "L";
      if(Boolean.parseBoolean(sagex.api.Configuration.GetProperty(SortMethods.PropertyPrefix+"ImportedTV","true"))||Boolean.parseBoolean(sagex.api.Configuration.GetProperty(SortMethods.PropertyPrefix+"Movies","true"))){
         Types = "VDB"+Types;}
   if(Boolean.parseBoolean(sagex.api.Configuration.GetProperty(SortMethods.PropertyPrefix+"RecordedTV","true"))){
   Types = "T"+Types;}
   LOG.info("Type of videos = "+Types);
   return Types;
   }

   public static boolean IsIncludingPlayon(){
   return Boolean.parseBoolean(sagex.api.Configuration.GetProperty(SortMethods.PropertyPrefix+"OnlineTV","false"));}


  public static HashMap<String,Vector> GetPlayonFiles(){
  Object[] AllPlayonTV=sagex.api.MediaFileAPI.GetMediaFiles("TVDBL");
  AllPlayonTV=(Object[]) sagex.api.Database.FilterByBoolMethod(AllPlayonTV,"PloxeeTV_MetadataCalls_IsMediaTypeTV", true);
  AllPlayonTV = (Object[]) sagex.api.Database.FilterByBoolMethod(AllPlayonTV,"PloxeeTV_MetadataCalls_IsPlayonFile", true);
  AllPlayonTV = (Object[]) sagex.api.Database.SortLexical(AllPlayonTV,false,SortMethods.GetMainSortMethod());
  HashMap<String,Vector> tester = (HashMap<String,Vector>) sagex.api.Database.GroupByArrayMethod(AllPlayonTV, "PloxeeTV_MetadataCalls_GetMediaTitle");
  return tester;
  }

  public static HashMap<String,Vector> FilterInPlayonFiles(HashMap<String,Vector> AllTV){
  HashMap<String,Vector> PlayonTV = GetPlayonFiles();
  Iterator Playon = PlayonTV.keySet().iterator();
  while(Playon.hasNext()){

  String currkey = (String) Playon.next();
  if(!AllTV.containsKey(currkey)){
  AllTV.put(currkey, PlayonTV.get(currkey));}
  else{
  Vector currplay=PlayonTV.get(currkey);
  Vector currtv=AllTV.get(currkey);
  for(Object curr:currplay){
  String Episode=MetadataCalls.GetEpisodeTitle(curr);
  int NumMatchFound = 0;
     for(Object cuurtv:currtv){
     String TVEpisode = MetadataCalls.GetEpisodeTitle(cuurtv);
     if(TVEpisode.equals(Episode)){
     NumMatchFound++;}}
  if(NumMatchFound>1){
  LOG.debug("More than one show found with episode Title go ahead and remove="+Episode+";"+curr.toString());
  currtv.remove(curr);}
  }
  AllTV.put(currkey, currtv);

  }

  }
  return AllTV;
  }


}
