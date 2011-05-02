/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PloxeeTV;

import PloxeeTV.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import org.apache.log4j.Logger;
import sagex.UIContext;

/**
 *
 * @author SBANTA
 */
public class SeasonHandler {
    
    static private final Logger LOG = Logger.getLogger(Groups.class);

     public static HashMap<String,HashMap<String,HashMap<String,HashMap<String,Vector>>>> SeasonsMap= new HashMap<String,HashMap<String,HashMap<String,HashMap<String,Vector>>>>();


     public static void BuildSeasonHashMap(Object[] shows,Object currentkey){
     LOG.info("Building Season HashMap for this client");
     String ClientName=sagex.api.Global.GetUIContextName();
     Object [] seasonshows = shows.clone();
      LOG.info("Building Season HashMap for this client Grouping by seasons");
     HashMap<Integer,Vector> GroupedEpisodes = (HashMap<Integer, Vector>) sagex.api.Database.GroupByMethod(seasonshows,SortMethods.Seasons);
      LOG.info("Building Season HashMap for this client Grouping by Episode within season");
     HashMap<String,HashMap<String,Vector>> Current = new HashMap<String,HashMap<String,Vector>>();
     Iterator Seasonkeys = GroupedEpisodes.keySet().iterator();
     while(Seasonkeys.hasNext()){
     LOG.debug("Working on current Season to group episodes inside season");
     int Season=(Integer)Seasonkeys.next();
      LOG.debug("Current Seasons ="+ Season);
     Vector CurrSeason=GroupedEpisodes.get(Season);
       LOG.debug("BuldingHash");
     Current.put(String.format("%02d",Season),(HashMap<String,Vector>)sagex.api.Database.GroupByMethod(CurrSeason,"PloxeeTV_MetadataCalls_GetEpisodeTitle"));}




     LOG.trace("Putting current key into hash sorted by season. Key="+currentkey.toString());
     if(SeasonsMap.containsKey(ClientName)){
     HashMap<String,HashMap<String,HashMap<String,Vector>>>  test=SeasonsMap.get(ClientName);
     test.put(currentkey.toString(),(HashMap<String, HashMap<String, Vector>>) sagex.api.Database.Sort(Current,false,null));
     LOG.info("Done building seasons hashmap");
     SeasonsMap.put(ClientName, test);}
     else{
     HashMap<String,HashMap<String,HashMap<String,Vector>>>  test= new HashMap<String,HashMap<String,HashMap<String,Vector>>>();
     test.put(currentkey.toString(),(HashMap<String, HashMap<String, Vector>>) sagex.api.Database.Sort(Current,false,null));
     SeasonsMap.put(ClientName, test);}
     }

     public static HashMap<String,HashMap<String,Vector>> GetSeasons(String Key){
    String ClientName=sagex.api.Global.GetUIContextName();
    HashMap<String,HashMap<String,HashMap<String,Vector>>> CurrentValues= SeasonsMap.get(ClientName);
    LOG.trace("Getting Seasons from cache hashmap for key="+Key);
    LOG.trace("Current Key="+CurrentValues.get(Key).toString());
    return CurrentValues.get(Key);

     }

     public static boolean IsShowingSeasons(){
     return IsSortingBySeasons()&&Boolean.parseBoolean(sagex.api.Configuration.GetProperty(SortMethods.PropertyPrefix+"ShowSeasons","true"));
     }

     public static boolean IsSortingBySeasons(){
     return SortMethods.GetEpisodeSortMethod().equals("PloxeeTV_MetadataCalls_GetSeasonNumber");
     }











}
