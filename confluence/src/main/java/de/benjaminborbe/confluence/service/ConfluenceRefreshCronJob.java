package de.benjaminborbe.confluence.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.confluence.util.ConfluenceRefresher;
import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIteratorException;

@Singleton
public class ConfluenceRefreshCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 0 * * * ?"; // ones per hour

	private final ConfluenceRefresher confluenceRefresher;

	private final Logger logger;

	@Inject
	public ConfluenceRefreshCronJob(final Logger logger, final ConfluenceRefresher confluenceRefresher) {
		this.logger = logger;
		this.confluenceRefresher = confluenceRefresher;
	}

	@Override
	public String getScheduleExpression() {
		return SCHEDULE_EXPRESSION;
	}

	@Override
	public void execute() {
		try {
			logger.info("execute");
			confluenceRefresher.refresh();
		}
		catch (final EntityIteratorException e) {
			logger.warn(e.getClass().getName(), e);
		}
		catch (final StorageException e) {
			logger.warn(e.getClass().getName(), e);
		}
	}
}
