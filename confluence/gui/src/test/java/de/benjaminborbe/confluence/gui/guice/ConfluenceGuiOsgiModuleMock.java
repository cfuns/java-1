package de.benjaminborbe.confluence.gui.guice;

import com.google.inject.AbstractModule;
import javax.inject.Singleton;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.mock.AuthenticationServiceMock;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.mock.AuthorizationServiceMock;
import de.benjaminborbe.confluence.api.ConfluenceService;
import de.benjaminborbe.confluence.mock.ConfluenceServiceMock;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.navigation.mock.NavigationWidgetMock;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.osgi.mock.LogServiceMock;
import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

public class ConfluenceGuiOsgiModuleMock extends AbstractModule {

	@Override
	protected void configure() {
		bind(AuthorizationService.class).to(AuthorizationServiceMock.class).in(Singleton.class);
		bind(AuthenticationService.class).to(AuthenticationServiceMock.class).in(Singleton.class);
		bind(ConfluenceService.class).to(ConfluenceServiceMock.class).in(Singleton.class);
		bind(NavigationWidget.class).to(NavigationWidgetMock.class).in(Singleton.class);
		bind(LogService.class).to(LogServiceMock.class).in(Singleton.class);
		bind(ExtHttpService.class).to(ExtHttpServiceMock.class).in(Singleton.class);
	}
}
