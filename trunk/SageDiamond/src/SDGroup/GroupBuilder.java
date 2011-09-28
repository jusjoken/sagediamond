/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SDGroup;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.apache.log4j.Logger;

/**
 *
 * @author SBANTA
 * @author JUSJOKEN
 * - 9/29/2011 - minor code flow updating, log message changes for testing
 */
public class GroupBuilder {

    static private final Logger LOG = Logger.getLogger(GroupBuilder.class);


    public static Object GetAllVideosGrouped(String PropertyAdder,String Method){
        LOG.info("GettingAllVideosGroupedby "+Method);
        return  sagex.api.Database.Sort(GetAllVideosForGroup(PropertyAdder,Method,GetFolderType(Method)),false,"SDGroup_GroupHandler_GetGroupName");
    }

    public static Object GetAllVideosByGenre(String PropertyAdder){
        String Method = "GetShowCategory";
        LOG.info("GettingAllVideosByGenre");
        return  sagex.api.Database.Sort(GetAllVideosForGroup(PropertyAdder,Method,"Genre"),false,"SDGroup_GroupHandler_GetGroupName");
    }

    public static List<GroupObject> GetAllVideosByYear(String PropertyAdder){
        String Method= "GetShowYear";
        return GetAllVideosForGroup(PropertyAdder,Method,"Year");
    }

    public static List<GroupObject> GetAllVideosForGroup(String PropertyAdder,String Method,String Folder){
        LOG.debug("Method = '" + Method + "' Folder = '" + Folder + "'");
        ArrayList<GroupObject> Grouped=new ArrayList<GroupObject>();
        //     Method = "PloxeeTV_MetadataCalls_"+Method;
        Object[] AllCurrentFiles = (Object[]) Groups.GetAllVidsByTitle(PropertyAdder, "GettingFiles", Folder,null);
        LOG.debug("AllCurrentFiles size = '" + AllCurrentFiles.length + "'");
        
        for(Object Curr:AllCurrentFiles){
            LOG.debug("AllCurrent: = '" + sagex.api.ShowAPI.GetShowTitle(Curr) + "' Curr = '" + Curr + "'");
        }
        
        //HashMap<String,Object> Cats= (HashMap<String, Object>) sagex.api.Database.GroupByMethod(AllCurrentFiles,Method);
        
        HashMap<String,Vector> Cats= (HashMap<String, Vector>) sagex.api.Database.GroupByMethod(AllCurrentFiles,Method);
        LOG.debug("Cats size = '" + Cats.size() + "'");
        Iterator AllCats = Cats.keySet().iterator();
        Integer counter = 0;
        while(AllCats.hasNext()){
            counter++;
            GroupObject Group=new GroupObject();
            Object CurrentCategory=AllCats.next();
            LOG.debug("Getting and building group " + counter + " = '" + CurrentCategory + "'");
            HashMap<String,HashMap<String,Vector>> GroupedShows =(HashMap<String, HashMap<String, Vector>>) Groups.GetAllVidsByTitle(PropertyAdder,Method,CurrentCategory,AllCurrentFiles);
            if(GroupedShows.size()>0){
                LOG.debug("Done building group "+CurrentCategory+ "Go Ahead and Build Group Object");
                Group.setName(CurrentCategory);
                Group.setShows((HashMap<String, HashMap<String, Vector>>) GroupedShows);
                Group.setHashShows(GroupHandler.GetAllShows(Group));
                Group.setFanart(new File(ShowHandler.DefaultImageFolder+"\\Groups\\"+Folder+" Fanarts\\"+CurrentCategory+".jpg"));
                Group.setPoster(new File(ShowHandler.DefaultImageFolder+"\\Groups\\"+Folder+" Posters\\"+CurrentCategory+".png"));
                LOG.debug("Done building group object "+CurrentCategory+ "Go Ahead add to List");
                Grouped.add(Group);
            }
            else{
                LOG.debug("No Videos found for group "+CurrentCategory);}
        }
        LOG.debug("Done building all groups go ahead and return size="+Grouped.size());
        return Grouped;
    }

    public static String GetFolderType(String method){
        if(method.contains("Category")){
            return "Genre";
        }
        else if(method.contains("Actor")){
            return "Actor";
        }
        else if (method.contains("Year")){
            return "Year";
        }
        else if (method.contains("Folder")){
            return "Folder";
        }
        else if (method.contains("Rated")){
            return "Rated";
        }
        return "";
    }

}


    

