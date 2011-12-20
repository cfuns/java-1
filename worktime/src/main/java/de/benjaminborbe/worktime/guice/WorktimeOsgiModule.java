package de.benjaminborbe.worktime.guice;

import static org.ops4j.peaberry.Peaberry.service;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import com.google.inject.AbstractModule;

import de.benjaminborbe.storage.api.PersistentStorageService;

public class WorktimeOsgiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(PersistentStorageService.class).toProvider(service(PersistentStorageService.class).single());
		bind(LogService.class).toProvider(service(LogService.class).single());
		bind(ExtHttpService.class).toProvider(service(ExtHttpService.class).single());
	}
}
