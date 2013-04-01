package de.benjaminborbe.notification.util;

import de.benjaminborbe.notification.api.Notification;
import de.benjaminborbe.notification.api.NotificationMediaIdentifier;

public interface NotificationNotifier {

	NotificationMediaIdentifier getNotificationMediaIdentifier();

	void notify(Notification notification) throws NotificationNotifierException;

}
