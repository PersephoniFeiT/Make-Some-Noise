package BackEnd.Accounts;

import Exceptions.*;
import ServerEnd.BasicDatabaseActions;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Maya Malavasi
 * CurrentSession stores all the data required for data persistence in the current window and current editor, such as
 * signing in, unsaved changes in the window, etc. Interfaces directly with the panels and windows.
 */
public class CurrentSession {
    private Integer signedIn;

    /**
     * Initializes the current session information upon opening a new window. Signed in state starts as null
     */
    public CurrentSession(){
        signedIn = null;
    }

    /** Checks whether the user is signed in, and if so returns the account ID
     * @return {@link Integer}  account ID of signed-in user.
     * @throws NotSignedInException if user is not signed in
     */
    public Integer getSignedIn() throws NotSignedInException {
        if (this.signedIn != null) return signedIn;
        else throw new NotSignedInException("");
    }

    /** Check whether teh signed in user is an admin
     * @return true if an admin, false if not signed in or not an admin
     */
    public boolean isAdmin(){
        try {
            if (BasicDatabaseActions.isAdmin(this.getSignedIn())) return true;
        } catch (Exception e){
            ExceptionHandler.handleException(e);
            return false;
        }
        return false;
    }

    //////////////////////

    /** Creates a new account and stores new account data in database
     * @param username {@link String} username
     * @param password {@link String} password
     * @param email {@link String} email
     */
    public void CreateNewAccount(String username, String password, String email) {
        try {
            boolean admin = false;
            if (username.equals("Admin1") || username.equals("Admin2") || username.equals("Admin3")) admin = true;
            this.signedIn = BasicDatabaseActions.createNewAccount(username, password, email, admin);
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    /** Attempts a sign in for current session. If successful, saves the account ID to the currentSession variables.
     * @param username {@link String} username
     * @param password {@link String} password
     * @throws IncorrectPasswordException if password is incorrect
     * @throws NoSuchAccountException if username is not connected to any existing account
     * @throws InvalidInputException if username or password are empty, null, or " "
     */
    public void SignIn(String username, String password) throws IncorrectPasswordException, NoSuchAccountException, InvalidInputException {
        Integer ID = null;
        try {
            ID = BasicDatabaseActions.signIn(username, password);
        }
        catch (NoSuchAccountException e) {
            throw e;
        }
        catch (IncorrectPasswordException e) {
            throw e;
        }
        catch (InvalidInputException e) {
            throw e;
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
        this.signedIn = ID;
        System.out.println("You have successfully signed in as: " + username);
    }

    /** Signs out of current session. Reverts stored signIn data in CurrentSession instance to null.
     */
    public void SignOut(){
        try {
            BasicDatabaseActions.signOut(getSignedIn());
            this.signedIn = null;
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    /** Deletes the account entirely from the database. Must be signed in.
     */
    public void DeleteAccount(){
        try {
            BasicDatabaseActions.deleteAccount(getSignedIn());
            this.SignOut();
        } catch (Exception e){
            ExceptionHandler.handleException(e);
        }
    }

    /** Changes the username associated with the account ID
     * @param username {@link String} new username
     */
    public void ChangeUsername(String username){
        try {
            if (BasicDatabaseActions.getAccountInfoType(getSignedIn(), "username").equals("Admin1") ||
                (BasicDatabaseActions.getAccountInfoType(getSignedIn(), "username").equals("Admin2")) ||
                (BasicDatabaseActions.getAccountInfoType(getSignedIn(), "username").equals("Admin3")))
                return;
            BasicDatabaseActions.modifyAccount(getSignedIn(), "username", username);
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    /** Changes the password associated with the account ID
     * @param password {@link String} new password
     */
    public void ChangePassword(String password){
        try {
            BasicDatabaseActions.modifyAccount(getSignedIn(), "password", password);
            System.out.println("Password changed.");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    /** Changes the email associated with the account ID
     * @param email {@link String} new email. Must be in the format x@x.xxx
     */
    public void ChangeEmail(String email){
        try {
            Pattern pattern = Pattern.compile(".+@.+\\.(?:com|net|org|edu|gov|io|ai|xyz|site|me)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            boolean matchFound = matcher.find();
            if(matchFound) {
                BasicDatabaseActions.modifyAccount(getSignedIn(), "email", email);
            } else {
                ExceptionHandler.handleException(new InvalidInputException("Incorrect email format: '" + email +"' is not an email."));
            }
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    /** Retrieves the account information to display in the account profile page
     * @return {@link HashMap} of <{@link String}, {@link String}> pairs where the key is the name of the info and the object is the {@link String} value
     */
    public HashMap<String, String> GetAccountInfo() {
        HashMap<String, String> accountInfo = new HashMap<>();
        try {
            accountInfo.put("username", BasicDatabaseActions.getAccountInfoType(getSignedIn(), "username"));
            accountInfo.put("email", BasicDatabaseActions.getAccountInfoType(getSignedIn(), "email"));
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
        return accountInfo;
    }

    /** Retrieves a List of project IDs associated with current account
     * @return List of {@link {@link Integer} }  project IDs, or empty List [] */
    public List<Integer> GetProjectsInAccount(){
        List<Integer> IDList = new ArrayList<>();
        try {
            String projectStringList = BasicDatabaseActions.getAccountInfoType(getSignedIn(), "projectList");
            List<String> IDStringList = Arrays.stream(projectStringList.split("[\\[\\],\\s]"))
                    .filter(s -> !s.isEmpty())
                    .toList();
            //String trimmed = projectStringList.substring(1, projectStringList.length() - 1).trim();
            //List<String> IDStringList = new ArrayList<String>(Arrays.asList(trimmed.split(",")));
            for (int i = 0; i < IDStringList.size(); i++) IDList.add(i, Integer.parseInt(IDStringList.get(i)));
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
        return IDList;
    }

    //////////////////////////////////////////////////////////////////

    /** Creates a new {@link  Project} instance. Does not automatically save to database.
     * @return new {@link  Project} instance with default settings
     * */
    public Project CreateNewProject() {
        Project p = new Project("New Project");
        return p;
    }

    /**
     * Saves the project to database. Checks if signed in, makes sure all metadata is up to date
     * @param p {@link  Project} instance to save to database
     * @return true if successfully saved
     * @throws NotSignedInException if not signed in to account
     * */
    public boolean SaveProject(Project p) throws NotSignedInException {
        try {
            Integer accountID = this.getSignedIn();
            Integer projectID = p.getID();
            if (p.getID() == null) {
                projectID = BasicDatabaseActions.createNewProject(accountID, p.toJSONString());
                p.setID(projectID);
            }
            else {
                BasicDatabaseActions.saveProject(this.getSignedIn(), p.getID(), p.toJSONString());
            }
            BasicDatabaseActions.modifyProject(projectID, "title", p.title);
            BasicDatabaseActions.modifyProject(projectID, "username", BasicDatabaseActions.getAccountInfoType(accountID, "username"));
            BasicDatabaseActions.modifyProject(projectID, "status", p.status);
            BasicDatabaseActions.modifyProject(projectID, "tags", p.tags.toString());
            BasicDatabaseActions.modifyProject(projectID, "thumbnail", p.thumbnail);
            return true;
        } catch (NotSignedInException e){
            throw e;
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
        return false;
    }



    ///////////////////////////
    /** Static function, retrieves all project tags associated with the project, as a List of {@link String}s. A single tag can only be comprised
     * of letters, numbers, dashes, or underscores.
     * @param ID the project ID
     * @return List of {@link String}s with each tag*/
    public static List<String> getProjectTags(int ID) {
        try {
            String taglistString = BasicDatabaseActions.getProjectInfoType(ID, "tags");
            return Arrays.stream(taglistString.split("[^A-Za-z0-9_-]+"))
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
        return new ArrayList<>();
    }

    /** Static function, retrieves all project tags associated with the project, as a List of {@link String}s. A single tag can only be comprised
     * of letters, numbers, dashes, or underscores.
     * @param projectID the project ID
     * @return {@link Map} of  <{@link String}, {@link String}> pairs where the keys are the column names of the info
     * */
    public static Map<String, String> GetProjectInfo(int projectID) {
        Map<String, String> projectInfo = new HashMap<>();
        try {
            projectInfo.put("title", BasicDatabaseActions.getProjectInfoType(projectID, "title"));
            projectInfo.put("username", BasicDatabaseActions.getProjectInfoType(projectID, "username"));
            projectInfo.put("thumbnail", BasicDatabaseActions.getProjectInfoType(projectID, "thumbnail"));
            projectInfo.put("dateCreated", BasicDatabaseActions.getProjectInfoType(projectID, "dateCreated"));
            projectInfo.put("projectInfoStruct", BasicDatabaseActions.getProjectInfoType(projectID, "projectInfoStruct"));
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
        return projectInfo;
    }

    /** Static function, changes the {@link  Project} instance title. Does not store in database yet.
     * @param p {@link  Project} instance to modify
     * @param title {@link String} new title to change to */
    public static void ChangeTitle(Project p, String title){
        p.title = title;
            //BasicDatabaseActions.modifyProject(ID, "title", title);
    }

    /** Changes the {@link  Project} instance status, either "public" or "private". Does not store in database yet.
     * @param p {@link  Project} instance to modify
     * @param status {@link String} new status to change to, either "public" or "private" */
    public void ChangeStatus(Project p, String status){
        try {
            this.getSignedIn();
            if (!status.equals("public")) status = "private";
            p.status = status;
            //BasicDatabaseActions.modifyProject(ID, "status", status);
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    /** Changes the {@link  Project} instance tags to a given list. Does not store in database yet.
     * @param p {@link  Project} instance to modify
     * @param tags List of {@link String} tags to replace existing list */
    public static void ChangeTags(Project p, List<String> tags){
        p.tags = new ArrayList<String>(tags);
    }

    /** Changes the {@link  Project} instance thumbnail. Does not store in database yet.
     * @param p {@link  Project} instance to modify
     * @param tn the {@link String} path where the thumbnail image is stored */
    public static void ChangeThumbnail(Project p, String tn){
        p.thumbnail = tn;
    }

    /** Deletes project from account and from database.
     * @param accountID account ID associated with project (or if admin any account ID)
     * @param projectID project ID of project to delete */
    public static void DeleteProject(Integer accountID, int projectID){
        try {
            BasicDatabaseActions.deleteProject(accountID, projectID);
        } catch (Exception e){
            ExceptionHandler.handleException(e);
        }
    }

    /** Checks whether the project data in the database is up to date with an existing {@link  Project} instance
     * @param p {@link  Project} instance
     * @return true if up to date */
    public static boolean isSaved(Project p){
        try {
            return BasicDatabaseActions.compareToCurrentSave(p.getID(), p.toJSONString());
        } catch (Exception e){
            ExceptionHandler.handleException(e);
        }
        return false;
    }

    /** Returns the {@link Integer}  account ID associated with the given project
     * @param projectID the ID of the project
     * @return an {@link Integer}  value of account ID that created the project. Null if created by a guest */
    public static Integer getProjectAccountID(Integer projectID){
        try {
            Integer IDstring = BasicDatabaseActions.getProjectAccountID(projectID);
            return IDstring;
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
        return null;
    }

    /** checks whether the current account is an admin account
     * @param accountID account ID
     * @return true if the given account is an Admin */
    public static boolean isAdmin(Integer accountID){
        try {
            return BasicDatabaseActions.isAdmin(accountID);
        }catch (Exception e){
            ExceptionHandler.handleException(e);
        }
        return false;
    }
}
