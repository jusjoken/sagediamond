/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Diamond;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 *
 * @author SBANTA
 * @author JUSJOKEN
 * - 10/02/2011 - added logging and minor changes
 */
public class InstantSearch {

    static private final Logger LOG = Logger.getLogger(InstantSearch.class);

//    public static Object GetInstantSearch(boolean keyboard,String SearchKeys,Object MediaFiles){
//        Object[] files=FanartCaching.toArray(MediaFiles);
//        if(!keyboard){
//            SearchKeys=CreateRegexFromKeypad(SearchKeys);
//        }
//        ArrayList<Object> matches= new ArrayList<Object>();
//        for(Object curr:files){
//            String Title=MetadataCalls.GetMediaTitle(curr);
//            if(Title.contains(SearchKeys)){
//                matches.add(curr);
//            }
//        }
//        return matches;
//    }

    public static Object FilteredList(String FlowName,String SearchKeys,Object MediaFiles){
        if (FlowName==null || SearchKeys==null || SearchKeys.isEmpty()){
            LOG.debug("FilteredList: invalid parameters so returning full list. FlowName '" + FlowName + "' SearchKeys '" + SearchKeys + "'");
            return MediaFiles;
        }
        StopWatch Elapsed = new StopWatch("Flitering " + FlowName + " by '" + SearchKeys + '"');
        Elapsed.Start();
        Boolean IsNumericKeyListener = Flow.GetTrueFalseOption(FlowName, Const.InstantSearchIsNumericListener, Boolean.FALSE);
        Object[] InputMediaFiles = FanartCaching.toArray(MediaFiles);
        LOG.debug("FilteredList: InputMediaFiles = '" + InputMediaFiles.length);
        Object OutputMediaFiles = null;
        if (IsNumericKeyListener){
            SearchKeys=CreateRegexFromKeypad(SearchKeys);
        }
        Pattern SearchPattern = Pattern.compile(SearchKeys);
        OutputMediaFiles = sagex.api.Database.FilterByMethodRegex(InputMediaFiles, "Diamond_MetadataCalls_GetTitleLowerCase", SearchPattern, true, false);
        //remove these 2 lines after testing
        Object[] Tempfiles=FanartCaching.toArray(OutputMediaFiles);
        LOG.debug("FilteredList: OutputMediaFiles = '" + Tempfiles.length);
        //remove these 2 lines above after testing
        //LOG.debug("InstantSearch using RegEx = '" + SearchKeys + "'");
        Elapsed.StopandLog();
        if (sagex.api.Utility.Size(OutputMediaFiles)>0){
            return OutputMediaFiles;
        }
        LOG.debug("FilteredList: empty search result so returning full list. FlowName '" + FlowName + "' SearchKeys '" + SearchKeys + "'");
        return MediaFiles;
    }

//    public static Object FilteredList(String FlowName,String SearchKeys,Object MediaFiles){
//        StopWatch Elapsed = new StopWatch("Flitering " + FlowName + " by '" + SearchKeys + '"');
//        Elapsed.Start();
//        Boolean IsNumericKeyListener = Flow.GetTrueFalseOption(FlowName, Const.InstantSearchIsNumericListener, Boolean.FALSE);
//        Object[] InputMediaFiles = FanartCaching.toArray(MediaFiles);
//        LOG.debug("InputMediaFiles = '" + InputMediaFiles.length);
//        Object OutputMediaFiles = null;
//        if (IsNumericKeyListener){
//            SearchKeys=CreateRegexFromKeypad(SearchKeys);
//        }
//        Pattern SearchPattern = Pattern.compile(SearchKeys);
//        OutputMediaFiles = sagex.api.Database.FilterByMethodRegex(InputMediaFiles, "Diamond_MetadataCalls_GetMediaTitleLowerCase", SearchPattern, true, false);
//        //remove these 2 lines after testing
//        Object[] Tempfiles=FanartCaching.toArray(OutputMediaFiles);
//        LOG.debug("OutputMediaFiles = '" + Tempfiles.length);
//        //remove these 2 lines above after testing
//        //LOG.debug("InstantSearch using RegEx = '" + SearchKeys + "'");
//        Elapsed.StopandLog();
//        return OutputMediaFiles;
//    }
    
    public static String AddKey(String FlowName, String SearchString, String AddedString){
        String NewString = "";
        if (FlowName==null){
            return NewString;
        }
        Boolean IsNumericKeyListener = Flow.GetTrueFalseOption(FlowName, Const.InstantSearchIsNumericListener, Boolean.FALSE);
        if (IsNumericKeyListener){
            NewString = SearchString + AddedString;
        }else{
            //add keyboard keypress
            if (AddedString.equals("Space")){
                NewString = SearchString + " ";
            }else{
                NewString = SearchString + AddedString.toLowerCase();
            }
        }
        LOG.debug("FlowName = '" + FlowName + "' SearchString = '" + NewString + "' AddedString = '" + AddedString + "'");
        return NewString;
    }
    
    public static Boolean ValidKey(String FlowName, String KeyPress, Boolean IsNumericKeyListener){
        //see if the KeyPress is valid for the InstantSearchMode
        if (FlowName==null || KeyPress==null){
            return Boolean.FALSE;
        }
        Boolean IsValid = Boolean.FALSE;
        if (!Flow.GetTrueFalseOption(FlowName, Const.InstantSearchFilteredJumpTo, Boolean.FALSE)){ //JumpTo
            IsValid = KeyPress.matches("[A-Za-z0-9]");
        }else if (IsNumericKeyListener){
            //check numeric input
            IsValid = KeyPress.matches("[0-9]");
        }else{
            //check keyboard input
            if (KeyPress.equals("Space")){
                IsValid = Boolean.TRUE;
            }else{
                IsValid = KeyPress.matches("[A-Za-z]");
            }
        }
        if (IsValid){
            Flow.SetTrueFalseOption(FlowName, Const.InstantSearchIsNumericListener, IsNumericKeyListener);
        }
        LOG.debug("FlowName = '" + FlowName + "' KeyPress = '" + KeyPress + "' Valid = '" + IsValid + "' NumericKeyListener = '" + IsNumericKeyListener + "'");
        return IsValid;
    }
    
    /** From Phoenix api
     * Given a keypad of numbers return a regex that can be used to find titles
     * based on the number pattern
     * 
     * @param numbers
     * @author seans
     * @return
     */
    public static String CreateRegexFromKeypad(String numbers) {
            if (numbers == null)
                    return null;
            int s = numbers.length();
            StringBuilder regex = new StringBuilder();
            for (int i = 0; i < s; i++) {
                    String reg = keyregex.get(numbers.charAt(i));
                    if (reg != null) {
                            regex.append(reg);
                    } else {
                            LOG.debug("Invalid Charact for KeyPad Regex Search: " + numbers.charAt(i) + " in " + numbers);
                    }
            }
            LOG.debug("KeyPad Search Regex: " + regex);
            return regex.toString();
    }
    
        private static Map<Character, String> keyregex = new HashMap<Character, String>();
        static {
                keyregex.put('1', "[\\p{Punct}1]");
                keyregex.put('2', "[abcABC2]");
                keyregex.put('3', "[defDEF3]");
                keyregex.put('4', "[ghiGHI4]");
                keyregex.put('5', "[jklJKL5]");
                keyregex.put('6', "[mnoMNO6]");
                keyregex.put('7', "[pqrsPQRS7]");
                keyregex.put('8', "[tuvTUV8]");
                keyregex.put('9', "[wxyzWXYZ9]");
                keyregex.put('0', "[ 0]");
        }    
}
