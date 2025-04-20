package BackEnd.Accounts;

import Exceptions.*;
import ServerEnd.BasicDatabaseActions;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CurrentSession {
    private Integer signedIn;

    public CurrentSession(){
        signedIn = null;
    }

    public Integer getSignedIn() throws NotSignedInException {
        if (this.signedIn != null) return signedIn;
        else throw new NotSignedInException("");
    }

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

    public void CreateNewAccount(String username, String password, String email) {
        try {
            boolean admin = false;
            if (username.equals("Admin1") || username.equals("Admin2") || username.equals("Admin3")) admin = true;
            this.signedIn = BasicDatabaseActions.createNewAccount(username, password, email, admin);
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

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

    public void SignOut(){
        try {
            BasicDatabaseActions.signOut(getSignedIn());
            this.signedIn = null;
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    public void DeleteAccount(){
        try {
            BasicDatabaseActions.deleteAccount(getSignedIn());
            this.SignOut();
        } catch (Exception e){
            ExceptionHandler.handleException(e);
        }
    }

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

    public void ChangePassword(String password){
        try {
            BasicDatabaseActions.modifyAccount(getSignedIn(), "password", password);
            System.out.println("Password changed.");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

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

    public List<Integer> GetProjectsInAccount(){
        List<Integer> IDList = new ArrayList<>();
        try {
            String projectStringList = BasicDatabaseActions.getAccountInfoType(getSignedIn(), "projectList");
            String trimmed = projectStringList.substring(1, projectStringList.length() - 1).trim();
            List<String> IDStringList = new ArrayList<String>(Arrays.asList(trimmed.split(",")));
            for (int i = 0; i < IDStringList.size(); i++) IDList.add(i, Integer.parseInt(IDStringList.get(i).trim()));
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
        return IDList;
    }

    //////////////////////////////////////////////////////////////////

    public Project CreateNewProject() {
        Project p = new Project("New Project");
        return p;
    }

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

    public static void ChangeTitle(Project p, String title){
        p.title = title;
            //BasicDatabaseActions.modifyProject(ID, "title", title);
    }


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

    public static void ChangeTags(Project p, List<String> tags){
        p.tags = new ArrayList<String>(tags);
    }

    public static void ChangeThumbnail(Project p, String tn){
        p.thumbnail = tn;
    }

    public static void DeleteProject(Integer accountID, int projectID){
        try {
            BasicDatabaseActions.deleteProject(accountID, projectID);
        } catch (Exception e){
            ExceptionHandler.handleException(e);
        }
    }

    public static boolean isSaved(Project p){
        try {
            return BasicDatabaseActions.compareToCurrentSave(p.getID(), p.toJSONString());
        } catch (Exception e){
            ExceptionHandler.handleException(e);
        }
        return false;
    }

    public static Integer getProjectAccountID(Integer projectID){
        try {
            String IDstring = BasicDatabaseActions.getProjectInfoType(projectID, "accountID");
            return Integer.parseInt(IDstring);
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
        return null;
    }

    public static boolean isAdmin(Integer accountID){
        try {
            return BasicDatabaseActions.isAdmin(accountID);
        }catch (Exception e){
            ExceptionHandler.handleException(e);
        }
        return false;
    }
}
