package de.benjaminborbe.websearch.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.websearch.config.WebsearchConfig;
import de.benjaminborbe.websearch.util.WebsearchRefresher;

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
			logger.trace("websearch refresh cron => started");
			websearchRefresher.refresh();
			logger.trace("websearch refresh cron => finished");
		}
		else {
			logger.trace("websearch refresh cron => skip");
		}
	}

	@Override
	public boolean disallowConcurrentExecution() {
		return true;
	}

}
