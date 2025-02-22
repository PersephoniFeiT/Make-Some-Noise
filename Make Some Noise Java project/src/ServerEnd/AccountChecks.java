package ServerEnd;

import Exceptions.Accounts.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Back-end class that interfaces with the script to connect to the SQL server. */

public class AccountChecks {
    // MySQL Connection URL
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/makeSomeNoiseDatabase";
    private static final String USER = "appUser";
    private static final String PASSWORD = "make some noise";
    
    private AccountChecks(String username, String password){}

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

    public static void deleteAccount(String ID) throws DatabaseConnectionException, InvalidInputException {
        try {
            AccountChecks.assertFormat(new String[]{ID});
        } catch (AssertionError e){
            throw new InvalidInputException("Invalid input.");
        }

        SQLConnection.delete("accounts", "ID = '" + ID +"'");
    }
    

}