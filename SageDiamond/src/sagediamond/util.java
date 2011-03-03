/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sagediamond;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author SBANTA
 */
public class util {

        public static Object CheckSeasonSize(Map<String,Object> Files){
    LinkedHashMap<String,Object> WithBlanks=new LinkedHashMap<String,Object>();
    WithBlanks.put("blankelement1", null);
    WithBlanks.put("blankelement2",null);
    WithBlanks.putAll(Files);
    WithBlanks.put("blankelement3",null);
    WithBlanks.put("blankelement4",null);


    return WithBlanks;

    }

    public static Object CheckCategorySize(Map<String,Object> Files){
    LinkedHashMap<String,Object> WithBlanks=new LinkedHashMap<String,Object>();
    WithBlanks.put("blankelement1", null);
    WithBlanks.putAll(Files);
    WithBlanks.put("blankelement4",null);


    return WithBlanks;

    }

    public static Object CheckSimpleSize(Object[] Files){
    if(!Files.toString().contains("blankelement")){

    List<Object> WithBlanks=new ArrayList<Object>();
    WithBlanks.add("blankelement1");
    WithBlanks.add("blankelement2");
    for (Object curr:Files){
    WithBlanks.add(curr);}
    WithBlanks.add("blankelement3");
    WithBlanks.add("blankelement4");
    return WithBlanks;}
    return Files;


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



        public static LinkedHashMap<String,Integer> GetLetterLocations(Object[] MediaFiles){
        String CurrentLocation="845948921";
        Boolean ScrapTitle=Boolean.parseBoolean(sagex.api.Configuration.GetProperty("ui/ignore_the_when_sorting","false"));
        LinkedHashMap<String,Integer> Values=new LinkedHashMap<String,Integer>();
        String Title="";
        int i=0;

        for(Object curr:MediaFiles){
         
        if(ScrapTitle){
        Title=MetadataCalls.GetSortTitle(curr);}
        else{
        Title=MetadataCalls.GetMediaTitle(curr);}
        if(!Title.startsWith(CurrentLocation)){

            CurrentLocation=Title.substring(0,1);
         Values.put(CurrentLocation.toLowerCase(),i);}
        i++;


        }
        return Values;
        }

}
