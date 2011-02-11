/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sagediamond;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author SBANTA
 */
public class util {

        public static Object CheckSeasonSize(Map<String,Object> Files){
    LinkedHashMap<String,Object> WithBlanks=new LinkedHashMap<String,Object>();
    WithBlanks.put("b1", null);
    WithBlanks.put("b2",null);
    WithBlanks.putAll(Files);
    WithBlanks.put("b3",null);
    WithBlanks.put("b4",null);
    return WithBlanks;

    }

        public static Object CheckFileSize(List<Object> files,String diamondprop){
        int viewtype = CustomViews.GetViewStyle(diamondprop);
        ArrayList<Object> NewList = new ArrayList<Object>();
        if(viewtype==1&&files.size()<5){

        }
        else if(viewtype==3&&files.size()<7){
        NewList.add(null);
        NewList.add(null);
        NewList.add(null);
        NewList.addAll(files);
        NewList.add(null);
        NewList.add(null);
        return NewList;}


        return files;
        }

}
