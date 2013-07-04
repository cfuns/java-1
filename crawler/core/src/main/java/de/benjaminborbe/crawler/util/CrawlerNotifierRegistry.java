package de.benjaminborbe.crawler.util;

import de.benjaminborbe.crawler.api.CrawlerNotifier;
import de.benjaminborbe.tools.registry.RegistryBase;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CrawlerNotifierRegistry extends RegistryBase<CrawlerNotifier> {

	@Inject
	public CrawlerNotifierRegistry() {
	}
}
