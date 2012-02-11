package de.benjaminborbe.storage.tools;

public class DaoException extends Exception {

	private static final long serialVersionUID = 2056661090064743762L;

	public DaoException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public DaoException(final String message) {
		super(message);
	}

	public DaoException(final Throwable cause) {
		super(cause);
	}

}
