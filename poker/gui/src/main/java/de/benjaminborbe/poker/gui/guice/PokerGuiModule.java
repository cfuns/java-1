package de.benjaminborbe.poker.gui.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import javax.inject.Singleton;

import de.benjaminborbe.poker.gui.config.PokerGuiConfig;
import de.benjaminborbe.poker.gui.config.PokerGuiConfigImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class PokerGuiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(PokerGuiConfig.class).to(PokerGuiConfigImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
