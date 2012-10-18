package de.benjaminborbe.xmpp.api;

public class XmppServiceException extends Exception {

	private static final long serialVersionUID = -7714651663811578148L;

	public XmppServiceException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public XmppServiceException(final String arg0) {
		super(arg0);
	}

	public XmppServiceException(final Throwable arg0) {
		super(arg0);
	}

}
