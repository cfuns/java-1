package de.benjaminborbe.xmpp.connector;

public class XmppConnectorException extends Exception {

	private static final long serialVersionUID = -6710911301190729234L;

	public XmppConnectorException(final String arg0) {
		super(arg0);
	}

	public XmppConnectorException(final Throwable arg0) {
		super(arg0);
	}

	public XmppConnectorException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

}
