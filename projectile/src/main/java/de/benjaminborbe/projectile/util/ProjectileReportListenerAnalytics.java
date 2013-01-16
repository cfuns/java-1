package de.benjaminborbe.projectile.util;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.projectile.api.ProjectileSlacktimeReportInterval;

public class ProjectileReportListenerAnalytics implements ProjectileReportListener {

	private static final String PREFIX = "Projectile";

	private final AnalyticsService analyticsService;

	private final Logger logger;

	@Inject
	public ProjectileReportListenerAnalytics(final Logger logger, final AnalyticsService analyticsService) {
		this.logger = logger;
		this.analyticsService = analyticsService;
	}

	@Override
	public void onReport(final ProjectileSlacktimeReportInterval interval, final ProjectileCsvReportToDto report) {
		try {
			if (ProjectileSlacktimeReportInterval.YEAR.equals(interval)) {
				{
					final AnalyticsReportIdentifier analyticsReportIdentifier = new AnalyticsReportIdentifier(PREFIX + "_" + report.getUsername() + "_BILLABLE");
					analyticsService.addReportValue(analyticsReportIdentifier, report.getBillable());
				}
				{
					final AnalyticsReportIdentifier analyticsReportIdentifier = new AnalyticsReportIdentifier(PREFIX + "_" + report.getUsername() + "_EXTERN");
					analyticsService.addReportValue(analyticsReportIdentifier, report.getExtern());
				}
				{
					final AnalyticsReportIdentifier analyticsReportIdentifier = new AnalyticsReportIdentifier(PREFIX + "_" + report.getUsername() + "_INTERN");
					analyticsService.addReportValue(analyticsReportIdentifier, report.getIntern());
				}
				{
					final AnalyticsReportIdentifier analyticsReportIdentifier = new AnalyticsReportIdentifier(PREFIX + "_" + report.getUsername() + "_TARGET");
					analyticsService.addReportValue(analyticsReportIdentifier, report.getTarget());
				}
			}
		}
		catch (final AnalyticsServiceException e) {
			logger.warn(e.getClass().getName(), e);
		}
	}

}
