package de.benjaminborbe.dhl.util;

import de.benjaminborbe.authentication.api.UserIdentifier;

public interface DhlStatusNotifier {

	void notify(UserIdentifier userIdentifier, DhlStatus status) throws DhlStatusNotifierException;
}
