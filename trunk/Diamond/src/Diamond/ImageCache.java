/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Diamond;

import java.util.Collections;
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
    private static LinkedList IQueue = new LinkedList();
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

    //This will return an image from Cache or direct or add to the Queue depending on the settings
    public static Object GetImage(IMediaResource imediaresource, String resourcetype ){
        return GetImage(imediaresource, resourcetype, Boolean.FALSE, "");
    }
    public static Object GetImage(IMediaResource imediaresource, String resourcetype, String defaultImage){
        return GetImage(imediaresource, resourcetype, Boolean.FALSE, defaultImage);
    }
    public static Object GetImage(IMediaResource imediaresource, String resourcetype, Boolean originalSize){
        return GetImage(imediaresource, resourcetype, originalSize, "");
    }
    public static Object GetImage(IMediaResource imediaresource, String resourcetype, Boolean originalSize, String defaultImage){
        //return the default image passed in when none found or waiting for background processing from the queue
        LOG.debug("GetImage: imediaresource '" + imediaresource + "' resourcetype '" + resourcetype + "' defaultImage '" + defaultImage + "'");
        if (imediaresource == null) {
            LOG.debug("GetImage: imediaresource is NULL so returning NULL");
            return null;
        }

        Object tKey = BuildImageKey(imediaresource, resourcetype, originalSize, defaultImage);
        if (!IsValidKey(tKey)){
            //this must already be a Image Object or other return key
            return tKey;
        }
        return GetImageFromKey(tKey, defaultImage);
    }

    public static Object GetImageFromKey(Object tKey, String defaultImage){
        MediaArtifactType faArtifactType = GetTypeFromKey(tKey.toString());
        Object mediaObject = null;
        String tImageString = GetPathFromKey(tKey.toString());
        Object tImage = null;
        String ImageID = GetIDFromKey(tKey.toString());
        Boolean originalSize = GetOriginalSizeFromKey(tKey.toString());
        
        //see if we are caching or just returning an image
        if (UseCache(faArtifactType)){
            //see if the image is in the cache and if so return it
            mediaObject = ICache.get(tImageString);
            if (mediaObject!=null){
                LOG.debug("GetImage: found Image in Cache and return it based on '" + tImageString + "'");
                return mediaObject;
            }else{
                if (UseQueue(faArtifactType)){
                    //see if the item is already in the queue
                    if (IQueue.contains(tKey)){
                        LOG.debug("GetImage: already in the Queue '" + tImageString + "' defaultImage returned '" + defaultImage + "'");
                        return defaultImage;
                    }else{
                        //add the imagestring to the queue for background processing later
                        IQueue.add(tKey);
                        LOG.debug("GetImage: adding to Queue '" + tImageString + "' defaultImage returned '" + defaultImage + "'");
                        return defaultImage;
                    }
                }else{
                    //get the image and add it to the cache then return it
                    tImage = CreateImage(tImageString, faArtifactType, originalSize, ImageID);
                    ICache.put(tImageString, tImage);
                    LOG.debug("GetImage: adding to Cache '" + tImageString + "'");
                    return tImage;
                }
            }
        }else{
            //get the image and return it
            tImage = CreateImage(tImageString, faArtifactType, originalSize, ImageID);
            LOG.debug("GetImage: cache off so returning image for '" + tImageString + "'");
            return tImage;
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
        if (imediaresource == null) {
            return null;
        }
        LOG.debug("GetImage: Convenience method called with Class = '" + imediaresource.getClass() + "'");
        IMediaResource proxy = phoenix.media.GetMediaResource(imediaresource);
        if (proxy==null) {
            LOG.debug("GetImage: GetMediaResource failed to convert '" + imediaresource + "'");
            return null; // do nothing
        }
        return GetImage(proxy, resourcetype, originalSize, defaultImage);
    }

    public static Object BuildImageKey(IMediaResource imediaresource, String resourcetype, Boolean originalSize, String defaultImage){
        resourcetype = resourcetype.toLowerCase();
        Object tImage = null;
        Object mediaObject = null;
        String tImageString = "";
        IMediaResource childmediaresource = null;
        String Grouping = "NoGroup";
        Object faMediaObject = null;
        MediaType faMediaType = null;
        String faMediaTitle = null;
        MediaArtifactType faArtifactType = ConvertStringtoMediaArtifactType(resourcetype);
        String faArtifiactTitle = null;
        Map<String,String> faMetadata = null;
        
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
                    LOG.debug("GetImage: TV show found '" + phoenix.media.GetTitle(imediaresource) + "' using Series Fanart");
                    //use Series type fanart
                    faMediaObject = phoenix.media.GetMediaObject(childmediaresource);
                    faMetadata = Collections.emptyMap();
                    faMediaType = MediaType.TV;
                }else{
                    LOG.debug("GetImage: Other show found '" + phoenix.media.GetTitle(imediaresource) + "' using Child for Fanart");
                    //use a child for the show fanart
                    faMediaObject = phoenix.media.GetMediaObject(childmediaresource);
                }
            }else if (Grouping.equals("genre")){
                LOG.debug("GetImage: genre group found '" + phoenix.media.GetTitle(imediaresource) + "' using Child for Fanart");
                faMediaObject = phoenix.media.GetMediaObject(childmediaresource);
                //TODO:SPECIAL handling to get GENRE images
                
            }else if (Grouping.equals("season")){
                LOG.debug("GetImage: season group found '" + phoenix.media.GetTitle(imediaresource) + "' using Child for Fanart");
                //just use a child item so you get fanart for the specific season
                faMediaObject = phoenix.media.GetMediaObject(childmediaresource);
            }else if (Grouping.equals("NoGroup")){
                LOG.debug("GetImage: Folder found but no grouping for '" + phoenix.media.GetTitle(imediaresource) + "' using passed in object for Fanart");
                faMediaObject = phoenix.media.GetMediaObject(imediaresource);
            }else{
                LOG.debug("GetImage: unhandled grouping found '" + Grouping + "' for Title '" + phoenix.media.GetTitle(imediaresource) + "' using Child for Fanart");
                faMediaObject = phoenix.media.GetMediaObject(childmediaresource);
            }
        }else{
            //not a FOLDER
            if (phoenix.media.IsMediaType( imediaresource , "TV" )){
                //for TV items we need to get an Episode Fanart
                //no need to cache these so just return the image object
                faMediaObject = phoenix.media.GetMediaObject(imediaresource);
                tImageString = phoenix.fanart.GetEpisode(faMediaObject);
                if (tImageString==null || tImageString.equals("")){
                    LOG.debug("GetImage: Episode '" + phoenix.media.GetTitle(imediaresource) + "' returning Fanart based on GetDefaultEpisode");
                    return phoenix.fanart.GetDefaultEpisode(faMediaObject);
                }else{
                    LOG.debug("GetImage: Episode '" + phoenix.media.GetTitle(imediaresource) + "' Fanart found '" + tImageString + "'");
                }
            }else{
                faMediaObject = phoenix.media.GetMediaObject(imediaresource);
                //faMediaType = MediaType.MOVIE;
                LOG.debug("GetImage: Title '" + phoenix.media.GetTitle(imediaresource) + "' using passed in object for Fanart");
            }
                
        }
        
        if (tImageString.equals("")){
            String tMediaType = null;
            if (faMediaType!=null){
                tMediaType = faMediaType.toString();
            }
            tImageString = phoenix.fanart.GetFanartArtifact(faMediaObject, tMediaType, faMediaTitle, faArtifactType.toString(), faArtifiactTitle, faMetadata);
            LOG.debug("GetImage: GetFanartArtifact returned '" + tImageString + "'");
        }
        if (tImageString==null || tImageString.equals("")){
            LOG.debug("GetImage: tImageString blank or NULL so returning defaultImage");
            return defaultImage;
        }
        String ImageID = phoenix.fanart.ImageKey(faMediaObject, faMediaType, faMediaTitle, faArtifactType, faArtifiactTitle, faMetadata);
        LOG.debug("GetImage: ImageID created '" + ImageID + "'");
        String tKey = GetQueueKey(tImageString, faArtifactType, originalSize, ImageID);
        return tKey;
    }
    
//    public static Object GetImage(IMediaResource imediaresource, String resourcetype, Boolean originalSize, String defaultImage){
//        //return the default image passed in when none found or waiting for background processing from the queue
//        LOG.debug("GetImage: imediaresource '" + imediaresource + "' resourcetype '" + resourcetype + "' defaultImage '" + defaultImage + "'");
//        if (imediaresource == null) {
//            LOG.debug("GetImage: imediaresource is NULL so returning NULL");
//            return null;
//        }
//        resourcetype = resourcetype.toLowerCase();
//        Object tImage = null;
//        Object mediaObject = null;
//        String tImageString = "";
//        IMediaResource childmediaresource = null;
//        String Grouping = "NoGroup";
//        Object faMediaObject = null;
//        MediaType faMediaType = null;
//        String faMediaTitle = null;
//        MediaArtifactType faArtifactType = ConvertStringtoMediaArtifactType(resourcetype);
//        String faArtifiactTitle = null;
//        Map<String,String> faMetadata = null;
//        
//        //see if this is a FOLDER item
//        //we will need a MediaObject to get any fanart so get it from the passed in resource OR the child if any
//        if (phoenix.media.IsMediaType( imediaresource , "FOLDER" )){
//            ViewFolder Folder = (ViewFolder) imediaresource;
//            ViewFolder Parent = (ViewFolder) phoenix.media.GetParent(imediaresource);
//            //get a child item (if any) from the Folder
//            if (phoenix.media.GetAllChildren(Folder, 1).size()>0){
//                Integer RandomElement = phoenix.util.GetRandomNumber(phoenix.media.GetAllChildren(Folder).size());
//                childmediaresource = (IMediaResource) phoenix.media.GetAllChildren(Folder).get(RandomElement);
//            }
//            //see how the folder is grouped
//            if (phoenix.umb.GetGroupers(Parent).size() > 0){
//                Grouping = phoenix.umb.GetName( phoenix.umb.GetGroupers(Parent).get(0) );
//                //tImageString = 
//                //"genre" - get genre specific images
//                //"season" - for banners or posters get Season Specific ones if available
//                //"show" - get the first item in the group and use it for the image
//                //else - get the first item in the group and use it for the image
//            }
//            if (Grouping.equals("show")){
//                //need to know if this is a TV show grouping to get a Series fanart item
//                if (phoenix.media.IsMediaType( childmediaresource , "TV" )){
//                    LOG.debug("GetImage: TV show found '" + phoenix.media.GetTitle(imediaresource) + "' using Series Fanart");
//                    //use Series type fanart
//                    faMediaObject = phoenix.media.GetMediaObject(childmediaresource);
//                    faMetadata = Collections.emptyMap();
//                    faMediaType = MediaType.TV;
//                }else{
//                    LOG.debug("GetImage: Other show found '" + phoenix.media.GetTitle(imediaresource) + "' using Child for Fanart");
//                    //use a child for the show fanart
//                    faMediaObject = phoenix.media.GetMediaObject(childmediaresource);
//                }
//            }else if (Grouping.equals("genre")){
//                LOG.debug("GetImage: genre group found '" + phoenix.media.GetTitle(imediaresource) + "' using Child for Fanart");
//                faMediaObject = phoenix.media.GetMediaObject(childmediaresource);
//                //TODO:SPECIAL handling to get GENRE images
//                
//            }else if (Grouping.equals("season")){
//                LOG.debug("GetImage: season group found '" + phoenix.media.GetTitle(imediaresource) + "' using Child for Fanart");
//                //just use a child item so you get fanart for the specific season
//                faMediaObject = phoenix.media.GetMediaObject(childmediaresource);
//            }else if (Grouping.equals("NoGroup")){
//                LOG.debug("GetImage: Folder found but no grouping for '" + phoenix.media.GetTitle(imediaresource) + "' using passed in object for Fanart");
//                faMediaObject = phoenix.media.GetMediaObject(imediaresource);
//            }else{
//                LOG.debug("GetImage: unhandled grouping found '" + Grouping + "' for Title '" + phoenix.media.GetTitle(imediaresource) + "' using Child for Fanart");
//                faMediaObject = phoenix.media.GetMediaObject(childmediaresource);
//            }
//        }else{
//            //not a FOLDER
//            if (phoenix.media.IsMediaType( imediaresource , "TV" )){
//                //for TV items we need to get an Episode Fanart
//                //no need to cache these so just return the image object
//                faMediaObject = phoenix.media.GetMediaObject(imediaresource);
//                tImageString = phoenix.fanart.GetEpisode(faMediaObject);
//                if (tImageString==null || tImageString.equals("")){
//                    LOG.debug("GetImage: Episode '" + phoenix.media.GetTitle(imediaresource) + "' returning Fanart based on GetDefaultEpisode");
//                    return phoenix.fanart.GetDefaultEpisode(faMediaObject);
//                }else{
//                    LOG.debug("GetImage: Episode '" + phoenix.media.GetTitle(imediaresource) + "' Fanart found '" + tImageString + "'");
//                }
//            }else{
//                faMediaObject = phoenix.media.GetMediaObject(imediaresource);
//                //faMediaType = MediaType.MOVIE;
//                LOG.debug("GetImage: Title '" + phoenix.media.GetTitle(imediaresource) + "' using passed in object for Fanart");
//            }
//                
//        }
//        
//        if (tImageString.equals("")){
//            String tMediaType = null;
//            if (faMediaType!=null){
//                tMediaType = faMediaType.toString();
//            }
//            tImageString = phoenix.fanart.GetFanartArtifact(faMediaObject, tMediaType, faMediaTitle, faArtifactType.toString(), faArtifiactTitle, faMetadata);
//            LOG.debug("GetImage: GetFanartArtifact returned '" + tImageString + "'");
//        }
//        if (tImageString==null || tImageString.equals("")){
//            LOG.debug("GetImage: tImageString blank or NULL so returning defaultImage");
//            return defaultImage;
//        }
//        String ImageID = phoenix.fanart.ImageKey(faMediaObject, faMediaType, faMediaTitle, faArtifactType, faArtifiactTitle, faMetadata);
//        LOG.debug("GetImage: ImageID created '" + ImageID + "'");
//        String tKey = GetQueueKey(tImageString, faArtifactType, originalSize, ImageID);
//        
//        //see if we are caching or just returning an image
//        if (UseCache(faArtifactType)){
//            //see if the image is in the cache and if so return it
//            mediaObject = ICache.get(tImageString);
//            if (mediaObject!=null){
//                LOG.debug("GetImage: found Image in Cache and return it based on '" + tImageString + "'");
//                return mediaObject;
//            }else{
//                if (UseQueue(faArtifactType)){
//                    //see if the item is already in the queue
//                    if (IQueue.contains(tKey)){
//                        LOG.debug("GetImage: already in the Queue '" + tImageString + "' defaultImage returned '" + defaultImage + "'");
//                        return defaultImage;
//                    }else{
//                        //add the imagestring to the queue for background processing later
//                        IQueue.add(tKey);
//                        LOG.debug("GetImage: adding to Queue '" + tImageString + "' defaultImage returned '" + defaultImage + "'");
//                        return defaultImage;
//                    }
//                }else{
//                    //get the image and add it to the cache then return it
//                    tImage = CreateImage(tImageString, faArtifactType, originalSize, ImageID);
//                    ICache.put(tImageString, tImage);
//                    LOG.debug("GetImage: adding to Cache '" + tImageString + "'");
//                    return tImage;
//                }
//            }
//        }else{
//            //get the image and return it
//            tImage = CreateImage(tImageString, faArtifactType, originalSize, ImageID);
//            LOG.debug("GetImage: cache off so returning image for '" + tImageString + "'");
//            return tImage;
//        }
//    }
    
    public static void GetImageFromQueue(){
        if (IQueue.size()>0){
            UIContext UIc = new UIContext(sagex.api.Global.GetUIContextName());
            String tItem = IQueue.pop().toString();
            String tImageString = GetPathFromKey(tItem);
            MediaArtifactType resourcetype = GetTypeFromKey(tItem);
            Boolean originalSize = GetOriginalSizeFromKey(tItem);
            String ImageID = GetIDFromKey(tItem);
            //get the image and add it to the cache then return it
            Object tImage = CreateImage(tImageString, resourcetype, originalSize, ImageID);
            sagex.api.Global.RefreshAreaForVariable(UIc, "PreloadTag", tItem);
            //RefreshAreaForVariable("PreloadTag",CurPreloadItem)
            ICache.put(tImageString, tImage);
            LOG.debug("GetImageFromQueue: remaining(" + IQueue.size() + ") adding to Cache '" + tImageString + "'");
        }else{
            LOG.debug("GetImageFromQueue: EMPTY QUEUE");
        }
    }
    
    public static Integer GetQueueSize(){
        LOG.debug("GetQueueSize: '" + IQueue.size() + "'");
        return IQueue.size();
    }
    
    private static String GetQueueKey(String ImageString, MediaArtifactType ImageType, Boolean originalSize, String ImageID){
        return CreateImageTag + util.ListToken + ImageString + util.ListToken + ImageType + util.ListToken + originalSize.toString() + util.ListToken + ImageID;
    }
    
    private static MediaArtifactType ConvertStringtoMediaArtifactType(String ImageType){
        if (ImageType.equals("poster")){
            return MediaArtifactType.POSTER;
        }else if (ImageType.equals("banner")){
            return MediaArtifactType.BANNER;
        }else if (ImageType.equals("background")){
            return MediaArtifactType.BACKGROUND;
        }else{
            return MediaArtifactType.POSTER;
        }
    }
    
    private static Boolean IsValidKey(Object Key){
        List<String> tList = util.ConvertStringtoList(Key.toString());
        if (tList.size()>0){
            if (tList.get(0).equals(CreateImageTag)){
                return Boolean.TRUE;
            }else{
                return Boolean.FALSE;
            }
        }else{
            return Boolean.FALSE;
        }
    }
    private static String GetPathFromKey(String Key){
        List<String> tList = util.ConvertStringtoList(Key);
        if (tList.size()>1){
            return tList.get(1);
        }else{
            return "";
        }
    }
    private static MediaArtifactType GetTypeFromKey(String Key){
        List<String> tList = util.ConvertStringtoList(Key);
        if (tList.size()>2){
            return ConvertStringtoMediaArtifactType(tList.get(2));
        }else{
            return MediaArtifactType.POSTER;
        }
    }
    private static Boolean GetOriginalSizeFromKey(String Key){
        List<String> tList = util.ConvertStringtoList(Key);
        if (tList.size()>3){
            return Boolean.parseBoolean(tList.get(3));
        }else{
            return Boolean.FALSE;
        }
    }
    private static String GetIDFromKey(String Key){
        List<String> tList = util.ConvertStringtoList(Key);
        if (tList.size()>4){
            return tList.get(4);
        }else{
            return "";
        }
    }

    public static Object CreateImage(String ImageString, MediaArtifactType ImageType, Boolean originalSize, String ImageID){
        if (ImageString.equals("")){
            return null;
        }
        Object ThisImage = null;
        //See if the image is already cached in the filesystem by a previous CreateImage call
        ThisImage = phoenix.image.GetImage(ImageID, CreateImageTag);
        if (ThisImage!=null){
            LOG.debug("CreateImage: Filesystem cached item found for Tag '" + CreateImageTag + "' ID '" + ImageID + "'");
            return ThisImage;
        }
        
        UIContext UIc = new UIContext(sagex.api.Global.GetUIContextName());
        //based on the ImageType determine the scalewidth to use
        Integer UIWidth = sagex.api.Global.GetFullUIWidth(UIc);
        Double scalewidth = 0.2;
        if (originalSize){
            scalewidth = 1.0;
        }else{
            if (ImageType.equals(MediaArtifactType.POSTER)){
                scalewidth = 0.2;
            }else if (ImageType.equals(MediaArtifactType.BANNER)){
                scalewidth = 0.6;
            }else if (ImageType.equals(MediaArtifactType.BACKGROUND)){
                scalewidth = 0.4;
            }else{
                //use default
            }
        }
        Double finalscalewidth = scalewidth * UIWidth;
        try {
            ThisImage = phoenix.image.CreateImage(ImageID, CreateImageTag, ImageString, "{name: scale, width: " + finalscalewidth + ", height: -1}", false);
        } catch (Exception e) {
            LOG.debug("CreateImage: phoenix.image.CreateImage FAILED using LoagImage(loadImage)) - scalewidth = '" + scalewidth + "' UIWidth = '" + UIWidth + "' finalscalewidth = '" + finalscalewidth + "' for Type = '" + ImageType + "' Image = '" + ImageString + "' Error: '" + e + "'");
            return null;
        }
        if (!sagex.api.Utility.IsImageLoaded(UIc, ThisImage)){
            LOG.debug("CreateImage: Loaded using LoagImage(loadImage)) - scalewidth = '" + scalewidth + "' UIWidth = '" + UIWidth + "' finalscalewidth = '" + finalscalewidth + "' for Type = '" + ImageType + "' Image = '" + ImageString + "'");
            sagex.api.Utility.LoadImage(UIc, sagex.api.Utility.LoadImage(UIc, ThisImage));
        }else{
            LOG.debug("CreateImage: already Loaded - scalewidth = '" + scalewidth + "' UIWidth = '" + UIWidth + "' finalscalewidth = '" + finalscalewidth + "' for Type = '" + ImageType + "' Image = '" + ImageString + "'");
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
