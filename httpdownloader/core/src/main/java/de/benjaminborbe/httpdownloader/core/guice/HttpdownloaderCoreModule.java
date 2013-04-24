package de.benjaminborbe.httpdownloader.core.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderService;
import de.benjaminborbe.httpdownloader.core.service.HttpdownloaderCoreServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class HttpdownloaderCoreModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(HttpdownloaderService.class).to(HttpdownloaderCoreServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
