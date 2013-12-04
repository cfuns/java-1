package de.benjaminborbe.lunch.gui.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.lunch.gui.config.LunchGuiConfig;
import de.benjaminborbe.lunch.gui.config.LunchGuiConfigImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class LunchGuiModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(LunchGuiConfig.class).to(LunchGuiConfigImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
