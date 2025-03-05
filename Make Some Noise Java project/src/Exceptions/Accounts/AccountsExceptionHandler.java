package Exceptions.Accounts;

import java.util.logging.Logger;

public class AccountsExceptionHandler {

    public static final Logger logger = Logger.getLogger(AccountsExceptionHandler.class.getName());

    public static void handleException(Exception e) {
        if (e instanceof DatabaseConnectionException) AccountsExceptionHandler.handleDatabaseConnectionException(e);
        else if (e instanceof DuplicateAccountException) AccountsExceptionHandler.handleDuplicateAccountException(e);
        else if (e instanceof IncorrectPasswordException) AccountsExceptionHandler.handleIncorrectPasswordException(e);
        else if (e instanceof InvalidInputException) AccountsExceptionHandler.handleInvalidInputException(e);
        else if (e instanceof NoSuchAccountException) AccountsExceptionHandler.handleNoSuchAccountException(e);
        else AccountsExceptionHandler.handleUnknownException(e);
    }

    private static void handleDatabaseConnectionException(Exception e) {
        logger.severe("Sorry, the database connection failed. Please try again.\n"+
                "Error: " + e.getMessage());
    }
    private static void handleDuplicateAccountException(Exception e) {
        logger.severe("This account already exists: " + e.getMessage());
    }
    private static void handleIncorrectPasswordException(Exception e) {
        logger.severe(e.getMessage());
    }
    private static void handleNoSuchAccountException(Exception e) {
        logger.severe("No such account exists: " + e.getMessage());
    }

    private static void handleInvalidInputException(Exception e) {
        logger.severe("Invalid Input: " + e.getMessage());
    }

    private static void handleUnknownException(Exception e) {
        logger.severe("Unknown: " + e.getMessage());
    }
}
