package de.benjaminborbe.analytics.util;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import de.benjaminborbe.analytics.api.AnalyticsReportValue;
import de.benjaminborbe.analytics.api.AnalyticsReportValueIterator;
import de.benjaminborbe.analytics.api.AnalyticsReportValueListIterator;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;

public class AnalyticsReportValueListIteratorImpl implements AnalyticsReportValueListIterator {

	private final List<AnalyticsReportValueIteratorCurrent> analyticsReportValueIterators = new ArrayList<AnalyticsReportValueIteratorCurrent>();

	private List<AnalyticsReportValue> next;

	public AnalyticsReportValueListIteratorImpl(final List<AnalyticsReportValueIterator> analyticsReportValueIterators) {
		for (final AnalyticsReportValueIterator analyticsReportValueIterator : analyticsReportValueIterators) {
			this.analyticsReportValueIterators.add(new AnalyticsReportValueIteratorCurrent(analyticsReportValueIterator));
		}
	}

	@Override
	public boolean hasNext() throws AnalyticsServiceException {
		if (next != null) {
			return true;
		}

		final List<AnalyticsReportValue> result = new ArrayList<AnalyticsReportValue>();
		boolean hasNext = false;
		for (final AnalyticsReportValueIteratorCurrent analyticsReportValueIterator : analyticsReportValueIterators) {

			if (analyticsReportValueIterator.getCurrent() == null && analyticsReportValueIterator.hasNext()) {
				analyticsReportValueIterator.next();
			}

			final AnalyticsReportValue value;
			if (analyticsReportValueIterator.getCurrent() != null) {
				value = analyticsReportValueIterator.getCurrent();
				hasNext = true;
			}
			else {
				value = null;
			}
			result.add(value);
		}

		if (hasNext) {
			next = result;
			for (final AnalyticsReportValueIteratorCurrent analyticsReportValueIterator : analyticsReportValueIterators) {
				analyticsReportValueIterator.setCurrent(null);
			}
		}

		return hasNext;
	}

	@Override
	public List<AnalyticsReportValue> next() throws AnalyticsServiceException {
		if (hasNext()) {
			final List<AnalyticsReportValue> result = next;
			next = null;
			return result;
		}
		else {
			throw new NoSuchElementException();
		}
	}

}
