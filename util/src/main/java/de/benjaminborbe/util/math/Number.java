package de.benjaminborbe.util.math;

public class Number implements HasValue {

	private final double value;

	public Number(final double value) {
		this.value = value;
	}

	@Override
	public double getValue() {
		return value;
	}

}
