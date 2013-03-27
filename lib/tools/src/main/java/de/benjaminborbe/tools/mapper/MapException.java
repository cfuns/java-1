package de.benjaminborbe.tools.mapper;

public class MapException extends Exception {

	private static final long serialVersionUID = -1916712690177864549L;

	public MapException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public MapException(final String arg0) {
		super(arg0);
	}

	public MapException(final Throwable arg0) {
		super(arg0);
	}

}
