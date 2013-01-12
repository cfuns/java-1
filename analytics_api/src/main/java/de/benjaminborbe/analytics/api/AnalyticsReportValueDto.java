package de.benjaminborbe.analytics.api;

import java.util.Calendar;

public class AnalyticsReportValueDto implements AnalyticsReportValue {

	private Double value;

	private Calendar date;

	public AnalyticsReportValueDto() {
	}

	public AnalyticsReportValueDto(final Calendar date, final Double value) {
		this.date = date;
		this.value = value;
	}

	@Override
	public Double getValue() {
		return value;
	}

	@Override
	public Calendar getDate() {
		return date;
	}

	public void setValue(final Double value) {
		this.value = value;
	}

	public void setDate(final Calendar date) {
		this.date = date;
	}

}
