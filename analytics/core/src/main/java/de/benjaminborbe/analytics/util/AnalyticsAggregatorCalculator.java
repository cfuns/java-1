package de.benjaminborbe.analytics.util;

import de.benjaminborbe.analytics.api.AnalyticsReportValue;

import java.util.Calendar;
import java.util.Collection;

public interface AnalyticsAggregatorCalculator {

	AnalyticsReportValue aggregate(Calendar calendar, final Collection<AnalyticsReportValue> values);
}
