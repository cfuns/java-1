package de.benjaminborbe.notification.util;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.notification.api.NotificationMediaIdentifier;
import de.benjaminborbe.xmpp.api.XmppService;
import de.benjaminborbe.xmpp.api.XmppServiceException;

public class NotifcationNotifierXmpp implements NotifcationNotifier {

	private static final NotificationMediaIdentifier TYPE = new NotificationMediaIdentifier("xmpp");

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

			if (subject != null && !subject.trim().isEmpty() && message != null && !message.trim().isEmpty() && !subject.equals(message)) {
				xmppService.send(userIdentifier, subject + "\n\n" + message);
			}
			else if (subject != null && !subject.trim().isEmpty()) {
				xmppService.send(userIdentifier, subject);
			}
			else if (message != null && !message.trim().isEmpty()) {
				xmppService.send(userIdentifier, message);
			}
		}
		catch (final XmppServiceException e) {
			throw new NotifcationNotifierException(e);
		}
	}

	@Override
	public NotificationMediaIdentifier getNotificationMediaIdentifier() {
		return TYPE;
	}
}
