package de.benjaminborbe.websearch.guice;

import static org.ops4j.peaberry.Peaberry.service;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import com.google.inject.AbstractModule;

import de.benjaminborbe.index.api.IndexSearcherService;
import de.benjaminborbe.index.api.IndexerService;
import de.benjaminborbe.navigation.api.NavigationWidget;

public class WebsearchOsgiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IndexSearcherService.class).toProvider(service(IndexSearcherService.class).single());
		bind(IndexerService.class).toProvider(service(IndexerService.class).single());
		bind(NavigationWidget.class).toProvider(service(NavigationWidget.class).single());
		bind(LogService.class).toProvider(service(LogService.class).single());
		bind(ExtHttpService.class).toProvider(service(ExtHttpService.class).single());
	}
}