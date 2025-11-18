package place.sita.quickparse.exc;

import place.sita.architecture.PrivateApi;

/**
 * Thrown when template does not match text. This can indicate that
 * the template is not defined well, or that the input generally does not match
 * the template.
 */
@PrivateApi
public class TemplateMismatchException extends RuntimeException {
    public TemplateMismatchException(String message) {
        super(message);
    }
}
