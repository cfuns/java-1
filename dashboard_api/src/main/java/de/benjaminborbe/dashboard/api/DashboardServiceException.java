package de.benjaminborbe.dashboard.api;

public class DashboardServiceException extends Exception {

	private static final long serialVersionUID = 1254464855713027520L;

	public DashboardServiceException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public DashboardServiceException(final String arg0) {
		super(arg0);
	}

	public DashboardServiceException(final Throwable arg0) {
		super(arg0);
	}

}
