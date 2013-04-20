package de.benjaminborbe.monitoring.gui.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import javax.inject.Singleton;

import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class MonitoringGuiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
