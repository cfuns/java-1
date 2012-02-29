package de.benjaminborbe.microblog.post;

public class MicroblogPostMailerException extends Exception {

	private static final long serialVersionUID = -8335069479089200369L;

	public MicroblogPostMailerException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public MicroblogPostMailerException(final String arg0) {
		super(arg0);
	}

	public MicroblogPostMailerException(final Throwable arg0) {
		super(arg0);
	}

}
