package de.benjaminborbe.notification.util;

import org.slf4j.Logger;

import javax.inject.Inject;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.notification.api.Notification;
import de.benjaminborbe.notification.api.NotificationMediaIdentifier;
import de.benjaminborbe.xmpp.api.XmppService;
import de.benjaminborbe.xmpp.api.XmppServiceException;

public class NotificationNotifierXmpp implements NotificationNotifier {

	private static final NotificationMediaIdentifier TYPE = new NotificationMediaIdentifier("xmpp");

	private final XmppService xmppService;

	private final Logger logger;

	@Inject
	public NotificationNotifierXmpp(final Logger logger, final XmppService xmppService) {
		this.logger = logger;
		this.xmppService = xmppService;
	}

	@Override
	public void notify(final Notification notification) throws NotificationNotifierException {
		try {
			logger.debug("notify");
			final String subject = notification.getSubject();
			final String message = notification.getMessage();
			final UserIdentifier userIdentifier = notification.getTo();
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
			throw new NotificationNotifierException(e);
		}
	}

	@Override
	public NotificationMediaIdentifier getNotificationMediaIdentifier() {
		return TYPE;
	}
}
