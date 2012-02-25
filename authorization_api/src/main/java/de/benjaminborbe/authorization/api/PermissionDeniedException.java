package de.benjaminborbe.authorization.api;

public class PermissionDeniedException extends Exception {

	private static final long serialVersionUID = 3959445156806487060L;

	public PermissionDeniedException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public PermissionDeniedException(final String arg0) {
		super(arg0);
	}

	public PermissionDeniedException(final Throwable arg0) {
		super(arg0);
	}

}
