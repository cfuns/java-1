package de.benjaminborbe.cron.guice;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.mock.LogServiceMock;

public class CronOsgiModuleMock extends AbstractModule {

	@Override
	protected void configure() {
		bind(LogService.class).to(LogServiceMock.class).in(Singleton.class);
		bind(ExtHttpService.class).to(ExtHttpServiceMock.class).in(Singleton.class);
	}
}
