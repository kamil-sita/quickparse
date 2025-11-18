package place.sita.quickparse.exc;

import place.sita.architecture.PublicApi;

/**
 * Thrown when given value cannot be parsed into output of given type.
 */
@PublicApi
public class ParsingException extends RuntimeException {

	public ParsingException(String message) {
		super(message);
	}

	public ParsingException(String message, Throwable cause) {
		super(message, cause);
	}

}
