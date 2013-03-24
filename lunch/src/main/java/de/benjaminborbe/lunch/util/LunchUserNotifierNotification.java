package de.benjaminborbe.lunch.util;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.notification.api.NotificationService;
import de.benjaminborbe.notification.api.NotificationServiceException;
import de.benjaminborbe.notification.api.NotificationTypeIdentifier;

public class LunchUserNotifierNotification implements LunchUserNotifier {

	private static final NotificationTypeIdentifier TYPE = new NotificationTypeIdentifier("lunch");

	private final Logger logger;

	private final NotificationService notificationService;

	@Inject
	public LunchUserNotifierNotification(final Logger logger, final NotificationService notificationService) {
		this.logger = logger;
		this.notificationService = notificationService;
	}

	@Override
	public void notify(final UserIdentifier userIdentifier, final String message) throws LunchUserNotifierException {
		try {
			logger.debug("notify user: " + userIdentifier);

			final String subject = "Mittagessen ist da!";
			final StringBuilder sb = new StringBuilder();
			sb.append("Microblog-Nachricht:\n");
			sb.append(message);

			notificationService.notify(userIdentifier, TYPE, subject, sb.toString());
		}
		catch (NotificationServiceException | ValidationException e) {
			throw new LunchUserNotifierException(e);
		}
	}
}
