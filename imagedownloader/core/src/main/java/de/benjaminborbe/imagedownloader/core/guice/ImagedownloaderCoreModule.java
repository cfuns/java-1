package de.benjaminborbe.imagedownloader.core.guice;

import com.google.inject.AbstractModule;
import javax.inject.Singleton;
import de.benjaminborbe.imagedownloader.api.ImagedownloaderService;
import de.benjaminborbe.imagedownloader.core.service.ImagedownloaderCoreServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

public class ImagedownloaderCoreModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ImagedownloaderService.class).to(ImagedownloaderCoreServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
