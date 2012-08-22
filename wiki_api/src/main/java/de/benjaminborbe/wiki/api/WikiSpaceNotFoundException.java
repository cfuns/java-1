package de.benjaminborbe.wiki.api;

public class WikiSpaceNotFoundException extends WikiServiceException {

	private static final long serialVersionUID = -4897438951646204200L;

	public WikiSpaceNotFoundException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public WikiSpaceNotFoundException(final String arg0) {
		super(arg0);
	}

	public WikiSpaceNotFoundException(final Throwable arg0) {
		super(arg0);
	}

}
