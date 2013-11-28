package de.benjaminborbe.storage.memory.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.memory.service.StorageServiceImpl;

import javax.inject.Singleton;

public class StorageModuleMock extends AbstractModule {

	@Override
	protected void configure() {
		bind(StorageService.class).to(StorageServiceImpl.class).in(Singleton.class);
	}
}
