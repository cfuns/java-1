package de.benjaminborbe.analytics.gui.chart;

import de.benjaminborbe.analytics.api.AnalyticsReport;
import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;

import javax.inject.Inject;

public class AnalyticsReportLabelBuilder {

	@Inject
	public AnalyticsReportLabelBuilder() {
	}

	public String createLabel(final AnalyticsReport analyticsReport) {
		final String description = analyticsReport.getDescription();
		if (description != null && !description.trim().isEmpty()) {
			return description;
		} else {
			return analyticsReport.getId().getId();
		}
	}
}
