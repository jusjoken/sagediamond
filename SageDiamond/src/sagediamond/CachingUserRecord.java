/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sagediamond;

/**
 *
 * @author SBANTA
 */
public class CachingUserRecord {

    private static String SName = sagex.api.Global.IsClient()?"SageDiamondTeam"+sagex.api.Global.GetUIContextName():"SageDiamondTeam";

    public static void main(String[] args) {

        Object[] test = sagex.api.UserRecordAPI.GetAllUserRecords(SName);
        for (Object curr : test) {
            System.out.println(curr.toString());

            String[] currecorders = sagex.api.UserRecordAPI.GetUserRecordNames(curr);
            for (String cr : currecorders) {
                System.out.println("Curr=" + cr);
                System.out.println("Value=" + sagex.api.UserRecordAPI.GetUserRecordData(curr, cr));
            }
        }



    }

    public static void DeleteStoredLocations() {
        sagex.api.UserRecordAPI.DeleteAllUserRecords(SName);
    }

    public static Boolean HasStoredLocation(String ID, String Type) {
        Object Record = sagex.api.UserRecordAPI.GetUserRecord(SName, ID);
        if (Record == null) {
            return false;
        }

        String Curr = sagex.api.UserRecordAPI.GetUserRecordData(Record, Type);
        return Curr != null && !Curr.equals("");

    }

    public static String GetStoredLocation(String ID, String Type) {
        Object Record = sagex.api.UserRecordAPI.GetUserRecord(SName, ID);
        return sagex.api.UserRecordAPI.GetUserRecordData(Record, Type);

    }

    public static void setStoredLocation(String ID, String Type, String Location) {
        sagex.api.UserRecordAPI.AddUserRecord(SName, ID);
        Object Record = sagex.api.UserRecordAPI.GetUserRecord(SName, ID);
        sagex.api.UserRecordAPI.SetUserRecordData(Record, Type, Location);

    }
}
