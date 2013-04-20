package de.benjaminborbe.storage.guice;

import static org.ops4j.peaberry.Peaberry.service;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import com.google.inject.AbstractModule;
import javax.inject.Singleton;

import de.benjaminborbe.storage.config.StorageConfig;
import de.benjaminborbe.storage.config.StorageConfigImpl;

public class StorageOsgiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(StorageConfig.class).to(StorageConfigImpl.class).in(Singleton.class);
		bind(LogService.class).toProvider(service(LogService.class).single());
		bind(ExtHttpService.class).toProvider(service(ExtHttpService.class).single());
	}
}
