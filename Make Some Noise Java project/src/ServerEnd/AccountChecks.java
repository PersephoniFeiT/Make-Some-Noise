package ServerEnd;

import Exceptions.Accounts.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        SQLConnection.insert("accounts", new String[]{username, password, email, "{\"projects\": []}"});
    }

    public static String signIn(String username, String password) throws DatabaseConnectionException, NoSuchAccountException, IncorrectPasswordException, InvalidInputException {
        try {
            AccountChecks.assertFormat(new String[]{username, password});
        } catch (AssertionError e){
            throw new InvalidInputException("Invalid input.");
        }

        try {
            List<Map<String, Object>> rs = SQLConnection.select("accounts", "*","username = '" + username+"'");
            if (rs.isEmpty()) throw new NoSuchAccountException("Username does not exist.");
            for (Map<String, Object> user : rs) {
                if (user.get("password").equals(password))
                    return user.get("ID").toString();
            }
            throw new IncorrectPasswordException("Your username or password is incorrect.");

        } catch (DatabaseConnectionException e){
            throw new DatabaseConnectionException(e.getMessage());
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