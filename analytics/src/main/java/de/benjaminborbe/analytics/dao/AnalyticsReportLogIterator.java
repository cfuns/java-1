package de.benjaminborbe.analytics.dao;

import de.benjaminborbe.analytics.api.AnalyticsServiceException;

public interface AnalyticsReportLogIterator {

	boolean hasNext() throws AnalyticsServiceException;

	AnalyticsReportLogValue next() throws AnalyticsServiceException;
}
