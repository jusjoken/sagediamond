/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Diamond;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.log4j.Logger;
import sagex.UIContext;

/**
 *
 * @author SBANTA
 * @author JUSJOKEN
 * - 10/09/2011 - added LOG4J setup and Main method for testing, and StringNumberFormat
 * - 10/10/2011 - added some generic functions
 */
public class util {

    static private final Logger LOG = Logger.getLogger(util.class);
    public static final char[] symbols = new char[36];
    private static final Random random = new Random();
    public static final String OptionNotFound = "Option not Found";
    public static enum TriState{YES,NO,OTHER};
    public static final String ListToken = ":&&:";
    
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
        String viewtype = Flow.GetFlowType(diamondprop);
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

    public static String GenerateRandomName(){
        char[] buf = new char[10];
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return "sd" + new String(buf);
    }

    public static Boolean HasProperty(String Property){
        String tValue = sagex.api.Configuration.GetProperty(new UIContext(sagex.api.Global.GetUIContextName()),Property, null);
        if (tValue==null || tValue.equals(OptionNotFound)){
            return Boolean.FALSE;
        }else{
            return Boolean.TRUE;
        }
    }
    
    public static String GetProperty(String Property, String DefaultValue){
        String tValue = sagex.api.Configuration.GetProperty(new UIContext(sagex.api.Global.GetUIContextName()),Property, null);
        if (tValue==null || tValue.equals(OptionNotFound)){
            return DefaultValue;
        }else{
            return tValue;
        }
    }
    
    public static Boolean GetPropertyAsBoolean(String Property, Boolean DefaultValue){
        String tValue = sagex.api.Configuration.GetProperty(new UIContext(sagex.api.Global.GetUIContextName()),Property, null);
        if (tValue==null || tValue.equals(OptionNotFound)){
            return DefaultValue;
        }else{
            return Boolean.parseBoolean(tValue);
        }
    }
    
    //Evaluates the property and returns it's value - must be true or false - returns true otherwise
    public static Boolean GetPropertyEvalAsBoolean(String Property, Boolean DefaultValue){
        String tValue = sagex.api.Configuration.GetProperty(new UIContext(sagex.api.Global.GetUIContextName()),Property, null);
        if (tValue==null || tValue.equals(OptionNotFound)){
            return DefaultValue;
        }else{
            return Boolean.parseBoolean(EvaluateAttribute(tValue));
        }
    }
    
    public static TriState GetPropertyAsTriState(String Property, TriState DefaultValue){
        String tValue = sagex.api.Configuration.GetProperty(new UIContext(sagex.api.Global.GetUIContextName()),Property, null);
        if (tValue==null || tValue.equals(OptionNotFound)){
            return DefaultValue;
        }else if(tValue.equals("YES")){
            return TriState.YES;
        }else if(tValue.equals("NO")){
            return TriState.NO;
        }else if(tValue.equals("OTHER")){
            return TriState.OTHER;
        }else if(Boolean.parseBoolean(tValue)){
            return TriState.YES;
        }else if(!Boolean.parseBoolean(tValue)){
            return TriState.NO;
        }else{
            return TriState.YES;
        }
    }
    
    public static List<String> GetPropertyAsList(String Property){
        String tValue = sagex.api.Configuration.GetProperty(new UIContext(sagex.api.Global.GetUIContextName()),Property, null);
        if (tValue==null || tValue.equals(OptionNotFound)){
            return new LinkedList<String>();
        }else{
            return ConvertStringtoList(tValue);
        }
    }
    
    public static Integer GetPropertyAsInteger(String Property, Integer DefaultValue){
        //read in the Sage Property and force convert it to an Integer
        Integer tInteger = DefaultValue;
        String tValue = sagex.api.Configuration.GetProperty(new UIContext(sagex.api.Global.GetUIContextName()),Property, null);
        if (tValue==null || tValue.equals(OptionNotFound)){
            return DefaultValue;
        }
        try {
            tInteger = Integer.valueOf(tValue);
        } catch (NumberFormatException ex) {
            //use DefaultValue
            return DefaultValue;
        }
        return tInteger;
    }
    
    public static Double GetPropertyAsDouble(String Property, Double DefaultValue){
        //read in the Sage Property and force convert it to an Integer
        Double tDouble = DefaultValue;
        String tValue = sagex.api.Configuration.GetProperty(new UIContext(sagex.api.Global.GetUIContextName()),Property, null);
        if (tValue==null || tValue.equals(OptionNotFound)){
            return DefaultValue;
        }
        try {
            tDouble = Double.valueOf(tValue);
        } catch (NumberFormatException ex) {
            //use DefaultValue
            return DefaultValue;
        }
        return tDouble;
    }
    

    public static String GetServerProperty(String Property, String DefaultValue){
        String tValue = sagex.api.Configuration.GetServerProperty(new UIContext(sagex.api.Global.GetUIContextName()),Property, null);
        if (tValue==null || tValue.equals(OptionNotFound)){
            return DefaultValue;
        }else{
            return tValue;
        }
    }

    public static void SetProperty(String Property, String Value){
        sagex.api.Configuration.SetProperty(new UIContext(sagex.api.Global.GetUIContextName()),Property, Value);
    }

    public static void SetPropertyAsTriState(String Property, TriState Value){
        sagex.api.Configuration.SetProperty(new UIContext(sagex.api.Global.GetUIContextName()),Property, Value.toString());
    }

    public static void SetPropertyAsList(String Property, List<String> ListValue){
        String Value = ConvertListtoString(ListValue);
        if (ListValue.size()>0){
            sagex.api.Configuration.SetProperty(new UIContext(sagex.api.Global.GetUIContextName()),Property, Value);
        }else{
            RemovePropertyAndChildren(Property);
        }
    }

    public static void SetServerProperty(String Property, String Value){
        sagex.api.Configuration.SetServerProperty(new UIContext(sagex.api.Global.GetUIContextName()),Property, Value);
    }

    public static void RemoveServerProperty(String Property){
        sagex.api.Configuration.RemoveServerProperty(new UIContext(sagex.api.Global.GetUIContextName()),Property);
    }

    public static void RemovePropertyAndChildren(String Property){
        sagex.api.Configuration.RemovePropertyAndChildren(new UIContext(sagex.api.Global.GetUIContextName()),Property);
    }

    public static void RemoveProperty(String Property){
        sagex.api.Configuration.RemoveProperty(new UIContext(sagex.api.Global.GetUIContextName()),Property);
    }

    public static void RemoveServerPropertyAndChildren(String Property){
        sagex.api.Configuration.RemoveServerPropertyAndChildren(new UIContext(sagex.api.Global.GetUIContextName()),Property);
    }

    public static String ConvertListtoString(List<String> ListValue){
        return ConvertListtoString(ListValue, ListToken);
    }
    public static String ConvertListtoString(List<String> ListValue, String Separator){
        String Value = "";
        if (ListValue.size()>0){
            Boolean tFirstItem = Boolean.TRUE;
            for (String ListItem : ListValue){
                if (tFirstItem){
                    Value = ListItem;
                    tFirstItem = Boolean.FALSE;
                }else{
                    Value = Value + Separator + ListItem;
                }
            }
        }
        return Value;
    }

    public static List<String> ConvertStringtoList(String tValue){
        if (tValue.equals(OptionNotFound) || tValue.equals("") || tValue==null){
            return new LinkedList<String>();
        }else{
            return Arrays.asList(tValue.split(ListToken));
        }
    }
    
    public static String EvaluateAttribute(String Attribute){
        //LOG.debug("EvaluateAttribute: Attribute = '" + Attribute + "'");
        Object[] passvalue = new Object[1];
        passvalue[0] = sagex.api.WidgetAPI.EvaluateExpression(new UIContext(sagex.api.Global.GetUIContextName()), Attribute);
        if (passvalue[0]==null){
            LOG.debug("EvaluateAttribute for Attribute = '" + Attribute + "' not evaluated.");
            return OptionNotFound;
        }else{
            LOG.debug("EvaluateAttribute for Attribute = '" + Attribute + "' = '" + passvalue[0].toString() + "'");
            return passvalue[0].toString();
        }
        
    }

    public static void OptionsClear(){
        //clear all the Options settings used only while the Options menu is open
        String tProp = Const.BaseProp + Const.PropDivider + Const.OptionsFocused;
        RemovePropertyAndChildren(tProp);
        tProp = Const.BaseProp + Const.PropDivider + Const.OptionsType;
        RemovePropertyAndChildren(tProp);
        tProp = Const.BaseProp + Const.PropDivider + Const.OptionsTitle;
        RemovePropertyAndChildren(tProp);
    }
    
    public static void OptionsLastFocusedSet(Integer CurrentLevel, String FocusedItem){
        String tProp = Const.BaseProp + Const.PropDivider + Const.OptionsFocused + Const.PropDivider + CurrentLevel.toString() + Const.PropDivider + Const.OptionsFocusedItem;
        //LOG.debug("OptionsLastFocusedSet: CurrentLevel = '" + CurrentLevel + "' FocusedItem = '" + FocusedItem + "'");
        SetProperty(tProp, FocusedItem);
    }

    public static String OptionsLastFocusedGet(Integer CurrentLevel){
        String tProp = Const.BaseProp + Const.PropDivider + Const.OptionsFocused + Const.PropDivider + CurrentLevel.toString() + Const.PropDivider + Const.OptionsFocusedItem;
        String FocusedItem = GetProperty(tProp, OptionNotFound);
        //LOG.debug("OptionsLastFocusedGet: CurrentLevel = '" + CurrentLevel + "' FocusedItem = '" + FocusedItem + "'");
        return FocusedItem;
    }

    public static String OptionsTypeGet(Integer CurrentLevel){
        String tProp = Const.BaseProp + Const.PropDivider + Const.OptionsType + Const.PropDivider + CurrentLevel.toString() + Const.PropDivider + Const.OptionsTypeName;
        String OptionsType = GetProperty(tProp, OptionNotFound);
        //LOG.debug("OptionsTypeGet: CurrentLevel = '" + CurrentLevel + "' OptionsType = '" + OptionsType + "'");
        return OptionsType;
    }

    public static Integer OptionsTypeSet(Integer CurrentLevel, String OptionsType){
        String tProp = Const.BaseProp + Const.PropDivider + Const.OptionsType + Const.PropDivider + CurrentLevel.toString() + Const.PropDivider + Const.OptionsTypeName;
        //LOG.debug("OptionsTypeSet: CurrentLevel = '" + CurrentLevel + "' OptionsType = '" + OptionsType + "'");
        SetProperty(tProp, OptionsType);
        return CurrentLevel;
    }

    public static void OptionsTitleSet(Integer CurrentLevel, String Title){
        String tProp = Const.BaseProp + Const.PropDivider + Const.OptionsTitle + Const.PropDivider + CurrentLevel.toString() + Const.PropDivider + Const.OptionsTitleName;
        //LOG.debug("OptionsTitleSet: CurrentLevel = '" + CurrentLevel + "' Title = '" + Title + "'");
        SetProperty(tProp, Title);
    }

    public static String OptionsTitleGet(Integer CurrentLevel){
        String tProp = Const.BaseProp + Const.PropDivider + Const.OptionsTitle + Const.PropDivider + CurrentLevel.toString() + Const.PropDivider + Const.OptionsTitleName;
        String Title = GetProperty(tProp, OptionNotFound);
        //LOG.debug("OptionsTitleGet: CurrentLevel = '" + CurrentLevel + "' Title = '" + Title + "'");
        return Title;
    }

    //Set of functions for Get/Set of generic True/False values with passed in test names to display
    public static Boolean GetTrueFalseOption(String PropSection, String PropName, Boolean DefaultValue){
        return GetTrueFalseOptionBase(Boolean.FALSE, PropSection, PropName, DefaultValue);
    }
    public static Boolean GetTrueFalseOptionBase(Boolean bFlow, String PropSection, String PropName, Boolean DefaultValue){
        String tProp = "";
        if (PropName.equals("")){  //expect the full property string in the PropSection
            tProp = PropSection;
        }else{
            if (bFlow){
                tProp = Flow.GetFlowBaseProp(PropSection) + Const.PropDivider + PropName;
            }else{
                tProp = Const.BaseProp + Const.PropDivider + PropSection + Const.PropDivider + PropName;
            }
        }
        return util.GetPropertyAsBoolean(tProp, DefaultValue);
    }
    public static String GetTrueFalseOptionName(String PropSection, String TrueValue, String FalseValue, Boolean DefaultValue){
        return GetTrueFalseOptionNameBase(Boolean.FALSE, PropSection, "", TrueValue, FalseValue, DefaultValue);
    }
    public static String GetTrueFalseOptionName(String PropSection, String TrueValue, String FalseValue){
        return GetTrueFalseOptionNameBase(Boolean.FALSE, PropSection, "", TrueValue, FalseValue, Boolean.FALSE);
    }
    public static String GetTrueFalseOptionName(String PropSection, String PropName, String TrueValue, String FalseValue){
        return GetTrueFalseOptionNameBase(Boolean.FALSE, PropSection, PropName, TrueValue, FalseValue, Boolean.FALSE);
    }
    public static String GetTrueFalseOptionName(String PropSection, String PropName, String TrueValue, String FalseValue, Boolean DefaultValue){
        return GetTrueFalseOptionNameBase(Boolean.FALSE, PropSection, PropName, TrueValue, FalseValue, DefaultValue);
    }
    public static String GetTrueFalseOptionNameBase(Boolean bFlow, String PropSection, String PropName, String TrueValue, String FalseValue, Boolean DefaultValue){
        String tProp = "";
        if (PropName.equals("")){  //expect the full property string in the PropSection
            tProp = PropSection;
        }else{
            if (bFlow){
                tProp = Flow.GetFlowBaseProp(PropSection) + Const.PropDivider + PropName;
            }else{
                tProp = Const.BaseProp + Const.PropDivider + PropSection + Const.PropDivider + PropName;
            }
        }
        Boolean CurrentValue = util.GetPropertyAsBoolean(tProp, DefaultValue);
        if (CurrentValue){
            return TrueValue;
        }else{
            return FalseValue;
        }
    }

    //option for full Property string passed in
    public static void SetTrueFalseOptionNext(String PropSection, Boolean DefaultValue){
        SetTrueFalseOptionNextBase(Boolean.FALSE, PropSection, "", DefaultValue);
    }
    //option for assuming a FALSE default value and full Property string passed in
    public static void SetTrueFalseOptionNext(String PropSection){
        SetTrueFalseOptionNextBase(Boolean.FALSE, PropSection, "", Boolean.FALSE);
    }
    //option for assuming a FALSE default value
    public static void SetTrueFalseOptionNext(String PropSection, String PropName){
        SetTrueFalseOptionNextBase(Boolean.FALSE, PropSection, PropName, Boolean.FALSE);
    }
    public static void SetTrueFalseOptionNext(String PropSection, String PropName, Boolean DefaultValue){
        SetTrueFalseOptionNextBase(Boolean.FALSE, PropSection, PropName, DefaultValue);
    }
    public static void SetTrueFalseOptionNextBase(Boolean bFlow, String PropSection, String PropName, Boolean DefaultValue){
        String tProp = "";
        if (PropName.equals("")){  //expect the full property string in the PropSection
            tProp = PropSection;
        }else{
            if (bFlow){
                tProp = Flow.GetFlowBaseProp(PropSection) + Const.PropDivider + PropName;
            }else{
                tProp = Const.BaseProp + Const.PropDivider + PropSection + Const.PropDivider + PropName;
            }
        }
        Boolean NewValue = !util.GetPropertyAsBoolean(tProp, DefaultValue);
        util.SetProperty(tProp, NewValue.toString());
    }
    
    //option for full Property string passed in
    public static void SetTrueFalseOption(String PropSection, Boolean NewValue){
        SetTrueFalseOption(PropSection, "", NewValue);
    }
    public static void SetTrueFalseOption(String PropSection, String PropName, Boolean NewValue){
        String tProp = "";
        if (PropName.equals("")){  //expect the full property string in the PropSection
            tProp = PropSection;
        }else{
            tProp = Const.BaseProp + Const.PropDivider + PropSection + Const.PropDivider + PropName;
        }
        util.SetProperty(tProp, NewValue.toString());
    }
    
    //Set of functions for Get/Set of generic passed in List
    //List items must be separated by ListToken
    public static String GetListOptionName(String PropSection, String PropName, String OptionList, String DefaultValue){
        return GetListOptionNameBase(Boolean.FALSE, PropSection, PropName, OptionList, DefaultValue);
    }
    public static String GetListOptionNameBase(Boolean bFlow, String PropSection, String PropName, String OptionList, String DefaultValue){
        String tProp = "";
        if (bFlow){
            tProp = Flow.GetFlowBaseProp(PropSection) + Const.PropDivider + PropName;
        }else{
            tProp = Const.BaseProp + Const.PropDivider + PropSection + Const.PropDivider + PropName;
        }
        String CurrentValue = util.GetProperty(tProp, DefaultValue);
        if (ConvertStringtoList(OptionList).contains(CurrentValue)){
            return CurrentValue;
        }else{
            return DefaultValue;
        }
    }
    public static void SetListOptionNext(String PropSection, String PropName, String OptionList){
        SetListOptionNextBase(Boolean.FALSE, PropSection, PropName, OptionList);
    }
    public static void SetListOptionNextBase(Boolean bFlow, String PropSection, String PropName, String OptionList){
        String tProp = "";
        if (bFlow){
            tProp = Flow.GetFlowBaseProp(PropSection) + Const.PropDivider + PropName;
        }else{
            tProp = Const.BaseProp + Const.PropDivider + PropSection + Const.PropDivider + PropName;
        }
        String CurrentValue = util.GetProperty(tProp, OptionNotFound);
        List<String> FullList = ConvertStringtoList(OptionList);
        if (CurrentValue.equals(OptionNotFound)){
            util.SetProperty(tProp, FullList.get(1));  //default to the 2nd item
        }else{
            Integer pos = FullList.indexOf(CurrentValue);
            if (pos==-1){ //not found
                util.SetProperty(tProp, FullList.get(0));
            }else if(pos==FullList.size()-1){ //last item
                util.SetProperty(tProp, FullList.get(0));
            }else{ //get next item
                util.SetProperty(tProp, FullList.get(pos+1));
            }
        }
    }
    
    //set of functions for generic String based properties
    public static String GetOptionName(String PropSection, String PropName, String DefaultValue){
        return GetOptionNameBase(Boolean.FALSE, PropSection, PropName, DefaultValue);
    }
    public static String GetOptionNameBase(Boolean bFlow, String PropSection, String PropName, String DefaultValue){
        String tProp = "";
        if (bFlow){
            tProp = Flow.GetFlowBaseProp(PropSection) + Const.PropDivider + PropName;
        }else{
            tProp = Const.BaseProp + Const.PropDivider + PropSection + Const.PropDivider + PropName;
        }
        return util.GetProperty(tProp, DefaultValue);
    }
    public static void SetOption(String PropSection, String PropName, String NewValue){
        SetOptionBase(Boolean.FALSE, PropSection, PropName, NewValue);
    }
    public static void SetOptionBase(Boolean bFlow, String PropSection, String PropName, String NewValue){
        String tProp = "";
        if (bFlow){
            tProp = Flow.GetFlowBaseProp(PropSection) + Const.PropDivider + PropName;
        }else{
            tProp = Const.BaseProp + Const.PropDivider + PropSection + Const.PropDivider + PropName;
        }
        util.SetProperty(tProp, NewValue);
    }

    public static String NotFound(){
        return OptionNotFound;
    }

    public static String UnknownName(){
        return Const.UnknownName;
    }

    public static Object PadMediaFiles(Integer PadBefore, Object MediaFiles, Integer PadAfter){
        for (int index=0;index<PadBefore;index++){
            MediaFiles = sagex.api.Database.DataUnion(sagex.api.Utility.CreateArray("BlankItem" + (index+1)), MediaFiles);
        }
        for (int index=0;index<PadAfter;index++){
            MediaFiles = sagex.api.Database.DataUnion(MediaFiles, sagex.api.Utility.CreateArray("BlankItem"+(index+1)*-1));
        }
        return MediaFiles;
    }
    
    //VFS utility functions
    
    //Get the menu title based on the flow and the current folder
    public static String GetMenuTitle(String FlowKey, Object thisFolder){
        String tFlowName = Flow.GetFlowName(FlowKey);
        String FolderName = phoenix.media.GetTitle(thisFolder);
        String ParentName = phoenix.media.GetTitle(phoenix.umb.GetParent((sagex.phoenix.vfs.IMediaResource) thisFolder));
        if (ParentName==null){
            return tFlowName + " : " + FolderName;
        }else{
            return tFlowName + " : " + ParentName + "/" + FolderName;
        }
    }
    
    public static ArrayList<String> GetFirstListItems(ArrayList<String> InList, Integer Items){
        //filter out null values and return at most Items items
        ArrayList<String> OutList = new ArrayList<String>();
        Integer counter = 0;
        for (String Item: InList){
            if (Item==null){
                //do nothing
            }else{
                OutList.add(Item);
                counter++;
                if (counter>=Items){
                    break;
                }
            }
        }
        return OutList;
    }
    
}


