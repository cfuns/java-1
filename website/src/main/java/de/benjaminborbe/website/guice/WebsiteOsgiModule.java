package de.benjaminborbe.website.guice;

import static org.ops4j.peaberry.Peaberry.service;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import com.google.inject.AbstractModule;

import de.benjaminborbe.dashboard.api.DashboardWidget;

public class WebsiteOsgiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(DashboardWidget.class).toProvider(service(DashboardWidget.class).single());
		bind(LogService.class).toProvider(service(LogService.class).single());
		bind(ExtHttpService.class).toProvider(service(ExtHttpService.class).single());
	}
}
