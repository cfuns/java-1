package de.benjaminborbe.util.math;

public class Subtraction implements HasValue {

	private final HasValue a;

	private final HasValue b;

	public Subtraction(final HasValue a, final HasValue b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public double getValue() {
		return a.getValue() - b.getValue();
	}

	@Override
	public String asString() {
		return a.asString() + " - " + b.asString();
	}

}
