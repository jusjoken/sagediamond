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
    
}
