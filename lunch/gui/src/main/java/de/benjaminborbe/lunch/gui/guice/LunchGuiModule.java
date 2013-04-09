package de.benjaminborbe.lunch.gui.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.lunch.gui.config.LunchGuiConfig;
import de.benjaminborbe.lunch.gui.config.LunchGuiConfigImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class LunchGuiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(LunchGuiConfig.class).to(LunchGuiConfigImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
