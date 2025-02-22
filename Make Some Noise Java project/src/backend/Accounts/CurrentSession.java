package BackEnd.Accounts;

import Exceptions.*;
import ServerEnd.AccountChecks;
import ServerEnd.SQLConnection;

public class CurrentSession {
    private String signedIn;

    public CurrentSession(){
        signedIn = null;
    }

    public void CreateNewAccount(String username, String password, String email) {
        try {
            AccountChecks.createNewAccount(username, password, email);
            this.SignIn(username, password);
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    public void SignIn(String username, String password){
        String ID = null;
        try {
            ID = AccountChecks.signIn(username, password);
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
        this.signedIn = ID;
    }

    public void SignOut(){
        try {
            AccountChecks.signOut(this.signedIn);
            this.signedIn = null;
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }

    public void DeleteAccount(){
        try {
            AccountChecks.deleteAccount(this.signedIn);
            this.SignOut();
        } catch (Exception e){
            ExceptionHandler.handleException(e);
        }
    }
}
