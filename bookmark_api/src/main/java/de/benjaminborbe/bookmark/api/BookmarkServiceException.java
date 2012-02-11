package de.benjaminborbe.bookmark.api;

public class BookmarkServiceException extends Exception {

	private static final long serialVersionUID = 6013053972339820724L;

	public BookmarkServiceException(final String message) {
		super(message);
	}

	public BookmarkServiceException(final Throwable cause) {
		super(cause);
	}

	public BookmarkServiceException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
