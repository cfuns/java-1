package de.benjaminborbe.projectile.service;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.projectile.config.ProjectileConfig;
import de.benjaminborbe.projectile.util.ProjectileMailReportFetcher;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ProjectileMailReportFetcherCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 */15 * * * ?"; // ones per hour

	private final ProjectileMailReportFetcher projectileMailReportFetcher;

	private final Logger logger;

	private final ProjectileConfig projectileConfig;

	@Inject
	public ProjectileMailReportFetcherCronJob(
		final Logger logger,
		final ProjectileConfig projectileConfig,
		final ProjectileMailReportFetcher projectileMailReportFetcher
	) {
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
			logger.debug("projectile mail report cron => started");
			projectileMailReportFetcher.fetch();
			logger.debug("projectile mail report cron => finished");
		} else {
			logger.debug("projectile mail report cron => skipped");
		}
	}

	@Override
	public boolean disallowConcurrentExecution() {
		return true;
	}

}
