package de.benjaminborbe.crawler.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.crawler.api.CrawlerNotifier;
import de.benjaminborbe.tools.util.RegistryImpl;

@Singleton
public class CrawlerNotifierRegistry extends RegistryImpl<CrawlerNotifier> {

	@Inject
	public CrawlerNotifierRegistry() {
	}
}
