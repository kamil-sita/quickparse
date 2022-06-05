package pl.ksitarski.quickparse.exc;

/**
 * Thrown when argument cannot be assigned to the output.
 */
public class AssignmentException extends RuntimeException {
    public AssignmentException(String message) {
        super(message);
    }
}
