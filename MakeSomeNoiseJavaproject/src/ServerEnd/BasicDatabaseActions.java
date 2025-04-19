package ServerEnd;

import BackEnd.Accounts.Project;
import Exceptions.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

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
        List<Map<String, String>> rs = SQLConnection.select("accounts",
                "username, COUNT(*) as count",
                new String[]{"username"},                 // WHERE username = ?
                new String[]{value},                      // value = "test", etc.
                new String[]{"username"} );
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
    public static String getAccountInfoType(int ID, String type) throws DatabaseConnectionException, InvalidInputException, NoSuchAccountException {
        BasicDatabaseActions.assertFormat(new String[]{type});
        List<Map<String, String>> rs = SQLConnection.select("accounts", type, new String[]{"ID"}, new String[]{""+ID}, null);
        if (rs.isEmpty() || rs.get(0).get(type) == null) throw new NoSuchAccountException("Cannot get account info of account that doesn't exist.");
        return rs.get(0).get(type);
    }

    public static boolean isAdmin(int ID) throws DatabaseConnectionException, InvalidInputException, NoSuchAccountException {
        List<Map<String, String>> rs = SQLConnection.select("accounts", "admin", new String[]{"ID"}, new String[]{""+ID}, null);
        if (rs.isEmpty() || rs.getFirst().get("admin") == null) throw new NoSuchAccountException("Cannot get account info of account that doesn't exist.");
        return (rs.getFirst().get("admin").equals("1"));
    }

    public static int createNewAccount(String username, String password, String email, boolean admin) throws SQLException, DatabaseConnectionException, DuplicateAccountException, InvalidInputException {
        BasicDatabaseActions.assertFormat(new String[]{username, password, email});

        if (BasicDatabaseActions.checkForDuplicateAccounts(username))
            throw new DuplicateAccountException("Account with username: " + username + " already exists.");

        // insert into table
        int ID = SQLConnection.insert("accounts",
                new String[] {"username",
                        "password",
                        "email",
                        "projectList",
                },
                new String[]{username, password, email, "[]"});
        SQLConnection.update("accounts", ID, "admin", (admin)?1:0 );
        return ID;
    }

    public static int signIn(String username, String password) throws DatabaseConnectionException, NoSuchAccountException, IncorrectPasswordException, InvalidInputException {
        BasicDatabaseActions.assertFormat(new String[]{username, password});

        List<Map<String, String>> rs = SQLConnection.select("accounts", "*", new String[]{"username"}, new String[]{username}, null);
        if (rs.isEmpty() || rs.get(0).isEmpty()) throw new NoSuchAccountException("There is no account with username " + username);
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
        if (rs.isEmpty() || rs.get(0).get(type) == null) throw new InvalidInputException("Cannot get project info of project that doesn't exist.");
        return rs.get(0).get(type);
    }

    public static int createNewProject(int accountID, String JSON) throws DatabaseConnectionException, InvalidInputException, NoSuchAccountException {
        BasicDatabaseActions.assertFormat(new String[]{JSON});

        Project p = Project.fromJSONtoProject(JSON);

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
                    p.title,
                    BasicDatabaseActions.getAccountInfoType(accountID, "username"),
                    p.dateCreated.toString(),
                    "private",
                    JSON,
                    "MakeSomeNoiseJavaproject/src/ImageSources/stockThumbnail.png",
                    p.tags.toString()
            });

        //update the JSON ID
        // update new project list
        String updatedJson;
        try {
            // Your original JSON string with "" as the key
            // Create Jackson ObjectMapper
            ObjectMapper mapper = new ObjectMapper();
            // Convert JSON string to Map
            Map<String, Object> jsonMap = mapper.readValue(JSON, Map.class);
            // Get the only entry with key ""
            Object value = jsonMap.remove("");  // remove the "" key
            // Put the new entry with actual project ID
            jsonMap.put(String.valueOf(ID), value);
            // Convert back to JSON string
            updatedJson = mapper.writeValueAsString(jsonMap);
        } catch (Exception e){
            updatedJson = JSON;
        }
        // Now you can store it in the DB
        SQLConnection.update("projects", ID, "projectInfoStruct", updatedJson);

        ////////////////////////////
        // update account list
        // Step 2: Fetch the existing projects list from the 'accounts' table
        List<Map<String, String>> existingProjectsRs = SQLConnection.select("accounts", "projectList", new String[]{"ID"}, new String[]{""+accountID}, null);

        // Step 3: Prepare the updated projects list (including the new project ID)
        String updatedProjects = "[]";
        //for (Map<String, String> m : existingProjectsRs) {
        if (!existingProjectsRs.isEmpty() && existingProjectsRs.get(0).get("projectList") != null) {
            String currentProjects = existingProjectsRs.get(0).get("projectList"); // Assuming 'projects' is a string field or JSON
            updatedProjects = addIDToStringList(currentProjects, ID);
        }

        // update new project list
        SQLConnection.update("accounts", accountID, "projectList", updatedProjects);
        return ID;
    }

    private static String addIDToStringList(String prevList, Integer ID){
        String updatedProjects = "[]";
        if (prevList != null && !prevList.isEmpty()) {
            // If there are existing projects, append the new project ID to the list
            // Remove brackets and whitespace
            String trimmed = prevList.substring(1, prevList.length() - 1).trim();
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
        return updatedProjects;
    }

    private static String removeIDFromStringList(String prevList, Integer ID){
        //String updatedProjects = "[]";
        if (prevList != null && !prevList.isEmpty()) {
            String trimmed = prevList.substring(1, prevList.length() - 1).trim();
            String[] IDListStrings = trimmed.split(", ");
            List<String> IDStringList = Arrays.asList(IDListStrings);

            List<Integer> IDList = new ArrayList<>();
            IDStringList.forEach(id -> IDList.add(Integer.parseInt(id.trim())));
            IDList.remove(ID);
            return IDList.toString();
        } else {
            return prevList;
        }
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

    public static void deleteProject(int accountID, int projectID) throws DatabaseConnectionException, InvalidInputException, NoSuchAccountException {
        SQLConnection.delete("projects", "ID", ""+projectID);

        // update new project list
        String prevProjects = BasicDatabaseActions.getAccountInfoType(accountID, "projectList");
        String updatedProjects = removeIDFromStringList(prevProjects, projectID);
        SQLConnection.update("accounts", accountID, "projectList", updatedProjects);
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

    public static void saveProject(Integer accountID, Integer projectID, String currentData) throws InvalidInputException, DatabaseConnectionException, NotSignedInException, NoSuchAccountException {
        BasicDatabaseActions.assertFormat(new String[]{currentData});

        if (accountID == null) throw new NotSignedInException("You must be signed in to save.");
        if (projectID == null){
            System.out.println("Creating new project in database.");
            int ID = BasicDatabaseActions.createNewProject(accountID, currentData);
            SQLConnection.update("projects", ID, "ID", ID+"");
            projectID = ID;
        }
        Project p = Project.fromJSONtoProject(currentData);
        SQLConnection.update("projects", projectID, "projectInfoStruct", currentData);
        SQLConnection.update("projects", projectID, "title", p.title);
        SQLConnection.update("projects", projectID, "username", BasicDatabaseActions.getAccountInfoType(accountID, "username"));
        SQLConnection.update("projects", projectID, "status", p.status);
        SQLConnection.update("projects", projectID, "thumbnail", p.thumbnail);
        SQLConnection.update("projects", projectID, "tags", p.tags.toString());
        System.out.println("Saved to database.");
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
    public static List<Integer> searchBy(String[] toSearchBy, String[] value) throws InvalidInputException, DatabaseConnectionException{
        BasicDatabaseActions.assertFormat(toSearchBy);
        BasicDatabaseActions.assertFormat(value);
        List<Map<String, String>> rs = SQLConnection.selectLike("projects", "ID", toSearchBy, value, null);
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