package de.benjaminborbe.task.api;

public class TaskServiceException extends Exception {

	private static final long serialVersionUID = -138816027942658001L;

	public TaskServiceException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public TaskServiceException(final String arg0) {
		super(arg0);
	}

	public TaskServiceException(final Throwable arg0) {
		super(arg0);
	}

}
