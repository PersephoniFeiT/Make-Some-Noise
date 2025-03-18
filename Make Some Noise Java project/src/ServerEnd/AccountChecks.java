package ServerEnd;

import Exceptions.Accounts.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/* Accounts table layout:
ID, username, password, email, list of project ids */

/** Back-end class that interfaces with the script to connect to the SQL server. */

public class AccountChecks {

    private static void assertFormat(String[] values){
        for (String item : values){
            assert(item != null);
            assert(!item.isEmpty());
            assert(!item.equals(" "));
        }
    }

    public static void createNewAccount(String username, String password, String email) throws DatabaseConnectionException, DuplicateAccountException, InvalidInputException {
        try {
            AccountChecks.assertFormat(new String[]{username, password, email});
        } catch (AssertionError e){
            throw new InvalidInputException("Invalid input.");
        }
        //TODO
        // check if duplicate account with email & username

        // insert into table
        SQLConnection.insert("accounts", new String[]{username, password, email});
    }

    public static String signIn(String username, String password) throws DatabaseConnectionException, NoSuchAccountException, IncorrectPasswordException, InvalidInputException {
        try {
            AccountChecks.assertFormat(new String[]{username, password});
        } catch (AssertionError e){
            throw new InvalidInputException("Invalid input.");
        }

        try {
            ResultSet rs = SQLConnection.select("accounts", "username = '" + username+"'");
            while (rs.next()) {
                //assuming 1st col is ID, 2nd is username, 3rd is pwd
                if (rs.getString(3).equals(password)) return rs.getString(1);
            }
            throw new IncorrectPasswordException("Your username or password is incorrect.");
        } catch (SQLException e){
            throw new NoSuchAccountException("Username does not exist.");
        }
    }

    public static void signOut(String ID) throws DatabaseConnectionException, InvalidInputException {
        // whatever keeps track of the current sign-in
    }

    /** Account:View
     * If a user is logged in, they may choose to view their account. They will be able to see their projects
     * saved on the server and the associated title, image preview, and tags. They will also be able to see their
     * biographical field. */

    /**  Account:Edit:User
     * If a user is viewing their own account, they may choose to edit their account. They may edit their
     * biographical field, delete any of their own patterns from the server as described in Server:DeletePattern,
     * or modify any of their own patterns as described in Server:EditPattern.  */

    /** Account:Edit:Admin
     * If an administrator is viewing an account as described in Server:ViewUser, they may choose to edit that
     * account as if they were the owner of that account, as described in Account:Edit:User. */

    /** Account:Delete */
    public static void deleteAccount(String ID) throws DatabaseConnectionException, InvalidInputException {
        try {
            AccountChecks.assertFormat(new String[]{ID});
        } catch (AssertionError e){
            throw new InvalidInputException("Invalid input.");
        }
        SQLConnection.delete("accounts", "ID = '" + ID +"'");
    }


}