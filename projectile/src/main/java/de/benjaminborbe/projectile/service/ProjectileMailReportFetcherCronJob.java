package de.benjaminborbe.projectile.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.projectile.util.ProjectileMailReportFetcher;

@Singleton
public class ProjectileMailReportFetcherCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 */5 * * * ?"; // ones per hour

	private final ProjectileMailReportFetcher projectileMailReportFetcher;

	private final Logger logger;

	@Inject
	public ProjectileMailReportFetcherCronJob(final Logger logger, final ProjectileMailReportFetcher projectileMailReportFetcher) {
		this.logger = logger;
		this.projectileMailReportFetcher = projectileMailReportFetcher;
	}

	@Override
	public String getScheduleExpression() {
		return SCHEDULE_EXPRESSION;
	}

	@Override
	public void execute() {
		logger.info("execute");
		projectileMailReportFetcher.fetch();
	}
}
