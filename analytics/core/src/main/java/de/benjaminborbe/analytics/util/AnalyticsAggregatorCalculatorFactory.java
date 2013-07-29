package de.benjaminborbe.analytics.util;

import de.benjaminborbe.analytics.api.AnalyticsReportAggregation;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class AnalyticsAggregatorCalculatorFactory {

	private final Map<AnalyticsReportAggregation, AnalyticsAggregatorCalculator> data = new HashMap<AnalyticsReportAggregation, AnalyticsAggregatorCalculator>();

	@Inject
	public AnalyticsAggregatorCalculatorFactory(
		final AnalyticsAggregatorCalculatorAvg analyticsAggregatorCalculatorAvg,
		final AnalyticsAggregatorCalculatorSum analyticsAggregatorCalculatorSum,
		final AnalyticsAggregatorCalculatorLatest analyticsAggregatorCalculatorLatest,
		final AnalyticsAggregatorCalculatorMax analyticsAggregatorCalculatorMax,
		final AnalyticsAggregatorCalculatorMin analyticsAggregatorCalculatorMin,
		final AnalyticsAggregatorCalculatorOldest analyticsAggregatorCalculatorOldest
	) {
		data.put(AnalyticsReportAggregation.AVG, analyticsAggregatorCalculatorAvg);
		data.put(AnalyticsReportAggregation.SUM, analyticsAggregatorCalculatorSum);
		data.put(AnalyticsReportAggregation.LATEST, analyticsAggregatorCalculatorLatest);
		data.put(AnalyticsReportAggregation.MAX, analyticsAggregatorCalculatorMax);
		data.put(AnalyticsReportAggregation.MIN, analyticsAggregatorCalculatorMin);
		data.put(AnalyticsReportAggregation.OLDEST, analyticsAggregatorCalculatorOldest);
	}

	public AnalyticsAggregatorCalculator get(final AnalyticsReportAggregation analyticsReportAggregation) {
		return data.get(analyticsReportAggregation);
	}
}
