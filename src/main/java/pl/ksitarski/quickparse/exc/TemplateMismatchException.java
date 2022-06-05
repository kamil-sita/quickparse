package pl.ksitarski.quickparse.exc;

/**
 * Thrown when template does not match text.
 */
public class TemplateMismatchException extends RuntimeException {
    public TemplateMismatchException(String message) {
        super(message);
    }
}
