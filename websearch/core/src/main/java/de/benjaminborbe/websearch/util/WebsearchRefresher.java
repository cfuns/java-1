package de.benjaminborbe.websearch.util;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.crawler.api.CrawlerInstruction;
import de.benjaminborbe.crawler.api.CrawlerInstructionBuilder;
import de.benjaminborbe.crawler.api.CrawlerService;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.tools.synchronize.RunOnlyOnceATime;
import de.benjaminborbe.websearch.config.WebsearchConfig;
import de.benjaminborbe.websearch.dao.WebsearchPageBean;

public class WebsearchRefresher {

	private static final int TIMEOUT = 5000;

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
						if (websearchConfig.getRefreshLimit() != null && websearchConfig.getRefreshLimit() >= 0 && counter > websearchConfig.getRefreshLimit()) {
							logger.debug("refresh pages limit reached => exit");
							return;
						}
						final String url = page.getUrl();
						if (!websearchRobotsTxtUtil.isAllowed(url)) {
							logger.debug("robots.txt disallow url: " + url);
						}
						else {
							counter++;

							logger.debug("trigger refresh of url " + url);
							final CrawlerInstruction crawlerInstruction = new CrawlerInstructionBuilder(url, TIMEOUT);
							crawlerService.processCrawlerInstruction(crawlerInstruction);
						}
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

	private final CrawlerService crawlerService;

	private final WebsearchUpdateDeterminer updateDeterminer;

	private final RunOnlyOnceATime runOnlyOnceATime;

	private final Logger logger;

	private final WebsearchConfig websearchConfig;

	private final WebsearchRobotsTxtUtil websearchRobotsTxtUtil;

	@Inject
	public WebsearchRefresher(
			final Logger logger,
			final WebsearchConfig websearchConfig,
			final WebsearchUpdateDeterminer updateDeterminer,
			final CrawlerService crawlerService,
			final RunOnlyOnceATime runOnlyOnceATime,
			final WebsearchRobotsTxtUtil websearchRobotsTxtUtil) {
		this.logger = logger;
		this.websearchConfig = websearchConfig;
		this.updateDeterminer = updateDeterminer;
		this.crawlerService = crawlerService;
		this.runOnlyOnceATime = runOnlyOnceATime;
		this.websearchRobotsTxtUtil = websearchRobotsTxtUtil;
	}

	public boolean refresh() {
		logger.debug("websearch refresh - started");
		if (runOnlyOnceATime.run(new RefreshPages())) {
			logger.debug("websearch refresh - finished");
			return true;
		}
		else {
			logger.debug("websearch refresh - skipped");
			return false;
		}
	}
}
