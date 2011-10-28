package Diamond;

import java.util.Arrays;
import java.util.Random;
        
import sagex.api.Database;
import sagex.api.MediaFileAPI;
import sagex.api.AiringAPI;
import sagex.api.ShowAPI;
import sagex.api.PlaylistAPI;
import sagex.api.Utility;


public class airing
{
	public static String GetAiringTitle(Object MediaObject)
	//Returns the airing title for the MediaObject.  If the MediaObject is a TV file it returns GetAiringTitle().
	//If it is not a TV file it returns the "MediaTitle" from the custom metadata fields. Returns "Unknown"
	//if the title is not known.
	{
		String MediaTitle = new String();	
		if (MediaFileAPI.IsTVFile(MediaObject)) {
			MediaTitle = AiringAPI.GetAiringTitle(MediaObject);		
		}
		else {
			MediaTitle = MetadataCalls.GetMediaTitle(MediaObject);
		}	
		if (MediaTitle.length() == 0) {
			return "Unknown";
		}
		else {
			return MediaTitle;
		}
	}		
	
//	public static String GetShowEpisode(Object MediaObject)
//	{
//		if (MediaFileAPI.IsTVFile(MediaObject)) {
//			return ShowAPI.GetShowEpisode(MediaObject);
//		}
//		else {
//			String EpisodeTitle = MetadataCalls.GetEpisodeTitle(MediaObject);
//
//			if (EpisodeTitle.length() == 0) {
//				return MediaFileAPI.GetMediaTitle(MediaObject);
//			}
//			return EpisodeTitle;
//		}
//	}
	
	public static long GetOriginalAiringDate(Object MediaObject) 
	{
		if (MediaFileAPI.IsTVFile(MediaObject)){
			return ShowAPI.GetOriginalAiringDate(MediaObject);
		} else{
			return MetadataCalls.GetOriginalAirDate(MediaObject);
		}
	}
	
	public static String GetAiringTitlePostpend(Object MediaObject)
	{
		String s1 = airing.GetAiringTitle(MediaObject);
		String s2 = s1.toLowerCase();
		
		if (s2.startsWith("the ") || s2.startsWith("the.") || s2.startsWith("the_"))
		{
			s1 = s1.substring(4) + ", The";
		}
		if (s2.startsWith("a ") || s2.startsWith("a.") || s2.startsWith("a_"))
		{
			s1 = s1.substring(2) + ", A";
		}
		if (s2.startsWith("an ") || s2.startsWith("an.") || s2.startsWith("an_"))
		{
			s1 = s1.substring(3) + ", An";
		}
		return s1;
	}
	
	public static Object MakePlaylist(Object MediaObjects, String NewPlaylistName)
	/* 
	 * returns a Sage Playlist object containing the airings in MediaObjects
	 * If NewPlaylistName already exists, it will be removed without prompt and recreated.
	 * 
	 * @param MediaObjects, a sage MediaFile, Airing, or Show Object in an Array, list, or vector
	 * @param NewPlaylistName, string for the title of the new playlist
	 */
	{
		System.out.println("flux.api.MakePlaylist START");
		
		Object[] AllPlaylists = PlaylistAPI.GetPlaylists();
		
		Object[] MediaObjectsArray = FanartCaching.toArray(MediaObjects);
						
		for (Object TempPlaylist : AllPlaylists)
		{
			String TempPlaylistName = PlaylistAPI.GetName(TempPlaylist);
			//System.out.println("TempPlaylist: '" + TempPlaylistName + "'");
			
			if (TempPlaylistName.equals(NewPlaylistName)){
				//System.out.println("Removing TempPlaylist: '" + TempPlaylistName + "'");
				PlaylistAPI.RemovePlaylist(TempPlaylist);
			}
		}
		
		//System.out.println("Adding Playlist: '" + NewPlaylistName +"'");
		Object NewPlaylist = PlaylistAPI.AddPlaylist(NewPlaylistName);
		
		for ( Object MediaObject : MediaObjectsArray){
			System.out.println("Adding To Playlist: '" + MediaFileAPI.GetMediaTitle(MediaObject)+"::"+ MediaFileAPI.GetMediaFileID(MediaObject)+"'");
			PlaylistAPI.AddToPlaylist(NewPlaylist, AiringAPI.GetMediaFileForAiring(MediaObject));
		}
		System.out.println("flux.api.MakePlaylist END");		
		return NewPlaylist;
	}
	
	public static boolean IsWatchedPartial(Object MediaObject)
	/* 
	 * returns true if the MediaObjects  the IsWatched() flag is false and it has been partially watched.
	 * 
	 * @param MediaObject, a sage MediaFile, Airing, or Show object
	 */
	{
		if (!AiringAPI.IsWatched(MediaObject) && ( AiringAPI.GetWatchedDuration(MediaObject) != 0 ) ){
			return true;
		} else {
			return false;
		}
	}
	
	public static Object GetLastWatched(Object MediaObjects)
	/*
	 * Given an array of Airings will return the last watched (in real time) object.
	 * 
	 * @param MediaObjects - sage MediaFiles, Airings, or Shows Objects in an Array, list, or vector.
	 */
	{
		MediaObjects = Database.Sort(MediaObjects, true, "GetRealWatchedStartTime");
		return Utility.GetElement(MediaObjects, 0);
	}

	
	public static Object GetNextShow(Object MediaObjects)
	/*
	 * Given an array of Airings will return the next episode to watch by AiringDate (in real time).
	 * Returns Null if Airings not found after last watched. (end of series)
	 * 
	 * @param MediaObjects - sage MediaFiles, Airings, or Shows Objects in an Array, list, or vector.
	 */
	{
		Object LastWatched = airing.GetLastWatched(MediaObjects);
		if(AiringAPI.IsWatched(LastWatched)){
			MediaObjects = Database.Sort(MediaObjects, false, "sagediamond_MetadataCalls_GetOriginalAirDate");
			int index = Utility.FindElementIndex(MediaObjects, LastWatched);
			
			if(index >= Utility.Size(MediaObjects)){	
				return null;
			}else{
				return Utility.GetElement(MediaObjects, index+1);}
		}else{
			return LastWatched;	
		}
	}

	public static Object GetShowNextPrev(Object MediaObjects, Object CurrentShow, Boolean DirectionNext)
	/*
	 * Given an array of Airings will return the Next or Previous episode by AiringDate (in real time).
	 * Returns Null if Airings not found after or before Current Show. (end or begin of series hit)
	 * 
	 * @param MediaObjects - sage MediaFiles, Airings, or Shows Objects in an Array, list, or vector.
	 * @param CurrentShow - sage MediaFile, Airing, or Show Object.
	 * @param DirectionNext - true gets Next, false gets Previous.
	 */
	{
                    MediaObjects = Database.Sort(MediaObjects, false, "sagediamond_MetadataCalls_GetOriginalAirDate");
                    int index = Utility.FindElementIndex(MediaObjects, CurrentShow);
                    
                    if(DirectionNext){
                        if(index >= Utility.Size(MediaObjects)){	
                                System.out.println("JUSJOKEN: GetShowNextPrev - NEXT returning null");
                                return null;
                        }else{
                                System.out.println("JUSJOKEN: GetShowNextPrev - NEXT returning item (" + (index+1) + ")" );
                                return Utility.GetElement(MediaObjects, index+1);}
                    }else{
                        if(index == 0){	
                                System.out.println("JUSJOKEN: GetShowNextPrev - PREV returning null");
                                return null;
                        }else{
                                System.out.println("JUSJOKEN: GetShowNextPrev - PREV returning item (" + (index-1) + ")" );
                                return Utility.GetElement(MediaObjects, index-1);}
                    }
	}
        
	public static Object GetNextShowRandom(Object MediaObjects)
	/*
	 * Given an array of Airings will return a random selection to watch by AiringDate (in real time).
	 * Returns Null if Airings not found after last watched. (end of series)
	 * 
	 * @param MediaObjects - sage MediaFiles, Airings, or Shows Objects in an Array, list, or vector.
	 */
	{
                Object Unwatched = airing.GetShowsFromLastWatched(MediaObjects);
                if(Utility.Size(Unwatched)== 0){
                    System.out.println("JUSJOKEN: GetNextShowRandom - No unwatched shows returned");
                    return null;
                }else{
                    //sort the unwatched shows
                    Random generator = new Random();
                    int randomIndex = generator.nextInt( Utility.Size(Unwatched) );
                    System.out.println("JUSJOKEN: GetNextShowRandom - returning item (" + randomIndex + ") of (" + Utility.Size(Unwatched) + ") unwatched items");
                    return Utility.GetElement(Unwatched, randomIndex);
                }
	}
        
	public static Object[] GetShowsFromLastWatched(Object Arr)
	/* 
	 * given an array of Airings will return a subarray sorted by original airing date 
	 * where the first element of the array is the last watched episode 
	 * (or the next episode if IsWatched() = true).  
	 * Subsequent elements are all "later" episodes (as defined by flux_api_GetOriginalAiringDate).
	 * Episodes "before" the last watched are truncated (again as defined by flux_api_GetOriginalAiringDate).
	 * 
	 * @param Arr - sage MediaFiles, Airings, or Shows Objects in an Array, list, or vector.
	 * Presumably grouped into AiringTitle and/or a filtered by a specific season.
	 */
	{
		Object NextWatch = airing.GetNextShow(Arr);
		if (NextWatch == null){
			return  null;
		}else{
			Arr = Database.Sort(Arr, false, "sagediamond_MetadataCalls_GetOriginalAirDate");
			Object[] Arr0 =FanartCaching.toArray(Arr);
			int	elementlocation = Utility.FindElementIndex(Arr0, NextWatch);
			
			return Arrays.copyOfRange(Arr0, elementlocation, Arr0.length);
		}
	}
		
	public static Object[] GetShowsFromShow(Object MediaObjects,Object MediaObject)
	/*
	 * Given an array of Airings and a member of Airing will return the remaining shows
	 * (1st element = the passed Airing)
	 * with  in array after current show.
	 * 
	 * Returns Null if Airings not found after current show. (end of series)
	 * @param MediaObjects - sage MediaFiles, Airings, or Shows Objects in an Array, list, or vector.
	 * @param MediaObject=sage object of current show to start from
	 */
	{
		MediaObjects = Database.Sort(MediaObjects, false, "sagediamond_MetadataCalls_GetOriginalAirDate");
		int elementlocation = Utility.FindElementIndex(MediaObjects, MediaObject);
		Object[] Arr0 = FanartCaching.toArray(MediaObjects);
		
		if(elementlocation > Arr0.length){
			return null;
		}else{
			return Arrays.copyOfRange(Arr0, elementlocation, Arr0.length);
		}
	}
}