package de.benjaminborbe.xmpp.api;

public class XmppChatException extends Exception {

	private static final long serialVersionUID = -3948756107370003916L;

	public XmppChatException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public XmppChatException(final String arg0) {
		super(arg0);
	}

	public XmppChatException(final Throwable arg0) {
		super(arg0);
	}

}
