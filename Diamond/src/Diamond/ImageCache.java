/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Diamond;

import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import sagex.UIContext;
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
    private static SoftHashMap ICache = null;
    public static enum ImageCacheTypes{OFF,BACKGROUND,NOQUEUE,BYIMAGETYPE};
    private static final String ImageCacheTypesList = ImageCacheTypes.OFF + util.ListToken + ImageCacheTypes.BACKGROUND + util.ListToken + ImageCacheTypes.NOQUEUE + util.ListToken + ImageCacheTypes.BYIMAGETYPE;
    private static final String ImageCacheTypesListByImageType = ImageCacheTypes.OFF + util.ListToken + ImageCacheTypes.BACKGROUND + util.ListToken + ImageCacheTypes.NOQUEUE;
    
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
    public static Object GetImage(IMediaResource imediaresource, String resourcetype, String defaultImage){
        //return the default image passed in when none found or waiting for background processing from the queue
        LOG.debug("GetImage: imediaresource '" + imediaresource + "' resourcetype '" + resourcetype + "' defaultImage '" + defaultImage + "'");
        if (imediaresource == null) {
            LOG.debug("GetImage: imediaresource is NULL so returning NULL");
            return null;
        }
        resourcetype = resourcetype.toLowerCase();
        Object tImage = null;
        Object mediaObject = null;
        String tImageString = "";
        IMediaResource childmediaresource = null;
        String Grouping = "NoGroup";

        //see if this is a FOLDER item
        if (phoenix.media.IsMediaType( imediaresource , "FOLDER" )){
            ViewFolder Folder = (ViewFolder) imediaresource;
            LOG.debug("GetImage: FOLDER found");
            //get the first child item if any from the Folder
            if (phoenix.media.GetAllChildren(Folder, 1).size()>0){
                //TODO: may want to introduce some random selection of a child record here!!!
                childmediaresource = (IMediaResource) phoenix.media.GetAllChildren(Folder, 1).get(0);
            }
            //see how the folder is grouped
            if (phoenix.umb.GetGroupers(Folder).size() > 0){
                Grouping = phoenix.umb.GetName( phoenix.umb.GetGroupers(Folder).get(0) );
                //tImageString = 
                //"genre" - get genre specific images
                //"season" - for banners or posters get Season Specific ones if available
                //"show" - get the first item in the group and use it for the image
                //else - get the first item in the group and use it for the image
            }
        }
        LOG.debug("GetImage: Grouping = '" + Grouping + "'");
        //we will need a MediaObject to get any fanart so get it from the passed in resource OR the child if any
        if (childmediaresource!=null){
            mediaObject = phoenix.media.GetMediaObject(childmediaresource);
            LOG.debug("GetImage: mediaObject set using child");
        }else{
            mediaObject = phoenix.media.GetMediaObject(imediaresource);
            LOG.debug("GetImage: mediaObject set using imediaresource");
        }
        
        if (tImageString.equals("")){
            tImageString = phoenix.fanart.GetFanartArtifact(mediaObject, null, null, resourcetype, null, null);
            LOG.debug("GetImage: GetFanartArtifact returned '" + tImageString + "'");
        }
        if (tImageString==null || tImageString.equals("")){
            LOG.debug("GetImage: tImageString blank or NULL so returning defaultImage");
            return defaultImage;
        }
        //see if we are caching or just returning an image
        if (UseCache(resourcetype)){
            //see if the image is in the cache and if so return it
            if (ICache.containsKey(tImageString)){
                LOG.debug("GetImage: found Image in Cache and return it based on '" + tImageString + "'");
                return ICache.get(tImageString);
            }else{
                if (UseQueue(resourcetype)){
                    //add the imagestring to the queue for background processing later
                    IQueue.add(GetQueueKey(tImageString, resourcetype));
                    LOG.debug("GetImage: adding to Queue '" + tImageString + "' defaultImage returned '" + defaultImage + "'");
                    return defaultImage;
                }else{
                    //get the image and add it to the cache then return it
                    tImage = CreateImage(tImageString, resourcetype);
                    ICache.put(tImageString, tImage);
                    LOG.debug("GetImage: adding to Cache '" + tImageString + "'");
                    return tImage;
                }
            }
        }else{
            //get the image and return it
            tImage = CreateImage(tImageString, resourcetype);
            LOG.debug("GetImage: cache off so returning image for '" + tImageString + "'");
            return tImage;
        }
    }
    //Convenience method that will convert the incoming object parameter to a IMediaResource type 
    public static Object GetImage(Object imediaresource, String resourcetype){
        if (imediaresource == null) {
            return null;
        }
        IMediaResource proxy = phoenix.media.GetMediaResource(imediaresource);
        if (proxy==null) {
            return null; // do nothing
        }
        return GetImage(proxy, resourcetype);
    }
    
    public static void GetImageFromQueue(){
        if (IQueue.size()>0){
            String tItem = IQueue.pop().toString();
            String tImageString = GetPathFromKey(tItem);
            String resourcetype = GetTypeFromKey(tItem);
            //get the image and add it to the cache then return it
            Object tImage = CreateImage(tImageString, resourcetype);
            ICache.put(tImageString, tImage);
            LOG.debug("GetImageFromQueue: adding to Cache '" + tImageString + "'");
        }
    }
    
    private static String GetQueueKey(String ImageString, String ImageType){
        return ImageString + util.ListToken + ImageType;
    }
    
    private static String GetPathFromKey(String Key){
        List<String> tList = util.ConvertStringtoList(Key);
        if (tList.size()>0){
            return tList.get(0);
        }else{
            return "";
        }
    }
    private static String GetTypeFromKey(String Key){
        List<String> tList = util.ConvertStringtoList(Key);
        if (tList.size()>1){
            return tList.get(1);
        }else{
            return "poster";
        }
    }

    public static Object CreateImage(String ImageString, String ImageType){
        if (ImageString.equals("")){
            return null;
        }
        UIContext UIc = new UIContext(sagex.api.Global.GetUIContextName());
        //based on the ImageType determine the scalewidth to use
        Integer UIWidth = sagex.api.Global.GetFullUIWidth(UIc);
        Double scalewidth = 0.2;
        if (ImageType.equals("poster")){
            scalewidth = 0.2;
        }else if (ImageType.equals("banner")){
            scalewidth = 0.6;
        }else if (ImageType.equals("background")){
            scalewidth = 0.4;
        }else{
            //use default
        }
        Double finalscalewidth = scalewidth * UIWidth;
        Object ThisImage = phoenix.image.CreateImage("gemstone-"+ImageType, ImageString, "{name: scale, width: " + finalscalewidth + ", height: -1}", false);
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
        return util.GetListOptionName(ICacheProps, Const.ImageCacheType, ImageCacheTypesList, ImageCacheTypes.OFF.toString());
    }
    public static void SetCacheTypeNext(){
        util.SetListOptionNext(ICacheProps, Const.ImageCacheType, ImageCacheTypesList);
    }

    public static String GetCacheType(String ImageType){
        if (GetCacheType().equals(ImageCacheTypes.BYIMAGETYPE.toString())){
            String tProp = ICacheProps + Const.PropDivider + Const.ImageCacheType;
            return util.GetListOptionName(tProp, ImageType, ImageCacheTypesListByImageType, ImageCacheTypes.OFF.toString());
        }else{
            return GetCacheType();
        }
    }
    public static void SetCacheTypeNext(String ImageType){
        if (GetCacheType().equals(ImageCacheTypes.BYIMAGETYPE.toString())){
            String tProp = ICacheProps + Const.PropDivider + Const.ImageCacheType;
            util.SetListOptionNext(tProp, ImageType, ImageCacheTypesListByImageType);
        }
    }
    
    public static Boolean UseQueue(String ImageType){
        if (GetCacheType(ImageType).equals(ImageCacheTypes.BACKGROUND.toString())){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }

    public static Boolean UseCache(String ImageType){
        if (GetCacheType(ImageType).equals(ImageCacheTypes.OFF.toString())){
            return Boolean.FALSE;
        }else{
            return Boolean.TRUE;
        }
    }
    
}
