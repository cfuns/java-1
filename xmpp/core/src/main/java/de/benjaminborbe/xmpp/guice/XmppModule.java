package de.benjaminborbe.xmpp.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import de.benjaminborbe.xmpp.api.XmppService;
import de.benjaminborbe.xmpp.config.XmppConfig;
import de.benjaminborbe.xmpp.config.XmppConfigImpl;
import de.benjaminborbe.xmpp.connector.XmppConnector;
import de.benjaminborbe.xmpp.connector.XmppConnectorImpl;
import de.benjaminborbe.xmpp.service.XmppServiceImpl;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class XmppModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(XmppConnector.class).to(XmppConnectorImpl.class).in(Singleton.class);
		bind(XmppConfig.class).to(XmppConfigImpl.class).in(Singleton.class);
		bind(XmppService.class).to(XmppServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
