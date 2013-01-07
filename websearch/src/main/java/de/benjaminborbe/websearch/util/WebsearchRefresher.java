package de.benjaminborbe.websearch.util;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.crawler.api.CrawlerInstruction;
import de.benjaminborbe.crawler.api.CrawlerInstructionBuilder;
import de.benjaminborbe.crawler.api.CrawlerService;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.tools.synchronize.RunOnlyOnceATime;
import de.benjaminborbe.tools.util.ThreadRunner;
import de.benjaminborbe.websearch.config.WebsearchConfig;
import de.benjaminborbe.websearch.dao.WebsearchPageBean;

public class WebsearchRefresher {

	private static final int TIMEOUT = 5000;

	private final class RefreshRunnable implements Runnable {

		@Override
		public void run() {
			try {
				logger.debug("RefreshRunnable started");
				runOnlyOnceATime.run(new RefreshPages());
				logger.debug("RefreshRunnable finished");
			}
			catch (final Exception e) {
				logger.error(e.getClass().getSimpleName(), e);
			}
		}
	}

	private final class RefreshPages implements Runnable {

		@Override
		public void run() {
			try {
				logger.debug("refresh pages started");
				final EntityIterator<WebsearchPageBean> i = updateDeterminer.determineExpiredPages();
				int counter = 0;
				while (i.hasNext()) {
					final WebsearchPageBean page = i.next();
					try {
						if (websearchConfig.getRefreshLimit() != null && counter > websearchConfig.getRefreshLimit()) {
							logger.debug("refresh pages limit reached => exit");
							return;
						}

						counter++;

						final String url = page.getUrl();
						logger.debug("trigger refresh of url " + url);
						final CrawlerInstruction crawlerInstruction = new CrawlerInstructionBuilder(url, TIMEOUT);
						crawlerService.processCrawlerInstruction(crawlerInstruction);

					}
					catch (final Exception e) {
						logger.error(e.getClass().getSimpleName(), e);
					}
				}
				logger.debug("refresh pages finished");
			}
			catch (final Exception e) {
				logger.error(e.getClass().getSimpleName(), e);
			}
		}
	}

	private final ThreadRunner threadRunner;

	private final CrawlerService crawlerService;

	private final WebsearchUpdateDeterminer updateDeterminer;

	private final RunOnlyOnceATime runOnlyOnceATime;

	private final Logger logger;

	private final WebsearchConfig websearchConfig;

	@Inject
	public WebsearchRefresher(
			final Logger logger,
			final WebsearchConfig websearchConfig,
			final WebsearchUpdateDeterminer updateDeterminer,
			final CrawlerService crawlerService,
			final RunOnlyOnceATime runOnlyOnceATime,
			final ThreadRunner threadRunner) {
		this.logger = logger;
		this.websearchConfig = websearchConfig;
		this.updateDeterminer = updateDeterminer;
		this.crawlerService = crawlerService;
		this.runOnlyOnceATime = runOnlyOnceATime;
		this.threadRunner = threadRunner;
	}

	public void refresh() {
		threadRunner.run("refreshpages", new RefreshRunnable());
	}
}
