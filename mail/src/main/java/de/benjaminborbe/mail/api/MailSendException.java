package de.benjaminborbe.mail.api;

public class MailSendException extends Exception {

	private static final long serialVersionUID = 1956608937457909831L;

	public MailSendException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public MailSendException(final String arg0) {
		super(arg0);
	}

	public MailSendException(final Throwable arg0) {
		super(arg0);
	}

}
