package de.benjaminborbe.notification.api;

public class NotificationServiceException extends Exception {

	private static final long serialVersionUID = -2810508716145641834L;

	public NotificationServiceException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public NotificationServiceException(final String message) {
		super(message);
	}

	public NotificationServiceException(final Throwable cause) {
		super(cause);
	}

}
