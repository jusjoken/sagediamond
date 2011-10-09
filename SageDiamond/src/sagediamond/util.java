/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sagediamond;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author SBANTA
 * @author JUSJOKEN
 * - 10/09/2011 - added LOG4J setup and Main method for testing, and StringNumberFormat
 */
public class util {

    static private final Logger LOG = Logger.getLogger(util.class);
    
    public static void main(String[] args){

        //String test = StringNumberFormat("27.96903", 0, 2);
        String test = StringNumberFormat("27.1", 0, 2);
        LOG.debug(test);
    }

    //pass in a String that contains a number and this will format it to a specific number of decimal places
    public static String StringNumberFormat(String Input, Integer DecimalPlaces){
        return StringNumberFormat(Input, DecimalPlaces, DecimalPlaces);
    }
    public static String StringNumberFormat(String Input, Integer MinDecimalPlaces, Integer MaxDecimalPlaces){
        float a = 0;
        try {
            a = Float.parseFloat(Input);
        } catch (NumberFormatException nfe) {
            LOG.error("StringNumberFormat - NumberFormatException for '" + Input + "'");
            return Input;
        }
        NumberFormat df = DecimalFormat.getInstance();
        df.setMinimumFractionDigits(MinDecimalPlaces);
        df.setMaximumFractionDigits(MaxDecimalPlaces);
        df.setRoundingMode(RoundingMode.DOWN);   
        String retString = df.format(a);
        return retString;
    }
    

    public static Object CheckSeasonSize(Map<String, Object> Files, int sizeneeded) {
        LinkedHashMap<String, Object> WithBlanks = new LinkedHashMap<String, Object>();
        for(int index=0;index<sizeneeded;index++){
        WithBlanks.put("blankelement1"+index, null);}
        
        WithBlanks.putAll(Files);
        for(int index=sizeneeded;index<sizeneeded+sizeneeded;index++){
        WithBlanks.put("blankelement"+index, null);}

        return WithBlanks;

    }

    public static Object CheckCategorySize(Map<String, Object> Files) {
        LinkedHashMap<String, Object> WithBlanks = new LinkedHashMap<String, Object>();
        WithBlanks.put("blankelement1", null);
        WithBlanks.putAll(Files);
        WithBlanks.put("blankelement4", null);


        return WithBlanks;

    }

    public static Object CheckSimpleSize(Object[] Files,int sizeneeded) {
        if (!Files.toString().contains("blankelement")) {

            List<Object> WithBlanks = new ArrayList<Object>();
            for(int index=0;index<sizeneeded;index++){
            WithBlanks.add("blankelement"+index);}


            for (Object curr : Files) {
                WithBlanks.add(curr);
            }
            for(int index=sizeneeded;index<sizeneeded+sizeneeded;index++){
            WithBlanks.add("blankelement"+index);}
            
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


