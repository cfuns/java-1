package de.benjaminborbe.websearch.cron;

import java.net.URL;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.crawler.api.CrawlerException;
import de.benjaminborbe.crawler.api.CrawlerInstruction;
import de.benjaminborbe.crawler.api.CrawlerInstructionBuilder;
import de.benjaminborbe.crawler.api.CrawlerService;
import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.tools.synchronize.RunOnlyOnceATime;
import de.benjaminborbe.tools.util.ThreadPoolExecuter;
import de.benjaminborbe.tools.util.ThreadRunner;
import de.benjaminborbe.websearch.page.PageBean;
import de.benjaminborbe.websearch.util.UpdateDeterminer;

@Singleton
public class RefreshPagesCronJob implements CronJob {

	private final class RefreshRunnable implements Runnable {

		@Override
		public void run() {
			runOnlyOnceATime.run(new RefreshPages());
		}
	}

	private final class RefreshPage implements Runnable {

		private final PageBean page;

		private RefreshPage(final PageBean page) {
			this.page = page;
		}

		@Override
		public void run() {
			try {
				final URL url = page.getUrl();
				logger.trace("trigger refresh of url " + url.toExternalForm());
				final CrawlerInstruction crawlerInstruction = new CrawlerInstructionBuilder(url);
				crawlerService.processCrawlerInstruction(crawlerInstruction);
			}
			catch (final CrawlerException e) {
				logger.error("CrawlerException", e);
			}
		}
	}

	private final class RefreshPages implements Runnable {

		@Override
		public void run() {
			logger.trace("refresh pages started");
			try {
				for (final PageBean page : updateDeterminer.determineExpiredPages()) {
					threadPoolExecuter.execute(new RefreshPage(page));
				}
			}
			catch (final StorageException e) {
				logger.error("StorageException", e);
			}
			logger.trace("refresh pages finished");
		}
	}

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 */15 * * * ?"; // ones per hour

	private final Logger logger;

	private final CrawlerService crawlerService;

	private final UpdateDeterminer updateDeterminer;

	private final RunOnlyOnceATime runOnlyOnceATime;

	private final ThreadRunner threadRunner;

	private final ThreadPoolExecuter threadPoolExecuter;

	@Inject
	public RefreshPagesCronJob(
			final Logger logger,
			final UpdateDeterminer updateDeterminer,
			final CrawlerService crawlerService,
			final RunOnlyOnceATime runOnlyOnceATime,
			final ThreadRunner threadRunner,
			final ThreadPoolExecuter threadPoolExecuter) {
		this.logger = logger;
		this.updateDeterminer = updateDeterminer;
		this.crawlerService = crawlerService;
		this.runOnlyOnceATime = runOnlyOnceATime;
		this.threadRunner = threadRunner;
		this.threadPoolExecuter = threadPoolExecuter;
	}

	@Override
	public String getScheduleExpression() {
		return SCHEDULE_EXPRESSION;
	}

	@Override
	public void execute() {
		logger.trace("execute started");
		threadRunner.run("refreshpages", new RefreshRunnable());
		logger.trace("execute finished");
	}
}
