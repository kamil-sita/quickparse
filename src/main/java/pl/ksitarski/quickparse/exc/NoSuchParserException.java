package pl.ksitarski.quickparse.exc;

/**
 * Thrown when no parser matches given requirements.
 */
public class NoSuchParserException extends RuntimeException {
    public NoSuchParserException(String message) {
        super(message);
    }
}
