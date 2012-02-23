/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Diamond;

import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import sagex.UIContext;
import sagex.api.MediaFileAPI;
import sagex.phoenix.db.UserRecordUtil;
import sagex.phoenix.metadata.IMetadata;
import sagex.phoenix.metadata.ISageCustomMetadataRW;
import sagex.phoenix.metadata.MediaArtifactType;
import sagex.phoenix.metadata.MediaType;
import sagex.phoenix.util.StringUtils;
import sagex.phoenix.util.Utils;
import sagex.phoenix.vfs.IAlbumInfo;
import sagex.phoenix.vfs.IMediaFile;
import sagex.phoenix.vfs.IMediaFolder;
import sagex.phoenix.vfs.IMediaResource;
import sagex.phoenix.vfs.MediaResourceType;
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
        return GetArtifact(imediaresource, "background", RefreshArea, Boolean.FALSE);
    }
    public static Object GetBackground(IMediaResource imediaresource, String RefreshArea, Boolean originalSize){
        return GetArtifact(imediaresource, "background", RefreshArea, originalSize);
    }
    public static Object GetBackground(Object imediaresource, String RefreshArea){
        return GetBackground(Source.ConvertToIMR(imediaresource), RefreshArea, Boolean.FALSE);
    }
    public static Object GetBackground(Object imediaresource, String RefreshArea, Boolean originalSize){
        return GetBackground(Source.ConvertToIMR(imediaresource), RefreshArea, originalSize);
    }

    //This will return a poster and refresh that specific area
    //Check for null in the STV to not change the poster if that is desired
    public static Object GetPoster(IMediaResource imediaresource, String RefreshArea){
        return GetArtifact(imediaresource, "poster", RefreshArea, Boolean.FALSE);
    }
    public static Object GetPoster(IMediaResource imediaresource, String RefreshArea, Boolean originalSize){
        return GetArtifact(imediaresource, "poster", RefreshArea, originalSize);
    }
    public static Object GetPoster(Object imediaresource, String RefreshArea){
        return GetPoster(Source.ConvertToIMR(imediaresource), RefreshArea, Boolean.FALSE);
    }
    public static Object GetPoster(Object imediaresource, String RefreshArea, Boolean originalSize){
        return GetPoster(Source.ConvertToIMR(imediaresource), RefreshArea, originalSize);
    }

    //This will return a banner and refresh that specific area
    //Check for null in the STV to not change the banner if that is desired
    public static Object GetBanner(IMediaResource imediaresource, String RefreshArea){
        return GetArtifact(imediaresource, "banner", RefreshArea, Boolean.FALSE);
    }
    public static Object GetBanner(IMediaResource imediaresource, String RefreshArea, Boolean originalSize){
        return GetArtifact(imediaresource, "banner", RefreshArea, originalSize);
    }
    public static Object GetBanner(Object imediaresource, String RefreshArea){
        return GetBanner(Source.ConvertToIMR(imediaresource), RefreshArea, Boolean.FALSE);
    }
    public static Object GetBanner(Object imediaresource, String RefreshArea, Boolean originalSize){
        return GetBanner(Source.ConvertToIMR(imediaresource), RefreshArea, originalSize);
    }

    //used to handle a specific refresh after the image is loaded in the cache
    public static Object GetArtifact(IMediaResource imediaresource, String resourcetype, String RefreshArea, Boolean originalSize){
        //return the default image passed in when none found or waiting for background processing from the queue
        LOG.debug("GetArtifact: imediaresource '" + imediaresource + "' resourcetype '" + resourcetype + "' RefreshArea '" + RefreshArea + "' originalSize '" + originalSize + "'");
        if (imediaresource == null) {
            LOG.debug("GetArtifact: imediaresource is NULL so returning NULL");
            return null;
        }

        ImageCacheKey tKey = GetImageKey(imediaresource, resourcetype, originalSize, null);
        if (!tKey.IsValidKey()){
            LOG.debug("GetArtifact: Not a valid Key so returning defaultimage '" + tKey + "'");
            return tKey.getDefaultImage();
        }
        tKey.setRefreshArea(RefreshArea);
        return GetImage(tKey);
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
        return GetImage(Key, Boolean.FALSE);
    }
    public static Object GetImage(ImageCacheKey Key, Boolean SkipQueue){
        MediaArtifactType faArtifactType = Key.getArtifactType();
        Object mediaObject = null;
        String tImageString = Key.getImagePath();
        Object tImage = null;

        //make sure there is a valid key available
        if (Key.IsValidKey()){
            //LOG.debug("GetImage: FromKey: '" + Key + "'");
            //see if we are caching or just returning an image
            if (UseCache(faArtifactType)){
                //see if the image is in the cache and if so return it
                mediaObject = ICache.get(Key.getKey());
                if (mediaObject!=null){
                    LOG.debug("GetImage: FromKey: found Image in Cache and return it based on '" + tImageString + "'");
                    return mediaObject;
                }else{
                    if (UseQueue(faArtifactType) && !SkipQueue){
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
                        if (tImage==null){
                            LOG.debug("GetImage: FromKey: null image returned from CreateImage '" + Key.getKey() + "'");
                        }else{
                            ICache.put(Key.getKey(), tImage);
                            LOG.debug("GetImage: FromKey: adding to Cache '" + Key.getKey() + "'");
                        }
                        return tImage;
                    }
                }
            }else{
                //get the image and return it
                tImage = CreateImage(Key);
                if (tImage==null){
                    LOG.debug("GetImage: FromKey: null image returned from CreateImage '" + Key.getKey() + "'");
                }else{
                    LOG.debug("GetImage: FromKey: cache off so returning image for '" + Key.getKey() + "'");
                }
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

    public static IMediaResource GetChild(IMediaResource imediaresource, Boolean UseRandom){
        IMediaResource childmediaresource = null;
        ViewFolder Folder = (ViewFolder) imediaresource;
        //get a child item (if any) from the Folder
        if (phoenix.media.GetAllChildren(Folder, 1).size()>0){
            Integer Element = 0;
            if (UseRandom){
                Element = phoenix.util.GetRandomNumber(phoenix.media.GetAllChildren(Folder).size());
            }
            childmediaresource = (IMediaResource) phoenix.media.GetAllChildren(Folder).get(Element);
        }
        return childmediaresource;
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
            ViewFolder Parent = (ViewFolder) phoenix.media.GetParent(imediaresource);
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
                childmediaresource = GetChild(imediaresource, Boolean.FALSE);
                if (phoenix.media.IsMediaType( childmediaresource , "TV" )){
                    LOG.debug("GetImageKey: TV show found '" + phoenix.media.GetTitle(imediaresource) + "' using Series Fanart");
                    //use Series type fanart
                    faMediaObject = phoenix.media.GetMediaObject(childmediaresource);
                    faMetadata = Collections.emptyMap();
                    faMediaType = MediaType.TV;
                }else{
                    LOG.debug("GetImageKey: Other show found '" + phoenix.media.GetTitle(imediaresource) + "' using Child for Fanart");
                    //use a child for the show fanart
                    if (resourcetype.equals("background")){
                        //only for backgrounds get a random child so the backgrounds vary
                        childmediaresource = GetChild(imediaresource, Boolean.TRUE);
                    }
                    faMediaObject = phoenix.media.GetMediaObject(childmediaresource);
                }
            }else if (Grouping.equals("genre")){
                LOG.debug("GetImageKey: genre group found '" + phoenix.media.GetTitle(imediaresource) + "' using Child for Fanart");
                childmediaresource = GetChild(imediaresource, Boolean.FALSE);
                faMediaObject = phoenix.media.GetMediaObject(childmediaresource);
                //TODO:SPECIAL handling to get GENRE images
                //genreImage ="Themes\\Diamond\\GenreImages\\"+phoenix_media_GetTitle(ThumbFile)+".png"
                // File[] Files = phoenix.util.GetFiles("Path", new String[] {"jpg","gif","png"}, Boolean.FALSE);
                // File[] Files = phoenix.util.GetImages("Path");
                
            }else if (Grouping.equals("season")){
                LOG.debug("GetImageKey: season group found '" + phoenix.media.GetTitle(imediaresource) + "' using Child for Fanart");
                //just use a child item so you get fanart for the specific season
                //TODO: use the first child so a seasons always has the same ImageID for POSTER and BANNER
                if (resourcetype.equals("background")){
                    //only for backgrounds get a random child so the backgrounds vary
                    childmediaresource = GetChild(imediaresource, Boolean.TRUE);
                }else{
                    childmediaresource = GetChild(imediaresource, Boolean.FALSE);
                }
                faMediaObject = phoenix.media.GetMediaObject(childmediaresource);
            }else if (Grouping.equals("NoGroup")){
                LOG.debug("GetImageKey: Folder found but no grouping for '" + phoenix.media.GetTitle(imediaresource) + "' using passed in object for Fanart");
                faMediaObject = phoenix.media.GetMediaObject(imediaresource);
            }else{
                LOG.debug("GetImageKey: unhandled grouping found '" + Grouping + "' for Title '" + phoenix.media.GetTitle(imediaresource) + "' using Child for Fanart");
                if (resourcetype.equals("background")){
                    //only for backgrounds get a random child so the backgrounds vary
                    childmediaresource = GetChild(imediaresource, Boolean.TRUE);
                }else{
                    childmediaresource = GetChild(imediaresource, Boolean.FALSE);
                }
                faMediaObject = phoenix.media.GetMediaObject(childmediaresource);
            }
        }else{
            //not a FOLDER
            if (phoenix.media.IsMediaType( imediaresource , "TV" )){
                //for TV items we need to get an Episode Fanart
                //the resourcetype changes to a background as poster and banner fanaet are not available
                faMediaObject = phoenix.media.GetMediaObject(imediaresource);
                if (resourcetype.equals("background")){
                    //special Episode handling for backgrounds
                    tImageString = phoenix.fanart.GetEpisode(faMediaObject);
                    if (tImageString==null || tImageString.equals("")){
                        LOG.debug("GetImageKey: Episode '" + phoenix.media.GetTitle(imediaresource) + "' using Fanart based on GetDefaultEpisode");
                        DefaultEpisodeImage = phoenix.fanart.GetDefaultEpisode(faMediaObject);
                        //use the title for the ImageString
                        tImageString = phoenix.media.GetTitle(imediaresource);
                    }else{
                        LOG.debug("GetImageKey: Episode '" + phoenix.media.GetTitle(imediaresource) + "' Fanart found '" + tImageString + "'");
                    }
                }else{
                    LOG.debug("GetImageKey: TV found for other than background '" + phoenix.media.GetTitle(imediaresource) + "'");
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
            //LOG.debug("GetImageKey: GetFanartArtifact returned '" + tImageString + "'");
        }
        if (tImageString==null || tImageString.equals("")){
            LOG.debug("GetImageKey: tImageString blank or NULL so returning defaultImage");
            ImageCacheKey tICK = new ImageCacheKey();
            tICK.setDefaultImage(defaultImage);
            return tICK;
        }
        String ImageID = phoenix.fanart.ImageKey(faMediaObject, faMediaType, faMediaTitle, faArtifactType, faArtifiactTitle, faMetadata);
        //LOG.debug("GetImageKey: ImageID created '" + ImageID + "'");
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
    
    public static String GetKeyFromImageKey(ImageCacheKey Key){
        return Key.getKey();
    }
    
    public static void GetImageFromQueue(){
        if (IQueue.size()>0){
            UIContext UIc = new UIContext(sagex.api.Global.GetUIContextName());
            String tItemKey = IQueue.entrySet().iterator().next().getKey();
            ImageCacheKey tItem = IQueue.get(tItemKey);
            IQueue.remove(tItemKey);
            //get the image and add it to the cache then return it
            Object tImage = CreateImage(tItem);
            if (tImage!=null){
                if (tItem.HasRefreshArea()){
                    sagex.api.Global.RefreshArea(UIc, tItem.getRefreshArea());
                }else{
                    sagex.api.Global.RefreshAreaForVariable(UIc, "PreloadTagKey", tItem.getKey());
                }
                ICache.put(tItem.getKey(), tImage);
                LOG.debug("GetImageFromQueue: remaining(" + IQueue.size() + ") adding to Cache '" + tItem + "'");
            }
        }else{
            LOG.debug("GetImageFromQueue: EMPTY QUEUE");
        }
    }

    public static Integer GetQueueSize(){
        //LOG.debug("GetQueueSize: '" + IQueue.size() + "'");
        return IQueue.size();
    }

    public static Object CreateImage(ImageCacheKey Key){
        return CreateImage(Key, Boolean.FALSE);
    }
    public static Object CreateImage(ImageCacheKey Key, Boolean OverWrite){
        if (!Key.IsValidKey()){
            LOG.debug("CreateImage: called with invalid Key '" + Key + "'");
            return null;
        }
        Object ThisImage = null;
        if (!OverWrite){
            //See if the image is already cached in the filesystem by a previous CreateImage call
            ThisImage = phoenix.image.GetImage(Key.getImageID(), CreateImageTag);
            if (ThisImage!=null){
                LOG.debug("CreateImage: Filesystem cached item found for Tag '" + CreateImageTag + "' ID '" + Key.getImageID() + "'");
                return ThisImage;
            }
        }
        
        //if we got this far then an OverWrite was either FORCED or the Image was not in the FileSystem Cache
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
                ThisImage = phoenix.image.CreateImage(Key.getImageID(), CreateImageTag, Key.getDefaultEpisodeImage(), "{name: scale, width: " + finalscalewidth + ", height: -1}", true);
            } catch (Exception e) {
                LOG.debug("CreateImage: phoenix.image.CreateImage FAILED for DefaultEpisodeImage - scalewidth = '" + scalewidth + "' UIWidth = '" + UIWidth + "' finalscalewidth = '" + finalscalewidth + "' for Type = '" + Key.getArtifactType().toString() + "' Image = '" + Key.getImagePath() + "' Error: '" + e + "'");
                return null;
            }
        }else{
            try {
                ThisImage = phoenix.image.CreateImage(Key.getImageID(), CreateImageTag, Key.getImagePath(), "{name: scale, width: " + finalscalewidth + ", height: -1}", true);
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
    
    //still testing if there is value add to this prefetch approach
    public static void PreFetchPosters(List<IMediaResource> Children){
        LOG.debug("PreFetchPosters: Started '" + Children + "'");
        for (IMediaResource Child: Children){
            LOG.debug("PreFetchPosters: processing Child '" + Child + "'");
            GetImage(Child, "poster");
        }
    }
    
    public static Object GetTVThumbnail(Object MediaFile, Boolean UseBackNotThumb){
        UIContext uIContext = new UIContext(sagex.api.Global.GetUIContextName());
        Object FinalThumb = null;
        //try to see if we can get a phoenix image first
        FinalThumb = GetImage(GetImageKey(MediaFile,"background"),Boolean.TRUE);
        if (FinalThumb!=null){
            LOG.debug("GetTVThumbnail: Using Gemstone GetImage");
            return FinalThumb;
        }
        
        if(MediaFile==null){
            //find some sort of image to display
            LOG.debug("GetTVThumbnail: null MediaFile");
        }else{
            //check if Sage has a Thumb for this MediaFile - xShowImage
            Boolean ImageFound = Boolean.FALSE;
            String[] ImageTypeList = {"PosterWide", "PosterTall", "PhotoWide", "PhotoTall"};
            for (String ImageType:ImageTypeList){
                if (sagex.api.ShowAPI.GetShowImageCount( uIContext, MediaFile, ImageType )>0){
                    FinalThumb = sagex.api.ShowAPI.GetShowImage( uIContext, MediaFile, ImageType, 0, 2 );
                    LOG.debug("GetTVThumbnail: Using ShowAPI.GetShowImage");
                    ImageFound = Boolean.TRUE;
                    break;
                }
            }
            if (!ImageFound){
                //No Zap2it-provided show images; try thumbnail
                if (sagex.api.MediaFileAPI.HasAnyThumbnail(uIContext,MediaFile)){
                    //xNormal
                    if (MetadataCalls.IsMediaTypeTV(MediaFile)  &&  sagex.api.ShowAPI.GetShowCategory(uIContext,MediaFile).indexOf("Movie") == -1){
                        //Check if we want to display Backgrounds rather than Thumbs
                        if (UseBackNotThumb){
                            FinalThumb = GetImage(MediaFile,"background");
                            LOG.debug("GetTVThumbnail: trying Gemstone backround");
                        }else{
                            FinalThumb = sagex.api.MediaFileAPI.GetThumbnail(uIContext,MediaFile);
                            LOG.debug("GetTVThumbnail: Using MediaFileAPI.GetThumbnail");
                        }
                    }
                }else{
                    //try Series
                    Object SeriesInfo = sagex.api.ShowAPI.GetShowSeriesInfo(uIContext,MediaFile);
                    if (sagex.api.SeriesInfoAPI.HasSeriesImage(uIContext,SeriesInfo)){
                        //xSeriesInfo
                        FinalThumb = sagex.api.SeriesInfoAPI.GetSeriesImage(uIContext,MediaFile);
                        LOG.debug("GetTVThumbnail: Using SeriesInfoAPI.GetSeriesImage");
                    }else{
                        //try Channel Logo
                        FinalThumb = sagex.api.ChannelAPI.GetChannelLogo( uIContext, MediaFile, "Large", 1, true );
                        LOG.debug("GetTVThumbnail: Using ChannelAPI.GetChannelLogo");
                    }
                            
                }
            }
        }
        if (FinalThumb==null){
            //last try to get an image
            if (MediaFile!=null){
                FinalThumb = GetImage(MediaFile,"backbround");
                LOG.debug("GetTVThumbnail: Trying Gemstone fanart as last resort");
            }
        }
        return FinalThumb;
    }

    //phoenix does not expose this as public so recreate this here
    public static final String STORE_SERIES_FANART = "phoenix.seriesfanart";
    private File getDefaultArtifact(IMediaFile file, MediaArtifactType artifactType) {

        if (file==null||artifactType==null) return null;

        String key = null;
        if (artifactType == MediaArtifactType.POSTER) {
                key=ISageCustomMetadataRW.FieldName.DEFAULT_POSTER;
        } else if (artifactType == MediaArtifactType.BACKGROUND) {
                key=ISageCustomMetadataRW.FieldName.DEFAULT_BACKGROUND;
        } else if (artifactType == MediaArtifactType.BANNER) {
                key=ISageCustomMetadataRW.FieldName.DEFAULT_BANNER;
        }

        String def = MediaFileAPI.GetMediaFileMetadata(file.getMediaObject(), key);
        if (def.isEmpty() && file.isType(MediaResourceType.TV.value())) {
                // defaults for TV shows need to be stored against the seriesname
                String title = resolveMediaTitle(file.getTitle(), file);
                def = UserRecordUtil.getField(STORE_SERIES_FANART, title, artifactType.name());
        }

        if (!def.isEmpty()) {
                File f = null;
                if (phoenix.fanart.GetFanartCentralFolder()!=null) {
                        f = new File(phoenix.fanart.GetFanartCentralFolder(), def);
                } else {
                        f = new File(def);
                }

                if (f.exists() && f.isFile()) {
                        return f;
                }
        }

        return null;
    }

    private String resolveMediaTitle(String mediaTitle, IMediaFile mf) {
        if (mf==null) return mediaTitle;
        if (!mediaTitle.isEmpty()) return mediaTitle;

        // check for music
        if (mf.isType(MediaResourceType.MUSIC.value())) {
                IAlbumInfo info = mf.getAlbumInfo();
                if (info!=null) {
                        mediaTitle = info.getArtist();
                }
                if (!mediaTitle.isEmpty()) return mediaTitle;
        }

        IMetadata md = mf.getMetadata();
        if (md != null) {
                mediaTitle = md.getMediaTitle();
                if (mediaTitle.isEmpty()) mediaTitle=null;
        }

        return Utils.returnNonNull(mediaTitle, mf.getTitle());
    }


    
    
    //TODO: Delete Cached Fanart for specific Show
    // remove it from memory in Sage using UnloadImage()
    //UnloadImage(Diamond_FanartCaching_GetCachedFanart(VideoCell,false,"Poster"))
    //Delete from Phoenix Image FileSystem Cache
    //Delete from the SoftHashMap Cache - ICache
    //Force a reload of the new Image - need to call CreateImage with an Overwrite flag so it forces the new image to be used
    
    //TODO: handle user set backgrounds and posters and banners located with the media files
    //use similar settings as Diamond settings so user can specific the name of the file for the fanart
    
}
