package Exceptions;

/**
 * @author Maya Malavasi
 * Thrown to indicate that a method has received invalid or improperly formatted input.
 * This exception can be used in various input validation scenarios where the input
 * does not meet expected criteria or constraints.
 */
public class InvalidInputException extends Exception {
    /** Constructs a new {@code IncorrectPasswordException} with the specified detail message. */
    public InvalidInputException(String message) {
            super(message);
        }
}
