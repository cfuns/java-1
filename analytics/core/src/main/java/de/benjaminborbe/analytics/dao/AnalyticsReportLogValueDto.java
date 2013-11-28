package de.benjaminborbe.analytics.dao;

import de.benjaminborbe.analytics.api.AnalyticsReportValueDto;

import java.util.Calendar;

public class AnalyticsReportLogValueDto extends AnalyticsReportValueDto implements AnalyticsReportLogValue {

	private String columnName;

	public AnalyticsReportLogValueDto() {
		super();
	}

	public AnalyticsReportLogValueDto(final String columnName, final Calendar date, final Double value) {
		super(date, value, 1L);
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
