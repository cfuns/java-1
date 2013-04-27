package de.benjaminborbe.timetracker.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.timetracker.connector.TimetrackerConnector;
import de.benjaminborbe.timetracker.connector.TimetrackerConnectorImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class TimetrackerModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(TimetrackerConnector.class).to(TimetrackerConnectorImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
