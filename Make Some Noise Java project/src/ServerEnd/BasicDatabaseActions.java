package ServerEnd;

import Exceptions.Accounts.*;
import BackEnd.Accounts.Project;

import javax.swing.plaf.basic.BasicIconFactory;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/* Accounts table layout:
ID, username, password, email, list of project ids
the list of project ids is stored in the string form: '23, 234, 1324, 1232'*/

/* Project table layout:
ID, Title, username, date created, status, struct with project info, thumbnail, list of tags */


/** Back-end class that interfaces with the script to connect to the SQL server. */

public class BasicDatabaseActions {

    private static void assertFormat(String[] values) throws InvalidInputException {
        try {
            for (String item : values) {
                assert (item != null);
                assert (!item.isEmpty());
                assert (!item.equals(" "));
            }
        } catch (AssertionError e){
            throw new InvalidInputException("Invalid input.");
        }
    }

    private static boolean checkForDuplicateAccounts(String value) throws SQLException, DatabaseConnectionException {
        // Check if duplicate account
        List<Map<String, String>> rs = SQLConnection.select("accounts", "username = " + value + ", COUNT(*) as count", new String[]{}, new String[]{}, null);
        for (Map<String,String> m : rs){
            int count = Integer.parseInt(m.get("count"));
            if (count > 0) return true;
        }
        return false;
    }

    /* -------------------------------------------------------------------------------------- */
    /* -------------------------------------------------------------------------------------- */
    /* -------------------------------------------------------------------------------------- */
    /* -------------------------------------- ACCOUNTS -------------------------------------- */
    /* -------------------------------------------------------------------------------------- */
    /* -------------------------------------------------------------------------------------- */
    /* -------------------------------------------------------------------------------------- */

    /** Account:View
     * If a user is logged in, they may choose to view their account. They will be able to see their projects
     * saved on the server and the associated title, image preview, and tags. They will also be able to see their
     * biographical field. */
    public static String getAccountInfoType(int ID, String type) throws SQLException, DatabaseConnectionException, InvalidInputException {
        BasicDatabaseActions.assertFormat(new String[]{type});
        List<Map<String, String>> rs = SQLConnection.select("accounts", type, new String[]{"ID"}, new String[]{""+ID}, null);
        return rs.getFirst().get(type);
    }

    public static int createNewAccount(String username, String password, String email) throws SQLException, DatabaseConnectionException, DuplicateAccountException, InvalidInputException {
        BasicDatabaseActions.assertFormat(new String[]{username, password, email});

        if (BasicDatabaseActions.checkForDuplicateAccounts(username))
            throw new DuplicateAccountException("Account with username: " + username + " already exists.");

        // insert into table
        int ID = SQLConnection.insert("accounts",
                new String[] {"username",
                        "password",
                        "email"
                },
                new String[]{username, password, email});
        return ID;
    }

    public static int signIn(String username, String password) throws DatabaseConnectionException, NoSuchAccountException, IncorrectPasswordException, InvalidInputException {
        BasicDatabaseActions.assertFormat(new String[]{username, password});

        List<Map<String, String>> rs = SQLConnection.select("accounts", "*", new String[]{"username"}, new String[]{username}, null);
        for (Map<String, String> m : rs){
            //assuming 1st col is ID, 2nd is username, 3rd is pwd
            if (m.get("password").equals(password)) return Integer.parseInt(m.get("ID"));
        }
        throw new IncorrectPasswordException("Your username or password is incorrect.");
    }

    public static void signOut(int ID) throws DatabaseConnectionException {
        // whatever keeps track of the current sign-in
    }

    /**  Account:Edit
     * for username, password, email */
    public static void modifyAccount(int ID, String fieldToEdit, String value) throws SQLException, InvalidInputException, DatabaseConnectionException, DuplicateAccountException {
        BasicDatabaseActions.assertFormat(new String[]{fieldToEdit, value});
        try {
            assert(fieldToEdit.equals("username") || fieldToEdit.equals("password") || fieldToEdit.equals("email"));
        } catch (AssertionError e){
            throw new InvalidInputException("Invalid input.");
        }

        // check if username already exists
        if (fieldToEdit.equals("username") && BasicDatabaseActions.checkForDuplicateAccounts(value))
            throw new DuplicateAccountException("Account with username: " + value + " already exists.");

        SQLConnection.update("accounts", ID, fieldToEdit, value);

    }

    /** Account:Delete */
    public static void deleteAccount(int ID) throws DatabaseConnectionException {
        SQLConnection.delete("accounts", "ID", ""+ID);
    }

    /* -------------------------------------------------------------------------------------- */
    /* -------------------------------------------------------------------------------------- */
    /* -------------------------------------------------------------------------------------- */
    /* -------------------------------------- PROJECTS -------------------------------------- */
    /* -------------------------------------------------------------------------------------- */
    /* -------------------------------------------------------------------------------------- */
    /* -------------------------------------------------------------------------------------- */

    //Title, username, date created, status, struct with project info, thumbnail, list of tags
    /** Open project: Get project info */
    public static String getProjectInfoType(int ID, String type) throws InvalidInputException, SQLException, DatabaseConnectionException{
        BasicDatabaseActions.assertFormat(new String[]{type});
        List<Map<String, String>> rs = SQLConnection.select("projects", type, new String[]{"ID"},new String[]{""+ID}, null);
        return rs.getFirst().get(type);
    }

    public static int createNewProject(int accountID, String JSON) throws SQLException, DatabaseConnectionException, InvalidInputException {
        BasicDatabaseActions.assertFormat(new String[]{JSON});

        //make a new project, get the ID
        int ID = SQLConnection.insert("projects",
            new String[] {
                    "title",
                    "username",
                    "dateCreated",
                    "status",
                    "projectInfoStruct",
                    "thumbnail",
                    "tags"
            },
            new String[] {
                    "",
                    BasicDatabaseActions.getAccountInfoType(accountID, "username"),
                    "date created",
                    "private",
                    JSON,
                    "thumbnail",
                    "[]"
            });
/////////////////////////////////
        // Step 2: Fetch the existing projects list from the 'accounts' table
        List<Map<String, String>> existingProjectsRs = SQLConnection.select("accounts", "projectList", new String[]{"ID"}, new String[]{""+accountID}, null);

        // Step 3: Prepare the updated projects list (including the new project ID)
        String updatedProjects = "[]";
        //for (Map<String, String> m : existingProjectsRs) {
        if (!existingProjectsRs.isEmpty()) {
            String currentProjects = existingProjectsRs.getFirst().get("projectList"); // Assuming 'projects' is a string field or JSON
            if (currentProjects != null && !currentProjects.isEmpty()) {
                // If there are existing projects, append the new project ID to the list
                // Remove brackets and whitespace
                String trimmed = currentProjects.substring(1, currentProjects.length() - 1).trim();
                // Append the new ID correctly
                if (trimmed.isEmpty()) {
                    updatedProjects = "[" + ID + "]";
                } else {
                    updatedProjects = "[" + trimmed + ", " + ID + "]";
                }
            } else {
                // If no existing projects, start the list with the new project ID
                updatedProjects = "[" + String.valueOf(ID) + "]";
            }
        }
        // update new project list
        SQLConnection.update("accounts", accountID, "projectList", updatedProjects);
        return ID;
    }

    /**  Project:Edit
     * Title, username, status, thumbnail, list of tags */
    public static void modifyProject(int ID, String fieldToEdit, String value) throws InvalidInputException, DatabaseConnectionException {
        BasicDatabaseActions.assertFormat(new String[]{fieldToEdit, value});
        try {
            assert(fieldToEdit.equals("status")
                    || fieldToEdit.equals("thumbnail")
                    || fieldToEdit.equals("tags"));
        } catch (AssertionError e){
            throw new InvalidInputException("Invalid input.");
        }
        SQLConnection.update("projects", ID, fieldToEdit, value);
    }

    public static void deleteProject(int ID) throws DatabaseConnectionException {
        SQLConnection.delete("projects", "ID", ""+ID);
    }


    /** Compares current state of project to the save. Returns false if different, true if the same.*/
    public static boolean compareToCurrentSave(int projectID, String currentData) throws SQLException, InvalidInputException, DatabaseConnectionException{
        BasicDatabaseActions.assertFormat(new String[]{currentData});

        // Check if duplicate account
        /* SELECT ID, projectStruct, COUNT(*) as count
            FROM projects
            WHERE ID = ? AND projectStruct = ?
            GROUP BY ID, projectStruct;
            */
        List<Map<String, String>> rs = SQLConnection.select("projects", "ID, projectInfoStruct, COUNT(*) as count",
                new String[]{"ID", "projectStruct"}, new String[]{""+projectID, currentData}, new String[]{"ID", "projectInfoStruct"});
        for (Map<String, String> m : rs){
            int count = Integer.parseInt(m.get("count"));
            if (count > 0) return true;
        }
        return false;
    }

    public static void saveProject(int projectID, String currentData) throws InvalidInputException, DatabaseConnectionException{
        BasicDatabaseActions.assertFormat(new String[]{currentData});
        SQLConnection.update("projects", projectID, "projectInfoStruct", currentData);
    }


    /* -------------------------------------------------------------------------------------- */
    /* -------------------------------------------------------------------------------------- */
    /* -------------------------------------------------------------------------------------- */
    /* -------------------------------------- SHARING --------------------------------------- */
    /* -------------------------------------------------------------------------------------- */
    /* -------------------------------------------------------------------------------------- */
    /* -------------------------------------------------------------------------------------- */


    /** Sharing:PatternSearch
     * If a user is logged in and connected to the internet, they may search the server for posts. They will be
     * prompted to enter search terms. The application will show the user a list of public posts with tags and
     * titles that match the search terms. */
    public static List<Integer> searchBy(String[] toSearchBy, String[] value) throws SQLException, InvalidInputException, DatabaseConnectionException{
        BasicDatabaseActions.assertFormat(toSearchBy);
        BasicDatabaseActions.assertFormat(value);
        List<Map<String, String>> rs = SQLConnection.select("projects", "ID", toSearchBy, value, null);
        List<Integer> projectIDs = new ArrayList<>();
        for (Map<String, String> m : rs){
            projectIDs.add(Integer.parseInt(m.get("ID")));
        }
        return projectIDs;
    }

    /** Sharing:unpublish */
    public static void unpublishProject(int projectID) throws DatabaseConnectionException{
        SQLConnection.update("projects", projectID,"status","private");
    }
}