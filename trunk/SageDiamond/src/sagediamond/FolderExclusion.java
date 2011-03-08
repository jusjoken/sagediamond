/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sagediamond;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author SBANTA
 */
public class FolderExclusion {

    public static void main(String[] args) {
//    File 3232=new File("C:\\TVFiles\\TestPath\\TV\\Battlestar Galactica (2003)&&true");
        String test = "C:\\TestMovies\\Blurays";
       
    }

    public static Map GetAllFolderRestrictions(String ViewName) {
        String ExclusionFolders = sagex.api.Configuration.GetProperty("JOrton/PathFilters/" + ViewName, "");
        System.out.println("Diamond Restriction Propery=" + ExclusionFolders);
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

    public static ArrayList<Object> RunFolderFilter(Object[] MediaFiles, String ViewName) {
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
                else if (!Include && path.contains(currs.toString()) && returnarray.contains(curr)) {
//                 System.out.println("Exlude Filter Movie Removed="+path+";Current Restriction="+currs.toString());
                    returnarray.remove(curr);
                    excludearray.add(curr);
                } else if (!NeedAdder&&!(!Include && path.contains(currs.toString()))&&!excludearray.contains(curr)) {
                    if (!returnarray.contains(curr)) {
//                     System.out.println("Movie added as end result="+path+";Current Restriction="+currs.toString());
                        returnarray.add(curr);
                    }
                }
            }
        }
        return returnarray;

    }

    public static boolean HasFilter(String ViewName, String Path, Boolean include) {
        String PropertyName = "JOrton/PathFilters/" + ViewName;
        String Element = Path + "&&" + include;
        if (sagex.api.Configuration.GetProperty(PropertyName, "").contains(Element + ";")) {
            return true;
        } else {
            return false;
        }
    }

    public static String SetFilter(String ViewName, String Path, Boolean include) {
        String PropertyName = "JOrton/PathFilters/" + ViewName;
        String Element = Path + "&&" + include;
        String CurrentElements = sagex.api.Configuration.GetProperty(PropertyName, "");
        String result = null;
        if (CurrentElements.contains(Element + ";")) {
            result = "0";

        } else {

            String NewElements = CurrentElements + Element + ";";
            sagex.api.Configuration.SetProperty(PropertyName, NewElements);
            result = "1";
        }
        return result;
    }

    public static String RemoveFilter(String ViewName, String Path, Boolean include) {
        String PropertyName = "JOrton/PathFilters/" + ViewName;
        String Element = Path + "&&" + include;
        String CurrElements = sagex.api.Configuration.GetProperty(PropertyName, "");
        String ElementRemoved = null;
        String result = null;
        if (CurrElements.contains(Element + ";")) {

            ElementRemoved = CurrElements.replace(Element + ";", "");

            sagex.api.Configuration.SetProperty(PropertyName, ElementRemoved);
            result = "1";
        } else {
            result = "0";
        }
        return result;

    }
}
