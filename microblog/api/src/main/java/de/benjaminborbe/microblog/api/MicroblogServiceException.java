package de.benjaminborbe.microblog.api;

public class MicroblogServiceException extends Exception {

	private static final long serialVersionUID = 8252254784041584523L;

	public MicroblogServiceException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public MicroblogServiceException(final String arg0) {
		super(arg0);
	}

	public MicroblogServiceException(final Throwable arg0) {
		super(arg0);
	}

}
