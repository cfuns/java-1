package de.benjaminborbe.lunch.util;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.xmpp.api.XmppService;
import de.benjaminborbe.xmpp.api.XmppServiceException;

public class LunchUserNotifierXmpp implements LunchUserNotifier {

	private final Logger logger;

	private final XmppService xmppService;

	@Inject
	public LunchUserNotifierXmpp(final Logger logger, final XmppService xmppService) {
		this.logger = logger;
		this.xmppService = xmppService;
	}

	@Override
	public void notify(final UserIdentifier userIdentifier, final String message) {
		try {
			logger.debug("notify user: " + userIdentifier);
			xmppService.send(userIdentifier, message);
		}
		catch (final XmppServiceException e) {
			logger.warn(e.getClass().getName(), e);
		}
	}
}
