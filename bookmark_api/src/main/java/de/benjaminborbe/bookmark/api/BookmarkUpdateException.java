package de.benjaminborbe.bookmark.api;

public class BookmarkUpdateException extends Exception {

	private static final long serialVersionUID = -7856782897335199373L;

	public BookmarkUpdateException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public BookmarkUpdateException(final String message) {
		super(message);
	}

	public BookmarkUpdateException(final Throwable cause) {
		super(cause);
	}

}
