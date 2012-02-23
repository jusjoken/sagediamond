/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Diamond;

import sagex.phoenix.vfs.IMediaResource;

/**
 *
 * @author jusjoken
 */
public class FanartManager {
    //based on the passed in MediaResource determine if a TV Series or Movie Fanart Object
    
    private IMediaResource MediaResource = null;
    private IMediaResource PrimaryMediaResource = null;
    public static enum FanartManagerTypes{TV,MOVIE,NONE};
    private FanartManagerTypes FanartManagerType = FanartManagerTypes.NONE;
    private String FanartType = "poster";
    
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
        this.FanartType = FanartType.toLowerCase();
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
