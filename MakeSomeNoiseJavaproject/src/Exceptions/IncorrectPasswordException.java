package Exceptions;

/**
 * @author Maya Malavasi
 * Thrown to indicate that an attempt to authenticate a user has failed due to an incorrect password.
 * This exception should be used in contexts where user authentication is required and the provided
 * password does not match the expected credentials.
 */
public class IncorrectPasswordException extends Exception {
    /** Constructs a new {@code IncorrectPasswordException} with the specified detail message. */
    public IncorrectPasswordException(String message) {
            super(message);
        }
}
