package de.benjaminborbe.analytics.util;

import java.util.Calendar;

import de.benjaminborbe.analytics.api.ReportValue;

public class ReportValueImpl implements ReportValue {

	private final Double value;

	private final Calendar date;

	public ReportValueImpl(final Calendar date, final Double value) {
		this.date = date;
		this.value = value;
	}

	@Override
	public Double getValue() {
		return value;
	}

	public Calendar getDate() {
		return date;
	}

}
