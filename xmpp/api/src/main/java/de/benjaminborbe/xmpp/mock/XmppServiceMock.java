package de.benjaminborbe.xmpp.mock;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.xmpp.api.XmppService;
import de.benjaminborbe.xmpp.api.XmppServiceException;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class XmppServiceMock implements XmppService {

	@Inject
	public XmppServiceMock() {
	}

	@Override
	public void send(final UserIdentifier userIdentifer, final String content) throws XmppServiceException {
	}

	@Override
	public void send(
		final SessionIdentifier sessionIdentifier,
		final UserIdentifier userIdentifer,
		final String message
	) throws XmppServiceException, LoginRequiredException,
		PermissionDeniedException {
	}

}
