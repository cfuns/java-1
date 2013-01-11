package de.benjaminborbe.analytics.util;

import de.benjaminborbe.analytics.api.ReportValue;

public class ReportValueImpl implements ReportValue {

	private final String name;

	private final Double value;

	public ReportValueImpl(final String name, final Double value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Double getValue() {
		return value;
	}

}
