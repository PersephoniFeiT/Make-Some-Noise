package ServerEnd;

import Exceptions.Accounts.*;

import java.sql.ResultSet;
import java.sql.SQLException;

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

    private static boolean checkForDuplicates(String tableName, String column, String value) throws DatabaseConnectionException {
        // Check if duplicate account
        try (ResultSet rs = SQLConnection.select(tableName, column+ " = " + value + ", COUNT(*) as count", "")) {
            while (rs.next()) {
                int count = rs.getInt("count");
                if (count > 0) return true;
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e.getMessage());
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
    public static String getUsername(int ID) throws DatabaseConnectionException{
        try {
            ResultSet rs = SQLConnection.select("accounts", "username", "ID = " + ID);
            return rs.getString("username");
        } catch (SQLException e){
            throw new DatabaseConnectionException(e.getMessage());
        }
    }

    public static String getEmail(int ID) throws DatabaseConnectionException{
        try {
            ResultSet rs = SQLConnection.select("accounts", "email", "ID = " + ID);
            return rs.getString("email");
        } catch (SQLException e){
            throw new DatabaseConnectionException(e.getMessage());
        }
    }

    public static void createNewAccount(String username, String password, String email) throws DatabaseConnectionException, DuplicateAccountException, InvalidInputException {
        BasicDatabaseActions.assertFormat(new String[]{username, password, email});

        if (BasicDatabaseActions.checkForDuplicates("accounts", "username", username))
            throw new DuplicateAccountException("Account with username: " + username + " already exists.");

        // insert into table
        SQLConnection.insert("accounts", new String[]{username, password, email});
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
    public static void modifyAccount(int ID, String fieldToEdit, String value) throws InvalidInputException, DatabaseConnectionException, DuplicateAccountException {
        BasicDatabaseActions.assertFormat(new String[]{fieldToEdit, value});
        try {
            assert(fieldToEdit.equals("username") || fieldToEdit.equals("password") || fieldToEdit.equals("email"));
        } catch (AssertionError e){
            throw new InvalidInputException("Invalid input.");
        }

        // check if username already exists
        if (fieldToEdit.equals("username") && BasicDatabaseActions.checkForDuplicates("accounts", "username", value))
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
    public static String getProjectTitle(int ID) throws DatabaseConnectionException{
        try {
            ResultSet rs = SQLConnection.select("projects", "title", "ID = " + ID);
            return rs.getString("title");
        } catch (SQLException e){
            throw new DatabaseConnectionException(e.getMessage());
        }
    }

    /** Open project: Get project info */
    public static String getProjectUser(int ID) throws DatabaseConnectionException{
        try {
            ResultSet rs = SQLConnection.select("projects", "username", "ID = " + ID);
            return rs.getString("username");
        } catch (SQLException e){
            throw new DatabaseConnectionException(e.getMessage());
        }
    }

    /** Open project: Get project info */
    public static String getProjectDateCreated(int ID) throws DatabaseConnectionException{
        try {
            ResultSet rs = SQLConnection.select("projects", "dateCreated", "ID = " + ID);
            return rs.getString("dateCreated");
        } catch (SQLException e){
            throw new DatabaseConnectionException(e.getMessage());
        }
    }

    /** Open project: Get project info */
    public static String getProjectStatus(int ID) throws DatabaseConnectionException{
        try {
            ResultSet rs = SQLConnection.select("projects", "status", "ID = " + ID);
            return rs.getString("status");
        } catch (SQLException e){
            throw new DatabaseConnectionException(e.getMessage());
        }
    }

    /** Open project: Get project info */
    public static String getProjectData(int ID) throws DatabaseConnectionException{
        try {
            ResultSet rs = SQLConnection.select("projects", "projectInfoStruct", "ID = " + ID);
            return rs.getString("projectInfoStruct");
        } catch (SQLException e){
            throw new DatabaseConnectionException(e.getMessage());
        }
        /// TODO
        /// translate from JSON to java

    }


    public static void createNewProject(String title, int accountID) throws DatabaseConnectionException, InvalidInputException {
        BasicDatabaseActions.assertFormat(new String[]{title});

        //make a new project, get the ID
        int ID = SQLConnection.insert("projects", new String[] {title});

        String defaultProject = "{"+ID+": {" +
                "layer1: {}" +
                "}}";
        // insert into table

        // Step 2: Fetch the existing projects list from the 'accounts' table
        ResultSet existingProjectsRs = SQLConnection.select("accounts", "projects", "ID = " + accountID);

        // Step 3: Prepare the updated projects list (including the new project ID)
        String updatedProjects = "";
        try {
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

        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error fetching existing projects list: " + e.getMessage());
        }
    }

    public static void deleteProject(int ID) throws DatabaseConnectionException {
        SQLConnection.delete("projects", "ID = '" + ID +"'");
    }


    /** Compares current state of project to the save. Returns false if different, true if the same.*/
    public static boolean compareToCurrentSave(int projectID, String currentData) throws InvalidInputException, DatabaseConnectionException{
        BasicDatabaseActions.assertFormat(new String[]{currentData});

        // Check if duplicate account
        /* SELECT ID, projectStruct, COUNT(*) as count
            FROM projects
            WHERE ID = ? AND projectStruct = ?
            GROUP BY ID, projectStruct;
            */
        try (ResultSet rs = SQLConnection.select("projects", "ID, projectInfoStruct, COUNT(*) as count",
                "WHERE ID = "+ projectID +" AND projectStruct = " +currentData +" GROUP BY ID, projectInfoStruct")) {
            while (rs.next()) {
                int count = rs.getInt("count");
                if (count > 0) return true;
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e.getMessage());
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

    /** Sharing:UserSearch
     * If a user is logged in and connected to the internet, they may search the server for users. They will be
     * prompted to enter a search term. The application will show the user a list of users with usernames matching
     * the search term. */

    /** Sharing:unpublish */
}