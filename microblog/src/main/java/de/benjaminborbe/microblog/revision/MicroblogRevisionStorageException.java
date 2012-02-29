package de.benjaminborbe.microblog.revision;

public class MicroblogRevisionStorageException extends Exception {

	private static final long serialVersionUID = 9034558986263784852L;

	public MicroblogRevisionStorageException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public MicroblogRevisionStorageException(final String arg0) {
		super(arg0);
	}

	public MicroblogRevisionStorageException(final Throwable arg0) {
		super(arg0);
	}

}
