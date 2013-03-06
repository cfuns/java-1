package de.benjaminborbe.note.api;

public class NoteServiceException extends Exception {

	private static final long serialVersionUID = -5612462493588614271L;

	public NoteServiceException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public NoteServiceException(final String message) {
		super(message);
	}

	public NoteServiceException(final Throwable cause) {
		super(cause);
	}

}
