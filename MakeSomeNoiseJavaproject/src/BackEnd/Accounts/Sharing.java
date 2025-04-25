package BackEnd.Accounts;

import Exceptions.ExceptionHandler;
import ServerEnd.BasicDatabaseActions;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Maya Malavasi
 * Provides the specific static methods for any search operations on shared projects in the forum/database. Uses LIKE
 * instead of an equality check on SQL calls.
 */
public class Sharing {
    /** Searches the database for projects whose titles contain the given substring.
     * @param title the substring to check for
     * @return a List of {@link Integer} IDs of projects matching the search parameters. */
    public static List<Integer> SearchByTitle(String title){
        List<Integer> projectInfo = new ArrayList<>();
        try {
            projectInfo = BasicDatabaseActions.searchBy(new String[]{"title", "status"}, new Object[]{title, "public"});
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
        return projectInfo;
    }

    /** Searches the database for projects whose usernames contain the given substring.
     * @param username the substring to check for
     * @return a List of {@link Integer} IDs of projects matching the search parameters. */
    public static List<Integer> SearchByUsername(String username){
        List<Integer> projectInfo = new ArrayList<>();
        try {
            projectInfo = BasicDatabaseActions.searchBy(new String[]{"username", "status"}, new Object[]{username, "public"});
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
        return projectInfo;
    }

    /** Searches the database for projects whose tags contain the given substring. Only matches one single tag at a time.
     * @param tag the tag substring to check for
     * @return a List of {@link Integer} IDs of projects matching the search parameters. */
    public static List<Integer> SearchByTag(String tag){
        List<Integer> projectInfo = new ArrayList<>();
        try {
            projectInfo = BasicDatabaseActions.searchBy(new String[]{"tags", "status"}, new Object[]{tag, "public"});
            //projectInfo = BasicDatabaseActions.searchByTag("tags", tag);
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
        return projectInfo;
    }
}
