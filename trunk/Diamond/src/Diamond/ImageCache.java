/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Diamond;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import sagex.UIContext;
import sagex.phoenix.metadata.MediaArtifactType;
import sagex.phoenix.metadata.MediaType;
import sagex.phoenix.vfs.IMediaResource;
import sagex.phoenix.vfs.views.ViewFolder;

/**
 *
 * @author jusjoken
 */
public class ImageCache {
//    public ImageCache(){
//        
//    }
    
    //a ImageCache object needs
    // - ICache - SoftHashMap - this is the Cache itself
    //   - MinCacheItems - keep this number of items in the Cache when Memory is cleaned
    // - IQueue - LinkedList - list of MediaObject Image strings to get a specific ImageType to add to the Cache
    // - 
    //Global Functions/Variables
    // - CacheType - Background - items get added to Queue for background processing
    // - CacheType - NoQueue - items get added to the Cache and returned
    // - CacheType - Off - items get returned and NOT added to Queue NOR Cache
    // - CacheType - ByImageType - 
    static private final Logger LOG = Logger.getLogger(ImageCache.class);
    private static final String ICacheProps = Const.BaseProp + Const.PropDivider + Const.ImageCacheProp;
    private static LinkedHashMap<String,ImageCacheKey> IQueue = new LinkedHashMap<String,ImageCacheKey>();
    private static SoftHashMap ICache = new SoftHashMap(GetMinSize());
    public static enum ImageCacheTypes{OFF,BACKGROUND,NOQUEUE,BYIMAGETYPE};
    private static final String ImageCacheTypesList = ImageCacheTypes.OFF + util.ListToken + ImageCacheTypes.BACKGROUND + util.ListToken + ImageCacheTypes.NOQUEUE + util.ListToken + ImageCacheTypes.BYIMAGETYPE;
    private static final String ImageCacheTypesListByImageType = ImageCacheTypes.OFF + util.ListToken + ImageCacheTypes.BACKGROUND + util.ListToken + ImageCacheTypes.NOQUEUE;
    public static final String CreateImageTag = "GemstoneImages";
    
    //Initialize the Cache and the Queue
    public static void Init(){
        SoftHashMap ICache = new SoftHashMap(GetMinSize());
        ClearQueue();
    }
    
    //Clear all lists - Queue and Cache
    public static void Clear(){
        IQueue.clear();
        ICache.clear();
    }
    
    public static void ClearQueue(){
        IQueue.clear();
    }

    //This will return a background and refresh that specific area
    //Check for null in the STV to not change the background if that is desired
    public static Object GetBackground(IMediaResource imediaresource, String RefreshArea){
        return GetBackground(imediaresource, RefreshArea, Boolean.FALSE);
    }
    public static Object GetBackground(IMediaResource imediaresource, String RefreshArea, Boolean originalSize){
        //return the default image passed in when none found or waiting for background processing from the queue
        LOG.debug("GetBackground: imediaresource '" + imediaresource + "' RefreshArea '" + RefreshArea + "' originalSize '" + originalSize + "'");
        if (imediaresource == null) {
            LOG.debug("GetBackground: imediaresource is NULL so returning NULL");
            return null;
        }

        ImageCacheKey tKey = GetImageKey(imediaresource, "background", originalSize, null);
        if (!tKey.IsValidKey()){
            LOG.debug("GetBackground: Not a valid Key so returning defaultimage '" + tKey + "'");
            return tKey.getDefaultImage();
        }
        tKey.setRefreshArea(RefreshArea);
        return GetImage(tKey);
    }
    public static Object GetBackground(Object imediaresource, String RefreshArea){
        return GetBackground(imediaresource, RefreshArea, Boolean.FALSE);
    }
    public static Object GetBackground(Object imediaresource, String RefreshArea, Boolean originalSize){
        if (imediaresource == null || imediaresource.toString().isEmpty() || imediaresource.toString().contains("BlankItem")) {
            return null;
        }
        LOG.debug("GetBackground: Convenience method called with Class = '" + imediaresource.getClass() + "'");
        IMediaResource proxy = phoenix.media.GetMediaResource(imediaresource);
        if (proxy==null) {
            LOG.debug("GetBackground: GetMediaResource failed to convert '" + imediaresource + "'");
            return null; // do nothing
        }
        return GetBackground(proxy, RefreshArea, originalSize);
    }
    
    //This will return an image from Cache or direct or add to the Queue depending on the settings
    public static Object GetImage(IMediaResource imediaresource, String resourcetype ){
        return GetImage(imediaresource, resourcetype, Boolean.FALSE, null);
    }
    public static Object GetImage(IMediaResource imediaresource, String resourcetype, String defaultImage){
        return GetImage(imediaresource, resourcetype, Boolean.FALSE, defaultImage);
    }
    public static Object GetImage(IMediaResource imediaresource, String resourcetype, Boolean originalSize){
        return GetImage(imediaresource, resourcetype, originalSize, null);
    }
    public static Object GetImage(IMediaResource imediaresource, String resourcetype, Boolean originalSize, String defaultImage){
        //return the default image passed in when none found or waiting for background processing from the queue
        LOG.debug("GetImage: imediaresource '" + imediaresource + "' resourcetype '" + resourcetype + "' defaultImage '" + defaultImage + "'");
        if (imediaresource == null) {
            LOG.debug("GetImage: imediaresource is NULL so returning NULL");
            return null;
        }

        ImageCacheKey tKey = GetImageKey(imediaresource, resourcetype, originalSize, defaultImage);
        if (!tKey.IsValidKey()){
            LOG.debug("GetImage: Not a valid Key so returning defaultimage '" + tKey + "'");
            return tKey.getDefaultImage();
        }
        return GetImage(tKey);
    }

    public static Object GetImage(ImageCacheKey Key){
        MediaArtifactType faArtifactType = Key.getArtifactType();
        Object mediaObject = null;
        String tImageString = Key.getImagePath();
        Object tImage = null;

        //make sure there is a valid key available
        if (Key.IsValidKey()){
            LOG.debug("GetImage: FromKey: '" + Key + "'");
            //see if we are caching or just returning an image
            if (UseCache(faArtifactType)){
                //see if the image is in the cache and if so return it
                mediaObject = ICache.get(Key.getKey());
                if (mediaObject!=null){
                    LOG.debug("GetImage: FromKey: found Image in Cache and return it based on '" + tImageString + "'");
                    return mediaObject;
                }else{
                    if (UseQueue(faArtifactType)){
                        //see if the item is already in the queue
                        if (IQueue.containsKey(Key.getKey())){
                            LOG.debug("GetImage: FromKey: already in the Queue '" + Key.getKey() + "' defaultImage returned '" + Key.getDefaultImage() + "'");
                            return Key.getDefaultImage();
                        }else{
                            //add the imagestring to the queue for background processing later
                            IQueue.put(Key.getKey(),Key);
                            LOG.debug("GetImage: FromKey: adding to Queue '" + Key.getKey() + "' defaultImage returned '" + Key.getDefaultImage() + "'");
                            return Key.getDefaultImage();
                        }
                    }else{
                        //get the image and add it to the cache then return it
                        tImage = CreateImage(Key);
                        ICache.put(Key.getKey(), tImage);
                        LOG.debug("GetImage: FromKey: adding to Cache '" + Key.getKey() + "'");
                        return tImage;
                    }
                }
            }else{
                //get the image and return it
                tImage = CreateImage(Key);
                LOG.debug("GetImage: FromKey: cache off so returning image for '" + Key.getKey() + "'");
                return tImage;
            }
        }else{
            LOG.debug("GetImage: FromKey: Key is invalid '" + Key + "' defaultImage returned '" + Key.getDefaultImage() + "'");
            return Key.getDefaultImage();
        }
    }
    
    //Convenience method that will convert the incoming object parameter to a IMediaResource type 
    public static Object GetImage(Object imediaresource, String resourcetype){
        return GetImage(imediaresource, resourcetype, Boolean.FALSE, "");
    }
    public static Object GetImage(Object imediaresource, String resourcetype, String defaultImage){
        return GetImage(imediaresource, resourcetype, Boolean.FALSE, defaultImage);
    }
    public static Object GetImage(Object imediaresource, String resourcetype, Boolean originalSize){
        return GetImage(imediaresource, resourcetype, originalSize, "");
    }
    public static Object GetImage(Object imediaresource, String resourcetype, Boolean originalSize, String defaultImage){
        return GetImage(Source.ConvertToIMR(imediaresource), resourcetype, originalSize, defaultImage);
    }

    public static ImageCacheKey GetImageKey(IMediaResource imediaresource, String resourcetype){
        return GetImageKey(imediaresource, resourcetype, Boolean.FALSE, "");
    }
    public static ImageCacheKey GetImageKey(IMediaResource imediaresource, String resourcetype, Boolean originalSize){
        return GetImageKey(imediaresource, resourcetype, originalSize, "");
    }
    public static ImageCacheKey GetImageKey(IMediaResource imediaresource, String resourcetype, Boolean originalSize, String defaultImage){
        resourcetype = resourcetype.toLowerCase();
        Object tImage = null;
        Object mediaObject = null;
        String tImageString = "";
        IMediaResource childmediaresource = null;
        String Grouping = "NoGroup";
        Object faMediaObject = null;
        MediaType faMediaType = null;
        String faMediaTitle = null;
        MediaArtifactType faArtifactType = ImageCacheKey.ConvertStringtoMediaArtifactType(resourcetype);
        String faArtifiactTitle = null;
        Map<String,String> faMetadata = null;
        Object DefaultEpisodeImage = null;
        
        //see if this is a FOLDER item
        //we will need a MediaObject to get any fanart so get it from the passed in resource OR the child if any
        if (phoenix.media.IsMediaType( imediaresource , "FOLDER" )){
            ViewFolder Folder = (ViewFolder) imediaresource;
            ViewFolder Parent = (ViewFolder) phoenix.media.GetParent(imediaresource);
            //get a child item (if any) from the Folder
            if (phoenix.media.GetAllChildren(Folder, 1).size()>0){
                Integer RandomElement = phoenix.util.GetRandomNumber(phoenix.media.GetAllChildren(Folder).size());
                childmediaresource = (IMediaResource) phoenix.media.GetAllChildren(Folder).get(RandomElement);
            }
            //see how the folder is grouped
            if (phoenix.umb.GetGroupers(Parent).size() > 0){
                Grouping = phoenix.umb.GetName( phoenix.umb.GetGroupers(Parent).get(0) );
                //tImageString = 
                //"genre" - get genre specific images
                //"season" - for banners or posters get Season Specific ones if available
                //"show" - get the first item in the group and use it for the image
                //else - get the first item in the group and use it for the image
            }
            if (Grouping.equals("show")){
                //need to know if this is a TV show grouping to get a Series fanart item
                if (phoenix.media.IsMediaType( childmediaresource , "TV" )){
                    LOG.debug("GetImageKey: TV show found '" + phoenix.media.GetTitle(imediaresource) + "' using Series Fanart");
                    //use Series type fanart
                    faMediaObject = phoenix.media.GetMediaObject(childmediaresource);
                    faMetadata = Collections.emptyMap();
                    faMediaType = MediaType.TV;
                }else{
                    LOG.debug("GetImageKey: Other show found '" + phoenix.media.GetTitle(imediaresource) + "' using Child for Fanart");
                    //use a child for the show fanart
                    faMediaObject = phoenix.media.GetMediaObject(childmediaresource);
                }
            }else if (Grouping.equals("genre")){
                LOG.debug("GetImageKey: genre group found '" + phoenix.media.GetTitle(imediaresource) + "' using Child for Fanart");
                faMediaObject = phoenix.media.GetMediaObject(childmediaresource);
                //TODO:SPECIAL handling to get GENRE images
                //genreImage ="Themes\\Diamond\\GenreImages\\"+phoenix_media_GetTitle(ThumbFile)+".png"
                
            }else if (Grouping.equals("season")){
                LOG.debug("GetImageKey: season group found '" + phoenix.media.GetTitle(imediaresource) + "' using Child for Fanart");
                //just use a child item so you get fanart for the specific season
                faMediaObject = phoenix.media.GetMediaObject(childmediaresource);
            }else if (Grouping.equals("NoGroup")){
                LOG.debug("GetImageKey: Folder found but no grouping for '" + phoenix.media.GetTitle(imediaresource) + "' using passed in object for Fanart");
                faMediaObject = phoenix.media.GetMediaObject(imediaresource);
            }else{
                LOG.debug("GetImageKey: unhandled grouping found '" + Grouping + "' for Title '" + phoenix.media.GetTitle(imediaresource) + "' using Child for Fanart");
                faMediaObject = phoenix.media.GetMediaObject(childmediaresource);
            }
        }else{
            //not a FOLDER
            if (phoenix.media.IsMediaType( imediaresource , "TV" )){
                //for TV items we need to get an Episode Fanart
                //the resourcetype changes to a background as poster and banner fanaet are not available
                faMediaObject = phoenix.media.GetMediaObject(imediaresource);
                tImageString = phoenix.fanart.GetEpisode(faMediaObject);
                faArtifactType = MediaArtifactType.BACKGROUND;
                if (tImageString==null || tImageString.equals("")){
                    LOG.debug("GetImageKey: Episode '" + phoenix.media.GetTitle(imediaresource) + "' using Fanart based on GetDefaultEpisode");
                    DefaultEpisodeImage = phoenix.fanart.GetDefaultEpisode(faMediaObject);
                    //use the title for the ImageString
                    tImageString = phoenix.media.GetTitle(imediaresource);
                }else{
                    LOG.debug("GetImageKey: Episode '" + phoenix.media.GetTitle(imediaresource) + "' Fanart found '" + tImageString + "'");
                }
            }else{
                faMediaObject = phoenix.media.GetMediaObject(imediaresource);
                //faMediaType = MediaType.MOVIE;
                LOG.debug("GetImageKey: Title '" + phoenix.media.GetTitle(imediaresource) + "' using passed in object for Fanart");
            }
                
        }
        
        if (tImageString.equals("")){
            String tMediaType = null;
            if (faMediaType!=null){
                tMediaType = faMediaType.toString();
            }
            tImageString = phoenix.fanart.GetFanartArtifact(faMediaObject, tMediaType, faMediaTitle, faArtifactType.toString(), faArtifiactTitle, faMetadata);
            LOG.debug("GetImageKey: GetFanartArtifact returned '" + tImageString + "'");
        }
        if (tImageString==null || tImageString.equals("")){
            LOG.debug("GetImageKey: tImageString blank or NULL so returning defaultImage");
            ImageCacheKey tICK = new ImageCacheKey();
            tICK.setDefaultImage(defaultImage);
            return tICK;
        }
        String ImageID = phoenix.fanart.ImageKey(faMediaObject, faMediaType, faMediaTitle, faArtifactType, faArtifiactTitle, faMetadata);
        LOG.debug("GetImageKey: ImageID created '" + ImageID + "'");
        //String tKey = GetQueueKey(tImageString, faArtifactType, originalSize, ImageID);
        ImageCacheKey tICK = new ImageCacheKey(tImageString,originalSize,faArtifactType,ImageID);
        tICK.setDefaultEpisodeImage(DefaultEpisodeImage);
        tICK.setDefaultImage(defaultImage);
        LOG.debug("GetImageKey: Key '" + tICK + "'");
        return tICK;
    }
    //Convenience method that will convert the incoming object parameter to a IMediaResource type 
    public static ImageCacheKey GetImageKey(Object imediaresource, String resourcetype){
        return GetImageKey(imediaresource, resourcetype, Boolean.FALSE, "");
    }
    public static ImageCacheKey GetImageKey(Object imediaresource, String resourcetype, Boolean originalSize){
        return GetImageKey(imediaresource, resourcetype, originalSize, "");
    }
    public static ImageCacheKey GetImageKey(Object imediaresource, String resourcetype, Boolean originalSize, String defaultImage){
        return GetImageKey(Source.ConvertToIMR(imediaresource), resourcetype, originalSize, defaultImage);
    }
    
    public static void GetImageFromQueue(){
        if (IQueue.size()>0){
            UIContext UIc = new UIContext(sagex.api.Global.GetUIContextName());
            String tItemKey = IQueue.entrySet().iterator().next().getKey();
            ImageCacheKey tItem = IQueue.get(tItemKey);
            IQueue.remove(tItemKey);
            //get the image and add it to the cache then return it
            Object tImage = CreateImage(tItem);
            if (tItem.HasRefreshArea()){
                sagex.api.Global.RefreshArea(UIc, tItem.getRefreshArea());
            }else{
                sagex.api.Global.RefreshAreaForVariable(UIc, "PreloadTag", tItem);
            }
            ICache.put(tItem.getKey(), tImage);
            LOG.debug("GetImageFromQueue: remaining(" + IQueue.size() + ") adding to Cache '" + tItem + "'");
        }else{
            LOG.debug("GetImageFromQueue: EMPTY QUEUE");
        }
    }
    
    public static Integer GetQueueSize(){
        //LOG.debug("GetQueueSize: '" + IQueue.size() + "'");
        return IQueue.size();
    }

    public static Object CreateImage(ImageCacheKey Key){
        if (!Key.IsValidKey()){
            LOG.debug("CreateImage: called with invalid Key '" + Key + "'");
            return null;
        }
        Object ThisImage = null;
        //See if the image is already cached in the filesystem by a previous CreateImage call
        ThisImage = phoenix.image.GetImage(Key.getImageID(), CreateImageTag);
        if (ThisImage!=null){
            LOG.debug("CreateImage: Filesystem cached item found for Tag '" + CreateImageTag + "' ID '" + Key.getImageID() + "'");
            return ThisImage;
        }
        
        UIContext UIc = new UIContext(sagex.api.Global.GetUIContextName());
        //based on the ImageType determine the scalewidth to use
        Integer UIWidth = sagex.api.Global.GetFullUIWidth(UIc);
        Double scalewidth = 0.2;
        if (Key.getOriginalSize()){
            scalewidth = 1.0;
        }else{
            if (Key.getArtifactType().equals(MediaArtifactType.POSTER)){
                scalewidth = 0.2;
            }else if (Key.getArtifactType().equals(MediaArtifactType.BANNER)){
                scalewidth = 0.6;
            }else if (Key.getArtifactType().equals(MediaArtifactType.BACKGROUND)){
                scalewidth = 0.4;
            }else{
                //use default
            }
        }
        Double finalscalewidth = scalewidth * UIWidth;
        if (Key.HasDefaultEpisodeImage()){
            try {
                ThisImage = phoenix.image.CreateImage(Key.getImageID(), CreateImageTag, Key.getDefaultEpisodeImage(), "{name: scale, width: " + finalscalewidth + ", height: -1}", false);
            } catch (Exception e) {
                LOG.debug("CreateImage: phoenix.image.CreateImage FAILED for DefaultEpisodeImage - scalewidth = '" + scalewidth + "' UIWidth = '" + UIWidth + "' finalscalewidth = '" + finalscalewidth + "' for Type = '" + Key.getArtifactType().toString() + "' Image = '" + Key.getImagePath() + "' Error: '" + e + "'");
                return null;
            }
        }else{
            try {
                ThisImage = phoenix.image.CreateImage(Key.getImageID(), CreateImageTag, Key.getImagePath(), "{name: scale, width: " + finalscalewidth + ", height: -1}", false);
            } catch (Exception e) {
                LOG.debug("CreateImage: phoenix.image.CreateImage FAILED - scalewidth = '" + scalewidth + "' UIWidth = '" + UIWidth + "' finalscalewidth = '" + finalscalewidth + "' for Type = '" + Key.getArtifactType().toString() + "' Image = '" + Key.getImagePath() + "' Error: '" + e + "'");
                return null;
            }
        }
        if (!sagex.api.Utility.IsImageLoaded(UIc, ThisImage)){
            LOG.debug("CreateImage: Loaded using LoagImage(loadImage)) - scalewidth = '" + scalewidth + "' UIWidth = '" + UIWidth + "' finalscalewidth = '" + finalscalewidth + "' for Type = '" + Key.getArtifactType().toString() + "' Image = '" + Key.getImagePath() + "'");
            sagex.api.Utility.LoadImage(UIc, sagex.api.Utility.LoadImage(UIc, ThisImage));
        }else{
            LOG.debug("CreateImage: already Loaded - scalewidth = '" + scalewidth + "' UIWidth = '" + UIWidth + "' finalscalewidth = '" + finalscalewidth + "' for Type = '" + Key.getArtifactType().toString() + "' Image = '" + Key.getImagePath() + "'");
        }
        return ThisImage;
    }
    
    public static Integer GetMinSize(){
        String tProp = ICacheProps + Const.PropDivider + Const.ImageCacheMinSize;
        return util.GetPropertyAsInteger(tProp, 100);
    }
    public static void SetMinSize(Integer Value){
        String tProp = ICacheProps + Const.PropDivider + Const.ImageCacheMinSize;
        util.SetProperty(tProp, Value.toString());
    }

    public static String GetCacheType(){
        return util.GetListOptionName(ICacheProps, Const.ImageCacheType, ImageCacheTypesList, ImageCacheTypes.BACKGROUND.toString());
    }
    public static void SetCacheTypeNext(){
        util.SetListOptionNext(ICacheProps, Const.ImageCacheType, ImageCacheTypesList);
    }

    public static String GetCacheType(MediaArtifactType ImageType){
        if (GetCacheType().equals(ImageCacheTypes.BYIMAGETYPE.toString())){
            String tProp = ICacheProps + Const.PropDivider + Const.ImageCacheType;
            return util.GetListOptionName(tProp, ImageType.toString(), ImageCacheTypesListByImageType, ImageCacheTypes.OFF.toString());
        }else{
            return GetCacheType();
        }
    }
    public static void SetCacheTypeNext(MediaArtifactType ImageType){
        if (GetCacheType().equals(ImageCacheTypes.BYIMAGETYPE.toString())){
            String tProp = ICacheProps + Const.PropDivider + Const.ImageCacheType;
            util.SetListOptionNext(tProp, ImageType.toString(), ImageCacheTypesListByImageType);
        }
    }
    
    public static Boolean UseQueue(){
        if (GetCacheType().equals(ImageCacheTypes.BACKGROUND.toString())){
            return Boolean.TRUE;
        }else if (GetCacheType().equals(ImageCacheTypes.BYIMAGETYPE.toString())){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }
    public static Boolean UseQueue(MediaArtifactType ImageType){
        if (GetCacheType(ImageType).equals(ImageCacheTypes.BACKGROUND.toString())){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }

    public static Boolean UseCache(MediaArtifactType ImageType){
        if (GetCacheType(ImageType).equals(ImageCacheTypes.OFF.toString())){
            return Boolean.FALSE;
        }else{
            return Boolean.TRUE;
        }
    }
    
}
