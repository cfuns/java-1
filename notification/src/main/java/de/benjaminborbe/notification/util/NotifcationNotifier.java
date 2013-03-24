package de.benjaminborbe.notification.util;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.notification.api.NotificationMediaIdentifier;

public interface NotifcationNotifier {

	NotificationMediaIdentifier getNotificationMediaIdentifier();

	void notify(UserIdentifier userIdentifier, String subject, String message) throws NotifcationNotifierException;

}
