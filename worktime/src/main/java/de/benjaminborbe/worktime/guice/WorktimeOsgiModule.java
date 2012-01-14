package de.benjaminborbe.worktime.guice;

import static org.ops4j.peaberry.Peaberry.service;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import com.google.inject.AbstractModule;

import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.storage.api.StorageService;

public class WorktimeOsgiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(NavigationWidget.class).toProvider(service(NavigationWidget.class).single());
		bind(StorageService.class).toProvider(service(StorageService.class).single());
		bind(LogService.class).toProvider(service(LogService.class).single());
		bind(ExtHttpService.class).toProvider(service(ExtHttpService.class).single());
	}
}
