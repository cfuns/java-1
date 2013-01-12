package de.benjaminborbe.analytics.api;

public interface AnalyticsReportValueIterator {

	boolean hasNext() throws AnalyticsServiceException;

	AnalyticsReportValue next() throws AnalyticsServiceException;
}
