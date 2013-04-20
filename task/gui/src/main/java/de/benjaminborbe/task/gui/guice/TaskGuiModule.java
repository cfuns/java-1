package de.benjaminborbe.task.gui.guice;

import com.google.inject.AbstractModule;
import javax.inject.Singleton;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

public class TaskGuiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
