package de.benjaminborbe.microblog.service;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.microblog.config.MicroblogConfig;
import de.benjaminborbe.microblog.util.MicroblogPostRefresher;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MicroblogCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 * * * * ?";

	private final MicroblogPostRefresher microblogRefresher;

	private final Logger logger;

	private final MicroblogConfig microblogConfig;

	@Inject
	public MicroblogCronJob(final Logger logger, final MicroblogPostRefresher microblogRefresher, final MicroblogConfig microblogConfig) {
		this.logger = logger;
		this.microblogRefresher = microblogRefresher;
		this.microblogConfig = microblogConfig;
	}

	@Override
	public String getScheduleExpression() {
		return SCHEDULE_EXPRESSION;
	}

	@Override
	public boolean disallowConcurrentExecution() {
		return true;
	}

	@Override
	public void execute() {
		if (microblogConfig.isCronEnabled()) {
			logger.debug("microblog refresh cron => started");
			microblogRefresher.refresh();
			logger.debug("microblog refresh cron => finished");
		} else {
			logger.debug("microblog refresh cron => skip");
		}
	}
}
