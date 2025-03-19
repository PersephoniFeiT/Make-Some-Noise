package ServerEnd;

import Exceptions.Accounts.DatabaseConnectionException;
import Exceptions.Accounts.InvalidInputException;

/* Project table layout:
ID, Title, username, date created, status, struct with project info, thumbnail, list of tags */

/** Back-end class that interfaces with the script to connect to the SQL server. */

public class SharingChecks {

    private static void assertFormat(String[] values){
        for (String item : values){
            assert(item != null);
            assert(!item.isEmpty());
            assert(!item.equals(" "));
        }
    }

    /** Sharing:PatternSearch
     * If a user is logged in and connected to the internet, they may search the server for posts. They will be
     * prompted to enter search terms. The application will show the user a list of public posts with tags and
     * titles that match the search terms. */

    /** Sharing:UserSearch
     * If a user is logged in and connected to the internet, they may search the server for users. They will be
     * prompted to enter a search term. The application will show the user a list of users with usernames matching
     * the search term. */

    /** Sharing:unpublish */


}