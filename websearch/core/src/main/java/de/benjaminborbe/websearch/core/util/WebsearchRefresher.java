package de.benjaminborbe.websearch.core.util;

import de.benjaminborbe.crawler.api.CrawlerInstruction;
import de.benjaminborbe.crawler.api.CrawlerInstructionBuilder;
import de.benjaminborbe.crawler.api.CrawlerService;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.tools.synchronize.RunOnlyOnceATimeAsync;
import de.benjaminborbe.websearch.core.config.WebsearchConfig;
import de.benjaminborbe.websearch.core.dao.WebsearchPageBean;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.net.URL;

public class WebsearchRefresher {

	public static final int DEFAULT_TIMEOUT = 5000;

	private final class RefreshPages implements Runnable {

		@Override
		public void run() {
			try {
				logger.trace("refresh pages started");
				final EntityIterator<WebsearchPageBean> i = updateDeterminer.determineExpiredPages();
				int counter = 0;
				while (i.hasNext()) {
					final WebsearchPageBean page = i.next();
					try {
						final Integer refreshLimit = websearchConfig.getRefreshLimit();
						if (refreshLimit != null && refreshLimit >= 0 && counter > refreshLimit) {
							logger.debug("refresh pages limit reached => exit");
							return;
						}
						final URL url = page.getUrl();
						if (!websearchRobotsTxtUtil.isAllowed(url)) {
							logger.debug("robots.txt disallow url: " + url);
						} else {
							counter++;

							logger.debug("trigger refresh of url " + url);
							final CrawlerInstruction crawlerInstruction = new CrawlerInstructionBuilder(url, page.getDepth(), getTimeout(page));
							crawlerService.processCrawlerInstruction(crawlerInstruction);
						}
					} catch (final Exception e) {
						logger.error(e.getClass().getSimpleName(), e);
					}
				}
				logger.trace("refresh pages finished");
			} catch (final Exception e) {
				logger.error(e.getClass().getSimpleName(), e);
			}
		}
	}

	private Integer getTimeout(final WebsearchPageBean page) {
		final Integer timeout = page.getTimeout();
		return timeout != null ? timeout : DEFAULT_TIMEOUT;
	}

	private final CrawlerService crawlerService;

	private final WebsearchUpdateDeterminer updateDeterminer;

	private final RunOnlyOnceATimeAsync runOnlyOnceATime;

	private final Logger logger;

	private final WebsearchConfig websearchConfig;

	private final WebsearchRobotsTxtUtil websearchRobotsTxtUtil;

	@Inject
	public WebsearchRefresher(
		final Logger logger,
		final WebsearchConfig websearchConfig,
		final WebsearchUpdateDeterminer updateDeterminer,
		final CrawlerService crawlerService,
		final RunOnlyOnceATimeAsync runOnlyOnceATime,
		final WebsearchRobotsTxtUtil websearchRobotsTxtUtil
	) {
		this.logger = logger;
		this.websearchConfig = websearchConfig;
		this.updateDeterminer = updateDeterminer;
		this.crawlerService = crawlerService;
		this.runOnlyOnceATime = runOnlyOnceATime;
		this.websearchRobotsTxtUtil = websearchRobotsTxtUtil;
	}

	public boolean refresh() {
		logger.trace("websearch refresh - started");
		if (runOnlyOnceATime.run(new RefreshPages())) {
			logger.trace("websearch refresh - finished");
			return true;
		} else {
			logger.trace("websearch refresh - skipped");
			return false;
		}
	}
}
