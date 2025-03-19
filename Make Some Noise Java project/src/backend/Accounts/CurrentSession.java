package backend.Accounts;

import Exceptions.Accounts.AccountsExceptionHandler;
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
            AccountsExceptionHandler.handleException(e);
        }
    }

    public void SignIn(String username, String password){
        Integer ID = null;
        try {
            ID = BasicDatabaseActions.signIn(username, password);
        } catch (Exception e) {
            AccountsExceptionHandler.handleException(e);
        }
        this.signedIn = ID;
    }

    public void SignOut(){
        try {
            BasicDatabaseActions.signOut(this.signedIn);
            this.signedIn = null;
        } catch (Exception e) {
            AccountsExceptionHandler.handleException(e);
        }
    }

    public void DeleteAccount(){
        try {
            BasicDatabaseActions.deleteAccount(this.signedIn);
            this.SignOut();
        } catch (Exception e){
            AccountsExceptionHandler.handleException(e);
        }
    }
}
