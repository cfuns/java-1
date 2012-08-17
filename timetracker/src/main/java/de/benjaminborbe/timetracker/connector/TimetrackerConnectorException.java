package de.benjaminborbe.timetracker.connector;

public class TimetrackerConnectorException extends Exception {

	private static final long serialVersionUID = -5022675475687155947L;

	public TimetrackerConnectorException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public TimetrackerConnectorException(final String arg0) {
		super(arg0);
	}

	public TimetrackerConnectorException(final Throwable arg0) {
		super(arg0);
	}

}
