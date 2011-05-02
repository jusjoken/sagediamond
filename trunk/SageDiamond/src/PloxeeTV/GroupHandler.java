/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PloxeeTV;


import PloxeeTV.*;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author SBANTA
 */
public class GroupHandler {


    public static Object GetGroupName(GroupObject Current){
    return Current.getName();}

    public static Object GetGroupShows(GroupObject Current){
    return Current.getShows();}

    public static boolean HasShowFanart(GroupObject Current){
    return Current.getFanart().exists();}

    public static File GetShowFanart(GroupObject Current){
    return Current.getFanart();}



    public static File GetShowPoster(GroupObject Current,boolean showsfocused){
    return Current.getPoster();}

    public static boolean HasShowPoster(GroupObject Current){
    return Current.getPoster().exists();}

    public static boolean IsMovie(GroupObject Current){
    return false;}

    public static int GetShowEpisodeCount(GroupObject Current){
    return ShowHandler.GetShowEpisodeCount(Current.getHashShows());}

    public static String GetNewestEpisode(GroupObject Current){
    return ShowHandler.GetNewestEpisode(Current.getHashShows());}

    public static int GetShowWatchedCount(GroupObject Current){
    return ShowHandler.GetShowWatchedCount(Current.getHashShows());}

    public static String GetLastWatched(GroupObject Current){
    return ShowHandler.GetLastWatched(Current.getHashShows());}

    public static String GetShowDescription(GroupObject Current){
    return "";}

    public static String GetShowCategory(GroupObject Current){
    return "";}

    public static boolean IsGroup(GroupObject Current){
    return true;}

    public static String GetShowRated(GroupObject Current){
    return "";}

    public static int GetShowRuntime(GroupObject Current){
    return 0;}


    public static HashMap<String,Vector> GetAllShows(GroupObject Current){
    HashMap<String,HashMap<String,Vector>> Org =  Current.getShows();
    HashMap<String,Vector> Return = new HashMap<String,Vector>();
    Iterator keys = Org.keySet().iterator();
    while(keys.hasNext()){
    Return.putAll(Org.get((String)keys.next()));
    }
    return Return;

    }
}
