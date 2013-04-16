package de.benjaminborbe.util.core.math.function;

import de.benjaminborbe.util.core.math.HasValue;

public class FunctionValue implements HasValue {

	private final HasValue[] values;

	private final Function function;

	public FunctionValue(final Function function, final HasValue... values) {
		this.function = function;
		this.values = values;
	}

	@Override
	public double getValue() {
		return function.calucate(values);
	}

	@Override
	public String asString() {
		return function.asString(values);
	}

}
