package de.benjaminborbe.tools.lua;

public class LuaParseException extends Exception {

	private static final long serialVersionUID = 1117478369851855741L;

	public LuaParseException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public LuaParseException(final String arg0) {
		super(arg0);
	}

	public LuaParseException(final Throwable arg0) {
		super(arg0);
	}

}
