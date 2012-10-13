package de.benjaminborbe.xmpp.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.xmpp.api.XmppService;
import de.benjaminborbe.xmpp.service.XmppServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class XmppModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(XmppService.class).to(XmppServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
