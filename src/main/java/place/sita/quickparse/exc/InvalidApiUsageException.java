package place.sita.quickparse.exc;

import place.sita.architecture.PrivateApi;

/**
 * Indicates that the API has been used incorrectly.
 */
@PrivateApi
public class InvalidApiUsageException extends RuntimeException {
	public InvalidApiUsageException(String message) {
		super(message);
	}

	public InvalidApiUsageException(String message, Throwable cause) {
		super(message, cause);
	}
}
