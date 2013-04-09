package de.benjaminborbe.checklist.api;

public class ChecklistServiceException extends Exception {

	private static final long serialVersionUID = -4860773568930764967L;

	public ChecklistServiceException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public ChecklistServiceException(final String arg0) {
		super(arg0);
	}

	public ChecklistServiceException(final Throwable arg0) {
		super(arg0);
	}

}
