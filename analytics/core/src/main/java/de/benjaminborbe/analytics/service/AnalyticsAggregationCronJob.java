package de.benjaminborbe.analytics.service;

import de.benjaminborbe.analytics.config.AnalyticsConfig;
import de.benjaminborbe.analytics.util.AnalyticsAggregator;
import de.benjaminborbe.cron.api.CronJob;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AnalyticsAggregationCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 */5 * * * ?"; // ones per hour

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
			logger.trace("execute");
			analyticsAggregator.aggregate();
		} else {
			logger.trace("skip execute, cron not active");
		}
	}

	@Override
	public boolean disallowConcurrentExecution() {
		return true;
	}

}
