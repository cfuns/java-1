package de.benjaminborbe.analytics.util;

import de.benjaminborbe.analytics.api.AnalyticsReportValue;
import de.benjaminborbe.analytics.api.AnalyticsReportValueIterator;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;

public class AnalyticsReportValueIteratorFillMissingValues implements AnalyticsReportValueIterator {

	private final AnalyticsReportValueIterator analyticsReportValueIterator;

	public AnalyticsReportValueIteratorFillMissingValues(final AnalyticsReportValueIterator analyticsReportValueIterator) {
		this.analyticsReportValueIterator = analyticsReportValueIterator;
	}

	@Override
	public boolean hasNext() throws AnalyticsServiceException {
		return analyticsReportValueIterator.hasNext();
	}

	@Override
	public AnalyticsReportValue next() throws AnalyticsServiceException {
		return analyticsReportValueIterator.next();
	}

}
