package Exceptions;


/**
 * @author Maya Malavasi
 * Thrown to indicate that a connection to the database could not be established or was interrupted.
 * This exception is commonly used in data access layers to signal connectivity issues,
 * such as timeouts, unreachable databases, or invalid configurations.
 */
public class DatabaseConnectionException extends Exception {
    /** Constructs a new {@code IncorrectPasswordException} with the specified detail message. */
    public DatabaseConnectionException(String message) {
            super(message);
        }
}
