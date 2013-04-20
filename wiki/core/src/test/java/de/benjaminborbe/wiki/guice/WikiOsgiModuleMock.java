package de.benjaminborbe.wiki.guice;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import com.google.inject.AbstractModule;
import javax.inject.Singleton;

import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.mock.StorageServiceMock;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.osgi.mock.LogServiceMock;

public class WikiOsgiModuleMock extends AbstractModule {

	@Override
	protected void configure() {
		bind(StorageService.class).to(StorageServiceMock.class).in(Singleton.class);
		bind(LogService.class).to(LogServiceMock.class).in(Singleton.class);
		bind(ExtHttpService.class).to(ExtHttpServiceMock.class).in(Singleton.class);
	}
}
