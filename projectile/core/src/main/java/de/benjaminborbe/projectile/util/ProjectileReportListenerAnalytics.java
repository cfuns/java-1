package de.benjaminborbe.projectile.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import javax.inject.Inject;

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
					final AnalyticsReportIdentifier analyticsReportIdentifier = buildAnalyticsReportIdentifier(PREFIX, report.getUsername(), "BILLABLE");
					analyticsService.addReportValue(analyticsReportIdentifier, report.getBillable());
				}
				{
					final AnalyticsReportIdentifier analyticsReportIdentifier = buildAnalyticsReportIdentifier(PREFIX, report.getUsername(), "EXTERN");
					analyticsService.addReportValue(analyticsReportIdentifier, report.getExtern());
				}
				{
					final AnalyticsReportIdentifier analyticsReportIdentifier = buildAnalyticsReportIdentifier(PREFIX, report.getUsername(), "INTERN");
					analyticsService.addReportValue(analyticsReportIdentifier, report.getIntern());
				}
				{
					final AnalyticsReportIdentifier analyticsReportIdentifier = buildAnalyticsReportIdentifier(PREFIX, report.getUsername(), "TARGET");
					analyticsService.addReportValue(analyticsReportIdentifier, report.getTarget());
				}
			}
		}
		catch (final AnalyticsServiceException e) {
			logger.warn(e.getClass().getName(), e);
		}
	}

	private AnalyticsReportIdentifier buildAnalyticsReportIdentifier(final String... parts) {
		return new AnalyticsReportIdentifier(StringUtils.join(parts, '-'));
	}

}
