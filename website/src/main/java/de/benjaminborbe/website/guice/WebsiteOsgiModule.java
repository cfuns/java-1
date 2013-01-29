package de.benjaminborbe.website.guice;

import static org.ops4j.peaberry.Peaberry.service;

import com.google.inject.AbstractModule;

import de.benjaminborbe.cache.api.CacheService;

public class WebsiteOsgiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(CacheService.class).toProvider(service(CacheService.class).single());
	}

}
