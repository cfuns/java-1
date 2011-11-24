package de.benjaminborbe.monitoring.guice;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.mail.api.MailServiceMock;
import de.benjaminborbe.tools.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.mock.LogServiceMock;

public class MonitoringOsgiModuleMock extends AbstractModule {

	@Override
	protected void configure() {
		bind(MailService.class).to(MailServiceMock.class).in(Singleton.class);
		bind(LogService.class).to(LogServiceMock.class).in(Singleton.class);
		bind(ExtHttpService.class).to(ExtHttpServiceMock.class).in(Singleton.class);
	}
}
