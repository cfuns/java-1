package de.benjaminborbe.notification.util;

public class NotificationNotifierException extends Exception {

	private static final long serialVersionUID = -6018583734004739789L;

	public NotificationNotifierException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public NotificationNotifierException(final String message) {
		super(message);
	}

	public NotificationNotifierException(final Throwable cause) {
		super(cause);
	}

}
