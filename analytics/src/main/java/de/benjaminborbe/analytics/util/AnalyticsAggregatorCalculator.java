package de.benjaminborbe.analytics.util;

import java.util.Calendar;
import java.util.Collection;
import de.benjaminborbe.analytics.api.AnalyticsReportValue;

public interface AnalyticsAggregatorCalculator {

	AnalyticsReportValue aggregate(Calendar calendar, final Collection<AnalyticsReportValue> values);
}
