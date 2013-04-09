package de.benjaminborbe.cron.api;

public class CronControllerException extends Exception {

	private static final long serialVersionUID = -523552540798160239L;

	public CronControllerException(final String arg0) {
		super(arg0);
	}

	public CronControllerException(final Throwable arg0) {
		super(arg0);
	}

	public CronControllerException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

}
