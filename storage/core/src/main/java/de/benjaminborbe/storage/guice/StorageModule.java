package de.benjaminborbe.storage.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.config.ConfigurationServiceJndi;
import de.benjaminborbe.storage.service.StorageServiceImpl;
import de.benjaminborbe.storage.util.StorageConnectionPool;
import de.benjaminborbe.storage.util.StorageConnectionPoolImpl;
import de.benjaminborbe.storage.util.StorageDaoUtil;
import de.benjaminborbe.storage.util.StorageDaoUtilImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class StorageModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ConfigurationService.class).to(ConfigurationServiceJndi.class).in(Singleton.class);
		bind(StorageConnectionPool.class).to(StorageConnectionPoolImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
		bind(StorageDaoUtil.class).to(StorageDaoUtilImpl.class).in(Singleton.class);
		bind(StorageService.class).to(StorageServiceImpl.class).in(Singleton.class);
	}
}
