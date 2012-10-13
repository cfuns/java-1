package de.benjaminborbe.xmpp.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.xmpp.api.XmppService;

@Singleton
public class XmppServiceMock implements XmppService {

	@Inject
	public XmppServiceMock() {
	}

	@Override
	public void execute() {
	}
}
