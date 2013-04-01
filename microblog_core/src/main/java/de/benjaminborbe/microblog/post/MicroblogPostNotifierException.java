package de.benjaminborbe.microblog.post;

public class MicroblogPostNotifierException extends Exception {

	private static final long serialVersionUID = -8335069479089200369L;

	public MicroblogPostNotifierException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public MicroblogPostNotifierException(final String arg0) {
		super(arg0);
	}

	public MicroblogPostNotifierException(final Throwable arg0) {
		super(arg0);
	}

}
