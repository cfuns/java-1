package de.benjaminborbe.xmpp.api;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.SuperAdminRequiredException;

public interface XmppService {

	void send(SessionIdentifier sessionIdentifier, String content) throws XmppServiceException, LoginRequiredException, SuperAdminRequiredException;

	void send(String content) throws XmppServiceException;
}
