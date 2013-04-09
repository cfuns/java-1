package de.benjaminborbe.bookmark.api;

public class BookmarkDeletionException extends Exception {

	private static final long serialVersionUID = -3794322793634003663L;

	public BookmarkDeletionException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public BookmarkDeletionException(final String message) {
		super(message);
	}

	public BookmarkDeletionException(final Throwable cause) {
		super(cause);
	}

}
