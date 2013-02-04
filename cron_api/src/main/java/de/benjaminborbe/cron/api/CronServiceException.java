package de.benjaminborbe.cron.api;

public class CronServiceException extends Exception {

	private static final long serialVersionUID = 2198966759987204560L;

	public CronServiceException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public CronServiceException(final String arg0) {
		super(arg0);
	}

	public CronServiceException(final Throwable arg0) {
		super(arg0);
	}

}
