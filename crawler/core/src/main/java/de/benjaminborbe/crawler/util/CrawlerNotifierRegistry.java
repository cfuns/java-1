package de.benjaminborbe.crawler.util;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.benjaminborbe.crawler.api.CrawlerNotifier;
import de.benjaminborbe.tools.registry.RegistryBase;

@Singleton
public class CrawlerNotifierRegistry extends RegistryBase<CrawlerNotifier> {

	@Inject
	public CrawlerNotifierRegistry() {
	}
}
