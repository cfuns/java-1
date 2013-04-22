package de.benjaminborbe.analytics.util;

import java.util.Calendar;
import java.util.Collection;

import javax.inject.Inject;

import de.benjaminborbe.analytics.api.AnalyticsReportValue;

public class AnalyticsAggregatorCalculatorLatest implements AnalyticsAggregatorCalculator {

	@Inject
	public AnalyticsAggregatorCalculatorLatest() {
	}

	@Override
	public AnalyticsReportValue aggregate(final Calendar calendar, final Collection<AnalyticsReportValue> values) {
		AnalyticsReportValue value = null;
		for (final AnalyticsReportValue e : values) {
			if (value == null || e.getDate().after(value.getDate())) {
				value = e;
			}
		}
		return value;
	}
}