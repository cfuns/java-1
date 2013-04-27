package de.benjaminborbe.xmpp.api;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface XmppService {

	void send(
		SessionIdentifier sessionIdentifier,
		UserIdentifier userIdentifier,
		String message
	) throws XmppServiceException, LoginRequiredException, PermissionDeniedException;

	void send(UserIdentifier userIdentifier, String message) throws XmppServiceException;
}
