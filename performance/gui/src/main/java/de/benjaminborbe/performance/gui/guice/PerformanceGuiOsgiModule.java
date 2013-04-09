package de.benjaminborbe.performance.gui.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.performance.api.PerformanceService;
import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import static org.ops4j.peaberry.Peaberry.service;

public class PerformanceGuiOsgiModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(AnalyticsService.class).toProvider(service(AnalyticsService.class).single());
		bind(AuthenticationService.class).toProvider(service(AuthenticationService.class).single());
		bind(AuthorizationService.class).toProvider(service(AuthorizationService.class).single());
		bind(PerformanceService.class).toProvider(service(PerformanceService.class).single());
		bind(NavigationWidget.class).toProvider(service(NavigationWidget.class).single());
		bind(LogService.class).toProvider(service(LogService.class).single());
		bind(ExtHttpService.class).toProvider(service(ExtHttpService.class).single());
	}
}
