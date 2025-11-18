package place.sita.quickparse.exc;

import place.sita.architecture.PrivateApi;

/**
 * Thrown when no parser matches given requirements.
 */
@PrivateApi
public class NoSuchParserException extends RuntimeException {
    public NoSuchParserException(String message) {
        super(message);
    }
}
