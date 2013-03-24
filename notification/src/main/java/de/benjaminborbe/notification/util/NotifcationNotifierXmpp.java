package de.benjaminborbe.notification.util;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.xmpp.api.XmppService;
import de.benjaminborbe.xmpp.api.XmppServiceException;

public class NotifcationNotifierXmpp implements NotifcationNotifier {

	private final XmppService xmppService;

	private final Logger logger;

	@Inject
	public NotifcationNotifierXmpp(final Logger logger, final XmppService xmppService) {
		this.logger = logger;
		this.xmppService = xmppService;
	}

	@Override
	public void notify(final UserIdentifier userIdentifier, final String subject, final String message) throws NotifcationNotifierException {
		try {
			logger.debug("notify");

			if (subject == null || subject.equals(message)) {
				xmppService.send(userIdentifier, message);
			}
			else {
				xmppService.send(userIdentifier, subject + "\n\n" + message);
			}
		}
		catch (final XmppServiceException e) {
			throw new NotifcationNotifierException(e);
		}
	}

}
