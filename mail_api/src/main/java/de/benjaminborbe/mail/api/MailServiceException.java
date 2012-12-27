package de.benjaminborbe.mail.api;

public class MailServiceException extends Exception {

	private static final long serialVersionUID = 1956608937457909831L;

	public MailServiceException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public MailServiceException(final String arg0) {
		super(arg0);
	}

	public MailServiceException(final Throwable arg0) {
		super(arg0);
	}

}
