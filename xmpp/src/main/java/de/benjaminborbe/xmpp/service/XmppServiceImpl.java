package de.benjaminborbe.xmpp.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.xmpp.api.XmppService;

@Singleton
public class XmppServiceImpl implements XmppService {

	private final Logger logger;

	@Inject
	public XmppServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void execute() {
		logger.trace("execute");
	}

}
