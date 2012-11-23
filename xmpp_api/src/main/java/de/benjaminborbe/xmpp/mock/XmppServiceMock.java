package de.benjaminborbe.xmpp.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.xmpp.api.XmppService;
import de.benjaminborbe.xmpp.api.XmppServiceException;

@Singleton
public class XmppServiceMock implements XmppService {

	@Inject
	public XmppServiceMock() {
	}

	@Override
	public void send(final SessionIdentifier sessionIdentifier, final String content) throws XmppServiceException {
	}

	@Override
	public void send(final String content) throws XmppServiceException {
	}

}
