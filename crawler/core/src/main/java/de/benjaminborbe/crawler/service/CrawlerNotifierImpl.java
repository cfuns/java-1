package de.benjaminborbe.crawler.service;

import de.benjaminborbe.crawler.api.CrawlerNotifier;
import de.benjaminborbe.crawler.api.CrawlerNotifierResult;
import de.benjaminborbe.crawler.util.CrawlerNotifierRegistry;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CrawlerNotifierImpl implements CrawlerNotifier {

	private final Logger logger;

	private final CrawlerNotifierRegistry crawlerNotifierRegistry;

	@Inject
	public CrawlerNotifierImpl(final Logger logger, final CrawlerNotifierRegistry crawlerNotifierRegistry) {
		this.logger = logger;
		this.crawlerNotifierRegistry = crawlerNotifierRegistry;
	}

	@Override
	public void notifiy(final CrawlerNotifierResult result) {
		logger.trace("notifiy");
		for (final CrawlerNotifier n : crawlerNotifierRegistry.getAll()) {
			try {
				n.notifiy(result);
			} catch (final Exception e) {
				logger.error(e.getClass().getSimpleName(), e);
			}
		}
	}
}
