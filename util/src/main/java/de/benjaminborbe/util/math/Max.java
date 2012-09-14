package de.benjaminborbe.util.math;

import java.io.StringWriter;

public class Max implements HasValue {

	private final HasValue[] values;

	public Max(final HasValue... values) {
		this.values = values;
	}

	@Override
	public double getValue() {
		double result = values[0].getValue();
		for (final HasValue value : values) {
			result = Math.max(result, value.getValue());
		}
		return result;
	}

	@Override
	public String asString() {
		final StringWriter sw = new StringWriter();
		sw.append("max(");
		boolean first = true;
		for (final HasValue value : values) {
			if (first) {
				first = false;
			}
			else {
				sw.append(",");
			}
			sw.append(value.asString());
		}
		sw.append(")");
		return sw.toString();
	}
}
