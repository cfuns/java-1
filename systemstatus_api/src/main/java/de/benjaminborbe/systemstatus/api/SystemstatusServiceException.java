package de.benjaminborbe.systemstatus.api;

public class SystemstatusServiceException extends Exception {

	private static final long serialVersionUID = -6787310883441854711L;

	public SystemstatusServiceException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public SystemstatusServiceException(final String arg0) {
		super(arg0);
	}

	public SystemstatusServiceException(final Throwable arg0) {
		super(arg0);
	}

}
