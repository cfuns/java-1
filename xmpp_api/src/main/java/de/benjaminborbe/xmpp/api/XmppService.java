package de.benjaminborbe.xmpp.api;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface XmppService {

	void send(SessionIdentifier sessionIdentifier, String content) throws XmppServiceException, LoginRequiredException, PermissionDeniedException;

	void send(String content) throws XmppServiceException;
}
