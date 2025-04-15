package BackEnd.Accounts;

import Exceptions.ExceptionHandler;
import ServerEnd.BasicDatabaseActions;

import java.util.ArrayList;
import java.util.List;

public class Sharing {
    public static List<Integer> SearchByTitle(String title){
        List<Integer> projectInfo = new ArrayList<>();
        try {
            projectInfo = BasicDatabaseActions.searchBy(new String[]{"title", "status"}, new String[]{title, "public"});
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
        return projectInfo;
    }

    public static List<Integer> SearchByUsername(String username){
        List<Integer> projectInfo = new ArrayList<>();
        try {
            projectInfo = BasicDatabaseActions.searchBy(new String[]{"username", "status"}, new String[]{username, "public"});
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
        return projectInfo;
    }

    ///TODO
    public static List<Integer> SearchByTag(String tag){
        List<Integer> projectInfo = new ArrayList<>();
        try {
            projectInfo = BasicDatabaseActions.searchBy(new String[]{"tags", "status"}, new String[]{tag, "public"});
            //projectInfo = BasicDatabaseActions.searchByTag("tags", tag);
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
        return projectInfo;
    }
}
