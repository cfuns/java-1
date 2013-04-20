package de.benjaminborbe.website.guice;

import com.google.inject.AbstractModule;
import javax.inject.Singleton;

import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.cache.mock.CacheServiceMock;

public class WebsiteOsgiModuleMock extends AbstractModule {

	@Override
	protected void configure() {
		bind(CacheService.class).to(CacheServiceMock.class).in(Singleton.class);
	}

}
