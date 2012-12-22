package de.benjaminborbe.projectile.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.projectile.config.ProjectileConfig;
import de.benjaminborbe.projectile.util.ProjectileMailReportFetcher;

@Singleton
public class ProjectileMailReportFetcherCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 */15 * * * ?"; // ones per hour

	private final ProjectileMailReportFetcher projectileMailReportFetcher;

	private final Logger logger;

	private final ProjectileConfig projectileConfig;

	@Inject
	public ProjectileMailReportFetcherCronJob(final Logger logger, final ProjectileConfig projectileConfig, final ProjectileMailReportFetcher projectileMailReportFetcher) {
		this.logger = logger;
		this.projectileConfig = projectileConfig;
		this.projectileMailReportFetcher = projectileMailReportFetcher;
	}

	@Override
	public String getScheduleExpression() {
		return SCHEDULE_EXPRESSION;
	}

	@Override
	public void execute() {
		if (Boolean.TRUE.equals(projectileConfig.getCronActive())) {
			logger.debug("execute");
			projectileMailReportFetcher.fetch();
		}
		else {
			logger.debug("skip execute, cron not active");
		}
	}
}
