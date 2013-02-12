package de.benjaminborbe.analytics.util;

import java.util.Calendar;
import java.util.Collection;

import com.google.inject.Inject;

import de.benjaminborbe.analytics.api.AnalyticsReportValue;
import de.benjaminborbe.analytics.api.AnalyticsReportValueDto;

public class AnalyticsAggregatorCalculatorSum implements AnalyticsAggregatorCalculator {

	@Inject
	public AnalyticsAggregatorCalculatorSum() {
	}

	@Override
	public AnalyticsReportValue aggregate(final Calendar calendar, final Collection<AnalyticsReportValue> values) {
		double value = 0d;
		long counter = 0;
		for (final AnalyticsReportValue e : values) {
			value += e.getValue();
			counter += e.getCounter();
		}
		return new AnalyticsReportValueDto(calendar, value, counter);
	}
}
