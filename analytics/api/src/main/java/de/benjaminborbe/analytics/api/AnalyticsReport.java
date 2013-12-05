package de.benjaminborbe.analytics.api;

public interface AnalyticsReport {

	AnalyticsReportIdentifier getId();

	AnalyticsReportAggregation getAggregation();

	String getName();

	String getDescription();
}
