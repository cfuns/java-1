package de.benjaminborbe.meta.guice;

import static org.ops4j.peaberry.Peaberry.service;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;
import org.osgi.service.obr.RepositoryAdmin;
import com.google.inject.AbstractModule;

public class MetaOsgiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(RepositoryAdmin.class).toProvider(service(RepositoryAdmin.class).single());
		bind(LogService.class).toProvider(service(LogService.class).single());
		bind(ExtHttpService.class).toProvider(service(ExtHttpService.class).single());
	}
}
