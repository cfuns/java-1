package de.benjaminborbe.authentication.api;

public class SuperAdminRequiredException extends Exception {

	private static final long serialVersionUID = -7882440362438961388L;

	public SuperAdminRequiredException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public SuperAdminRequiredException(final String arg0) {
		super(arg0);
	}

	public SuperAdminRequiredException(final Throwable arg0) {
		super(arg0);
	}

}
