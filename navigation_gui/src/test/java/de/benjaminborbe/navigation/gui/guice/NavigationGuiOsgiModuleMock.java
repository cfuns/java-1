package de.benjaminborbe.navigation.gui.guice;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.mock.AuthenticationServiceMock;
import de.benjaminborbe.navigation.api.NavigationService;
import de.benjaminborbe.navigation.mock.NavigationServiceMock;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.osgi.mock.LogServiceMock;

public class NavigationGuiOsgiModuleMock extends AbstractModule {

	@Override
	protected void configure() {
		bind(AuthenticationService.class).to(AuthenticationServiceMock.class).in(Singleton.class);
		bind(NavigationService.class).to(NavigationServiceMock.class).in(Singleton.class);
		bind(LogService.class).to(LogServiceMock.class).in(Singleton.class);
		bind(ExtHttpService.class).to(ExtHttpServiceMock.class).in(Singleton.class);
	}
}
