package de.benjaminborbe.util.core.math;

public class Bracket implements HasValue {

	private final HasValue value;

	public Bracket(final HasValue value) {
		this.value = value;
	}

	@Override
	public double getValue() {
		return value.getValue();
	}

	@Override
	public String asString() {
		return "(" + value.asString() + ")";
	}

}
