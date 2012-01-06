package de.benjaminborbe.website.guice;

import static org.ops4j.peaberry.Peaberry.service;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import com.google.inject.AbstractModule;
import de.benjaminborbe.dashboard.api.DashboardWidget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.search.api.SearchWidget;

public class WebsiteOsgiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(SearchWidget.class).toProvider(service(SearchWidget.class).single());
		bind(NavigationWidget.class).toProvider(service(NavigationWidget.class).single());
		bind(DashboardWidget.class).toProvider(service(DashboardWidget.class).single());
		bind(LogService.class).toProvider(service(LogService.class).single());
		bind(ExtHttpService.class).toProvider(service(ExtHttpService.class).single());
	}
}
