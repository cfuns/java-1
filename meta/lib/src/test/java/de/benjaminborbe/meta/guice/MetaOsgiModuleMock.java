package de.benjaminborbe.meta.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.osgi.mock.LogServiceMock;
import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;
import org.osgi.service.obr.RepositoryAdmin;

import javax.inject.Singleton;

public class MetaOsgiModuleMock extends AbstractModule {

	@Override
	protected void configure() {
		bind(RepositoryAdmin.class).to(RepositoryAdminMock.class).in(Singleton.class);
		bind(LogService.class).to(LogServiceMock.class).in(Singleton.class);
		bind(ExtHttpService.class).to(ExtHttpServiceMock.class).in(Singleton.class);
	}
}
