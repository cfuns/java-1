package de.benjaminborbe.websearch.cron;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.crawler.api.CrawlerInstruction;
import de.benjaminborbe.crawler.api.CrawlerInstructionBuilder;
import de.benjaminborbe.crawler.api.CrawlerService;
import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.tools.synchronize.RunOnlyOnceATime;
import de.benjaminborbe.tools.util.ThreadRunner;
import de.benjaminborbe.websearch.dao.WebsearchPageBean;
import de.benjaminborbe.websearch.util.WebsearchUpdateDeterminer;

@Singleton
public class WebsearchRefreshPagesCronJob implements CronJob {

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
				while (i.hasNext()) {
					final WebsearchPageBean page = i.next();
					try {
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

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 40 * * * ?"; // ones per hour

	private final Logger logger;

	private final CrawlerService crawlerService;

	private final WebsearchUpdateDeterminer updateDeterminer;

	private final RunOnlyOnceATime runOnlyOnceATime;

	private final ThreadRunner threadRunner;

	@Inject
	public WebsearchRefreshPagesCronJob(
			final Logger logger,
			final WebsearchUpdateDeterminer updateDeterminer,
			final CrawlerService crawlerService,
			final RunOnlyOnceATime runOnlyOnceATime,
			final ThreadRunner threadRunner) {
		this.logger = logger;
		this.updateDeterminer = updateDeterminer;
		this.crawlerService = crawlerService;
		this.runOnlyOnceATime = runOnlyOnceATime;
		this.threadRunner = threadRunner;
	}

	@Override
	public String getScheduleExpression() {
		return SCHEDULE_EXPRESSION;
	}

	@Override
	public void execute() {
		logger.debug("execute started");
		threadRunner.run("refreshpages", new RefreshRunnable());
		logger.debug("execute finished");
	}

	@Override
	public boolean disallowConcurrentExecution() {
		return true;
	}

}
