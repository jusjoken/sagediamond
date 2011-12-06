/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Diamond;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import org.apache.log4j.Logger;
import sagex.phoenix.factory.ConfigurableOption;
import sagex.phoenix.vfs.filters.Filter;
import sagex.phoenix.vfs.views.ViewFolder;

/**
 *
 * @author jusjoken
 */
public class Source {
    static private final Logger LOG = Logger.getLogger(Source.class);
    
    public static void ApplyFilters(String ViewName, ViewFolder Folder){
        LOG.debug("ApplyFilters: Before Count = '" + phoenix.media.GetAllChildren(Folder).size() + "'");
        ApplyGenreFilters(ViewName, Folder);
        LOG.debug("ApplyFilters: After Genre Count = '" + phoenix.media.GetAllChildren(Folder).size() + "'");
        ApplyFolderFilters(ViewName, Folder);
        LOG.debug("ApplyFilters: After Folder Count = '" + phoenix.media.GetAllChildren(Folder).size() + "'");
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
    
    public static String FolderFilterAppend(String filters, String filter){
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
    
    public static void ApplyFolderFilters(String ViewName, ViewFolder Folder){
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
                    IncludeFilters = FolderFilterAppend(IncludeFilters, filter);
                    //LOG.debug("ApplyFilters Include adding: = '" + filter + "' New = '" + IncludeFilters + "'");
                }else{  //Exclude
                    ExcludeFilters = FolderFilterAppend(ExcludeFilters, filter);
                    //LOG.debug("ApplyFilters Exclude adding: = '" + filter + "' New = '" + ExcludeFilters + "'");
                }
            }
            ApplyFolderFilters(ViewName, Folder, IncludeFilters, ExcludeFilters);
        }
    }
    public static void ApplyFolderFilters(String ViewName, ViewFolder Folder, String IncludeFilters, String ExcludeFilters){
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
    
    public static String SetFolderFilter(String ViewName, String Path, Boolean include) {
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

    public static String RemoveFolderFilter(String ViewName, String Path, Boolean include) {
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
    
    public static ArrayList<String> GetGenres(ViewFolder Folder){
        if (Folder==null){
            LOG.debug("GetGenres: request for null Folder returned empty list");
            return new ArrayList<String>();
        }
        TreeSet<String> GenreList = new TreeSet<String>();
        for (Object Item: phoenix.media.GetAllChildren(Folder)){
            //LOG.debug("GetGenres: proecessing '" + phoenix.media.GetTitle(Item) + "' Genres '" + MetadataCalls.GetGenresasString((IMediaResource)Item, ","));
            GenreList.addAll(phoenix.metadata.GetGenres(Item));
        }
        return new ArrayList<String>(GenreList);
    }
    
    public static void ApplyGenreFilters(String ViewName, ViewFolder Folder){
        //LOG.debug("ApplyFilters: = '" + ViewName + "'");
        //make sure we have a filter
        String tProp = Flow.GetFlowBaseProp(ViewName) + Const.PropDivider + Const.FlowGenreFilters;
        String FilterString = util.ConvertListtoString(util.GetPropertyAsList(tProp),"|");
        LOG.debug("ApplyGenreFilters: FilterString = '" + FilterString + "'");
        if (!FilterString.equals("")){
            FilterString = "(" + FilterString + ")";
            Filter NewFilter = phoenix.umb.CreateFilter("genre");
            ConfigurableOption tOption = phoenix.umb.GetOption(NewFilter, "use-regex-matching");
            phoenix.opt.SetValue(tOption, "true");
            tOption = phoenix.umb.GetOption(NewFilter, "scope");
            tProp = Flow.GetFlowBaseProp(ViewName) + Const.PropDivider + Const.FlowGenreFilterMode;
            if (util.GetPropertyAsBoolean(tProp, Boolean.TRUE)){
                phoenix.opt.SetValue(tOption, "include");
                LOG.debug("ApplyGenreFilters: include = '" + Flow.GetFlowName(ViewName) + "' RegExFilter = '" + FilterString + "'");
            }else{
                phoenix.opt.SetValue(tOption, "exclude");
                LOG.debug("ApplyGenreFilters: exclude = '" + Flow.GetFlowName(ViewName) + "' RegExFilter = '" + FilterString + "'");
            }
            tOption = phoenix.umb.GetOption(NewFilter, "value");
            phoenix.opt.SetValue(tOption, FilterString);
            phoenix.umb.SetChanged(NewFilter);
            phoenix.umb.SetFilter(Folder, NewFilter);
            phoenix.umb.Refresh(Folder);
        }
    }

}
