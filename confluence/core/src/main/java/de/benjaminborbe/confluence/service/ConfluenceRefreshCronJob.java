package de.benjaminborbe.confluence.service;

import de.benjaminborbe.confluence.config.ConfluenceConfig;
import de.benjaminborbe.confluence.util.ConfluencePagesRefresher;
import de.benjaminborbe.cron.api.CronJob;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ConfluenceRefreshCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 */15 * * * ?"; // ones per hour

	private final ConfluencePagesRefresher confluenceRefresher;

	private final Logger logger;

	private final ConfluenceConfig confluenceConfig;

	@Inject
	public ConfluenceRefreshCronJob(final Logger logger, final ConfluenceConfig confluenceConfig, final ConfluencePagesRefresher confluenceRefresher) {
		this.logger = logger;
		this.confluenceConfig = confluenceConfig;
		this.confluenceRefresher = confluenceRefresher;
	}

	@Override
	public String getScheduleExpression() {
		return SCHEDULE_EXPRESSION;
	}

	@Override
	public void execute() {
		if (Boolean.TRUE.equals(confluenceConfig.getCronEnabled())) {
			logger.trace("confluence refresh cron => started");
			confluenceRefresher.refresh();
			logger.trace("confluence refresh cron => finished");
		} else {
			logger.trace("confluence refresh cron => skip");
		}
	}

	@Override
	public boolean disallowConcurrentExecution() {
		return true;
	}
}
