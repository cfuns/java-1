package de.benjaminborbe.storage.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.storage.config.StorageConfig;
import de.benjaminborbe.storage.config.StorageConfigImpl;
import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import javax.inject.Singleton;

import static org.ops4j.peaberry.Peaberry.service;

public class StorageOsgiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(StorageConfig.class).to(StorageConfigImpl.class).in(Singleton.class);
		bind(LogService.class).toProvider(service(LogService.class).single());
		bind(ExtHttpService.class).toProvider(service(ExtHttpService.class).single());
	}
}
