package de.benjaminborbe.worktime.guice;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.storage.api.PersistentStorageService;
import de.benjaminborbe.storage.service.PersistentStorageServiceMock;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.osgi.mock.LogServiceMock;

public class WorktimeOsgiModuleMock extends AbstractModule {

	@Override
	protected void configure() {
		bind(PersistentStorageService.class).to(PersistentStorageServiceMock.class).in(Singleton.class);
		bind(LogService.class).to(LogServiceMock.class).in(Singleton.class);
		bind(ExtHttpService.class).to(ExtHttpServiceMock.class).in(Singleton.class);
	}
}
