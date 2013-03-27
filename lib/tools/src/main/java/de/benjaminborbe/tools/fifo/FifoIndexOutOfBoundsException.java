package de.benjaminborbe.tools.fifo;

public class FifoIndexOutOfBoundsException extends Exception {

	private static final long serialVersionUID = -7533961186822591631L;

	public FifoIndexOutOfBoundsException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public FifoIndexOutOfBoundsException(final String arg0) {
		super(arg0);
	}

	public FifoIndexOutOfBoundsException(final Throwable arg0) {
		super(arg0);
	}

}
