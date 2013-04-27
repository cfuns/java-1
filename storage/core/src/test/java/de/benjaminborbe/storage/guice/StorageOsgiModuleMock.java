package de.benjaminborbe.storage.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.storage.config.StorageConfig;
import de.benjaminborbe.storage.util.StorageConfigMock;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.osgi.mock.LogServiceMock;
import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import javax.inject.Singleton;

public class StorageOsgiModuleMock extends AbstractModule {

	@Override
	protected void configure() {
		bind(StorageConfig.class).to(StorageConfigMock.class).in(Singleton.class);
		bind(LogService.class).to(LogServiceMock.class).in(Singleton.class);
		bind(ExtHttpService.class).to(ExtHttpServiceMock.class).in(Singleton.class);
	}

}
