package de.benjaminborbe.wow.core.guice;

import com.google.inject.AbstractModule;
import javax.inject.Singleton;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import de.benjaminborbe.wow.api.WowService;
import de.benjaminborbe.wow.core.service.WowServiceImpl;
import org.slf4j.Logger;

public class WowModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(WowService.class).to(WowServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
