package de.benjaminborbe.util.math.function;

import java.io.StringWriter;

import de.benjaminborbe.util.math.HasValue;

public class Max implements Function {

	@Override
	public final double calucate(final HasValue... values) {
		double result = values[0].getValue();
		for (final HasValue value : values) {
			result = Math.max(result, value.getValue());
		}
		return result;
	}

	@Override
	public String asString(final HasValue... values) {
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
