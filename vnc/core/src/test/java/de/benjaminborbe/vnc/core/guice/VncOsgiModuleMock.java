package de.benjaminborbe.vnc.core.guice;

import com.google.inject.AbstractModule;
import javax.inject.Singleton;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.osgi.mock.LogServiceMock;
import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

public class VncOsgiModuleMock extends AbstractModule {

	@Override
	protected void configure() {
		bind(LogService.class).to(LogServiceMock.class).in(Singleton.class);
		bind(ExtHttpService.class).to(ExtHttpServiceMock.class).in(Singleton.class);
	}
}
