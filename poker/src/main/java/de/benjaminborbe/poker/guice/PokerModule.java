package de.benjaminborbe.poker.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.service.PokerServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class PokerModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(PokerService.class).to(PokerServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
