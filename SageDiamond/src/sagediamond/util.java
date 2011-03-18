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



    public static Object CheckSeasonSize(Map<String, Object> Files) {
        LinkedHashMap<String, Object> WithBlanks = new LinkedHashMap<String, Object>();
        WithBlanks.put("blankelement1", null);
        WithBlanks.put("blankelement2", null);
        WithBlanks.putAll(Files);
        WithBlanks.put("blankelement3", null);
        WithBlanks.put("blankelement4", null);


        return WithBlanks;

    }

    public static Object CheckCategorySize(Map<String, Object> Files) {
        LinkedHashMap<String, Object> WithBlanks = new LinkedHashMap<String, Object>();
        WithBlanks.put("blankelement1", null);
        WithBlanks.putAll(Files);
        WithBlanks.put("blankelement4", null);


        return WithBlanks;

    }

    public static Object CheckSimpleSize(Object[] Files) {
        if (!Files.toString().contains("blankelement")) {

            List<Object> WithBlanks = new ArrayList<Object>();
            WithBlanks.add("blankelement1");
            WithBlanks.add("blankelement2");
            for (Object curr : Files) {
                WithBlanks.add(curr);
            }
            WithBlanks.add("blankelement3");
            WithBlanks.add("blankelement4");
            return WithBlanks;
        }
        return Files;


    }

    public static Object CheckFileSize(List<Object> files, String diamondprop) {
        String viewtype = CustomViews.GetViewStyle(diamondprop);
        ArrayList<Object> NewList = new ArrayList<Object>();
        if (viewtype == "Wall Flow" && files.size() < 5) {
        } else if (viewtype == "Cover Flow" && files.size() < 7) {
            NewList.add(null);
            NewList.add(null);
            NewList.add(null);
            NewList.addAll(files);
            NewList.add(null);
            NewList.add(null);
            return NewList;
        }


        return files;
    }

    public static LinkedHashMap<String, Integer> GetLetterLocations(Object[] MediaFiles) {
        String CurrentLocation = "845948921";
        Boolean ScrapTitle = Boolean.parseBoolean(sagex.api.Configuration.GetProperty("ui/ignore_the_when_sorting", "false"));
        LinkedHashMap<String, Integer> Values = new LinkedHashMap<String, Integer>();
        String Title = "";
        int i = 0;

        for (Object curr : MediaFiles) {

            if (ScrapTitle) {
                Title = MetadataCalls.GetSortTitle(curr);
            } else {
                Title = MetadataCalls.GetMediaTitle(curr);
            }
            if (!Title.startsWith(CurrentLocation)) {

                CurrentLocation = Title.substring(0, 1);
                Values.put(CurrentLocation.toLowerCase(), i);
            }
            i++;


        }
        return Values;
    }

//    public static HashMap<Object,Object> GetHeaders(Object MediaFiles,String Method){
//    Object[] Files = FanartCaching.toArray(MediaFiles);
//    HashMap<Object,Object> Headers = new HashMap<Object,Object>();
//    Object CurrGroup=null;
//    if(Method.contains("AirDate")){
//    for(Object curr:Files){
//    Object thisvalue=GetTimeAdded(curr);
//    if(thisvalue!=CurrGroup){
//    Headers.put(curr,thisvalue);
//    CurrGroup=thisvalue;}}
//
//    }
//    else if (Method.contains("Title")){
//    for(Object curr:Files){
//    String thisvalues=ClassFromString.GetSortDividerClass(Method,curr).toString();
//    Object thisvalue=thisvalues.substring(0,1);
//    if(!thisvalue.equals(CurrGroup)){
//    Headers.put(curr,thisvalue);
//    CurrGroup=thisvalue;}
//    }
//
//    }
//    return Headers;}
    public static boolean IsHeader(Map headers, Object Key) {
        return headers.containsKey(Key);
    }
}
//         public static String GetTimeAdded(Object Title) {
//    // Check to see if date variables have been set
//    String PropertyPrefix= "JOrton/DateDividers";
//             if(!DateConverter.IsDateVariableSet){
//
//    System.out.println("DateVariablesNot set Go ahead and set them");
//    String First =sagex.api.Configuration.GetServerProperty(PropertyPrefix+"DateGroupFirstTime","10080");
//    String Second = sagex.api.Configuration.GetServerProperty(PropertyPrefix+"DateGroupSecondTime","20160");
//    String Third = sagex.api.Configuration.GetServerProperty(PropertyPrefix+"DateGroupThirdTime","43200");
//    String Fourth = sagex.api.Configuration.GetServerProperty("DateGroupFourthTime","86400");
//    Long LFirst = java.lang.Long.parseLong(First);
//    LFirst = LFirst *60*1000;
//    Long LSecond = java.lang.Long.parseLong(Second);
//    LSecond = LSecond*60*1000;
//    Long LThird = java.lang.Long.parseLong(Third);
//    LThird = LThird*60*1000;
//    Long LFourth = java.lang.Long.parseLong(Fourth);
//    LFourth = LFourth *60*1000;
//        Long CurrTime = System.currentTimeMillis();
//    DateConverter.FirstDateGroup =CurrTime-LFirst;
//    DateConverter.SecondDateGroup=CurrTime-LSecond;
//    DateConverter.ThirdDateGroup=CurrTime-LThird;
//    DateConverter.FourthDateGroup=CurrTime-LFourth;
//    DateConverter.FifthDateGroup =CurrTime-(Long.parseLong(sagex.api.Configuration.GetProperty(PropertyPrefix+"DateGroupFifthTime","86401"))*60*1000);
//    DateConverter.IsDateVariableSet=true;
//    System.out.println("DateVariables Set to="+DateConverter.FirstDateGroup+":");
//    System.out.println("DateVariables Set to="+DateConverter.SecondDateGroup+":");
//    System.out.println("DateVariables Set to="+DateConverter.ThirdDateGroup+":");
//    System.out.println("DateVariables Set to="+DateConverter.FourthDateGroup+":");
//    System.out.println("DateVariables Set to="+DateConverter.FifthDateGroup+":");}
//
//
//    Long DateAdded = DateConverter.;
//    if(DateAdded>=DateConverter.FirstDateGroup){
//    return sagex.api.Configuration.GetServerProperty(PropertyPrefix+"DateGroupFirstName","New");}
//    else if (DateAdded>=DateConverter.SecondDateGroup){
//    return sagex.api.Configuration.GetServerProperty(PropertyPrefix+"DateGroupSecondName","Last Week");}
//    else if (DateAdded>=DateConverter.ThirdDateGroup){
//    return sagex.api.Configuration.GetServerProperty(PropertyPrefix+"DateGroupThirdName","30 days");}
//   else if (DateAdded>=DateConverter.FourthDateGroup){
//    return sagex.api.Configuration.GetServerProperty(PropertyPrefix+"DateGroupFourthName","60 days");}
//   else{
//
//    return sagex.api.Configuration.GetServerProperty(PropertyPrefix+"DateGroupFifthName","Older");}
//
//        }


