package de.benjaminborbe.analytics.util;

import de.benjaminborbe.analytics.api.AnalyticsReportValue;
import de.benjaminborbe.analytics.api.AnalyticsReportValueIterator;
import de.benjaminborbe.analytics.api.AnalyticsReportValueListIterator;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;

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
		if (next == null) {
			Calendar nextDate = null;
			for (final AnalyticsReportValueIteratorCurrent analyticsReportValueIterator : analyticsReportValueIterators) {

				if (analyticsReportValueIterator.getCurrent() == null && analyticsReportValueIterator.hasNext()) {
					analyticsReportValueIterator.next();
				}

				if (analyticsReportValueIterator.getCurrent() != null) {
					final Calendar date = analyticsReportValueIterator.getCurrent().getDate();
					if (nextDate == null || date.before(nextDate)) {
						nextDate = date;
					}
				}
			}

			if (nextDate != null) {
				final List<AnalyticsReportValue> result = new ArrayList<AnalyticsReportValue>();
				for (final AnalyticsReportValueIteratorCurrent analyticsReportValueIterator : analyticsReportValueIterators) {
					final AnalyticsReportValue current = analyticsReportValueIterator.getCurrent();
					if (current != null && current.getDate().equals(nextDate)) {
						result.add(current);
						analyticsReportValueIterator.setCurrent(null);
					} else {
						result.add(null);
					}
				}
				next = result;
			}
		}
		return next != null;
	}

	@Override
	public List<AnalyticsReportValue> next() throws AnalyticsServiceException {
		if (hasNext()) {
			final List<AnalyticsReportValue> result = next;
			next = null;
			return result;
		} else {
			throw new NoSuchElementException();
		}
	}

}
