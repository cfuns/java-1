package de.benjaminborbe.storage.memory.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.memory.service.StorageServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class StorageModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
		bind(StorageService.class).to(StorageServiceImpl.class).in(Singleton.class);
	}
}
