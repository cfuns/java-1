package de.benjaminborbe.analytics.api;

import java.util.Calendar;

public class ReportValueDto implements ReportValue {

	private Double value;

	private Calendar date;

	public ReportValueDto() {
	}

	public ReportValueDto(final Calendar date, final Double value) {
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
