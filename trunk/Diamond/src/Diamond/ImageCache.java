/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Diamond;

/**
 *
 * @author jusjoken
 */
public class ImageCache {
    public ImageCache(){
        //a ImageCache object needs
        // - Cache - SoftHashMap - this is the Cache itself
        //   - MinCacheItems - keep this number of items in the Cache when Memory is cleaned
        // - Queue - HashMap - list of MediaObjects to get a specific ImageType to add to the Cache
        // - 
        
    }
    
    //Global Functions/Variables
    // - CacheType - Background - items get added to Queue for background processing
    // - CacheType - Immediate - items get added to the Cache and returned
    // - CacheType - Off - items get returned and NOT added to Queue NOR Cache
    
}
