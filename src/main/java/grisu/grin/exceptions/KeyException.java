package grisu.grin.exceptions;

public class KeyException extends RuntimeException {

	public KeyException() {
	}

	public KeyException(String message) {
		super(message);
	}

	public KeyException(String message, Throwable cause) {
		super(message, cause);
	}

	public KeyException(Throwable cause) {
		super(cause);
	}

}
