package de.benjaminborbe.worktime.api;

public class WorktimeRecorderException extends Exception {

	private static final long serialVersionUID = 7775211104339561645L;

	public WorktimeRecorderException(final String message) {
		super(message);
	}

	public WorktimeRecorderException(final Throwable cause) {
		super(cause);
	}

	public WorktimeRecorderException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
