package de.benjaminborbe.storage.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.storage.api.CacheStorageService;
import de.benjaminborbe.storage.api.PersistentStorageService;
import de.benjaminborbe.storage.service.CacheStorageServiceImpl;
import de.benjaminborbe.storage.service.PersistentStorageServiceImpl;
import de.benjaminborbe.storage.util.StorageConnection;
import de.benjaminborbe.storage.util.StorageConnectionImpl;
import de.benjaminborbe.storage.util.StorageDaoUtil;
import de.benjaminborbe.storage.util.StorageDaoUtilImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class StorageModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
		bind(StorageDaoUtil.class).to(StorageDaoUtilImpl.class).in(Singleton.class);
		bind(StorageConnection.class).to(StorageConnectionImpl.class).in(Singleton.class);
		bind(PersistentStorageService.class).to(PersistentStorageServiceImpl.class).in(Singleton.class);
		bind(CacheStorageService.class).to(CacheStorageServiceImpl.class).in(Singleton.class);
	}
}
