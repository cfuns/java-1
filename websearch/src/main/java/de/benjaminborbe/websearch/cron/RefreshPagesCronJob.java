package de.benjaminborbe.websearch.cron;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.crawler.api.CrawlerException;
import de.benjaminborbe.crawler.api.CrawlerInstruction;
import de.benjaminborbe.crawler.api.CrawlerInstructionBuilder;
import de.benjaminborbe.crawler.api.CrawlerService;
import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.websearch.page.PageBean;
import de.benjaminborbe.websearch.page.PageDao;

@Singleton
public class RefreshPagesCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 15 * * * ?";

	private final Logger logger;

	private final PageDao pageDao;

	private final CrawlerService crawlerService;

	@Inject
	public RefreshPagesCronJob(final Logger logger, final PageDao pageDao, final CrawlerService crawlerService) {
		this.logger = logger;
		this.pageDao = pageDao;
		this.crawlerService = crawlerService;
	}

	@Override
	public String getScheduleExpression() {
		return SCHEDULE_EXPRESSION;
	}

	@Override
	public void execute() {
		logger.debug("execute");
		for (final PageBean page : pageDao.findExpiredPages()) {
			final CrawlerInstruction crawlerInstruction = new CrawlerInstructionBuilder(page.getId());
			try {
				crawlerService.processCrawlerInstruction(crawlerInstruction);
			}
			catch (final CrawlerException e) {
				logger.error("CrawlerException", e);
			}
		}
	}
}
