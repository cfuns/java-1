package de.benjaminborbe.crawler.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.crawler.api.CrawlerNotifier;
import de.benjaminborbe.tools.registry.RegistryBase;

@Singleton
public class CrawlerNotifierRegistry extends RegistryBase<CrawlerNotifier> {

	@Inject
	public CrawlerNotifierRegistry() {
	}
}
