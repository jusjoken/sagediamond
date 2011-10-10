/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sagediamond;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author SBANTA
 * @author JUSJOKEN
 * - 10/02/2011 - added logging and minor changes
 */
public class InstantSearch {

    static private final Logger LOG = Logger.getLogger(InstantSearch.class);
    //public static enum InstantSearchMode{KEYBOARD,KEYPAD,JUMPTO};

    public static Object GetInstantSearch(boolean keyboard,String SearchKeys,Object MediaFiles){
        Object[] files=FanartCaching.toArray(MediaFiles);
        if(!keyboard){
            SearchKeys=CreateRegexFromKeypad(SearchKeys);
        }
        ArrayList<Object> matches= new ArrayList<Object>();
        for(Object curr:files){
            String Title=MetadataCalls.GetMediaTitle(curr);
            if(Title.contains(SearchKeys)){
                matches.add(curr);
            }
        }
        return matches;
    }

    public static Object FilteredList(String FlowName,String SearchKeys,Object MediaFiles){
        Object[] files=FanartCaching.toArray(MediaFiles);
        if (Flow.GetInstantSearchMode(FlowName).equals(api.InstantSearchMode.KEYBOARD.toString())){
            
        }else if (Flow.GetInstantSearchMode(FlowName).equals(api.InstantSearchMode.KEYPAD.toString())){
            SearchKeys=CreateRegexFromKeypad(SearchKeys);
        }
        ArrayList<Object> matches= new ArrayList<Object>();
        LOG.debug("SearchKeys = '" + SearchKeys + "'");
//        for(Object curr:files){
//            String Title=MetadataCalls.GetMediaTitle(curr);
//            if(Title.contains(SearchKeys)){
//                matches.add(curr);
//            }
//        }
        return matches;
    }

    public static String AddKey(String FlowName, String SearchString, String AddedString){
        String NewString = "";
        if (Flow.GetInstantSearchMode(FlowName).equals(api.InstantSearchMode.KEYBOARD.toString())){
            //add keyboard keypress directly
            NewString = SearchString + AddedString.toLowerCase();
        }else if (Flow.GetInstantSearchMode(FlowName).equals(api.InstantSearchMode.KEYPAD.toString())){
            //NewString = SearchString + CreateRegexFromKeypad(AddedString);
            NewString = SearchString + AddedString;
        }
        LOG.debug("FlowName = '" + FlowName + "' SearchString = '" + NewString + "' AddedString = '" + AddedString + "'");
        return NewString;
    }
    
    public static Boolean ValidKey(String FlowName, String KeyPress){
        //see if the KeyPress is valid for the InstantSearchMode
        Boolean IsValid = Boolean.FALSE;
        if (Flow.GetInstantSearchMode(FlowName).equals(api.InstantSearchMode.KEYBOARD.toString())){
            //check keyboard input
            IsValid = KeyPress.matches("[A-Za-z]");
        }else if (Flow.GetInstantSearchMode(FlowName).equals(api.InstantSearchMode.KEYPAD.toString())){
            //check numeric input
            IsValid = KeyPress.matches("[0-9]");
        }else if (Flow.GetInstantSearchMode(FlowName).equals(api.InstantSearchMode.JUMPTO.toString())){
            IsValid = KeyPress.matches("[A-Za-z]");
        }else{
            //return the default FALSE value
        }
        LOG.debug("FlowName = '" + FlowName + "' KeyPress = '" + KeyPress + "' Valid = '" + IsValid + "'");
        return IsValid;
    }
    
    public static String ModeJumpTo(){
        return api.InstantSearchMode.JUMPTO.toString();
    }
    public static String ModeKeyboard(){
        return api.InstantSearchMode.KEYBOARD.toString();
    }
    public static String ModeKeyPad(){
        return api.InstantSearchMode.KEYPAD.toString();
    }

    public static String ExecuteModeSelect(){
        return api.InstantSearchExecuteMode.SELECT.toString();
    }
    public static String ExecuteModeAuto(){
        return api.InstantSearchExecuteMode.AUTO.toString();
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
