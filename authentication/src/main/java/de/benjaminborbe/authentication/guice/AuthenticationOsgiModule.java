package de.benjaminborbe.authentication.guice;

import static org.ops4j.peaberry.Peaberry.service;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import com.google.inject.AbstractModule;

import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.storage.api.StorageService;

public class AuthenticationOsgiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ConfigurationService.class).toProvider(service(ConfigurationService.class).single());
		bind(StorageService.class).toProvider(service(StorageService.class).single());
		bind(LogService.class).toProvider(service(LogService.class).single());
		bind(ExtHttpService.class).toProvider(service(ExtHttpService.class).single());
	}
}
