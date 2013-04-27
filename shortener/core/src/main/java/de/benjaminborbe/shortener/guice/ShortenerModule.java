package de.benjaminborbe.shortener.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.shortener.api.ShortenerService;
import de.benjaminborbe.shortener.dao.ShortenerUrlDao;
import de.benjaminborbe.shortener.dao.ShortenerUrlDaoStorage;
import de.benjaminborbe.shortener.service.ShortenerServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class ShortenerModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ShortenerUrlDao.class).to(ShortenerUrlDaoStorage.class).in(Singleton.class);
		bind(ShortenerService.class).to(ShortenerServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);

		requestStaticInjection(ShortenerValidatorLinker.class);
	}
}
