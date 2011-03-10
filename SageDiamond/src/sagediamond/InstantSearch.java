/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sagediamond;

import java.util.ArrayList;

/**
 *
 * @author SBANTA
 */
public class InstantSearch {


    public static Object GetInstantSearch(boolean keyboard,String SearchKeys,Object MediaFiles){
     Object[] files=FanartCaching.toArray(MediaFiles);
        if(!keyboard){
        SearchKeys=phoenix.api.CreateRegexFromKeypad(SearchKeys);}
    ArrayList<Object> matches= new ArrayList<Object>();
    for(Object curr:files){
    String Title=MetadataCalls.GetMediaTitle(curr);
    if(Title.contains(SearchKeys)){
    matches.add(curr);}}
    return matches;
    }

}
