package de.benjaminborbe.website.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.cache.api.CacheService;

import static org.ops4j.peaberry.Peaberry.service;

public class WebsiteOsgiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(CacheService.class).toProvider(service(CacheService.class).single());
	}

}
