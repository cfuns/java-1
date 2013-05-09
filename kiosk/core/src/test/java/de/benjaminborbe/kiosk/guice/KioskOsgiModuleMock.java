package de.benjaminborbe.kiosk.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.mock.ConfigurationServiceMock;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderService;
import de.benjaminborbe.httpdownloader.mock.HttpdownloaderServiceMock;
import de.benjaminborbe.message.api.MessageService;
import de.benjaminborbe.message.mock.MessageServiceMock;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.osgi.mock.LogServiceMock;
import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import javax.inject.Singleton;

public class KioskOsgiModuleMock extends AbstractModule {

	@Override
	protected void configure() {
		bind(HttpdownloaderService.class).to(HttpdownloaderServiceMock.class).in(Singleton.class);
		bind(ConfigurationService.class).to(ConfigurationServiceMock.class).in(Singleton.class);
		bind(MessageService.class).to(MessageServiceMock.class).in(Singleton.class);
		bind(LogService.class).to(LogServiceMock.class).in(Singleton.class);
		bind(ExtHttpService.class).to(ExtHttpServiceMock.class).in(Singleton.class);
	}
}
