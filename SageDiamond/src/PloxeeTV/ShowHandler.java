/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PloxeeTV;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author SBANTA
 */
public class ShowHandler extends GroupHandler {
    public static String DefaultImageFolder = "plugins\\PloxeeTV\\Images\\";
    public static String DefaultFanart = "";


    // Below are calls to handle the main series/or show calls


    public static Object GetFirstEpisode(HashMap<String,Vector> CurrShows){
    Iterator CurrEpisodes = CurrShows.keySet().iterator();
        Vector Episode =CurrShows.get((String)CurrEpisodes.next());
   Object curr= Episode.firstElement();
   if(Dividers.IsDivider(curr)){
  Episode =CurrShows.get((String)CurrEpisodes.next());
   curr= Episode.firstElement();
  }

   return curr;
    }


    public static Boolean HasShowPoster(HashMap<String,Vector> CurrShows){
    Object Episode=GetFirstEpisode(CurrShows);
    return phoenix.api.HasFanartPoster(Episode)||sagex.api.MediaFileAPI.HasAnyThumbnail(Episode);}

    public static Object GetShowPoster(HashMap<String,Vector> CurrShows,boolean ShowsFocused){
    Object Episode = GetFirstEpisode(CurrShows);
    String ArtifactType = "tv";
    if(!MetadataCalls.IsMediaTypeTV(Episode)){
    ArtifactType = "movie";}
    if(phoenix.api.HasFanartPoster(Episode)){
    if(ShowsFocused){
    return phoenix.fanart.GetFanartArtifact(Episode,ArtifactType,null,"poster",null,new HashMap<String,String>());
    }
    else{
    return phoenix.api.GetFanartPoster(Episode);
    }}
    else if(!MetadataCalls.IsMediaTypeTV(Episode)&&sagex.api.MediaFileAPI.HasAnyThumbnail(Episode)){

    return   sagex.api.MediaFileAPI.GetThumbnail(Episode);}

    else{
     return Theme.GetNoPoster();}
    }




    public static Boolean HasShowBanner(HashMap<String,Vector> CurrShows){
    Object Episode=GetFirstEpisode(CurrShows);
    return phoenix.api.HasFanartBanner(Episode);}

    public static String GetShowDescription(HashMap<String,Vector> CurrShows){
      Object Episode=GetFirstEpisode(CurrShows);
    return sagex.api.ShowAPI.GetShowDescription(Episode);
    }



    public static String GetShowBanner(HashMap<String,Vector> CurrShows,boolean ShowsFocused){
    Object Episode = GetFirstEpisode(CurrShows);
    if(phoenix.api.HasFanartBanner(Episode)){
    if(ShowsFocused){
    return phoenix.fanart.GetFanartArtifact(Episode,"tv",null,"banner",null,new HashMap<String,String>());
    }
    else{
    return phoenix.fanart.GetFanartBanner(Episode);

    }}
    else{
    return Theme.GetNoPoster().toString();}}

    public static String GetShowFanart(HashMap<String,Vector> CurrShows){
    Object Episode = GetFirstEpisode(CurrShows);
    String ArtifactType = "tv";
      if(!MetadataCalls.IsMediaTypeTV(Episode)){
    ArtifactType = "movie";}
    if(phoenix.api.HasFanartBackground(Episode)){
    return phoenix.fanart.GetFanartArtifact(Episode,ArtifactType,null,"background",null,new HashMap<String,String>());
    }
    else{
    return DefaultFanart;}}

    public static int GetShowEpisodeCount(HashMap<String,Vector> CurrShows){
    int ShowCount = 0;
    Vector Episodes = GetAllShowEpisodes(CurrShows);
    for(Object Episode:Episodes){
    if(!Dividers.IsDivider(Episode)){
    ShowCount++;}}
    return ShowCount;
    }

    public static Vector GetAllShowEpisodes(HashMap<String,Vector> CurrShows){
    Iterator EpisodeNames = CurrShows.values().iterator();
    Vector Episodes = new Vector();
    while(EpisodeNames.hasNext()){
    Episodes.addAll((Vector)EpisodeNames.next());}
    return Episodes;}

    public static Vector GetWatchedEpisodes (HashMap<String,Vector> CurrShows){
    HashMap<String,Vector> CopyShows =(HashMap<String, Vector>) CurrShows.clone();
    Vector Episodes = GetAllShowEpisodes(CopyShows);
    return  (Vector) sagex.api.Database.FilterByBoolMethod(Episodes,"IsWatched", true);}

    public static int GetShowWatchedCount(HashMap<String,Vector> CurrShows){
    return GetWatchedEpisodes(CurrShows).size();}

    public static String GetLastWatched(HashMap<String,Vector> CurrShows){
    Vector Episodes = GetAllShowEpisodes(CurrShows);
    Object LastWatched =airing.GetLastWatched(Episodes);
    return airing.GetShowEpisode(LastWatched);}

    public static String GetNewestEpisode(HashMap<String,Vector> CurrShows){
    Vector Episodes = GetAllShowEpisodes(CurrShows);
    Object LastWatched =airing.GetNewestEpisode(Episodes);
    return airing.GetShowEpisode(LastWatched);}

    public static String GetShowCategory(HashMap<String,Vector> CurrShows){
    Object Episodes = GetFirstEpisode(CurrShows);
    return sagex.api.ShowAPI.GetShowCategory(Episodes);
    }

   public static String GetShowRated(HashMap<String,Vector> CurrShows){
   Object Episode = GetFirstEpisode(CurrShows);
   return sagex.api.ShowAPI.GetShowRated(Episode);
   }
   
   public static String GetShowRating(HashMap<String,Vector> CurrShows){
   Object Episode = GetFirstEpisode(CurrShows);
   return "";}

   public static boolean IsMovie(HashMap<String,Vector> CurrShows){
   Object Episode = GetFirstEpisode(CurrShows);
   return !MetadataCalls.IsMediaTypeTV(Episode);}

   public static int GetShowRuntime(HashMap<String,Vector> CurrShows){
   Object Episode = GetFirstEpisode(CurrShows);
   return MetadataCalls.GetShowDuration(Episode);
   }

   public static boolean IsSingleFile(HashMap<String,Vector> CurrShows){
   int count = GetShowEpisodeCount(CurrShows);
   return count==1;
   }

     public static boolean IsGroup(HashMap<String,Vector> CurrShows){
     return false;}


//     public static Object GetFirstEpisode(Vector CurrShows){
//      return CurrShows.firstElement();
//    }
//
//    public static Boolean HasShowPoster(Vector CurrShows){
//    Object Episode=GetFirstEpisode(CurrShows);
//    return phoenix.api.HasFanartPoster(Episode);}
//
//    public static String GetShowPoster(Vector CurrShows,boolean ShowsFocused){
//    Object Episode = GetFirstEpisode(CurrShows);
//    if(phoenix.api.HasFanartPoster(Episode)){
//    if(ShowsFocused){
//    return phoenix.fanart.GetFanartArtifact(Episode,"tv",null,"poster",null,new HashMap<String,String>());
//    }
//    else{
//    return phoenix.api.GetFanartPoster(Episode);
//    }}
//    else{
//    return DefaultPoster;}}
//
//
//    public static Boolean HasShowBanner(Vector CurrShows){
//    Object Episode=GetFirstEpisode(CurrShows);
//    return phoenix.api.HasFanartBanner(Episode);}
//
//
//
//    public static String GetShowBanner(Vector CurrShows,boolean ShowsFocused){
//    Object Episode = GetFirstEpisode(CurrShows);
//    if(phoenix.api.HasFanartBanner(Episode)){
//    if(ShowsFocused){
//    return phoenix.fanart.GetFanartArtifact(Episode,"tv",null,"banner",null,new HashMap<String,String>());
//    }
//    else{
//    return phoenix.fanart.GetFanartBanner(Episode);
//
//    }}
//    else{
//    return DefaultPoster;}}
//
//    public static String GetShowFanart(Vector CurrShows){
//    Object Episode = GetFirstEpisode(CurrShows);
//    if(phoenix.api.HasFanartBackground(Episode)){
//    return phoenix.fanart.GetFanartArtifact(Episode,"tv",null,"background",null,new HashMap<String,String>());
//    }
//    else{
//    return DefaultFanart;}}
//
//    public static int GetShowEpisodeCount(Vector CurrShows){
//    int ShowCount = 0;
//    Vector Episodes = GetAllShowEpisodes(CurrShows);
//    for(Object Episode:Episodes){
//    if(!Dividers.IsDivider(Episode)){
//    ShowCount++;}}
//    return ShowCount;
//    }
//
//    public static Vector GetAllShowEpisodes(Vector CurrShows){
//    return CurrShows;}
//
//    public static Vector GetWatchedEpisodes (Vector CurrShows){
//    HashMap<String,Vector> CopyShows =(HashMap<String, Vector>) CurrShows.clone();
//    Vector Episodes = GetAllShowEpisodes(CopyShows);
//    return  (Vector) sagex.api.Database.FilterByBoolMethod(Episodes,"IsWatched", true);}
//
//    public static int GetShowWatchedCount(Vector CurrShows){
//    return GetWatchedEpisodes(CurrShows).size();}
//
//    public static String GetLastWatched(Vector CurrShows){
//    Vector Episodes = GetAllShowEpisodes(CurrShows);
//    Object LastWatched =airing.GetLastWatched(Episodes);
//    return airing.GetShowEpisode(LastWatched);}
//
//    public static String GetNewestEpisode(Vector CurrShows){
//    Vector Episodes = GetAllShowEpisodes(CurrShows);
//    Object LastWatched =airing.GetNewestEpisode(Episodes);
//    return airing.GetShowEpisode(LastWatched);}
}
