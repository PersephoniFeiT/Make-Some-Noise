package backend.Accounts;

import Exceptions.Accounts.AccountsExceptionHandler;
import ServerEnd.AccountChecks;

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
            AccountsExceptionHandler.handleException(e);
        }
    }

    public void SignIn(String username, String password){
        String ID = null;
        try {
            ID = AccountChecks.signIn(username, password);
        } catch (Exception e) {
            AccountsExceptionHandler.handleException(e);
        }
        this.signedIn = ID;
        System.out.println("You have successfully signed in as: " + username);
    }

    public void SignOut(){
        try {
            AccountChecks.signOut(this.signedIn);
            this.signedIn = null;
        } catch (Exception e) {
            AccountsExceptionHandler.handleException(e);
        }
    }

    public void DeleteAccount(){
        try {
            AccountChecks.deleteAccount(this.signedIn);
            this.SignOut();
        } catch (Exception e){
            AccountsExceptionHandler.handleException(e);
        }
    }
}
