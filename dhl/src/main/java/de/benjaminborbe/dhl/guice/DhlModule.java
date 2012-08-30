package de.benjaminborbe.dhl.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.dhl.api.DhlService;
import de.benjaminborbe.dhl.service.DhlServiceImpl;
import de.benjaminborbe.dhl.status.DhlDao;
import de.benjaminborbe.dhl.status.DhlDaoStorage;
import de.benjaminborbe.dhl.util.DhlStatusFetcher;
import de.benjaminborbe.dhl.util.DhlStatusFetcherImpl;
import de.benjaminborbe.dhl.util.DhlStatusNotifier;
import de.benjaminborbe.dhl.util.DhlStatusNotifierImpl;
import de.benjaminborbe.dhl.util.DhlStatusStorage;
import de.benjaminborbe.dhl.util.DhlStatusStorageImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class DhlModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(DhlDao.class).to(DhlDaoStorage.class).in(Singleton.class);
		bind(DhlStatusStorage.class).to(DhlStatusStorageImpl.class).in(Singleton.class);
		bind(DhlStatusNotifier.class).to(DhlStatusNotifierImpl.class).in(Singleton.class);
		bind(DhlStatusFetcher.class).to(DhlStatusFetcherImpl.class).in(Singleton.class);
		bind(DhlService.class).to(DhlServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
