package de.benjaminborbe.analytics.util;

import de.benjaminborbe.analytics.api.AnalyticsReportValue;
import de.benjaminborbe.analytics.api.AnalyticsReportValueDto;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.Collection;

public class AnalyticsAggregatorCalculatorAvg implements AnalyticsAggregatorCalculator {

	@Inject
	public AnalyticsAggregatorCalculatorAvg() {
	}

	@Override
	public AnalyticsReportValue aggregate(final Calendar calendar, final Collection<AnalyticsReportValue> values) {
		double value = 0d;
		long counter = 0;
		for (final AnalyticsReportValue e : values) {
			value += e.getValue() * e.getCounter();
			counter += e.getCounter();
		}
		return new AnalyticsReportValueDto(calendar, counter > 0 ? value / counter : 0, counter);
	}
}
