package de.benjaminborbe.crawler.service;

import de.benjaminborbe.crawler.api.CrawlerNotifier;
import de.benjaminborbe.crawler.util.CrawlerNotifierRegistry;
import de.benjaminborbe.tools.osgi.service.RegistryServiceTracker;
import org.osgi.framework.BundleContext;

import javax.inject.Singleton;

@Singleton
public class CrawlerNotifierServiceTracker extends RegistryServiceTracker<CrawlerNotifier> {

	public CrawlerNotifierServiceTracker(final CrawlerNotifierRegistry crawlerNotifierRegistry, final BundleContext context, final Class<?> clazz) {
		super(crawlerNotifierRegistry, context, clazz);
	}

}
