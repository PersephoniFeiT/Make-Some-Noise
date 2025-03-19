package BackEnd.Accounts;

import Exceptions.Accounts.ExceptionHandler;
import ServerEnd.BasicDatabaseActions;

public class CurrentSession {
    private Integer signedIn;

    public CurrentSession(){
        signedIn = null;
    }

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
            BasicDatabaseActions.signOut(this.signedIn);
            this.signedIn = null;
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    public void DeleteAccount(){
        try {
            BasicDatabaseActions.deleteAccount(this.signedIn);
            this.SignOut();
        } catch (Exception e){
            ExceptionHandler.handleException(e);
        }
    }
}
