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
import org.apache.log4j.Logger;

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
        LOG.debug("GetAllFolderRestrictions: = '" + ExclusionFolders + "'");
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

    public static Object[] RunFolderFilter(Object[] MediaFiles, String ViewName) {
        Map<String, Boolean> filters = GetAllFolderRestrictions(ViewName);
        Set Restrictions = filters.keySet();
        Boolean NeedAdder = filters.values().contains(true);
        ArrayList returnarray = new ArrayList<Object>();
        ArrayList excludearray=new ArrayList<Object>();

        for (Object curr : MediaFiles) {
            String path = sagex.api.MediaFileAPI.GetParentDirectory(curr).toString();
            for (Object currs : Restrictions) {
                Boolean Include = filters.get(currs);
                if (Include && path.contains(currs.toString()) && !returnarray.contains(curr)) {
//                    System.out.println("Include Filter Movie added="+path+";Current Restriction="+currs.toString());
                    returnarray.add(curr);
                }
                else if (!Include && path.contains(currs.toString())) {
//                 System.out.println("Exlude Filter Movie Removed="+path+";Current Restriction="+currs.toString());
                    if(returnarray.contains(curr)){
                    
                    returnarray.remove(curr);}
                    excludearray.add(curr);
                } else if (!NeedAdder&&!excludearray.contains(curr)) {
                    if (!returnarray.contains(curr)) {
//                     System.out.println("Movie added as end result="+path+";Current Restriction="+currs.toString());
                        returnarray.add(curr);
                    }
                }
            }
        }
        return returnarray.toArray();

    }

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
