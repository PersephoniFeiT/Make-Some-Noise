package ServerEnd;

import Exceptions.Accounts.*;
import BackEnd.Accounts.Project;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        ResultSet rs = SQLConnection.select("accounts", "username = " + value + ", COUNT(*) as count", "");
        while (rs.next()) {
            int count = rs.getInt("count");
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
        ResultSet rs = SQLConnection.select("accounts", type, "ID = " + ID);
        return rs.getString(type);
    }

    public static int createNewAccount(String username, String password, String email) throws SQLException, DatabaseConnectionException, DuplicateAccountException, InvalidInputException {
        BasicDatabaseActions.assertFormat(new String[]{username, password, email});

        if (BasicDatabaseActions.checkForDuplicateAccounts(username))
            throw new DuplicateAccountException("Account with username: " + username + " already exists.");

        // insert into table
        int ID = SQLConnection.insert("accounts", new String[]{username, password, email});
        return ID;
    }

    public static int signIn(String username, String password) throws DatabaseConnectionException, NoSuchAccountException, IncorrectPasswordException, InvalidInputException {
        BasicDatabaseActions.assertFormat(new String[]{username, password});

        try (ResultSet rs = SQLConnection.select("accounts", "username = '" + username+"'", "")){
            while (rs.next()) {
                //assuming 1st col is ID, 2nd is username, 3rd is pwd
                if (rs.getString(3).equals(password)) return rs.getInt(1);
            }
            throw new IncorrectPasswordException("Your username or password is incorrect.");
        } catch (SQLException e){
            throw new NoSuchAccountException("Username does not exist.");
        }
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

        SQLConnection.update("account", ID, fieldToEdit, value);

    }

    /** Account:Delete */
    public static void deleteAccount(int ID) throws DatabaseConnectionException {
        SQLConnection.delete("accounts", "ID = '" + ID +"'");
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
        ResultSet rs = SQLConnection.select("projects", type, "ID = " + ID);
        return rs.getString(type);
    }

    public static int createNewProject(int accountID, String JSON) throws SQLException, DatabaseConnectionException, InvalidInputException {
        BasicDatabaseActions.assertFormat(new String[]{JSON});

        //make a new project, get the ID
        int ID = SQLConnection.insert("projects", new String[] {"",
                BasicDatabaseActions.getAccountInfoType(accountID, "username"),
                "date created",
                "private",
                JSON,
                "thumbnail",
                ""
        });

        // Step 2: Fetch the existing projects list from the 'accounts' table
        ResultSet existingProjectsRs = SQLConnection.select("accounts", "projects", "ID = " + accountID);

        // Step 3: Prepare the updated projects list (including the new project ID)
        String updatedProjects = "";
        if (existingProjectsRs.next()) {
            String currentProjects = existingProjectsRs.getString("projects"); // Assuming 'projects' is a string field or JSON
            if (currentProjects != null && !currentProjects.isEmpty()) {
                // If there are existing projects, append the new project ID to the list
                updatedProjects = currentProjects + ", " + ID; // Assuming comma-separated values
            } else {
                // If no existing projects, start the list with the new project ID
                updatedProjects = String.valueOf(ID);
            }
        }
        // update new project list
        SQLConnection.update("accounts", accountID, "projects", updatedProjects);
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
        SQLConnection.update("account", ID, fieldToEdit, value);
    }

    public static void deleteProject(int ID) throws DatabaseConnectionException {
        SQLConnection.delete("projects", "ID = '" + ID +"'");
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
        ResultSet rs = SQLConnection.select("projects", "ID, projectInfoStruct, COUNT(*) as count",
                "WHERE ID = "+ projectID +" AND projectStruct = " +currentData +" GROUP BY ID, projectInfoStruct");
        while (rs.next()) {
            int count = rs.getInt("count");
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
    public static List<Integer> searchBy(String toSearchBy, String value) throws SQLException, InvalidInputException, DatabaseConnectionException{
        BasicDatabaseActions.assertFormat(new String[]{value});
        ResultSet rs = SQLConnection.select("projects", "ID", toSearchBy+ " = " + value
                + "AND status = 1");
        List<Integer> projectIDs = new ArrayList<>();
        while (rs.next()){
            projectIDs.add(rs.getInt("ID"));
        }
        return projectIDs;
    }

    public static List<Integer> searchByTag(String toSearchBy, String value) throws SQLException, InvalidInputException, DatabaseConnectionException{
        BasicDatabaseActions.assertFormat(new String[]{value});

        // Adjust pattern to match whole values in the list
        String searchPattern = "%[" + value + ",%]%" +   // Case: Middle of list
                "%[" + value + "]%" +     // Case: Single item
                "%, " + value + "]%" +    // Case: End of list
                "%[" + value + ",%";      // Case: Beginning of list

        ResultSet rs = SQLConnection.select("projects", "ID", toSearchBy+
                " LIKE " + searchPattern
                + " AND status = 1");
        List<Integer> projectIDs = new ArrayList<>();
        while (rs.next()){
            projectIDs.add(rs.getInt("ID"));
        }
        return projectIDs;
    }
}