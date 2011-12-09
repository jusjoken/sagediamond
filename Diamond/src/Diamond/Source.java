/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Diamond;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.log4j.Logger;
import sagex.phoenix.factory.ConfigurableOption;
import sagex.phoenix.vfs.IMediaResource;
import sagex.phoenix.vfs.MediaResourceType;
import sagex.phoenix.vfs.filters.*;
import sagex.phoenix.vfs.views.ViewFolder;

/**
 *
 * @author jusjoken
 */
public class Source {
    static private final Logger LOG = Logger.getLogger(Source.class);
    public static HashMap<String,String> InternalFilterTypes = new HashMap<String,String>();
    
    public static void AddFilterType(String FilterName, String FilterType){
        if (IsFilterTypeValid(FilterType)){
            InternalFilterTypes.put(FilterName, FilterType);
        }
    }
    public static Boolean IsFilterTypeValid(String FilterType){
        if (FilterType.equals("Off-Include-Exclude")){
            return Boolean.TRUE;
        }else if (FilterType.equals("List")){
            return Boolean.TRUE;
        }else if (FilterType.equals("pql")){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }
   
    public static void ApplyFilters(String ViewName, ViewFolder Folder){
        LOG.debug("ApplyFilters: '" + Flow.GetFlowName(ViewName) + "' Before Count = '" + phoenix.media.GetAllChildren(Folder).size() + "' Types '" + InternalFilterTypes + "'");
        Set<Filter> AllFilters = new HashSet<Filter>();
        //Apply genre filter if any
        if (HasGenreFilter(ViewName)){
            AllFilters.add(GetGenreFilter(ViewName));
        }
        //Apply Folder filter if any
        if (HasFolderFilter(ViewName)){
            AllFilters.add(GetFolderFilter(ViewName));
        }
        //Apply other filters passed in 
        for (String FilterName: InternalFilterTypes.keySet()){
            String FilterType = InternalFilterTypes.get(FilterName);
            if (IsFilterTypeValid(FilterType)){
                if (HasTriFilter(ViewName, FilterName)){
                    String FilterTypeforCreate = FilterName;
                    if (FilterType.equals("pql")){
                        FilterTypeforCreate = "pql";
                    }
                    Filter NewFilter = phoenix.umb.CreateFilter(FilterTypeforCreate);
                    ConfigurableOption tOption = phoenix.umb.GetOption(NewFilter, "scope");
                    if (TriFilterInclude(ViewName, FilterName)){
                        phoenix.opt.SetValue(tOption, "include");
                        LOG.debug("ApplyFilters: '" + Flow.GetFlowName(ViewName) + "' processing filter '" + FilterName + "' FilterType '" + FilterType + "' include");
                    }else{
                        phoenix.opt.SetValue(tOption, "exclude");
                        LOG.debug("ApplyFilters: '" + Flow.GetFlowName(ViewName) + "' processing filter '" + FilterName + "' FilterType '" + FilterType + "' exclude");
                    }
                    if (FilterType.equals("List")){
                        //get the list contents if any and set it to the value
                        String FilterString = Flow.PropertyListasString(ViewName, Const.FlowFilters + Const.PropDivider + FilterName + "FilterList");
                        LOG.debug("ApplyFilters: '" + Flow.GetFlowName(ViewName) + "' processing filter '" + FilterName + "' FilterType '" + FilterType + "' filter '" + FilterString + "'");
                        if (!FilterString.equals("")){
                            tOption = phoenix.umb.GetOption(NewFilter, "value");
                            phoenix.opt.SetValue(tOption, FilterString);
                        }
                        
                    }
                    if (FilterType.equals("pql")){  //custom handling for these
                        String FilterString = "";
                        if (FilterName.equals("rating")){
                            if (Flow.PropertyListCount(ViewName, GetFilterListProp(FilterName))>0){
                                FilterString = BuildPQL(ViewName, FilterName, "Rated", "=", Boolean.FALSE);
                                FilterString = FilterString + " or " + BuildPQL(ViewName, FilterName, "ParentalRating", "=", Boolean.FALSE);
                                LOG.debug("ApplyFilters: '" + Flow.GetFlowName(ViewName) + "' processing filter '" + FilterName + "' FilterType '" + FilterType + "' filter '" + FilterString + "'");
                            }
                        }
                        if (!FilterString.equals("")){
                            tOption = phoenix.umb.GetOption(NewFilter, "value");
                            phoenix.opt.SetValue(tOption, FilterString);
                        }
                    }
                    phoenix.umb.SetChanged(NewFilter);
                    AllFilters.add(NewFilter);
                }else{
                    LOG.debug("ApplyFilters: '" + Flow.GetFlowName(ViewName) + "' processing filter '" + FilterName + "' FilterType '" + FilterType + "' Filter is turned Off");
                }
            }else{
                LOG.debug("ApplyFilters: '" + Flow.GetFlowName(ViewName) + "' invalid filtertype passed '" + FilterName + "' FilterType '" + FilterType + "'");
            }
        }
        AndResourceFilter andFilter = new AndResourceFilter();
        for (Filter thisFilter: AllFilters){
            andFilter.addFilter(thisFilter);
        }
        WrappedResourceFilter filter = new WrappedResourceFilter(andFilter);
        phoenix.umb.SetFilter(Folder, filter);
        phoenix.umb.Refresh(Folder);
        LOG.debug("ApplyFilters: '" + Flow.GetFlowName(ViewName) + "' After Count = '" + phoenix.media.GetAllChildren(Folder).size() + "'");
    }

    public static String BuildPQL(String ViewName, String FilterName, String FieldName, String FieldVerb, Boolean AndValues){
        String tFilterString = "";
        for (String Item: Flow.PropertyList(ViewName, GetFilterListProp(FilterName))){
            if (tFilterString.equals("")){
                tFilterString = FieldName + " " + FieldVerb + " '" + Item + "' ";
            }else{
                String AndOr = " or ";
                if (AndValues){
                    AndOr = " and ";
                }
                tFilterString = tFilterString + AndOr + FieldName + " " + FieldVerb + " '" + Item + "' ";
            }
        }
        return tFilterString;
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

    public static Boolean HasFolderFilter(String ViewName) {
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

    public static Filter GetFolderFilter(String ViewName){
        if (HasFolderFilter(ViewName)){
            String IncludeFilters = "";
            String ExcludeFilters = "";
            Map<String, Boolean> filters = GetAllFolderRestrictions(ViewName);
            for (String filter:filters.keySet()){
                if (filters.get(filter)){  //Include
                    IncludeFilters = FolderFilterAppend(IncludeFilters, filter);
                }else{  //Exclude
                    ExcludeFilters = FolderFilterAppend(ExcludeFilters, filter);
                }
            }
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
                return NewFilter;
            }
        }
        return null;
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
        //TODO: get all children and group by Show to shorten then list
        TreeSet<String> GenreList = new TreeSet<String>();
        for (Object Item: phoenix.media.GetAllChildren(Folder)){
            //LOG.debug("GetGenres: proecessing '" + phoenix.media.GetTitle(Item) + "' Genres '" + MetadataCalls.GetGenresasString((IMediaResource)Item, ","));
            GenreList.addAll(phoenix.metadata.GetGenres(Item));
        }
        return new ArrayList<String>(GenreList);
    }
    
    public static ArrayList<String> GetRatings(ViewFolder Folder){
        if (Folder==null){
            LOG.debug("GetRatings: request for null Folder returned empty list");
            return new ArrayList<String>();
        }
        TreeSet<String> RatingList = new TreeSet<String>();
        //TODO: get all children and group by Show to shorten then list
        
        for (Object Item: phoenix.media.GetAllChildren(Folder)){
            String thisRating = "";
            IMediaResource thisMedia = (IMediaResource)Item;
            if (thisMedia.isType(MediaResourceType.TV.value())){
                thisRating = phoenix.metadata.GetParentalRating(thisMedia);
            }else{
                thisRating = phoenix.metadata.GetRated(thisMedia);
            }
            //LOG.debug("GetRating: proecessing '" + phoenix.media.GetTitle(Item) + "' Ratings '" + thisRating + "'");
            if (!thisRating.equals("")){
                RatingList.add(thisRating);
            }
        }
        return new ArrayList<String>(RatingList);
    }
    
    public static Boolean HasGenreFilter(String ViewName){
        String tProp = Flow.GetFlowBaseProp(ViewName) + Const.PropDivider + Const.FlowGenreFilters;
        List<String> FilterList = util.GetPropertyAsList(tProp);
        if (FilterList.isEmpty()){
            return Boolean.FALSE;
        }else{
            return Boolean.TRUE;
        }
    }
    
    public static Filter GetGenreFilter(String ViewName){
        //LOG.debug("ApplyFilters: = '" + ViewName + "'");
        //make sure we have a filter
        String tProp = Flow.GetFlowBaseProp(ViewName) + Const.PropDivider + Const.FlowGenreFilters;
        String FilterString = util.ConvertListtoString(util.GetPropertyAsList(tProp),"|");
        LOG.debug("GetGenreFilter: FilterString = '" + FilterString + "'");
        if (!FilterString.equals("")){
            FilterString = "(" + FilterString + ")";
            Filter NewFilter = phoenix.umb.CreateFilter("genre");
            ConfigurableOption tOption = phoenix.umb.GetOption(NewFilter, "use-regex-matching");
            phoenix.opt.SetValue(tOption, "true");
            tOption = phoenix.umb.GetOption(NewFilter, "scope");
            tProp = Flow.GetFlowBaseProp(ViewName) + Const.PropDivider + Const.FlowGenreFilterMode;
            if (util.GetPropertyAsBoolean(tProp, Boolean.TRUE)){
                phoenix.opt.SetValue(tOption, "include");
                LOG.debug("GetGenreFilter: include = '" + Flow.GetFlowName(ViewName) + "' RegExFilter = '" + FilterString + "'");
            }else{
                phoenix.opt.SetValue(tOption, "exclude");
                LOG.debug("GetGenreFilter: exclude = '" + Flow.GetFlowName(ViewName) + "' RegExFilter = '" + FilterString + "'");
            }
            tOption = phoenix.umb.GetOption(NewFilter, "value");
            phoenix.opt.SetValue(tOption, FilterString);
            phoenix.umb.SetChanged(NewFilter);
            return NewFilter;
        }
        return null;
    }

    //check all filter types to see if there are any filters set
    public static Boolean HasFilter(String ViewName){
        if (HasGenreFilter(ViewName) || HasFolderFilter(ViewName)){
            return Boolean.TRUE;
        }else{
            //now check any valid Filter Type
            //String tProp = Flow.GetFlowBaseProp(ViewName) + Const.PropDivider + Const.FlowFilters;
            //String[] FilterProps = sagex.api.Configuration.GetSubpropertiesThatAreLeaves(new UIContext(sagex.api.Global.GetUIContextName()),tProp);
            for (String FilterItem: InternalFilterTypes.keySet()){
                //LOG.debug("HasFilter: checking '" + FilterItem + "'");
                if (FilterItem.equals(Const.FlowGenreFilters) || FilterItem.equals(Const.FlowPathFilters)){
                    //skip as already checked above
                }else{
                    if (HasTriFilter(ViewName, FilterItem)){
                        return Boolean.TRUE;
                    }
                }
            }
            return Boolean.FALSE;
        }
    }

    public static void ClearAllFilters(String ViewName){
        String tProp = Flow.GetFlowBaseProp(ViewName) + Const.PropDivider + Const.FlowFilters;
        util.RemovePropertyAndChildren(tProp);
    }

    //calls to handle generic TriState Filters - Off, Include or Exclude types - example watched, dvd etc
    public static final String TriFilterList = "Off:&&:Include:&&:Exclude";
    public static String GetTriFilterName(String ViewName, String FilterType){
        return Flow.GetListOptionName(ViewName, GetFilterTypeProp(FilterType), TriFilterList, "Off");
    }
    public static void SetTriFilterNext(String ViewName, String FilterType){
        Flow.SetListOptionNext(ViewName, GetFilterTypeProp(FilterType), TriFilterList);
    }
    public static Boolean HasTriFilter(String ViewName, String FilterType){
        if (GetTriFilterName(ViewName, FilterType).equals("Off")){
            return Boolean.FALSE;
        }else{
            return Boolean.TRUE;
        }
    }
    public static Boolean TriFilterInclude(String ViewName, String FilterType){
        if (GetTriFilterName(ViewName, FilterType).equals("Include")){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }
    public static Boolean TriFilterExclude(String ViewName, String FilterType){
        if (GetTriFilterName(ViewName, FilterType).equals("Exclude")){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }

    public static void RemoveAllTriFilter(String ViewName, String FilterType){
        //clear the list associated with this Filter
        Flow.PropertyListRemoveAll(ViewName, GetFilterListProp(FilterType));
        //turn the filter state to Off
        Flow.SetOption(ViewName, GetFilterTypeProp(FilterType), "Off");
    }

    public static String GetFilterListProp(String FilterName){
        return Const.FlowFilters + Const.PropDivider + FilterName + "FilterList";
    }
    public static String GetFilterTypeProp(String FilterName){
        return Const.FlowFilters + Const.PropDivider + FilterName;
    }
}
