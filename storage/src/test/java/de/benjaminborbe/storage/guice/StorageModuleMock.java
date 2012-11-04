package de.benjaminborbe.storage.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.service.StorageServiceImpl;
import de.benjaminborbe.storage.util.StorageConfig;
import de.benjaminborbe.storage.util.StorageConfigImpl;
import de.benjaminborbe.storage.util.StorageDaoUtil;
import de.benjaminborbe.storage.util.StorageDaoUtilImpl;

public class StorageModuleMock extends AbstractModule {

	@Override
	protected void configure() {
		bind(StorageDaoUtil.class).to(StorageDaoUtilImpl.class).in(Singleton.class);
		bind(StorageConfig.class).to(StorageConfigImpl.class).in(Singleton.class);
		bind(StorageService.class).to(StorageServiceImpl.class).in(Singleton.class);
	}
}
