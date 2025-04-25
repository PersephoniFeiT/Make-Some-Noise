package Exceptions;

/**
 * @author Maya Malavasi
 * Thrown to indicate that an operation failed because the specified account does not exist.
 * This exception is typically used in authentication contexts where an account lookup fails
 * due to an invalid or nonexistent identifier.
 */
public class NoSuchAccountException extends Exception {
    /** Constructs a new {@code IncorrectPasswordException} with the specified detail message. */
    public NoSuchAccountException(String message) {
            super(message);
        }
}
