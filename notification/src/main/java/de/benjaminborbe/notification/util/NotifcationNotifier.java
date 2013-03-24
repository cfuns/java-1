package de.benjaminborbe.notification.util;

import de.benjaminborbe.authentication.api.UserIdentifier;

public interface NotifcationNotifier {

	void notify(UserIdentifier userIdentifier, String subject, String message) throws NotifcationNotifierException;

}
