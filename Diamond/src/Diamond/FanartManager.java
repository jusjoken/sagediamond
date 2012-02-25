/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Diamond;

import java.io.File;
import java.util.ArrayList;
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
    private String[] FanartList = new String[0];
    private String[] TVModeList = new String[0];
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
        //default to poster
        setFanartType("poster");
    }

    public String getTitle() {
        String Title = "No Fanart Found";
        if (this.FanartManagerType.equals(FanartManagerTypes.MOVIE)){
            Title = PrimaryMediaResource.getTitle();
        }else if (this.FanartManagerType.equals(FanartManagerTypes.TV)){
            Title = PrimaryMediaResource.getTitle();
            //append the Current Season if selected
        }
        return Title;
    }

    public String[] getFanartList() {
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

    public void setTVMode(String NewMode) {
        //get a string from the STV code to indicate Series or a specific Season
        Boolean UpdateSettings = Boolean.FALSE;
        if (NewMode.equals(ConstSeries)){
            //this is a Series Mode
            if (!this.TVMode.equals(TVModes.SERIES)){
                //change to Series Mode and reload settings
                this.TVMode = TVModes.SERIES;
                UpdateSettings = Boolean.TRUE;
            }
        }else{
            //must be a Season Number
            if (!this.TVMode.equals(TVModes.SEASON)){
                //change to Season Mode and reload settings
                this.TVMode = TVModes.SEASON;
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
        FanartList = new String[0];
        Map<String,String> faMetadata = null;
        MediaType faMediaType = null;
        Object faMediaObject = null;
        String faMediaTitle = null;
       
        if (IsTV()){
            if (TVMode.equals(TVModes.SERIES)){
                faMediaObject = PrimaryMediaResource.getMediaObject();
                faMediaType = MediaType.TV;
                faMetadata = Collections.emptyMap();
            }else{ //must be SEASON
                faMediaObject = null;
                faMediaType = MediaType.TV;
                faMediaTitle = PrimaryMediaResource.getTitle();
                faMetadata = new HashMap<String,String>();
                faMetadata.put("SeasonNumber",CurrentSeason);
            }
        }else if (IsMovie()){
            faMediaObject = PrimaryMediaResource.getMediaObject();
        }else{ //must be invalid
            return;
        }
        FanartList = phoenix.fanart.GetFanartArtifacts(faMediaObject, faMediaType.toString(), faMediaTitle, FanartType, null, faMetadata);
        //Set the default fanart item if there are any fanart items
        if (FanartList.length>0){
            DefaultFanart = ImageCache.GetDefaultArtifact(PrimaryMediaResource, FanartType);
        }
    }

    //this will be a list of modes such as Series,1,2,3 - number representing the Seasons
    public String[] getTVModeList() {
        return TVModeList;
    }

    private String[] GetFanartSeasons(){
        if (!IsTV()){
            LOG.debug("GetFanartSeasons: not valid for non TV Media File '" + PrimaryMediaResource.getTitle() + "'");
            return new String[0];
        }
        List tFanartSeasons = new ArrayList();
        String faMediaTitle = null;
        Object faMediaObject = PrimaryMediaResource.getMediaObject();
        MediaType faMediaType = MediaType.TV;
        Map<String,String> faMetadata = Collections.emptyMap();
        String FanartFolder = null;

        //Get a Series Folder and then get it's parent
        FanartFolder = phoenix.fanart.GetFanartArtifactDir(faMediaObject, faMediaType.toString(), null, "poster", null, faMetadata, Boolean.FALSE);
        if (FanartFolder==null){
            LOG.debug("GetFanartSeasons: no Fanart available for '" + PrimaryMediaResource.getTitle() + "'");
            return new String[0];
        }
        File FanartFile = new File(FanartFolder);
        if (!FanartFile.exists()){
            LOG.debug("GetFanartSeasons: Fanart Dir not found '" + FanartFolder + "'");
            return new String[0];
        }
        FanartFile = FanartFile.getParentFile();
        if (FanartFile==null){
            LOG.debug("GetFanartSeasons: Parent not found for '" + FanartFolder + "'");
            return new String[0];
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
            tFanartSeasons.add(0, ConstSeries);
        }
        LOG.debug("GetFanartSeasons: Seasons found '" + tFanartSeasons + "' for '" + PrimaryMediaResource.getTitle() + "'");
        return (String[]) tFanartSeasons.toArray();
    }
    
    public Integer getTableCols() {
        if (IsFanartTypePoster()){
            return 4;
        }else if (IsFanartTypeBackground()){
            return 3;
        }else if (IsFanartTypeBanner()){
            return 2;
        }else{
            return 4; //assume poster
        }
    }

    public Integer getTableRows() {
        if (IsFanartTypePoster()){
            return 2;
        }else if (IsFanartTypeBackground()){
            return 3;
        }else if (IsFanartTypeBanner()){
            return 4;
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
    
    public Boolean IsDefault(String FanartItem){
        if (this.DefaultFanart.isEmpty()){
            return Boolean.FALSE;
        }
        if (this.DefaultFanart.equals(FanartItem)){
            return Boolean.TRUE;
        }
        LOG.debug("IsDefault: no match found: CurrentItem '" + FanartItem + "' Default '" + this.DefaultFanart + "'");
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
