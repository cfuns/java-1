package de.benjaminborbe.analytics.util;

import com.google.inject.Inject;

import de.benjaminborbe.analytics.api.AnalyticsReportValue;
import de.benjaminborbe.analytics.api.AnalyticsReportValueIterator;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.api.IteratorWithException;
import de.benjaminborbe.tools.iterator.IteratorCurrent;

public class AnalyticsReportValueIteratorCurrent extends IteratorCurrent<AnalyticsReportValue, AnalyticsServiceException> implements AnalyticsReportValueIterator {

	@Inject
	public AnalyticsReportValueIteratorCurrent(final IteratorWithException<AnalyticsReportValue, AnalyticsServiceException> iterator) {
		super(iterator);
	}

}
