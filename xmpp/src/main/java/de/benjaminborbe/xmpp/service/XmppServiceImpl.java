package de.benjaminborbe.xmpp.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.SuperAdminRequiredException;
import de.benjaminborbe.xmpp.api.XmppService;
import de.benjaminborbe.xmpp.api.XmppServiceException;
import de.benjaminborbe.xmpp.connector.XmppConnector;
import de.benjaminborbe.xmpp.connector.XmppConnectorException;
import de.benjaminborbe.xmpp.connector.XmppUser;

@Singleton
public class XmppServiceImpl implements XmppService {

	private final Logger logger;

	private final XmppConnector xmppConnector;

	private final AuthenticationService authenticationService;

	@Inject
	public XmppServiceImpl(final Logger logger, final XmppConnector xmppConnector, final AuthenticationService authenticationService) {
		this.logger = logger;
		this.xmppConnector = xmppConnector;
		this.authenticationService = authenticationService;
	}

	@Override
	public void send(final SessionIdentifier sessionIdentifier, final String message) throws XmppServiceException, LoginRequiredException, SuperAdminRequiredException {
		try {
			logger.trace("send " + message);
			authenticationService.expectSuperAdmin(sessionIdentifier);
			send(message);
		}
		catch (final AuthenticationServiceException e) {
			throw new XmppServiceException(e);
		}
	}

	@Override
	public void send(final String message) throws XmppServiceException {
		final XmppUser user = new XmppUser("bborbe@mobile-bb/mobile-bb");
		try {
			xmppConnector.sendMessage(user, message);
		}
		catch (final XmppConnectorException e) {
			throw new XmppServiceException("fail to send message " + message + " to user " + user);
		}
	}
}
