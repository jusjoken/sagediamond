/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Diamond;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import sagex.phoenix.metadata.MediaType;
import sagex.phoenix.vfs.IMediaResource;

/**
 *
 * @author jusjoken
 */
public class FanartManager {
    //based on the passed in MediaResource determine if a TV Series or Movie Fanart Object
    
    static private final Logger LOG = Logger.getLogger(FanartManager.class);
    private IMediaResource MediaResource = null;
    private IMediaResource PrimaryMediaResource = null;
    public static enum FanartManagerTypes{TV,MOVIE,NONE};
    public static enum TVModes{SERIES,SEASON};
    private TVModes TVMode = TVModes.SERIES; 
    private FanartManagerTypes FanartManagerType = FanartManagerTypes.NONE;
    private String FanartType = ""; //set within Init to poster as a default
    private String[] FanartList = new String[0];
    private String DefaultFanart = "";
    
    public FanartManager(IMediaResource MediaResource){
        this.MediaResource = MediaResource;
        Init();
    }
    public FanartManager(Object MediaResource){
        this.MediaResource = Source.ConvertToIMR(MediaResource);
        Init();
    }
    
    private void Init(){
        this.PrimaryMediaResource = this.MediaResource;
        if (phoenix.media.IsMediaType( this.MediaResource , "FOLDER" )){
            //get the first child and use it to determine the Fanart Type
            PrimaryMediaResource = ImageCache.GetChild(this.MediaResource, Boolean.FALSE);
        }
        //Now determine the Primary Media Resource Type
        if (phoenix.media.IsMediaType( this.PrimaryMediaResource , "TV" )){
            this.FanartManagerType = FanartManagerTypes.TV;
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
        if (this.FanartManagerType.equals(FanartManagerType.MOVIE)){
            Title = PrimaryMediaResource.getTitle();
        }else if (this.FanartManagerType.equals(FanartManagerType.TV)){
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
                faMetadata.put("SeasonNumber","1");
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
