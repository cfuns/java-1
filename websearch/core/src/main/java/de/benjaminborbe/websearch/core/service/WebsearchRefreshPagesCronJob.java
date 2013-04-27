package de.benjaminborbe.websearch.core.service;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.websearch.core.config.WebsearchConfig;
import de.benjaminborbe.websearch.core.util.WebsearchRefresher;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WebsearchRefreshPagesCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 */15 * * * ?"; // ones per hour

	private final Logger logger;

	private final WebsearchConfig websearchConfig;

	private final WebsearchRefresher websearchRefresher;

	@Inject
	public WebsearchRefreshPagesCronJob(final Logger logger, final WebsearchConfig websearchConfig, final WebsearchRefresher websearchRefresher) {
		this.logger = logger;
		this.websearchConfig = websearchConfig;
		this.websearchRefresher = websearchRefresher;
	}

	@Override
	public String getScheduleExpression() {
		return SCHEDULE_EXPRESSION;
	}

	@Override
	public void execute() {
		if (Boolean.TRUE.equals(websearchConfig.getCronEnabled())) {
			logger.debug("websearch refresh cron => started");
			websearchRefresher.refresh();
			logger.debug("websearch refresh cron => finished");
		} else {
			logger.debug("websearch refresh cron => skip");
		}
	}

	@Override
	public boolean disallowConcurrentExecution() {
		return true;
	}

}
