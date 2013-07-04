package de.benjaminborbe.analytics.util;

import de.benjaminborbe.analytics.api.AnalyticsReportValue;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.Collection;

public class AnalyticsAggregatorCalculatorOldest implements AnalyticsAggregatorCalculator {

	@Inject
	public AnalyticsAggregatorCalculatorOldest() {
	}

	@Override
	public AnalyticsReportValue aggregate(final Calendar calendar, final Collection<AnalyticsReportValue> values) {
		AnalyticsReportValue value = null;
		for (final AnalyticsReportValue e : values) {
			if (value == null || e.getDate().before(value.getDate())) {
				value = e;
			}
		}
		return value;
	}
}
