package de.benjaminborbe.lunch.gui.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.lunch.api.LunchService;
import de.benjaminborbe.navigation.api.NavigationWidget;
import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import static org.ops4j.peaberry.Peaberry.service;

public class LunchGuiOsgiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ConfigurationService.class).toProvider(service(ConfigurationService.class).single());
		bind(AuthenticationService.class).toProvider(service(AuthenticationService.class).single());
		bind(AuthorizationService.class).toProvider(service(AuthorizationService.class).single());
		bind(LunchService.class).toProvider(service(LunchService.class).single());
		bind(NavigationWidget.class).toProvider(service(NavigationWidget.class).single());
		bind(LogService.class).toProvider(service(LogService.class).single());
		bind(ExtHttpService.class).toProvider(service(ExtHttpService.class).single());
	}
}
