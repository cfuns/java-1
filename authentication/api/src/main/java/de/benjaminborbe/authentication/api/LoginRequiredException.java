package de.benjaminborbe.authentication.api;

public class LoginRequiredException extends Exception {

	private static final long serialVersionUID = -6926883890022998284L;

	public LoginRequiredException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public LoginRequiredException(final String arg0) {
		super(arg0);
	}

	public LoginRequiredException(final Throwable arg0) {
		super(arg0);
	}

}
