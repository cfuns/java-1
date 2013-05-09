package de.benjaminborbe.googlesearch.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderService;
import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import static org.ops4j.peaberry.Peaberry.service;

public class GooglesearchOsgiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(HttpdownloaderService.class).toProvider(service(HttpdownloaderService.class).single());
		bind(LogService.class).toProvider(service(LogService.class).single());
		bind(ExtHttpService.class).toProvider(service(ExtHttpService.class).single());
	}
}
