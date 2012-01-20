package de.benjaminborbe.microblog.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.microblog.util.MicroblogConnector;
import de.benjaminborbe.microblog.util.MicroblogConnectorImpl;
import de.benjaminborbe.microblog.util.MicroblogPostMailer;
import de.benjaminborbe.microblog.util.MicroblogPostMailerImpl;
import de.benjaminborbe.microblog.util.MicroblogRevisionStorage;
import de.benjaminborbe.microblog.util.MicroblogRevisionStorageImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class MicroblogModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(MicroblogPostMailer.class).to(MicroblogPostMailerImpl.class).in(Singleton.class);
		bind(MicroblogConnector.class).to(MicroblogConnectorImpl.class).in(Singleton.class);
		bind(MicroblogRevisionStorage.class).to(MicroblogRevisionStorageImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
