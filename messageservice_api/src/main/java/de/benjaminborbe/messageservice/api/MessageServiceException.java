package de.benjaminborbe.messageservice.api;

public class MessageServiceException extends Exception {

	private static final long serialVersionUID = 8289368429300516336L;

	public MessageServiceException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public MessageServiceException(final String arg0) {
		super(arg0);
	}

	public MessageServiceException(final Throwable arg0) {
		super(arg0);
	}

}
