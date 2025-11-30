package place.sita.quickparse.exc;

import place.sita.architecture.PrivateApi;

/**
 * Thrown when argument cannot be assigned to the output.
 */
@PrivateApi
public class AssignmentException extends RuntimeException {
    public AssignmentException(String message) {
        super(message);
    }

	public AssignmentException(String message, Throwable cause) {
		super(message, cause);
	}
}
