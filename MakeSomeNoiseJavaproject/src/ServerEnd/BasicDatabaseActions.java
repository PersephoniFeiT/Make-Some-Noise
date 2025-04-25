package ServerEnd;

import BackEnd.Accounts.Project;
import Exceptions.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

/* Accounts table layout:
ID, username, password, email, {@link List} of project ids
the {@link List} of project ids is stored in the {@link String} form: '23, 234, 1324, 1232'*/

/* Project table layout:
ID, Title, username, date created, status, struct with project info, thumbnail, {@link List} of tags */


/**
 * @author Maya Malavasi
 * Back-end class that interfaces with the {@link  SQLConnection} script, using the building blocks defined in that script to perform
 * more complex actions.*/

public class BasicDatabaseActions {

    /** Private helper function to assert the correct format of {@link String} inputs.
     * @param values {@link String} array of values to check the format of
     * @throws InvalidInputException if any of the values is null, empty, or " "*/
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

    /** Private helper function to check whether the username of an account has already been taken.
     * @param value {@link String} username to check for.
     * @return true if account username has a duplicate
     * @throws DatabaseConnectionException if there is a syntax or connection failure in the database*/
    private static boolean checkForDuplicateAccounts(String value) throws DatabaseConnectionException {
        // Check if duplicate account
        List<Map<String, Object>> rs = SQLConnection.select("accounts",
                "username, COUNT(*) as count",
                new String[]{"username"},                 // WHERE username = ?
                new String[]{value},                      // value = "test", etc.
                new String[]{"username"} );
        for (Map<String,Object> m : rs){
            int count = (int) m.get("count");
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

    /* Account:View
     * If a user is logged in, they may choose to view their account. They will be able to see their projects
     * saved on the server and the associated title, image preview, and tags. They will also be able to see their
     * biographical field.*/
    /** Function that allows other scripts to query for specific data stored in the accounts table in the database.
     * @param ID the unique int ID number for an account
     * @param type the {@link String} name of the column to query
     * @return the {@link String} value of the requested information.
     * @throws DatabaseConnectionException if there is a syntax or connection failure in the database
     * @throws InvalidInputException if there is an error in the type name
     * @throws NoSuchAccountException if the ID entered is invalid */
    public static String getAccountInfoType(int ID, String type) throws DatabaseConnectionException, InvalidInputException, NoSuchAccountException {
        BasicDatabaseActions.assertFormat(new String[]{type});
        List<Map<String, Object>> rs = SQLConnection.select("accounts", type, new String[]{"ID"}, new Object[]{ID}, null);
        Object result = rs.get(0).get(type);
        if (rs.isEmpty() || result == null) throw new NoSuchAccountException("Cannot get account info of account that doesn't exist.");
        if (result instanceof String)
            return (String)rs.get(0).get(type);
        else throw new InvalidInputException("Error in finding string information for type " + type);
    }

    /** Checks whether the account associated with the given ID is an admin account.
     * @param ID the unique int ID number for an account
     * @return true if the account associated with the ID is an admin
     * @throws DatabaseConnectionException if there is a syntax or connection failure in the database
     * @throws InvalidInputException if there is an error in finding the {@link String} information for admin.
     * @throws NoSuchAccountException if the ID entered is invalid */
    public static boolean isAdmin(int ID) throws DatabaseConnectionException, NoSuchAccountException, InvalidInputException {
        List<Map<String, Object>> rs = SQLConnection.select("accounts", "admin", new String[]{"ID"}, new Object[]{ID}, null);
        if (rs.isEmpty() || rs.getFirst().get("admin") == null) throw new NoSuchAccountException("Cannot get account info of account that doesn't exist.");
        if (rs.get(0).get("admin") instanceof Boolean)
            return ((Boolean)rs.get(0).get("admin"));
        else throw new InvalidInputException("Error in finding string information for admin.");
    }

    /** Creates a new account and stores it in the database.
     * @param username {@link String} username
     * @param password {@link String} password
     * @param email {@link String} email
     * @param admin boolean, true if account is an admin
     * @return the int ID associated with the new account.
     * @throws DatabaseConnectionException if there is a syntax or connection failure in the database
     * @throws InvalidInputException if there is an error finding the {@link String} information
     * @throws NoSuchAccountException if the ID entered is invalid */
    public static int createNewAccount(String username, String password, String email, boolean admin) throws DatabaseConnectionException, DuplicateAccountException, InvalidInputException {
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

    /** Sign in to account, provides checks for whether the entered information is correct
     * @param username {@link String}
     * @param password {@link String}
     * @return the {@link Integer} ID of the account, null if impossible.
     * @throws DatabaseConnectionException if there is a syntax or connection failure in the database
     * @throws InvalidInputException if there is an error finding the {@link String} information
     * @throws NoSuchAccountException if the username entered is invalid
     * @throws IncorrectPasswordException if the password entered is incorrect */
    public static Integer signIn(String username, String password) throws DatabaseConnectionException, NoSuchAccountException, IncorrectPasswordException, InvalidInputException {
        BasicDatabaseActions.assertFormat(new String[]{username, password});

        List<Map<String, Object>> rs = SQLConnection.select("accounts", "*", new String[]{"username"}, new Object[]{username}, null);
        if (rs.isEmpty() || rs.get(0).isEmpty()) throw new NoSuchAccountException("There is no account with username " + username);
        for (Map<String, Object> m : rs){
            //assuming 1st col is ID, 2nd is username, 3rd is pwd
            if (m.get("password").equals(password)) return (Integer) m.get("ID");
        }
        throw new IncorrectPasswordException("Your username or password is incorrect.");
    }

    /** Placeholder function for SignOut */
    public static void signOut(int ID) throws DatabaseConnectionException {
        // whatever keeps track of the current sign-in
    }

    /** Edit the {@link String} values of the account info specified. Can currently only be used for username, password, and email
     * @param ID the unique int ID number for an account
     * @param fieldToEdit the {@link String} value of the column name to modify
     * @param value the new {@link String} value to change that entry to
     * @throws DatabaseConnectionException if there is a syntax or connection failure in the database
     * @throws InvalidInputException if there is an error finding the {@link String} information
     * @throws DuplicateAccountException if the user wishes to change the username to an existing username */
    public static void modifyAccount(int ID, String fieldToEdit, String value) throws InvalidInputException, DatabaseConnectionException, DuplicateAccountException {
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

    /** Deletes an account from the database
     * @param ID the unique int ID number for an account
     * @throws DatabaseConnectionException if there is a syntax or connection failure in the database */
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

    //Title, username, date created, status, struct with project info, thumbnail, {@link List} of tags
    /** Function that allows other scripts to query for specific data stored in the projects table in the database.
     * @param ID the unique int ID number for a project
     * @param type the {@link String} name of the column to query
     * @return the {@link String} value of the requested information.
     * @throws DatabaseConnectionException if there is a syntax or connection failure in the database
     * @throws InvalidInputException if there is an error in the type name */
    public static String getProjectInfoType(int ID, String type) throws InvalidInputException, DatabaseConnectionException{
        BasicDatabaseActions.assertFormat(new String[]{type});
        List<Map<String, Object>> rs = SQLConnection.select("projects", type, new String[]{"ID"},new Object[]{ID}, null);
        Object result = rs.get(0).get(type);
        if (rs.isEmpty() || result == null) throw new InvalidInputException("Cannot get project info of project that doesn't exist.");
        if (result instanceof String)
            return (String)result;
        else throw new InvalidInputException("Error in finding string information for type " + type);
    }

    /** Get the ID of the account associated with that project.
     * @param ID the unique int ID number for the project
     * @return an {@link Integer} value of the account that created the project, null if impossible or a guest project.
     * @throws DatabaseConnectionException if there is a syntax or connection failure in the database
     * @throws InvalidInputException if there is an error in the type name*/
    public static Integer getProjectAccountID(int ID) throws InvalidInputException, DatabaseConnectionException{
        List<Map<String, Object>> rs = SQLConnection.select("projects", "accountID", new String[]{"ID"},new Object[]{ID}, null);
        Object result = rs.get(0).get("accountID");
        if (rs.isEmpty() || result == null) throw new InvalidInputException("Cannot get project info of project that doesn't exist.");
        if (result instanceof Integer)
            return (Integer)result;
        else throw new InvalidInputException("Error in finding string information for accountID.");
    }

    /** Creates a new project and stores it in the database.
     * @param accountID the unique int ID of the account that created the project
     * @param JSON the JSON {@link String} that stores the project info
     * @return the int ID associated with the new project.
     * @throws DatabaseConnectionException if there is a syntax or connection failure in the database
     * @throws InvalidInputException if there is an error finding the {@link String} information
     * @throws NoSuchAccountException if the ID entered is invalid */
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
                    "tags",
                    "accountID"
            },
            new Object[] {
                    p.title,
                    BasicDatabaseActions.getAccountInfoType(accountID, "username"),
                    p.dateCreated.toString(),
                    "private",
                    JSON,
                    "MakeSomeNoiseJavaproject/src/ImageSources/stockThumbnail.png",
                    p.tags.toString(),
                    accountID
            });

        //update the JSON ID
        // update new project {@link List}
        String updatedJson;
        try {
            // Your original JSON {@link String} with "" as the key
            // Create Jackson ObjectMapper
            ObjectMapper mapper = new ObjectMapper();
            // Convert JSON {@link String} to Map
            Map<String, Object> jsonMap = mapper.readValue(JSON, Map.class);
            // Get the only entry with key ""
            Object value = jsonMap.remove("");  // remove the "" key
            // Put the new entry with actual project ID
            jsonMap.put(String.valueOf(ID), value);
            // Convert back to JSON {@link String}
            updatedJson = mapper.writeValueAsString(jsonMap);
        } catch (Exception e){
            updatedJson = JSON;
        }
        // Now you can store it in the DB
        SQLConnection.update("projects", ID, "projectInfoStruct", updatedJson);

        ////////////////////////////
        // update account {@link List}
        // Step 2: Fetch the existing projects {@link List} from the 'accounts' table
        List<Map<String, Object>> existingProjectsRs = SQLConnection.select("accounts", "projectList", new String[]{"ID"}, new Object[]{accountID}, null);

        // Step 3: Prepare the updated projects {@link List} (including the new project ID)
        String updatedProjects = "[]";
        //for (Map<{@link String}, {@link String}> m : existingProjectsRs) {
        if (!existingProjectsRs.isEmpty() && existingProjectsRs.get(0).get("projectList") != null) {
            String currentProjects = (String)existingProjectsRs.get(0).get("projectList"); // Assuming 'projects' is a {@link String} field or JSON
            updatedProjects = addIDToStringList(currentProjects, ID);
        }

        // update new project {@link List}
        SQLConnection.update("accounts", accountID, "projectList", updatedProjects);
        return ID;
    }

    /** Private helper function that adds an ID to a {@link String} {@link List} to store in the database.
     * @param prevList {@link String} of the original {@link List} of IDs, in the format "[1, 2, 3]"
     * @param ID the {@link Integer} value of the new ID to add.
     * @return the new {@link String} {@link List} in the format "[1, 2, 3, 4]"
     */
    private static String addIDToStringList(String prevList, Integer ID){
        String updatedProjects = "[]";
        if (prevList != null && !prevList.isEmpty()) {
            // If there are existing projects, append the new project ID to the {@link List}
            // Remove brackets and whitespace
            String trimmed = prevList.substring(1, prevList.length() - 1).trim();
            // Append the new ID correctly
            if (trimmed.isEmpty()) {
                updatedProjects = "[" + ID + "]";
            } else {
                updatedProjects = "[" + trimmed + ", " + ID + "]";
            }
        } else {
            // If no existing projects, start the {@link List} with the new project ID
            updatedProjects = "[" + String.valueOf(ID) + "]";
        }
        return updatedProjects;
    }

    /** Private helper function that removes an ID to a {@link String} {@link List} to store in the database.
     * @param prevList {@link String} of the original {@link List} of IDs, in the format "[1, 2, 3, 4]"
     * @param ID the {@link Integer} value of the ID to remove.
     * @return the new {@link String} {@link List} in the format "[1, 2, 3]"
     */
    private static String removeIDFromStringList(String prevList, Integer ID){
        //{@link String} updatedProjects = "[]";
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

    /** Edit the {@link String} values of the account info specified. Can currently only be used with status, thumbnail, and tags
     * @param ID the unique int ID number for a project
     * @param fieldToEdit the {@link String} value of the column name to modify
     * @param value the new {@link String} value to change that entry to
     * @throws DatabaseConnectionException if there is a syntax or connection failure in the database
     * @throws InvalidInputException if there is an error finding the {@link String} information */
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

    /** Deletes a project from the database and from the {@link List} of projects under the associated account
     * @param accountID the unique int ID of the account associated with the project
     * @param projectID the unique int ID of the project to be deleted
     * @throws DatabaseConnectionException if there is a syntax or connection failure in the database
     * @throws InvalidInputException if there is an error finding the {@link String} information
     * @throws NoSuchAccountException if the accountID entered is invalid */
    public static void deleteProject(int accountID, int projectID) throws DatabaseConnectionException, InvalidInputException, NoSuchAccountException {
        SQLConnection.delete("projects", "ID", ""+projectID);

        // update new project {@link List}
        String prevProjects = BasicDatabaseActions.getAccountInfoType(accountID, "projectList");
        String updatedProjects = removeIDFromStringList(prevProjects, projectID);
        SQLConnection.update("accounts", accountID, "projectList", updatedProjects);
    }


    /** Compares current state of project to the JSON stored in the database.
     * @param projectID the int ID of the project
     * @param currentData the JSON {@link String} of the current data
     * @return Returns true if currentData and the JSON stored in the database are identical.
     * @throws DatabaseConnectionException if there is a syntax or connection failure in the database
     * @throws InvalidInputException if there is an error finding the {@link String} information */
    public static boolean compareToCurrentSave(int projectID, String currentData) throws InvalidInputException, DatabaseConnectionException{
        BasicDatabaseActions.assertFormat(new String[]{currentData});

        List<Map<String, Object>> rs = SQLConnection.select("projects", "ID, projectInfoStruct, COUNT(*) as count",
                new String[]{"ID", "projectStruct"}, new Object[]{projectID, currentData}, new String[]{"ID", "projectInfoStruct"});
        for (Map<String, Object> m : rs){
            int count = (Integer)m.get("count");
            if (count > 0) return true;
        }
        return false;
    }

    /** Save the state of the current open project to the database if the user has been signed in, update the stored JSON
     * and all project-related information
     * @param accountID the unique int ID number for an account
     * @param projectID the unique int ID number for the open project
     * @param currentData the JSON {@link String} that stores the current project data
     * @throws DatabaseConnectionException if there is a syntax or connection failure in the database
     * @throws InvalidInputException if there is an error finding the {@link String} information
     * @throws NotSignedInException if the accountID does not match the project's stored accountID
     * @throws NoSuchAccountException if the accountID does not exist in the database */
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


    /* If a user is logged in and connected to the internet, they may search the server for posts. They will be
     * prompted to enter search terms. The application will show the user a {@link List} of public posts with tags and
     * titles that match the search terms. */
    /** Searches the database and returns a {@link List} of {@link Integer} IDs of projects that match the inputted conditions.
     * @param toSearchBy the {@link String} array of columns to search the values of
     * @param value the {@link String} array of values to search within the columns, in column order
     * @return a {@link List} of {@link Integer}s of projects that roughly match the search conditions
     * @throws DatabaseConnectionException if there is a syntax or connection failure in the database
     * @throws InvalidInputException if there is an error finding the {@link String} information
     * */
    public static List<Integer> searchBy(String[] toSearchBy, Object[] value) throws InvalidInputException, DatabaseConnectionException{
        BasicDatabaseActions.assertFormat(toSearchBy);
        List<Map<String, Object>> rs = SQLConnection.selectLike("projects", "ID", toSearchBy, value, null);
        List<Integer> projectIDs = new ArrayList<>();
        for (Map<String, Object> m : rs){
            projectIDs.add((Integer)(m.get("ID")));
        }
        return projectIDs;
    }
}