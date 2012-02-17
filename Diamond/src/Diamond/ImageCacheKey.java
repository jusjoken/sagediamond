/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Diamond;

import sagex.phoenix.metadata.MediaArtifactType;

/**
 *
 * @author jusjoken
 */
public class ImageCacheKey {

    private String ImagePath = "";
    private Boolean OriginalSize = Boolean.FALSE;
    private MediaArtifactType ArtifactType = null;
    private String ImageID = "";
    private Object DefaultEpisodeImage = null;
    private String defaultImage = null;
    private String RefreshArea = null;

    public ImageCacheKey() {
    }

    public ImageCacheKey(String ImagePath) {
        this.ImagePath = ImagePath;
    }
    
    public ImageCacheKey(String ImagePath, Boolean OriginalSize) {
        this.ImagePath = ImagePath;
        this.OriginalSize = OriginalSize;
    }
    public ImageCacheKey(String ImagePath, Boolean OriginalSize, MediaArtifactType ArtifactType) {
        this.ImagePath = ImagePath;
        this.OriginalSize = OriginalSize;
        this.ArtifactType = ArtifactType;
    }
    public ImageCacheKey(String ImagePath, Boolean OriginalSize, String ArtifactType) {
        this.ImagePath = ImagePath;
        this.OriginalSize = OriginalSize;
        this.ArtifactType = ConvertStringtoMediaArtifactType(ArtifactType);
    }
    public ImageCacheKey(String ImagePath, Boolean OriginalSize, MediaArtifactType ArtifactType, String ImageID) {
        this.ImagePath = ImagePath;
        this.OriginalSize = OriginalSize;
        this.ArtifactType = ArtifactType;
        this.ImageID = ImageID;
    }
    public ImageCacheKey(String ImagePath, Boolean OriginalSize, String ArtifactType, String ImageID) {
        this.ImagePath = ImagePath;
        this.OriginalSize = OriginalSize;
        this.ArtifactType = ConvertStringtoMediaArtifactType(ArtifactType);
        this.ImageID = ImageID;
    }

    @Override
    public String toString() {
        return "ImageCacheKey{" + "ImagePath=" + ImagePath + ", OriginalSize=" + OriginalSize + ", ArtifactType=" + ArtifactType + ", ImageID=" + ImageID + ", DefaultEpisodeImage=" + DefaultEpisodeImage + ", defaultImage=" + defaultImage + '}';
    }
    
    public String getKey(){
        return this.ImagePath + util.ListToken + this.OriginalSize.toString();
    }

    public MediaArtifactType getArtifactType() {
        return ArtifactType;
    }

    public void setArtifactType(MediaArtifactType ArtifactType) {
        this.ArtifactType = ArtifactType;
    }
    public void setArtifactType(String ArtifactType) {
        this.ArtifactType = ConvertStringtoMediaArtifactType(ArtifactType);
    }

    public String getImageID() {
        return ImageID;
    }

    public void setImageID(String ImageID) {
        this.ImageID = ImageID;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String ImagePath) {
        this.ImagePath = ImagePath;
    }

    public Boolean getOriginalSize() {
        return OriginalSize;
    }

    public void setOriginalSize(Boolean OriginalSize) {
        this.OriginalSize = OriginalSize;
    }

    public Object getDefaultEpisodeImage() {
        return DefaultEpisodeImage;
    }

    public void setDefaultEpisodeImage(Object DefaultEpisodeImage) {
        this.DefaultEpisodeImage = DefaultEpisodeImage;
    }

    public String getDefaultImage() {
        return defaultImage;
    }

    public void setDefaultImage(String defaultImage) {
        this.defaultImage = defaultImage;
    }

    public String getRefreshArea() {
        return RefreshArea;
    }

    public void setRefreshArea(String RefreshArea) {
        this.RefreshArea = RefreshArea;
    }
    
    public Boolean HasRefreshArea(){
        if (this.RefreshArea==null){
            return Boolean.FALSE;
        }else{
            return Boolean.TRUE;
        }
    }
    
    public Boolean IsValidKey(){
        if (this.ImagePath.isEmpty()){
            return Boolean.FALSE;
        }else{
            return Boolean.TRUE;
        }
    }
    
    public Boolean HasDefaultEpisodeImage(){
        if (this.DefaultEpisodeImage==null){
            return Boolean.FALSE;
        }else{
            return Boolean.TRUE;
        }
    }

    public static MediaArtifactType ConvertStringtoMediaArtifactType(String ImageType){
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
    
    
    
}
