package indep.vafl.utility;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class PredictionServiceOfflineException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6220810604655922262L;

	public PredictionServiceOfflineException() {
		super();
	}

	public PredictionServiceOfflineException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PredictionServiceOfflineException(String message, Throwable cause) {
		super(message, cause);
	}

	public PredictionServiceOfflineException(String message) {
		super(message);
	}

	public PredictionServiceOfflineException(Throwable cause) {
		super(cause);
	}
}