package de.benjaminborbe.xmpp.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.xmpp.api.XmppService;
import de.benjaminborbe.xmpp.api.XmppServiceException;
import de.benjaminborbe.xmpp.connector.XmppConnector;
import de.benjaminborbe.xmpp.connector.XmppConnectorException;
import de.benjaminborbe.xmpp.connector.XmppUser;

@Singleton
public class XmppServiceImpl implements XmppService {

	private final Logger logger;

	private final XmppConnector xmppConnector;

	@Inject
	public XmppServiceImpl(final Logger logger, final XmppConnector xmppConnector) {
		this.logger = logger;
		this.xmppConnector = xmppConnector;
	}

	@Override
	public void send(final String message) throws XmppServiceException {
		logger.trace("send " + message);
		final XmppUser user = new XmppUser("bborbe@mobile-bb/mobile-bb");
		try {
			xmppConnector.sendMessage(user, message);
		}
		catch (final XmppConnectorException e) {
			throw new XmppServiceException("fail to send message " + message + " to user " + user);
		}
	}
}
