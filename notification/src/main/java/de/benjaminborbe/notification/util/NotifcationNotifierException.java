package de.benjaminborbe.notification.util;

public class NotifcationNotifierException extends Exception {

	private static final long serialVersionUID = -6018583734004739789L;

	public NotifcationNotifierException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public NotifcationNotifierException(final String message) {
		super(message);
	}

	public NotifcationNotifierException(final Throwable cause) {
		super(cause);
	}

}
