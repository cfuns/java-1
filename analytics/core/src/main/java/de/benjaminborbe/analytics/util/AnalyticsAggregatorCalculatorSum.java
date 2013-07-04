package de.benjaminborbe.analytics.util;

import de.benjaminborbe.analytics.api.AnalyticsReportValue;
import de.benjaminborbe.analytics.api.AnalyticsReportValueDto;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.Collection;

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
