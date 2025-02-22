package ServerEnd;

import Exceptions.Accounts.*;

import java.sql.ResultSet;
import java.sql.SQLException;

/* Project table layout:
ID, Title, username, date created, status, struct with project info, thumbnail, list of tags */

/** Back-end class that interfaces with the script to connect to the SQL server. */

public class ProjectChecks {

    private static void assertFormat(String[] values){
        for (String item : values){
            assert(item != null);
            assert(!item.isEmpty());
            assert(!item.equals(" "));
        }
    }

    public static void createNewProject(String title, String accountID) throws DatabaseConnectionException, InvalidInputException {
        try {
            ProjectChecks.assertFormat(new String[]{accountID});
        } catch (AssertionError e){
            throw new InvalidInputException("Invalid input.");
        }

        // insert into table

        //TODO
        // figure out what values we want to store in the project table

        SQLConnection.insert("projects", new String[] {title});
    }

    public static void deleteProject(String ID) throws DatabaseConnectionException, InvalidInputException {
        try {
            ProjectChecks.assertFormat(new String[]{ID});
        } catch (AssertionError e){
            throw new InvalidInputException("Invalid input.");
        }

        SQLConnection.delete("projects", "ID = '" + ID +"'");
    }


}