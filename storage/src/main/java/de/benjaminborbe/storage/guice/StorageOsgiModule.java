package de.benjaminborbe.storage.guice;

import static org.ops4j.peaberry.Peaberry.service;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.storage.util.StorageConfig;
import de.benjaminborbe.storage.util.StorageConfigImpl;

public class StorageOsgiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(StorageConfig.class).to(StorageConfigImpl.class).in(Singleton.class);
		bind(ConfigurationService.class).toProvider(service(ConfigurationService.class).single());
		bind(LogService.class).toProvider(service(LogService.class).single());
		bind(ExtHttpService.class).toProvider(service(ExtHttpService.class).single());
	}
}
