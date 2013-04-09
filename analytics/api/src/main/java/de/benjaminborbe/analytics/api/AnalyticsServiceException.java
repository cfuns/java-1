package de.benjaminborbe.analytics.api;

public class AnalyticsServiceException extends Exception {

	private static final long serialVersionUID = 5347572179283783605L;

	public AnalyticsServiceException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public AnalyticsServiceException(final String arg0) {
		super(arg0);
	}

	public AnalyticsServiceException(final Throwable arg0) {
		super(arg0);
	}

}
