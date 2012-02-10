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
import de.benjaminborbe.websearch.page.PageBean;
import de.benjaminborbe.websearch.util.UpdateDeterminer;

@Singleton
public class RefreshPagesCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 15 * * * ?"; // ones per hour

	private final Logger logger;

	private final CrawlerService crawlerService;

	private final UpdateDeterminer updateDeterminer;

	@Inject
	public RefreshPagesCronJob(final Logger logger, final UpdateDeterminer updateDeterminer, final CrawlerService crawlerService) {
		this.logger = logger;
		this.updateDeterminer = updateDeterminer;
		this.crawlerService = crawlerService;
	}

	@Override
	public String getScheduleExpression() {
		return SCHEDULE_EXPRESSION;
	}

	@Override
	public void execute() {
		logger.debug("execute");
		for (final PageBean page : updateDeterminer.determineExpiredPages()) {
			try {
				final URL url = page.getUrl();
				logger.debug("trigger refresh of url " + url.toExternalForm());
				final CrawlerInstruction crawlerInstruction = new CrawlerInstructionBuilder(url);
				crawlerService.processCrawlerInstruction(crawlerInstruction);
			}
			catch (final CrawlerException e) {
				logger.error("CrawlerException", e);
			}
		}
	}
}
