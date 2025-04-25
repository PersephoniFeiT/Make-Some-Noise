package Exceptions;

/**
 * @author Maya Malavasi
 * Thrown to indicate that an operation requiring authentication was attempted
 * without a user being signed in.
 */
public class NotSignedInException extends Exception {
    /** Constructs a new {@code IncorrectPasswordException} with the specified detail message. */
    public NotSignedInException(String message) {
            super(message);
        }
}
