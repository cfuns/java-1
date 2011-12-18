package de.benjaminborbe.storage.guice;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.storage.util.StorageConfig;
import de.benjaminborbe.storage.util.StorageConfigMock;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.osgi.mock.LogServiceMock;

public class StorageOsgiModuleMock extends AbstractModule {

	@Override
	protected void configure() {
		bind(StorageConfig.class).to(StorageConfigMock.class).in(Singleton.class);
		bind(LogService.class).to(LogServiceMock.class).in(Singleton.class);
		bind(ExtHttpService.class).to(ExtHttpServiceMock.class).in(Singleton.class);
	}

}
