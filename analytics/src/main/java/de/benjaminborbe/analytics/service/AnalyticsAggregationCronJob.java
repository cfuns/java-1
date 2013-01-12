package de.benjaminborbe.analytics.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.analytics.config.AnalyticsConfig;
import de.benjaminborbe.analytics.util.AnalyticsAggregator;
import de.benjaminborbe.cron.api.CronJob;

@Singleton
public class AnalyticsAggregationCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 */15 * * * ?"; // ones per hour

	private final AnalyticsAggregator analyticsAggregator;

	private final Logger logger;

	private final AnalyticsConfig analyticsConfig;

	@Inject
	public AnalyticsAggregationCronJob(final Logger logger, final AnalyticsConfig analyticsConfig, final AnalyticsAggregator analyticsAggregator) {
		this.logger = logger;
		this.analyticsConfig = analyticsConfig;
		this.analyticsAggregator = analyticsAggregator;
	}

	@Override
	public String getScheduleExpression() {
		return SCHEDULE_EXPRESSION;
	}

	@Override
	public void execute() {
		if (Boolean.TRUE.equals(analyticsConfig.getCronActive())) {
			logger.debug("execute");
			analyticsAggregator.aggregate();
		}
		else {
			logger.debug("skip execute, cron not active");
		}
	}

	@Override
	public boolean disallowConcurrentExecution() {
		return true;
	}

}
