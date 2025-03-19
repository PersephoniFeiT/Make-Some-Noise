package BackEnd.Accounts;

import Exceptions.Accounts.DatabaseConnectionException;
import Exceptions.Accounts.ExceptionHandler;
import Exceptions.Accounts.InvalidInputException;
import Exceptions.Accounts.NotSignedInException;
import ServerEnd.BasicDatabaseActions;
import ServerEnd.SQLConnection;

import java.io.InvalidClassException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurrentSession {
    private Integer signedIn;

    public CurrentSession(){
        signedIn = null;
    }

    private Integer getSignedIn() throws NotSignedInException {
        if (this.signedIn != null) return signedIn;
        else throw new NotSignedInException("");
    }

    //////////////////////

    public void CreateNewAccount(String username, String password, String email) {
        try {
            BasicDatabaseActions.createNewAccount(username, password, email);
            this.SignIn(username, password);
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    public void SignIn(String username, String password){
        Integer ID = null;
        try {
            ID = BasicDatabaseActions.signIn(username, password);
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
            BasicDatabaseActions.modifyAccount(getSignedIn(), "username", username);
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    public void ChangePassword(String password){
        try {
            BasicDatabaseActions.modifyAccount(getSignedIn(), "password", password);
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
                BasicDatabaseActions.modifyAccount(getSignedIn(), "password", email);
            } else {
                ExceptionHandler.handleException(new InvalidInputException("Incorrect email format: '" + email +"' is not an email."));
            }
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    public HashMap<String, String> GetAccountInfo(){
        HashMap<String, String> accountInfo = new HashMap<>();
        try {
            accountInfo.put("username", BasicDatabaseActions.getAccountInfoType(getSignedIn(), "username"));
            accountInfo.put("email", BasicDatabaseActions.getAccountInfoType(getSignedIn(), "email"));
            accountInfo.put("projectList", BasicDatabaseActions.getAccountInfoType(getSignedIn(), "projectList"));
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
        return accountInfo;
    }

    //////////////////////////////////////////////////////////////////

    public Project CreateNewProject() {
        Project p = new Project("");
        try {
            BasicDatabaseActions.createNewProject(this.getSignedIn(), p.toJSONString());
            return p;
        } catch (NotSignedInException e){
            return p;
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
        return p;
    }

    public void SaveProject(Project p){
        try {
            this.getSignedIn();
            BasicDatabaseActions.saveProject(p.getID(), p.toJSONString());
        } catch (NotSignedInException e){
            SaveProject(signInLoadProject(p));
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    private Project signInLoadProject(Project p){
        try {
            ///TODO prompt a sign-in, then file current project in database
            int signinID = 0;
            p.username = BasicDatabaseActions.getAccountInfoType(signinID, "username");
            p.status = "private";
            int projectID = BasicDatabaseActions.createNewProject(this.getSignedIn(), p.toJSONString());
            return Project.fromJSONtoProject(BasicDatabaseActions.getAccountInfoType(signinID, "projectInfoStruct"));
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
        return p;
    }

    /** Open project: Get project info */
    ///TODO figure out how to deal with projects
    public List<String> getProjectTags(int ID) {
        try {
            String taglistString = BasicDatabaseActions.getProjectInfoType(0, "tags");
            String[] tagList = taglistString.split(", ");
            return Arrays.asList(tagList);
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
        return new ArrayList<>();
    }


    ///TODO check if not saved
}
