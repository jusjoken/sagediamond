/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Diamond;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import sagex.phoenix.factory.ConfigurableOption;
import sagex.phoenix.vfs.filters.Filter;
import sagex.phoenix.vfs.views.ViewFolder;

/**
 *
 * @author SBANTA
 */
public class FolderExclusion {

    static private final Logger LOG = Logger.getLogger(FolderExclusion.class);

    public static void main(String[] args) {
//    File 3232=new File("C:\\TVFiles\\TestPath\\TV\\Battlestar Galactica (2003)&&true");
        String test = "C:\\TestMovies\\Blurays";
       
    }

    public static Map GetAllFolderRestrictions(String ViewName) {
        String ExclusionFolders = Flow.GetOptionName(ViewName, Const.FlowPathFilters, "");
        //LOG.debug("GetAllFolderRestrictions: = '" + ExclusionFolders + "'");
        Map rest = new HashMap<String, Boolean>();
        if (!ExclusionFolders.equals("")) {
            String[] AllValues = ExclusionFolders.split(";");
            for (String curr : AllValues) {
                String[] currv = curr.split("&&");
                rest.put(currv[0], Boolean.parseBoolean(currv[1]));
            }
        }
        return rest;
    }

    public static Boolean HasFilters(String ViewName) {
        if (GetAllFolderRestrictions(ViewName).size()>0){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }
    
    //Same filter
    //"(?!(\\\\PlayOn\\\\TV\\\\Eureka|\\\\PlayOn\\\\Movies\\\\The Mechanic))(\\\\PlayOn\\\\Test1|\\\\PlayOn\\\\TV|\\\\PlayOn\\\\Movies)"
    
    public static String FilterAppend(String filters, String filter){
        //LOG.debug("FilterAppend: called '" + filters + "' add '" + filter + "'");
        String tFilter = filter;
        //format the filter as a RegEx compatible string
        tFilter = filter.replaceAll("\\\\","\\\\\\\\");
        //LOG.debug("FilterAppend: after replaceall '" + tFilter + "'");
        if (filters.equals("")){
            filters = tFilter;
        }else{
            filters = filters + "|" + tFilter;
        }
        //LOG.debug("FilterAppend: adding '" + filter + "' to '" + filters + "'");
        return filters;
    }
    
    public static void ApplyFilters(String ViewName, ViewFolder Folder){
        if (HasFilters(ViewName)){
            //LOG.debug("ApplyFilters: = '" + ViewName + "'");
            //String IncludeFilters = "\\\\PlayOn\\\\Test1|\\\\PlayOn\\\\TV|\\\\PlayOn\\\\Movies";
            //String ExcludeFilters = "\\\\PlayOn\\\\TV\\\\Eureka|\\\\PlayOn\\\\Movies\\\\The Mechanic";
            String IncludeFilters = "";
            String ExcludeFilters = "";
            Map<String, Boolean> filters = GetAllFolderRestrictions(ViewName);
            //LOG.debug("ApplyFilters: = filters '" + filters + "'");
            for (String filter:filters.keySet()){
                //LOG.debug("ApplyFilters: = processing filter '" + filter + "'");
                if (filters.get(filter)){  //Include
                    IncludeFilters = FilterAppend(IncludeFilters, filter);
                    //LOG.debug("ApplyFilters Include adding: = '" + filter + "' New = '" + IncludeFilters + "'");
                }else{  //Exclude
                    ExcludeFilters = FilterAppend(ExcludeFilters, filter);
                    //LOG.debug("ApplyFilters Exclude adding: = '" + filter + "' New = '" + ExcludeFilters + "'");
                }
            }
            ApplyFilters(ViewName, Folder, IncludeFilters, ExcludeFilters);
        }
    }
    public static void ApplyFilters(String ViewName, ViewFolder Folder, String IncludeFilters, String ExcludeFilters){
        //LOG.debug("ApplyFilters: = '" + ViewName + "'");
        //make sure we have a filter
        String FilterString = BuildFilterRegEx(IncludeFilters, ExcludeFilters);
        if (!FilterString.equals("")){
            Filter NewFilter = phoenix.umb.CreateFilter("filepath");
            ConfigurableOption tOption = phoenix.umb.GetOption(NewFilter, "use-regex-matching");
            phoenix.opt.SetValue(tOption, "true");
            tOption = phoenix.umb.GetOption(NewFilter, "scope");
            if (IncludeFilters.equals("")){
                phoenix.opt.SetValue(tOption, "exclude");
                LOG.debug("ApplyFilters: exclude = '" + Flow.GetFlowName(ViewName) + "' RegExFilter = '" + FilterString + "'");
            }else{
                phoenix.opt.SetValue(tOption, "include");
                LOG.debug("ApplyFilters: include = '" + Flow.GetFlowName(ViewName) + "' RegExFilter = '" + FilterString + "'");
            }
            tOption = phoenix.umb.GetOption(NewFilter, "value");
            phoenix.opt.SetValue(tOption, FilterString);
            phoenix.umb.SetChanged(NewFilter);
            phoenix.umb.SetFilter(Folder, NewFilter);
            phoenix.umb.Refresh(Folder);
        }
    }

    public static String BuildFilterRegEx(String IncludeFilters, String ExcludeFilters){
        if (IncludeFilters.equals("") && ExcludeFilters.equals("")){
            return "";
        }else{
            String RegExString = "";
            if (!ExcludeFilters.equals("")){
                if (IncludeFilters.equals("")){
                    RegExString = "(" + ExcludeFilters + ")";
                }else{
                    RegExString = "(?!(" + ExcludeFilters + "))";
                }
            }
            if (!IncludeFilters.equals("")){
                RegExString = RegExString + "(" + IncludeFilters + ")";
            }
            return RegExString;
        }
    }
    
//    public static Object[] RunFolderFilter(Object[] MediaFiles, String ViewName) {
//        Map<String, Boolean> filters = GetAllFolderRestrictions(ViewName);
//        Set Restrictions = filters.keySet();
//        Boolean NeedAdder = filters.values().contains(true);
//        ArrayList returnarray = new ArrayList<Object>();
//        ArrayList excludearray=new ArrayList<Object>();
//
//        for (Object curr : MediaFiles) {
//            String path = sagex.api.MediaFileAPI.GetParentDirectory(curr).toString();
//            for (Object currs : Restrictions) {
//                Boolean Include = filters.get(currs);
//                if (Include && path.contains(currs.toString()) && !returnarray.contains(curr)) {
////                    System.out.println("Include Filter Movie added="+path+";Current Restriction="+currs.toString());
//                    returnarray.add(curr);
//                }
//                else if (!Include && path.contains(currs.toString())) {
////                 System.out.println("Exlude Filter Movie Removed="+path+";Current Restriction="+currs.toString());
//                    if(returnarray.contains(curr)){
//                    
//                    returnarray.remove(curr);}
//                    excludearray.add(curr);
//                } else if (!NeedAdder&&!excludearray.contains(curr)) {
//                    if (!returnarray.contains(curr)) {
////                     System.out.println("Movie added as end result="+path+";Current Restriction="+currs.toString());
//                        returnarray.add(curr);
//                    }
//                }
//            }
//        }
//        return returnarray.toArray();
//
//    }

    public static boolean HasFilter(String ViewName, String Path, Boolean include) {
        String Element = Path + "&&" + include;
        if (Flow.GetOptionName(ViewName, Const.FlowPathFilters, "").contains(Element + ";")) {
            return true;
        } else {
            return false;
        }
    }

    public static String SetFilter(String ViewName, String Path, Boolean include) {
        String Element = Path + "&&" + include;
        String CurrentElements = Flow.GetOptionName(ViewName, Const.FlowPathFilters, "");
        String result = null;
        if (CurrentElements.contains(Element + ";")) {
            result = "0";
        } else {
            String NewElements = CurrentElements + Element + ";";
            Flow.SetOption(ViewName, Const.FlowPathFilters, NewElements);
            result = "1";
        }
        return result;
    }

    public static String RemoveFilter(String ViewName, String Path, Boolean include) {
        String Element = Path + "&&" + include;
        String CurrentElements = Flow.GetOptionName(ViewName, Const.FlowPathFilters, "");
        String ElementRemoved = null;
        String result = null;
        if (CurrentElements.contains(Element + ";")) {
            ElementRemoved = CurrentElements.replace(Element + ";", "");
            Flow.SetOption(ViewName, Const.FlowPathFilters, ElementRemoved);
            result = "1";
        } else {
            result = "0";
        }
        return result;

    }
}
