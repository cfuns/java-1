package de.benjaminborbe.analytics.gui;

import de.benjaminborbe.analytics.api.AnalyticsReportInterval;
import de.benjaminborbe.analytics.gui.chart.AnalyticsReportChartType;

public interface AnalyticsGuiConstants {

	AnalyticsReportInterval DEFAULT_INTERVAL = AnalyticsReportInterval.MINUTE;

	AnalyticsReportChartType DEFAULT_VIEW = AnalyticsReportChartType.LINECHART;

	String NAME = "analytics";

	String PARAMETER_AUTH_TOKEN = "token";

	String PARAMETER_CHART_TYPE = "chart_type";

	String PARAMETER_DATE = "date";

	String PARAMETER_REFERER = "referer";

	String PARAMETER_REPORT_AGGREGATION = "report_aggregation";

	String PARAMETER_REPORT_ID = "report_id";

	String PARAMETER_REPORT_INTERVAL = "report_interval";

	String PARAMETER_REPORT_NAME = "report_name";

	String PARAMETER_VALUE = "value";

	String URL_REPORT_ADD_DATA = "/report/add/data";

	String URL_REPORT_ADD_VALUE = "/report/add/value";

	String URL_REPORT_AGGREGATE = "/report/aggregate";

	String URL_REPORT_CREATE = "/report/create";

	String URL_REPORT_DELETE = "/report/delete";

	String URL_REPORT_LIST = "/";

	String URL_REPORT_VIEW = "/report/view";

	String CONFIG_AUTH_TOKEN = "AnalyticsAuthToken";

}
