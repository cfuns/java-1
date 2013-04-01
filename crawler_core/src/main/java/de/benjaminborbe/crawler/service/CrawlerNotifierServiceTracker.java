package de.benjaminborbe.crawler.service;

import org.osgi.framework.BundleContext;

import com.google.inject.Singleton;

import de.benjaminborbe.crawler.api.CrawlerNotifier;
import de.benjaminborbe.crawler.util.CrawlerNotifierRegistry;
import de.benjaminborbe.tools.osgi.service.RegistryServiceTracker;

@Singleton
public class CrawlerNotifierServiceTracker extends RegistryServiceTracker<CrawlerNotifier> {

	public CrawlerNotifierServiceTracker(final CrawlerNotifierRegistry crawlerNotifierRegistry, final BundleContext context, final Class<?> clazz) {
		super(crawlerNotifierRegistry, context, clazz);
	}

}
