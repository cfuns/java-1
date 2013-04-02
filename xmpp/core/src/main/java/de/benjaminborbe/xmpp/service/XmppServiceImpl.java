package de.benjaminborbe.xmpp.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.xmpp.api.XmppService;
import de.benjaminborbe.xmpp.api.XmppServiceException;
import de.benjaminborbe.xmpp.config.XmppConfig;
import de.benjaminborbe.xmpp.connector.XmppConnector;
import de.benjaminborbe.xmpp.connector.XmppConnectorException;
import de.benjaminborbe.xmpp.connector.XmppUser;
import org.slf4j.Logger;

@Singleton
public class XmppServiceImpl implements XmppService {

	private final Logger logger;

	private final XmppConnector xmppConnector;

	private final AuthorizationService authorizationService;

	private final XmppConfig xmppConfig;

	@Inject
	public XmppServiceImpl(final Logger logger, final XmppConnector xmppConnector, final XmppConfig xmppConfig, final AuthorizationService authorizationService) {
		this.logger = logger;
		this.xmppConnector = xmppConnector;
		this.xmppConfig = xmppConfig;
		this.authorizationService = authorizationService;
	}

	@Override
	public void send(final SessionIdentifier sessionIdentifier, final UserIdentifier userIdentifier, final String message) throws XmppServiceException, LoginRequiredException,
		PermissionDeniedException {
		try {
			authorizationService.expectAdminRole(sessionIdentifier);
			logger.trace("send " + message);
			send(userIdentifier, message);
		} catch (final AuthorizationServiceException e) {
			throw new XmppServiceException(e);
		}
	}

	@Override
	public void send(final UserIdentifier userIdentifier, final String message) throws XmppServiceException {
		final XmppUser user = new XmppUser(userIdentifier.getId() + "@" + xmppConfig.getServerHost());
		try {
			if (!xmppConnector.isConnected()) {
				xmppConnector.connect();
			}
			xmppConnector.sendMessage(user, message);
		} catch (final XmppConnectorException e) {
			throw new XmppServiceException("fail to send message " + message + " to user " + user);
		}
	}
}
