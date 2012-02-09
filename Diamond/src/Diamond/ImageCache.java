/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Diamond;

import java.util.LinkedList;
import sagex.phoenix.metadata.MediaArtifactType;
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
    // - Cache - SoftHashMap - this is the Cache itself
    //   - MinCacheItems - keep this number of items in the Cache when Memory is cleaned
    // - Queue - HashMap - list of MediaObjects to get a specific ImageType to add to the Cache
    // - 
    //Global Functions/Variables
    // - CacheType - Background - items get added to Queue for background processing
    // - CacheType - NoQueue - items get added to the Cache and returned
    // - CacheType - Off - items get returned and NOT added to Queue NOR Cache
    // - CacheType - ByImageType - 
    private static final String ICacheProps = Const.BaseProp + Const.PropDivider + Const.ImageCacheProp;
    private static LinkedList IQueue = new LinkedList();
    private static SoftHashMap ICache = new SoftHashMap(GetMinSize());
    
    //Initialize the Cache and the Queue
    public static void Init(){
        Clear();
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
    public static Object GetImage(IMediaResource imediaresource, String resourcetype, String defaultImage, ViewFolder Folder){
        //return the default image passed in when none found or waiting for background processing from the queue
        if (imediaresource == null) {
            return null;
        }
        Object tImage = null;
        String tImageString = "";

        //see if this is a FOLDER item - if so use a child and adjust image source depending on grouping
        if (phoenix.media.IsMediaType( imediaresource , "FOLDER" ) && Folder!=null){
            if (Folder!=null){
                if (phoenix.umb.GetGroupers(Folder).size() > 0){
                    String thisGroup = phoenix.umb.GetName( phoenix.umb.GetGroupers(Folder).get(0) );
                    //tImageString = 
                    //"genre" - get genre specific images
                    //"season" - for banners or posters get Season Specific ones if available
                }
            }
            if (tImageString.equals("")){
                //"show" - get the first item in the group and use it for the image
                //else - get the first item in the group and use it for the image
            }
            
        }
        
        Object mediaObject = phoenix.media.GetMediaObject(imediaresource);
        tImageString = phoenix.fanart.GetFanartArtifact(mediaObject, null, null, resourcetype, null, null);
        if (tImageString.equals("")){
            return tImage;
        }
        
        return tImage;
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
    
    public static Integer GetMinSize(){
        String tProp = ICacheProps + Const.PropDivider + Const.ImageCacheMinSize;
        return util.GetPropertyAsInteger(tProp, 100);
    }
    public static void SetMinSize(Integer Value){
        String tProp = ICacheProps + Const.PropDivider + Const.ImageCacheMinSize;
        util.SetProperty(tProp, Value.toString());
    }
    
}
