package Exceptions;

import java.sql.SQLException;
import java.util.logging.Logger;

/** @author Maya Malavasi
 * Handles any exceptions not handled directly by the user interface -- specifically to prevent system from
 * crashing, freezing, or otherwise experiencing an error due to unhandled Exceptions. Logs errors to console. */
public class ExceptionHandler {

    public static final Logger logger = Logger.getLogger(ExceptionHandler.class.getName());

    /** Determines exception given and directs process to specific method to handle each exception.
     * @param e the Exception thrown. */
    public static void handleException(Exception e) {
        if (e instanceof DatabaseConnectionException) ExceptionHandler.handleDatabaseConnectionException(e);
        else if (e instanceof DuplicateAccountException) ExceptionHandler.handleDuplicateAccountException(e);
        else if (e instanceof IncorrectPasswordException) ExceptionHandler.handleIncorrectPasswordException(e);
        else if (e instanceof InvalidInputException) ExceptionHandler.handleInvalidInputException(e);
        else if (e instanceof NoSuchAccountException) ExceptionHandler.handleNoSuchAccountException(e);
        else if (e instanceof NotSignedInException) ExceptionHandler.handleNotSignedInException(e);
        else if (e instanceof SQLException) ExceptionHandler.handleDatabaseConnectionException(e);
        else ExceptionHandler.handleUnknownException(e);
    }

    /** Logs the message for the {@link DatabaseConnectionException}
     * @param e the {@link DatabaseConnectionException} thrown. */
    private static void handleDatabaseConnectionException(Exception e) {
        logger.severe("Sorry, the database connection failed. Please try again.\n"+
                "Error: " + e.getMessage());
    }

    /** Logs the message for the {@link DuplicateAccountException}
     * @param e the {@link DuplicateAccountException} thrown. */
    private static void handleDuplicateAccountException(Exception e) {
        logger.warning("This account already exists: " + e.getMessage());
    }

    /** Logs the message for the {@link IncorrectPasswordException}
     * @param e the {@link IncorrectPasswordException} thrown. */
    private static void handleIncorrectPasswordException(Exception e) {
        logger.warning(e.getMessage());
    }

    /** Logs the message for the {@link NoSuchAccountException}
     * @param e the {@link NoSuchAccountException} thrown. */
    private static void handleNoSuchAccountException(Exception e) {
        logger.severe("No such account exists: " + e.getMessage());
    }

    /** Logs the message for the {@link NotSignedInException}
     * @param e the {@link NotSignedInException} thrown. */
    private static void handleNotSignedInException(Exception e) {
        logger.warning("User is not logged in." + e.getMessage());
    }

    /** Logs the message for the {@link InvalidInputException}
     * @param e the {@link InvalidInputException} thrown. */
    private static void handleInvalidInputException(Exception e) {
        logger.severe("Invalid Input: " + e.getMessage());
    }

    /** Logs the message for any exception not explicitly covered.
     * @param e the Exception thrown. */
    private static void handleUnknownException(Exception e) {
        logger.severe("Unknown: " + e.getMessage());
    }
}
