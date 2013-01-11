package de.benjaminborbe.analytics.api;

public interface ReportValueIterator {

	boolean hasNext() throws AnalyticsServiceException;

	ReportValue next() throws AnalyticsServiceException;
}
