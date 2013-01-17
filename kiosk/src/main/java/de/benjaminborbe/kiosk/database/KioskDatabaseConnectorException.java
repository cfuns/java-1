package de.benjaminborbe.kiosk.database;

public class KioskDatabaseConnectorException extends Exception {

	private static final long serialVersionUID = 6091381721736514153L;

	public KioskDatabaseConnectorException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public KioskDatabaseConnectorException(final String arg0) {
		super(arg0);
	}

	public KioskDatabaseConnectorException(final Throwable arg0) {
		super(arg0);
	}

}
