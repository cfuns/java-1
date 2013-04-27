package de.benjaminborbe.dhl.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.dhl.api.DhlService;
import de.benjaminborbe.dhl.dao.DhlDao;
import de.benjaminborbe.dhl.dao.DhlDaoStorage;
import de.benjaminborbe.dhl.service.DhlServiceImpl;
import de.benjaminborbe.dhl.util.DhlStatusFetcher;
import de.benjaminborbe.dhl.util.DhlStatusFetcherImpl;
import de.benjaminborbe.dhl.util.DhlStatusNotifier;
import de.benjaminborbe.dhl.util.DhlStatusNotifierImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class DhlModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(DhlDao.class).to(DhlDaoStorage.class).in(Singleton.class);
		bind(DhlStatusNotifier.class).to(DhlStatusNotifierImpl.class).in(Singleton.class);
		bind(DhlStatusFetcher.class).to(DhlStatusFetcherImpl.class).in(Singleton.class);
		bind(DhlService.class).to(DhlServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);

		requestStaticInjection(DhlValidatorLinker.class);
	}
}
