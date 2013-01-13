package de.benjaminborbe.analytics.util;

import java.util.Calendar;

import de.benjaminborbe.analytics.api.AnalyticsReportAggregation;
import de.benjaminborbe.analytics.api.AnalyticsReportInterval;
import de.benjaminborbe.analytics.api.AnalyticsReportValue;
import de.benjaminborbe.analytics.api.AnalyticsReportValueDto;
import de.benjaminborbe.analytics.api.AnalyticsReportValueIterator;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;

public class AnalyticsReportValueIteratorFillMissingValues implements AnalyticsReportValueIterator {

	private final AnalyticsReportValueIterator iterator;

	private final AnalyticsReportInterval interval;

	private final AnalyticsIntervalUtil analyticsIntervalUtil;

	private Calendar nextDate = null;

	private AnalyticsReportValue current;

	private final AnalyticsReportAggregation analyticsReportAggregation;

	public AnalyticsReportValueIteratorFillMissingValues(
			final AnalyticsIntervalUtil analyticsIntervalUtil,
			final AnalyticsReportValueIterator iterator,
			final AnalyticsReportAggregation analyticsReportAggregation,
			final AnalyticsReportInterval interval) {
		this.analyticsIntervalUtil = analyticsIntervalUtil;
		this.iterator = iterator;
		this.analyticsReportAggregation = analyticsReportAggregation;
		this.interval = interval;
	}

	@Override
	public boolean hasNext() throws AnalyticsServiceException {
		return current != null || iterator.hasNext();
	}

	@Override
	public AnalyticsReportValue next() throws AnalyticsServiceException {
		if (current == null) {
			current = iterator.next();
		}
		if (nextDate == null) {
			nextDate = current.getDate();
		}

		final AnalyticsReportValue result;
		if (compareDates()) {
			result = current;
			current = null;
		}
		else {
			result = buildDefaultValue();
		}
		nextDate = analyticsIntervalUtil.buildIntervalCalendarNext(nextDate, interval);
		return result;
	}

	private AnalyticsReportValue buildDefaultValue() {
		if (AnalyticsReportAggregation.SUM.equals(analyticsReportAggregation)) {
			return new AnalyticsReportValueDto(nextDate, new Double(0), 0l);
		}
		else {
			return new AnalyticsReportValueDto(nextDate, null, 0l);
		}
	}

	private boolean compareDates() {
		final Calendar d1 = nextDate;
		final Calendar d2 = analyticsIntervalUtil.buildIntervalCalendar(current.getDate(), interval);
		final boolean result = d1.equals(d2);
		return result;
	}
}
