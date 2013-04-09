package de.benjaminborbe.analytics.dao;

import java.util.Calendar;

import de.benjaminborbe.analytics.api.AnalyticsReportValueDto;

public class AnalyticsReportLogValueDto extends AnalyticsReportValueDto implements AnalyticsReportLogValue {

	private String columnName;

	public AnalyticsReportLogValueDto() {
		super();
	}

	public AnalyticsReportLogValueDto(final String columnName, final Calendar date, final Double value) {
		super(date, value, 1l);
		this.columnName = columnName;
	}

	@Override
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(final String columnName) {
		this.columnName = columnName;
	}

}
