package de.benjaminborbe.poker.table.server.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.poker.table.server.config.PokerTableConfig;
import de.benjaminborbe.poker.table.server.config.PokerTableConfigImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class PokerTableServerModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
		bind(PokerTableConfig.class).to(PokerTableConfigImpl.class).in(Singleton.class);
	}
}
