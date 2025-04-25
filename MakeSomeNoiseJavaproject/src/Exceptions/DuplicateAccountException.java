package Exceptions;

/**
 * @author Maya Malavasi
 * Thrown to indicate that an attempt to create a new account failed because
 * an account with the same identifier already exists.
 */
public class DuplicateAccountException extends Exception {
    /** Constructs a new {@code IncorrectPasswordException} with the specified detail message. */
    public DuplicateAccountException(String message) {
            super(message);
        }
}
