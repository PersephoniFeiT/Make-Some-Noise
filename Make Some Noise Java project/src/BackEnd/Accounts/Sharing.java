package BackEnd.Accounts;

import Exceptions.Accounts.ExceptionHandler;
import ServerEnd.BasicDatabaseActions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Sharing {
    public static List<Integer> SearchByTitle(String title){
        List<Integer> projectInfo = new ArrayList<>();
        try {
            projectInfo = BasicDatabaseActions.searchBy("title", title);
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
        return projectInfo;
    }

    public static List<Integer> SearchByUsername(String username){
        List<Integer> projectInfo = new ArrayList<>();
        try {
            projectInfo = BasicDatabaseActions.searchBy("username", username);
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
        return projectInfo;
    }

    public static List<Integer> SearchByTag(String tag){
        List<Integer> projectInfo = new ArrayList<>();
        try {
            projectInfo = BasicDatabaseActions.searchByTag("tags", tag);
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
        return projectInfo;
    }
}
