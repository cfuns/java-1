package de.benjaminborbe.microblog.util;

public class MicroblogConnectorException extends Exception {

	private static final long serialVersionUID = 161442643926131771L;

	public MicroblogConnectorException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public MicroblogConnectorException(final String arg0) {
		super(arg0);
	}

	public MicroblogConnectorException(final Throwable arg0) {
		super(arg0);
	}

}
