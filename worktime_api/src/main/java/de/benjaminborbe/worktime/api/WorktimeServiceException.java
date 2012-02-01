package de.benjaminborbe.worktime.api;

public class WorktimeServiceException extends Exception {

	private static final long serialVersionUID = 959221036757817320L;

	public WorktimeServiceException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public WorktimeServiceException(final String message) {
		super(message);
	}

	public WorktimeServiceException(final Throwable cause) {
		super(cause);
	}

}
