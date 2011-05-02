/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PloxeeTV;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author SBANTA
 */
public class GroupObject {


    private  HashMap<String,HashMap<String,Vector>> Shows;
    private  HashMap<String,Vector> HashShows;
    private Object Name;
    private File Fanart;
    private File Poster;

    /**
     * @return the Shows
     */
    public HashMap<String,HashMap<String,Vector>> getShows() {
        return Shows;
    }

    /**
     * @param Shows the Shows to set
     */
    public void setShows(HashMap<String,HashMap<String,Vector>> Shows) {
        this.Shows = Shows;
    }

    /**
     * @return the Name
     */
    public Object getName() {
        return Name;
    }

    /**
     * @param Name the Name to set
     */
    public void setName(Object Name) {
        this.Name = Name;
    }

    /**
     * @return the Fanart
     */
    public File getFanart() {
        return Fanart;
    }

    /**
     * @param Fanart the Fanart to set
     */
    public void setFanart(File Fanart) {
        this.Fanart = Fanart;
    }

    /**
     * @return the Poster
     */
    public File getPoster() {
        return Poster;
    }

    /**
     * @param Poster the Poster to set
     */
    public void setPoster(File Poster) {
        this.Poster = Poster;
    }

    /**
     * @return the HashShows
     */
    public HashMap<String, Vector> getHashShows() {
        return HashShows;
    }

    /**
     * @param HashShows the HashShows to set
     */
    public void setHashShows(HashMap<String, Vector> HashShows) {
        this.HashShows = HashShows;
    }
    

}
