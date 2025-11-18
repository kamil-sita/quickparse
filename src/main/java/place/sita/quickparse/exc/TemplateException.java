package place.sita.quickparse.exc;

import place.sita.architecture.PrivateApi;

/**
 * Thrown when template is invalid. This is always a problem with the template itself.
 */
@PrivateApi
public class TemplateException extends RuntimeException {
    public TemplateException(String message) {
        super(message);
    }
}
