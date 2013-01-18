package de.benjaminborbe.lunch.util;

import de.benjaminborbe.authentication.api.UserIdentifier;

public interface LunchUserNotifier {

	void notify(UserIdentifier userIdentifier, String content);
}
