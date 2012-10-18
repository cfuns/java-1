package de.benjaminborbe.xmpp.api;

public interface XmppService {

	void send(String content) throws XmppServiceException;
}
