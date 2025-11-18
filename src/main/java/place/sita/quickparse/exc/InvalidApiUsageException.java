package place.sita.quickparse.exc;

/**
 * Indicates that the API has been used incorrectly.
 */
public class InvalidApiUsageException extends RuntimeException {
	public InvalidApiUsageException(String message) {
		super(message);
	}
}
