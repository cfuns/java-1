package de.benjaminborbe.analytics.dao;

import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsReportInterval;
import de.benjaminborbe.api.IdentifierBase;

public class AnalyticsReportValueIdentifier extends IdentifierBase<String> {

	private final static String SEPERATOR = "-";

	private final AnalyticsReportIdentifier reportIdentifier;

	private final AnalyticsReportInterval reportInterval;

	public AnalyticsReportValueIdentifier(final AnalyticsReportIdentifier analyticsReportIdentifier, final AnalyticsReportInterval analyticsReportInterval) {
		super(analyticsReportIdentifier.getId() + SEPERATOR + analyticsReportInterval.name());
		this.reportIdentifier = analyticsReportIdentifier;
		this.reportInterval = analyticsReportInterval;
	}

	public AnalyticsReportIdentifier getReportIdentifier() {
		return reportIdentifier;
	}

	public AnalyticsReportInterval getReportInterval() {
		return reportInterval;
	}
}
