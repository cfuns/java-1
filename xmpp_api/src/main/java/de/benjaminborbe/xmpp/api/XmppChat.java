package de.benjaminborbe.xmpp.api;

public interface XmppChat {

	void send(String msg) throws XmppChatException;
}
