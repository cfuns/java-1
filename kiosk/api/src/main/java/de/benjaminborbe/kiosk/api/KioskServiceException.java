package de.benjaminborbe.kiosk.api;

public class KioskServiceException extends Exception {

	private static final long serialVersionUID = 5772493753585086432L;

	public KioskServiceException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public KioskServiceException(final String arg0) {
		super(arg0);
	}

	public KioskServiceException(final Throwable arg0) {
		super(arg0);
	}

}
