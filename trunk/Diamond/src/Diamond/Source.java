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
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.log4j.Logger;
import sagex.phoenix.factory.ConfigurableOption;
import sagex.phoenix.factory.Factory;
import sagex.phoenix.vfs.MediaResourceType;
import sagex.phoenix.vfs.filters.*;
import sagex.phoenix.vfs.groups.Grouper;
import sagex.phoenix.vfs.sorters.Sorter;
import sagex.phoenix.vfs.views.ViewFactory;
import sagex.phoenix.vfs.views.ViewFolder;
import sagex.phoenix.vfs.views.ViewPresentation;
import sagex.phoenix.Phoenix;
import sagex.phoenix.factory.IConfigurable;
import sagex.phoenix.vfs.IMediaFolder;

/**
 *
 * @author jusjoken
 */
public class Source {
    static private final Logger LOG = Logger.getLogger(Source.class);
    public static HashMap<String,String> InternalFilterTypes = new HashMap<String,String>();
    public static HashMap<String,String> InternalMediaTypeFilters = new HashMap<String,String>();
    public static HashMap<String,String> InternalGroupsList = new HashMap<String,String>();
    public static HashMap<String,String> InternalSortsList = new HashMap<String,String>();
    
    //add a SORT or GROUP including the Label to use optionally (internal Label will be used otherwise)
    public static void AddOrganizerType(String OrgName, String OrgType){
        AddOrganizerType(OrgName, OrgType, "");
    }
    public static void AddOrganizerType(String OrgName, String OrgType, String OrgLabel){
        IConfigurable thisOrganizer = null;
        SourceUI.OrganizerType thisType = null;
        if (OrgType.equals(SourceUI.OrganizerType.GROUP.toString())){
            thisOrganizer = phoenix.umb.CreateGrouper(OrgName);
            thisType = SourceUI.OrganizerType.GROUP;
        }else if (OrgType.equals(SourceUI.OrganizerType.SORT.toString())){
            thisOrganizer = phoenix.umb.CreateSorter(OrgName);
            thisType = SourceUI.OrganizerType.SORT;
        }
        if (thisOrganizer==null){
            //don't add the organizer as it is not valid
        }else{
            if (OrgLabel.equals("")){
                if (thisType.equals(SourceUI.OrganizerType.GROUP)){
                    Grouper tOrg = (Grouper) thisOrganizer;
                    OrgLabel = tOrg.getLabel();
                }else if (thisType.equals(SourceUI.OrganizerType.SORT)){
                    Sorter tOrg = (Sorter) thisOrganizer;
                    OrgLabel = tOrg.getLabel();
                }
            }
            if (thisType.equals(SourceUI.OrganizerType.GROUP)){
                InternalGroupsList.put(OrgName, OrgLabel);
            }else if (thisType.equals(SourceUI.OrganizerType.SORT)){
                InternalSortsList.put(OrgName, OrgLabel);
            }
        }
    }
    public static ArrayList<String> GetOrganizerGroups(){
        TreeMap<String,String> OrganizerTypesList = new TreeMap<String,String>();
        for (String tKey: InternalGroupsList.keySet()){
            OrganizerTypesList.put(InternalGroupsList.get(tKey), tKey);
        }
        return new ArrayList<String>(OrganizerTypesList.values());
    }
    public static String GetOrganizerName(String OrgName, String OrgType){
        //LOG.debug("GetOrganizerName: OrgName '" + OrgName + "' OrgType '" + OrgType + "'" );
        if (OrgType.equals(SourceUI.OrganizerType.GROUP.toString())){
            return GetOrganizerGroupName(OrgName);
        }else if (OrgType.equals(SourceUI.OrganizerType.SORT.toString())){
            return GetOrganizerSortName(OrgName);
        }else{
            return util.OptionNotFound;
        }
    }
    public static String GetOrganizerGroupName(String OrgName){
        if (InternalGroupsList.containsKey(OrgName)){
            return InternalGroupsList.get(OrgName);
        }
        return util.OptionNotFound;
    }
    public static ArrayList<String> GetOrganizerSorts(){
        TreeMap<String,String> OrganizerTypesList = new TreeMap<String,String>();
        for (String tKey: InternalSortsList.keySet()){
            OrganizerTypesList.put(InternalSortsList.get(tKey), tKey);
        }
        return new ArrayList<String>(OrganizerTypesList.values());
    }
    public static String GetOrganizerSortName(String OrgName){
        if (InternalSortsList.containsKey(OrgName)){
            return InternalSortsList.get(OrgName);
        }
        return util.OptionNotFound;
    }

    public static void AddMediaTypeFilter(String MediaType, String MediaTypeName){
        if (IsMediaTypeValid(MediaType)){
            InternalMediaTypeFilters.put(MediaType, MediaTypeName);
        }
    }
    public static Boolean IsMediaTypeValid(String MediaType){
        for (MediaResourceType t: MediaResourceType.values()){
            if (MediaType.equals(t.toString())){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
    public static ArrayList<String> GetMediaTypes(){
        TreeMap<String,String> MediaTypesList = new TreeMap<String,String>();
        for (String tKey: InternalMediaTypeFilters.keySet()){
            MediaTypesList.put(InternalMediaTypeFilters.get(tKey), tKey);
        }
        return new ArrayList<String>(MediaTypesList.values());
    }
    public static String GetMediaTypeName(String MediaType){
        if (InternalMediaTypeFilters.containsKey(MediaType)){
            return InternalMediaTypeFilters.get(MediaType);
        }
        return util.OptionNotFound;
    }
    
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
        //LOG.debug("ApplyFilters: '" + Flow.GetFlowName(ViewName) + "' Before Count = '" + phoenix.media.GetAllChildren(Folder).size() + "' Types '" + InternalFilterTypes + "'");
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
            String FilterValue = "";
            if (IsFilterTypeValid(FilterType)){
                if (HasTriFilter(ViewName, FilterName)){
                    String FilterTypeforCreate = FilterName;
                    //grab the value from the filtername if passed in - example "mediatype:tv"
                    //the filtername remains the same to differentiate the different filters in the properties
                    if (FilterName.contains(":")){
                        //LOG.debug("ApplyFilters: '" + Flow.GetFlowName(ViewName) + "' processing filter '" + FilterName + "' FilterType '" + FilterType + "' filter '" + FilterValue + "'");
                        FilterValue = FilterName.split(":")[1];
                        FilterTypeforCreate = FilterName.split(":")[0];
                        LOG.debug("ApplyFilters 1: '" + Flow.GetFlowName(ViewName) + "' processing filter '" + FilterName + "' FilterType '" + FilterType + "' filter '" + FilterValue + "'");
                    }
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
                        FilterValue = Flow.PropertyListasString(ViewName, Const.FlowFilters + Const.PropDivider + FilterName + "FilterList");
                    }
                    if (FilterType.equals("pql")){  //custom handling for these
                        if (FilterName.equals("rating")){
                            if (Flow.PropertyListCount(ViewName, GetFilterListProp(FilterName))>0){
                                FilterValue = BuildPQL(ViewName, FilterName, "Rated", "=", Boolean.FALSE);
                                FilterValue = FilterValue + " or " + BuildPQL(ViewName, FilterName, "ParentalRating", "=", Boolean.FALSE);
                            }
                        }else if (FilterName.equals("title")){
                            if (Flow.PropertyListCount(ViewName, GetFilterListProp(FilterName))>0){
                                FilterValue = BuildPQL(ViewName, FilterName, "Title", "=", Boolean.FALSE);
                            }
                        }
                    }
                    if (!FilterValue.equals("")){
                        LOG.debug("ApplyFilters: '" + Flow.GetFlowName(ViewName) + "' processing filter '" + FilterName + "' FilterType '" + FilterType + "' filter '" + FilterValue + "'");
                        tOption = phoenix.umb.GetOption(NewFilter, "value");
                        phoenix.opt.SetValue(tOption, FilterValue);
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
        //LOG.debug("ApplyFilters: '" + Flow.GetFlowName(ViewName) + "' After Count = '" + phoenix.media.GetAllChildren(Folder).size() + "'");
    }

    public static Filter ApplyFilters(String ViewName){
        //LOG.debug("ApplyFilters: '" + Flow.GetFlowName(ViewName) + "' Before Count = '" + phoenix.media.GetAllChildren(Folder).size() + "' Types '" + InternalFilterTypes + "'");
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
            String FilterValue = "";
            if (IsFilterTypeValid(FilterType)){
                if (HasTriFilter(ViewName, FilterName)){
                    String FilterTypeforCreate = FilterName;
                    //grab the value from the filtername if passed in - example "mediatype:tv"
                    //the filtername remains the same to differentiate the different filters in the properties
                    if (FilterName.contains(":")){
                        //LOG.debug("ApplyFilters: '" + Flow.GetFlowName(ViewName) + "' processing filter '" + FilterName + "' FilterType '" + FilterType + "' filter '" + FilterValue + "'");
                        FilterValue = FilterName.split(":")[1];
                        FilterTypeforCreate = FilterName.split(":")[0];
                        LOG.debug("ApplyFilters 1: '" + Flow.GetFlowName(ViewName) + "' processing filter '" + FilterName + "' FilterType '" + FilterType + "' filter '" + FilterValue + "'");
                    }
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
                        FilterValue = Flow.PropertyListasString(ViewName, Const.FlowFilters + Const.PropDivider + FilterName + "FilterList");
                    }
                    if (FilterType.equals("pql")){  //custom handling for these
                        if (FilterName.equals("rating")){
                            if (Flow.PropertyListCount(ViewName, GetFilterListProp(FilterName))>0){
                                FilterValue = BuildPQL(ViewName, FilterName, "Rated", "=", Boolean.FALSE);
                                FilterValue = FilterValue + " or " + BuildPQL(ViewName, FilterName, "ParentalRating", "=", Boolean.FALSE);
                            }
                        }else if (FilterName.equals("title")){
                            if (Flow.PropertyListCount(ViewName, GetFilterListProp(FilterName))>0){
                                FilterValue = BuildPQL(ViewName, FilterName, "Title", "=", Boolean.FALSE);
                            }
                        }
                    }
                    if (!FilterValue.equals("")){
                        LOG.debug("ApplyFilters: '" + Flow.GetFlowName(ViewName) + "' processing filter '" + FilterName + "' FilterType '" + FilterType + "' filter '" + FilterValue + "'");
                        tOption = phoenix.umb.GetOption(NewFilter, "value");
                        phoenix.opt.SetValue(tOption, FilterValue);
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
        return filter;
        //phoenix.umb.SetFilter(Folder, filter);
        //phoenix.umb.Refresh(Folder);
        //LOG.debug("ApplyFilters: '" + Flow.GetFlowName(ViewName) + "' After Count = '" + phoenix.media.GetAllChildren(Folder).size() + "'");
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

    public static void CleanFolderPresentation(ViewFolder Folder){
        //remove any existing groupers - there should not be any but to be safe
        while (phoenix.umb.GetGroupers(Folder).size()>0){
            Grouper curGrouper = phoenix.umb.GetGroupers(Folder).get(0);
            LOG.debug("GetGenres: removing existing grouper prior to adding one '" + curGrouper.getLabel() + "' Name '" + curGrouper.getName() + "'");
            phoenix.umb.RemoveGrouper(Folder, curGrouper);
        }
        //remove any existing sorters - there should not be any but to be safe
        while (phoenix.umb.GetSorters(Folder).size()>0){
            Sorter curSorter = phoenix.umb.GetSorters(Folder).get(0);
            LOG.debug("GetGenres: removing existing Sorters prior to adding one '" + curSorter.getLabel() + "' Name '" + curSorter.getName() + "'");
            phoenix.umb.RemoveSorter(Folder, curSorter);
        }
    }
    
    public static ArrayList<String> GetGenres(ViewFolder Folder){
        if (Folder==null){
            LOG.debug("GetGenres: request for null Folder returned empty list");
            return new ArrayList<String>();
        }
        //LOG.debug("GetGenres: first child check before '" + phoenix.media.GetTitle(phoenix.umb.GetChild(Folder, 0)) + "'");
        TreeSet<String> GenreList = new TreeSet<String>();
        CleanFolderPresentation(Folder);
        //add a group for genre
        Grouper NewGrouper = phoenix.umb.CreateGrouper("genre");
        ConfigurableOption tOption = phoenix.umb.GetOption(NewGrouper, "empty-foldername");
        phoenix.opt.SetValue(tOption, "NONE");
        phoenix.umb.SetChanged(NewGrouper);
        phoenix.umb.SetGrouper(Folder, NewGrouper);
        phoenix.umb.Refresh(Folder);
        //LOG.debug("GetGenres: first child check during '" + phoenix.media.GetTitle(phoenix.umb.GetChild(Folder, 0)) + "'");
        for (Object Item: phoenix.media.GetChildren(Folder)){
            //LOG.debug("GetGenres: proecessing '" + phoenix.media.GetTitle(Item) + "'");
            if (!phoenix.media.GetTitle(Item).equals("NONE")){
                GenreList.add(phoenix.media.GetTitle(Item));
            }
        }
        phoenix.umb.RemoveGrouper(Folder, NewGrouper);
        phoenix.umb.Refresh(Folder);
        //LOG.debug("GetGenres: first child check after '" + phoenix.media.GetTitle(phoenix.umb.GetChild(Folder, 0)) + "'");
        return new ArrayList<String>(GenreList);
    }
    
    //load or build a view from the saved Flow settings
    public static ViewFolder LoadView(String ViewName){
        SourceUI mySource = new SourceUI(ViewName);
        ViewFolder view = null;
        //check if the flow has a presentation saved
        // - if a presentation then build the view from the saved source plus apply the presentation and filters
        // - if no presentation - load the view and apply any filters
        if (mySource.HasUI()){
            ViewFactory vf = new ViewFactory();
            //set base view options
            vf.setName(mySource.Name());
            vf.getOption(ViewFactory.OPT_LABEL).value().set(mySource.Label());
            vf.getOption(ViewFactory.OPT_VISIBLE).value().set("false");
            //Optional attributes to set at the view level
            //TODO:need to get these from the Flow settings
            //process the options
            for (ConfigOption tConfig: mySource.ConfigOptions()){
                if (tConfig.IsSet()){
                    vf.getOption(tConfig.getName()).value().set(tConfig.GetValue());
                }
            }
            //add the source to the view
            ViewFactory source = null;
            try {
                source = (ViewFactory) Phoenix.getInstance().getVFSManager().getVFSViewFactory().getFactory(mySource.Source()).clone();
            } catch (Exception e) {
                LOG.debug("LoadView: unable to create source from '" + mySource.Source() + "'");
                return null;
            }
            vf.addViewSource((ViewFactory) source);
            //set presentations
            for (PresentationUI tUI: mySource.UIList()){
                ViewPresentation vp = new ViewPresentation(tUI.Level());
                if (HasFilter(ViewName)){
                    vp.getFilters().add(ApplyFilters(ViewName));
                }
                if (tUI.Group().HasContent()){
                    String tGroup = tUI.Group().Name();
                    Grouper grpr = phoenix.umb.CreateGrouper(tGroup);
                    //process the options
                    for (ConfigOption tConfig: tUI.Group().ConfigOptions()){
                        if (tConfig.IsSet()){
                            grpr.getOption(tConfig.getName()).value().set(tConfig.GetValue());
                        }
                    }
                    vp.getGroupers().add(grpr);
                }
                if (tUI.Sort().HasContent()){
                    String tSort = tUI.Sort().Name();
                    Sorter sort = phoenix.umb.CreateSorter(tSort);
                    //process the options
                    for (ConfigOption tConfig: tUI.Sort().ConfigOptions()){
                        if (tConfig.IsSet()){
                            sort.getOption(tConfig.getName()).value().set(tConfig.GetValue());
                        }
                    }
                    vp.getSorters().add(sort);
                }
                vf.addViewPresentations(vp);
                //LOG.debug("LoadView: Level '" + tUI.Level() + "' GroupBy '" + tGroup + "' SortBy '" + tSort + "'");
            }
            view = vf.create(null);
        }else{
            view = phoenix.umb.CreateView(mySource.Source());
            if (HasFilter(ViewName)){
                view.getFilters().add(ApplyFilters(ViewName));
                //ApplyFilters(ViewName, view);
                LOG.debug("LoadView: Aplying filters");
            }
            //TODO: test if these settings adjust an existing view
            for (ConfigOption tConfig: mySource.ConfigOptions()){
                if (tConfig.IsSet()){
                    view.getViewFactory().getOption(tConfig.getName()).value().set(tConfig.GetValue());
                }
            }
            //Refresh if required
            if (HasFilter(ViewName) || mySource.HasConfigOptionsSet()){
                phoenix.umb.Refresh(view);
                LOG.debug("LoadView: Refreshing view");
            }
            
            
        }
        //now apply the filters
        return view;
    }
    
    public static void BuildView(ViewFolder Folder) throws CloneNotSupportedException{
//        Factory<IMediaFolder> source = (Factory<IMediaFolder>) Phoenix.getInstance().getVFSManager().getVFSSourceFactory().getFactory("expression").clone();
//        source.getOption("expression").value().set("GetMediaFiles(\"TVDB\")");
        Factory<IMediaFolder> source = (Factory<IMediaFolder>) Phoenix.getInstance().getVFSManager().getVFSViewFactory().getFactory("gemstone.base.all").clone();
        
        ViewFactory vf = new ViewFactory();
        vf.setName("TitleLetterView");
        vf.addFolderSource(source);
        ViewPresentation vp = new ViewPresentation(0);
        //Grouper grpr = phoenix.umb.CreateGrouper("regextitle");
        FirstLetterTitleGrouper tgrpr = new FirstLetterTitleGrouper();
        Grouper grpr = new Grouper(tgrpr);
        //grpr.getOption("regex").value().set("^(?:(?:the|a|an)\\s+)?(\\S)");
        //grpr.getOption("ignore-all").value().set("true");
        grpr.getOption("ignore-the").value().set("true");
        //grpr.getOption("regex").value().set(".");
        vp.getGroupers().add(grpr);
        Sorter sort = phoenix.umb.CreateSorter("title");
        sort.getOption("ignore-all").value().set("true");
        vp.getSorters().add(sort);
        vf.addViewPresentations(vp);

        ViewPresentation vp2 = new ViewPresentation(1);
        Grouper grpr2 = phoenix.umb.CreateGrouper("show");
        grpr2.getOption("empty-foldername").value().set("NONE");
        vp2.getGroupers().add(grpr2);
        Sorter sort2 = phoenix.umb.CreateSorter("title");
        sort2.getOption("ignore-all").value().set("true");
        vp2.getSorters().add(sort2);
        vf.addViewPresentations(vp2);
        
        ViewFolder view = vf.create(null);
        //ViewFolder view = new ViewFolder(vf, 0, null, Folder); 
        LOG.debug("BuildView: view '" + view + "'");
        for (Object Item: phoenix.media.GetChildren(view)){
            LOG.debug("BuildView: level 1 '" + phoenix.media.GetTitle(Item) + "' Item '" + Item + "'");
            for (Object Item2: phoenix.media.GetChildren(Item)){
                LOG.debug("  BuildView: level 2 '" + phoenix.media.GetTitle(Item2) + "' Item2 '" + Item2 + "'");
            }
        }
    }
    
    public static ArrayList<String> GetRatings(ViewFolder Folder){
        if (Folder==null){
            LOG.debug("GetRatings: request for null Folder returned empty list");
            return new ArrayList<String>();
        }
        TreeSet<String> RatingList = new TreeSet<String>();
        CleanFolderPresentation(Folder);
        //add a group for show
        Grouper NewGrouper = phoenix.umb.CreateGrouper("parental-ratings");
        ConfigurableOption tOption = phoenix.umb.GetOption(NewGrouper, "empty-foldername");
        phoenix.opt.SetValue(tOption, "NONE");
        phoenix.umb.SetChanged(NewGrouper);
        phoenix.umb.SetGrouper(Folder, NewGrouper);
        phoenix.umb.Refresh(Folder);
        for (Object Item: phoenix.media.GetChildren(Folder)){
            String thisRating = phoenix.media.GetTitle(Item);
            //LOG.debug("GetRating: proecessing '" + phoenix.media.GetTitle(Item) + "' Ratings '" + thisRating + "' type '" + phoenix.media.GetId(Item) + "' Item '" +  Item + "'");
            if (!thisRating.equals("") && !thisRating.equals("null") && !thisRating.equals("NONE") && thisRating!=null && thisRating.equals(phoenix.media.GetId(Item))){
                RatingList.add(thisRating);
            }
        }
        phoenix.umb.RemoveGrouper(Folder, NewGrouper);
        phoenix.umb.Refresh(Folder);
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
    
    public static ArrayList<String> GetTags(){
        phoenix.umb.GetTags(false);
        TreeMap<String,String> TagList = new TreeMap<String,String>();
        for (String tKey: phoenix.umb.GetTags(false)){
            //see if the tag is valid for a ViewFactory
            if (!phoenix.umb.GetViewFactories (tKey).isEmpty()){
                TagList.put(phoenix.umb.GetTagLabel(tKey), tKey);
                LOG.debug("GetTags: Tag '" + tKey + "' Label '" + phoenix.umb.GetTagLabel(tKey) + "'");
            }
        }
        return new ArrayList<String>(TagList.values());
    }
}
