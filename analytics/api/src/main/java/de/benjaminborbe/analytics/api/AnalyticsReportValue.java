package de.benjaminborbe.analytics.api;

import java.util.Calendar;

public interface AnalyticsReportValue {

	Long getCounter();

	Double getValue();

	Calendar getDate();
}
