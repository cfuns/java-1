package de.benjaminborbe.analytics.util;

import de.benjaminborbe.analytics.api.AnalyticsReportAggregation;
import de.benjaminborbe.analytics.api.AnalyticsReportInterval;
import de.benjaminborbe.analytics.api.AnalyticsReportValue;
import de.benjaminborbe.analytics.api.AnalyticsReportValueDto;
import de.benjaminborbe.analytics.api.AnalyticsReportValueIterator;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;

import java.util.Calendar;
import java.util.NoSuchElementException;

public class AnalyticsReportValueIteratorFillMissingValues implements AnalyticsReportValueIterator {

	private final AnalyticsReportValueIteratorCurrent analyticsReportValueIterator;

	private final AnalyticsReportInterval analyticsReportInterval;

	private final AnalyticsIntervalUtil analyticsIntervalUtil;

	private final AnalyticsReportAggregation analyticsReportAggregation;

	private Calendar nextDate = null;

	private AnalyticsReportValue next;

	public AnalyticsReportValueIteratorFillMissingValues(
		final AnalyticsIntervalUtil analyticsIntervalUtil,
		final AnalyticsReportValueIterator analyticsReportValueIterator,
		final AnalyticsReportAggregation analyticsReportAggregation,
		final AnalyticsReportInterval analyticsReportInterval
	) {
		this.analyticsIntervalUtil = analyticsIntervalUtil;
		this.analyticsReportValueIterator = new AnalyticsReportValueIteratorCurrent(analyticsReportValueIterator);
		this.analyticsReportAggregation = analyticsReportAggregation;
		this.analyticsReportInterval = analyticsReportInterval;
	}

	@Override
	public boolean hasNext() throws AnalyticsServiceException {
		while (next == null) {
			if (analyticsReportValueIterator.getCurrent() == null) {
				if (analyticsReportValueIterator.hasNext()) {
					analyticsReportValueIterator.next();
				} else {
					return false;
				}
			}
			if (nextDate == null) {
				nextDate = analyticsIntervalUtil.buildIntervalCalendar(analyticsReportValueIterator.getCurrent().getDate(), analyticsReportInterval);
			}
			if (analyticsReportValueIterator.getCurrent().getDate().before(nextDate)) {
				next = buildDefaultValue();
			} else if (analyticsReportValueIterator.getCurrent().getDate().equals(nextDate)) {
				next = analyticsReportValueIterator.getCurrent();
				analyticsReportValueIterator.setCurrent(null);
			} else {
				analyticsReportValueIterator.setCurrent(null);
			}
		}
		return next != null;
	}

	@Override
	public AnalyticsReportValue next() throws AnalyticsServiceException {
		if (hasNext()) {
			final AnalyticsReportValue result = next;
			next = null;
			nextDate = analyticsIntervalUtil.buildIntervalCalendarNext(nextDate, analyticsReportInterval);
			return result;
		} else {
			throw new NoSuchElementException();
		}
	}

	private AnalyticsReportValue buildDefaultValue() {
		if (AnalyticsReportAggregation.SUM.equals(analyticsReportAggregation)) {
			return new AnalyticsReportValueDto(nextDate, new Double(0), 0l);
		} else {
			return new AnalyticsReportValueDto(nextDate, null, 0l);
		}
	}

}
