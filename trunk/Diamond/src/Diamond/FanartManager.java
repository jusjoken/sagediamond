/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Diamond;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import sagex.UIContext;
import sagex.phoenix.metadata.MediaType;
import sagex.phoenix.vfs.IMediaResource;

/**
 *
 * @author jusjoken
 * Get a new Object of this class in the STV
 * The default will be poster for either TV Series or Movie dependent on the passed in MediaObject
 * From the STV call setFanartType to another type which will load up the available fanart
 * From the STV call setTVMode to Series or a specific Season number which will load up the available fanart
 *
 */


public class FanartManager {

    static private final Logger LOG = Logger.getLogger(FanartManager.class);
    private static final String ConstSeries = "Series";
    private IMediaResource MediaResource = null;
    private IMediaResource PrimaryMediaResource = null;
    public static enum FanartManagerTypes{TV,MOVIE,NONE};
    public static enum TVModes{SERIES,SEASON};
    private TVModes TVMode = TVModes.SERIES; 
    private FanartManagerTypes FanartManagerType = FanartManagerTypes.NONE;
    private String FanartType = ""; //set within Init to poster as a default
    private List FanartList = Collections.emptyList();
    private List TVModeList = Collections.emptyList();
    private String DefaultFanart = "";
    private String CurrentSeason = "-1";
    
    public FanartManager(IMediaResource MediaResource){
        this.MediaResource = MediaResource;
        Init();
    }
    public FanartManager(Object MediaResource){
        this.MediaResource = Source.ConvertToIMR(MediaResource);
        Init();
    }
    
    private void Init(){
        //based on the passed in MediaResource determine if a TV Series or Movie Fanart Object
        this.PrimaryMediaResource = this.MediaResource;
        LOG.debug("Init: PrimaryMediaResource '" + PrimaryMediaResource + "'");
        if (phoenix.media.IsMediaType( this.MediaResource , "FOLDER" )){
            //get the first child and use it to determine the Fanart Type
            PrimaryMediaResource = ImageCache.GetChild(this.MediaResource, Boolean.FALSE);
        }
        //Now determine the Primary Media Resource Type
        if (phoenix.media.IsMediaType( this.PrimaryMediaResource , "TV" )){
            this.FanartManagerType = FanartManagerTypes.TV;
            TVModeList = GetFanartSeasons();
        }else if (phoenix.media.IsMediaType( this.PrimaryMediaResource , "VIDEO" )){
            this.FanartManagerType = FanartManagerTypes.MOVIE;
        }else if (phoenix.media.IsMediaType( this.PrimaryMediaResource , "DVD" )){
            this.FanartManagerType = FanartManagerTypes.MOVIE;
        }else if (phoenix.media.IsMediaType( this.PrimaryMediaResource , "BLURAY" )){
            this.FanartManagerType = FanartManagerTypes.MOVIE;
        }else{
            this.FanartManagerType = FanartManagerTypes.NONE;
        }
        LOG.debug("Init: FanartManagerType '" + FanartManagerType + "' PrimaryMediaResource '" + PrimaryMediaResource + "'");
        //default to poster
        setFanartType("poster");
    }

    public String getTitle() {
        String Title = "No Fanart Found";
        if (this.FanartManagerType.equals(FanartManagerTypes.MOVIE)){
            Title = PrimaryMediaResource.getTitle();
        }else if (this.FanartManagerType.equals(FanartManagerTypes.TV)){
            Title = PrimaryMediaResource.getTitle();
        }
        return Title;
    }
    public String getTitleFull() {
        String Title = "No Fanart Found";
        if (this.FanartManagerType.equals(FanartManagerTypes.MOVIE)){
            Title = PrimaryMediaResource.getTitle();
        }else if (this.FanartManagerType.equals(FanartManagerTypes.TV)){
            Title = PrimaryMediaResource.getTitle();
            if (TVMode.equals(TVModes.SEASON)){
                Title = Title + " (Season " + CurrentSeason + ")";
            }
        }
        return Title;
    }

    public List getFanartList() {
        return FanartList;
    }

    public String getFanartType() {
        return FanartType;
    }

    public Boolean IsFanartTypePoster(){
        return this.FanartType.toLowerCase().equals("poster");
    }
    public Boolean IsFanartTypeBanner(){
        return this.FanartType.toLowerCase().equals("banner");
    }
    public Boolean IsFanartTypeBackground(){
        return this.FanartType.toLowerCase().equals("background");
    }

    public String getTVMode() {
        if (!IsTV()){
            return "";
        }
        if (TVMode.equals(TVModes.SERIES)){
            return "Series";
        }else{
            return CurrentSeason;
        }
    }
    public void setTVMode(String NewMode) {
        //get a string from the STV code to indicate Series or a specific Season
        Boolean UpdateSettings = Boolean.FALSE;
        if (NewMode.equals(ConstSeries)){
            //this is a Series Mode
            if (!this.TVMode.equals(TVModes.SERIES)){
                //change to Series Mode and reload settings
                this.TVMode = TVModes.SERIES;
                this.CurrentSeason = "-1";
                UpdateSettings = Boolean.TRUE;
            }
        }else{
            //must be a Season Number
            if (!this.TVMode.equals(TVModes.SEASON)){
                //change to Season Mode and reload settings
                this.TVMode = TVModes.SEASON;
                CurrentSeason = NewMode;
                UpdateSettings = Boolean.TRUE;
            }else{
                //was previously Season so see if this is a different Season
                if (!NewMode.equals(CurrentSeason)){
                    CurrentSeason = NewMode;
                    UpdateSettings = Boolean.TRUE;
                }
            }
        }
        if (UpdateSettings){
            LoadFanartList();
        }
    }

    public void setFanartType(String FanartType) {
        //change the fanart type and then set specific settings related to this change
        if (!this.FanartType.equals(FanartType)){
            this.FanartType = FanartType.toLowerCase();
            //load the list of this fanart type
            LoadFanartList();
        }
    }
    
    private void LoadFanartList(){
        FanartList = Collections.emptyList();
        Map<String,String> faMetadata = null;
        MediaType faMediaType = null;
        Object faMediaObject = null;
        String faMediaTitle = null;
       
        if (IsTV()){
            //LOG.debug("LoadFanartList: TV item found");
            if (TVMode.equals(TVModes.SERIES)){
                //LOG.debug("LoadFanartList: TV SERIES item found");
                faMediaObject = PrimaryMediaResource.getMediaObject();
                faMediaType = MediaType.TV;
                faMetadata = Collections.emptyMap();
            }else{ //must be SEASON
                //LOG.debug("LoadFanartList: TV SEASON item found");
                faMediaObject = PrimaryMediaResource.getMediaObject();
                //faMediaObject = null;
                faMediaType = MediaType.TV;
                faMediaTitle = PrimaryMediaResource.getTitle();
                faMetadata = new HashMap<String,String>();
                faMetadata.put("SeasonNumber",CurrentSeason);
                faMetadata.put("EpisodeNumber","1");
            }
        }else if (IsMovie()){
            //LOG.debug("LoadFanartList: MOVIE item found");
            faMediaObject = PrimaryMediaResource.getMediaObject();
            faMediaType = MediaType.MOVIE;
        }else{ //must be invalid
            LOG.debug("LoadFanartList: Invalid - not TV nor MOVIE");
            return;
        }
        //LOG.debug("LoadFanartList: calling GetFanartArtifacts with faMediaObject'" + faMediaObject + "' faMediaType '" + faMediaType.toString() + "' faMediaTitle '" + faMediaTitle + "' FanartType '" + FanartType + "' faMetadata '" + faMetadata + "'");
        FanartList = new ArrayList<String>(Arrays.asList(phoenix.fanart.GetFanartArtifacts(faMediaObject, faMediaType.toString(), faMediaTitle, FanartType, null, faMetadata)));
        LOG.debug("LoadFanartList: FanartList '" + FanartList + "' for '" + PrimaryMediaResource.getTitle() + "'");
        //Set the default fanart item if there are any fanart items
        if (!FanartList.isEmpty()){
            DefaultFanart = ImageCache.GetDefaultArtifact(PrimaryMediaResource, FanartType);
            LOG.debug("LoadFanartList: DegaultFanart '" + DefaultFanart + "' for '" + PrimaryMediaResource.getTitle() + "'");
            //Add the default item (if any) to the TOP of the list - make sure it is also removed from the list
            if (DefaultFanart!=null){
                if (FanartList.contains(DefaultFanart)){
                    FanartList.remove(DefaultFanart);
                    FanartList.add(0, DefaultFanart);
                }
            }
        }
    }

    public void DeleteFanartItem(String FanartItem){
        //remove from the file system
        File f1 = new File(FanartItem);
        f1.delete();
        RemoveFanartItem(FanartItem);
        ReloadFanartItem();
        //reload the fanart list
        LoadFanartList();
    }
    
    private void RemoveFanartItem(String FanartItem){
        //clear caches
        phoenix.fanart.ClearMemoryCaches();
        //remove the fanart item from the cache for this media item
        ImageCache.RemoveItemFromCache(ImageCacheKey.BuildKey(FanartItem, Boolean.FALSE));
        ImageCache.RemoveItemFromCache(ImageCacheKey.BuildKey(FanartItem, Boolean.TRUE));
        UIContext UIc = new UIContext(sagex.api.Global.GetUIContextName());
        sagex.api.Utility.UnloadImage(UIc, FanartItem);
    }

    private void ReloadFanartItem(){
        //reload the image
        ImageCacheKey tKey = ImageCache.GetImageKey(MediaResource, FanartType, Boolean.FALSE);
        ImageCache.CreateImage(tKey, Boolean.TRUE);
        if (IsFanartTypeBackground()){
            //reload any large background
            tKey = ImageCache.GetImageKey(MediaResource, FanartType, Boolean.TRUE);
            ImageCache.CreateImage(tKey, Boolean.TRUE);
        }
    }

    public void SetFanartAsDefault(String FanartItem){
        Map<String,String> faMetadata = null;
        MediaType faMediaType = null;
        Object faMediaObject = null;
        String faMediaTitle = null;
       
        if (IsTV()){
            //LOG.debug("SetFanartAsDefault: TV item found");
            if (TVMode.equals(TVModes.SERIES)){
                //LOG.debug("SetFanartAsDefault: TV SERIES item found");
                faMediaObject = PrimaryMediaResource.getMediaObject();
                faMediaType = MediaType.TV;
                faMetadata = Collections.emptyMap();
            }else{ //must be SEASON
                //LOG.debug("SetFanartAsDefault: TV SEASON item found");
                faMediaObject = PrimaryMediaResource.getMediaObject();
                faMediaType = MediaType.TV;
                faMediaTitle = PrimaryMediaResource.getTitle();
                faMetadata = new HashMap<String,String>();
                faMetadata.put("SeasonNumber",CurrentSeason);
                faMetadata.put("EpisodeNumber","1");
            }
        }else if (IsMovie()){
            //LOG.debug("SetFanartAsDefault: MOVIE item found");
            faMediaObject = PrimaryMediaResource.getMediaObject();
            faMediaType = MediaType.MOVIE;
        }else{ //must be invalid
            LOG.debug("LoadFanartList: Invalid - not TV nor MOVIE");
            return;
        }
        LOG.debug("SetFanartAsDefault: calling SetFanartArtifact with FanartItem '" + FanartItem + "' faMediaObject'" + faMediaObject + "' faMediaType '" + faMediaType.toString() + "' faMediaTitle '" + faMediaTitle + "' FanartType '" + FanartType + "' faMetadata '" + faMetadata + "'");
        phoenix.fanart.SetFanartArtifact(faMediaObject, new File(FanartItem), faMediaType.toString(), faMediaTitle, FanartType, null, faMetadata);
        //reload the fanart list
        if (DefaultFanart!=null){
            RemoveFanartItem(DefaultFanart);
        }
        ReloadFanartItem();
        //reload the fanart list
        LoadFanartList();
    }

    //this will be a list of modes such as Series,1,2,3 - number representing the Seasons
    public List getTVModeList() {
        return TVModeList;
    }

    private List GetFanartSeasons(){
        List tFanartSeasons = new ArrayList();
        if (!IsTV()){
            LOG.debug("GetFanartSeasons: not valid for non TV Media File '" + PrimaryMediaResource.getTitle() + "'");
            return Collections.emptyList();
        }
        String faMediaTitle = null;
        Object faMediaObject = PrimaryMediaResource.getMediaObject();
        MediaType faMediaType = MediaType.TV;
        Map<String,String> faMetadata = Collections.emptyMap();
        String FanartFolder = null;

        //Get a Series Folder and then get it's parent
        FanartFolder = phoenix.fanart.GetFanartArtifactDir(faMediaObject, faMediaType.toString(), null, "poster", null, faMetadata, Boolean.FALSE);
        if (FanartFolder==null){
            LOG.debug("GetFanartSeasons: no Fanart available for '" + PrimaryMediaResource.getTitle() + "'");
            return Collections.emptyList();
        }
        File FanartFile = new File(FanartFolder);
        if (!FanartFile.exists()){
            LOG.debug("GetFanartSeasons: Fanart Dir not found '" + FanartFolder + "'");
            return Collections.emptyList();
        }
        FanartFile = FanartFile.getParentFile();
        if (FanartFile==null){
            LOG.debug("GetFanartSeasons: Parent not found for '" + FanartFolder + "'");
            return Collections.emptyList();
        }
        //find all the Season Folders
        File[] listOfFiles = FanartFile.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
          if (listOfFiles[i].isDirectory()) {
            if (listOfFiles[i].getName().startsWith("Season")){
                String tSeason = listOfFiles[i].getName().substring(7);
                
                Integer tInteger = -1;
                try {
                    tInteger = Integer.valueOf(tSeason);
                } catch (NumberFormatException ex) {
                    //skip as it is still -1
                }
                if (tInteger.equals(-1)){
                    LOG.debug("GetFanartSeasons: Season '" + tSeason + "' could not be converted to an Integer for '" + PrimaryMediaResource.getTitle() + "'");
                    //don't add
                }else{
                    LOG.debug("GetFanartSeasons: Adding Season '" + tSeason + "' for '" + PrimaryMediaResource.getTitle() + "'");
                    tFanartSeasons.add(tInteger);
                }
            }
          }
        }
        if (!tFanartSeasons.isEmpty()){
            //Now sort the list
            Collections.sort(tFanartSeasons);
            //add Series to the front of the list of Seasons
            //tFanartSeasons.add(0, ConstSeries);
        }
        LOG.debug("GetFanartSeasons: Seasons found '" + tFanartSeasons + "' for '" + PrimaryMediaResource.getTitle() + "'");
        return tFanartSeasons;
    }
    
    public Integer getTableCols() {
        if (IsFanartTypePoster()){
            return 7;
        }else if (IsFanartTypeBackground()){
            return 3;
        }else if (IsFanartTypeBanner()){
            return 2;
        }else{
            return 7; //assume poster
        }
    }

    public Integer getTableRows() {
        if (IsFanartTypePoster()){
            return 2;
        }else if (IsFanartTypeBackground()){
            return 2;
        }else if (IsFanartTypeBanner()){
            return 3;
        }else{
            return 2; //assume poster
        }
    }

    public Boolean IsTV(){
        if (this.FanartManagerType.equals(FanartManagerTypes.TV)){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }
    public Boolean IsMovie(){
        if (this.FanartManagerType.equals(FanartManagerTypes.MOVIE)){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }
    public Boolean IsValid(){
        if (this.FanartManagerType.equals(FanartManagerTypes.NONE)){
            return Boolean.FALSE;
        }else{
            return Boolean.TRUE;
        }
    }
    
    public Boolean IsCurrentFanartType(String FanartType){
        if (FanartType.toLowerCase().equals(this.FanartType)){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }
    
    public Boolean IsDefault(String FanartItem){
        if (this.DefaultFanart==null){
            return Boolean.FALSE;
        }
        if (this.DefaultFanart.isEmpty()){
            return Boolean.FALSE;
        }
        if (this.DefaultFanart.equals(FanartItem)){
            return Boolean.TRUE;
        }
        //LOG.debug("IsDefault: no match found: CurrentItem '" + FanartItem + "' Default '" + this.DefaultFanart + "'");
        return Boolean.FALSE;
    }
    
    //return a CreateImage object for the specific Fanart Item
    public Object GetImage(String FanartItem){
        if (FanartItem==null){
            LOG.debug("GetImage: null FanartItem passed in - returning null");
            return null;
        }
        UIContext UIc = new UIContext(sagex.api.Global.GetUIContextName());
        //based on the ImageType determine the scalewidth to use
        Integer UIWidth = sagex.api.Global.GetFullUIWidth(UIc);
        Double scalewidth = 0.2;
        if (IsFanartTypePoster()){
            scalewidth = 0.2;
        }else if (IsFanartTypeBanner()){
            scalewidth = 0.6;
        }else if (IsFanartTypeBackground()){
            scalewidth = 0.4;
        }else{
            //use default
        }
        Double finalscalewidth = scalewidth * UIWidth;
        Object tImage = phoenix.image.CreateImage("GemstoneFanartManager", FanartItem, "{name: scale, width: " + finalscalewidth + ", height: -1}", false);
        if (tImage==null){
            LOG.debug("GetImage: CreateImage returned null for FanartItem '" + FanartItem + "'");
            return null;
        }
        return tImage;
    }
    
    public String[] GetFanartTypes(){
        if (IsMovie()){
            return new String[] {"Poster","Background"};
        }else if (IsTV()){
            return new String[] {"Poster","Banner","Background"};
        }else{
            return new String[0];
        }
    }
    
}
