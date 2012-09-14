package de.benjaminborbe.util.math;

public class Addition implements HasValue {

	private final HasValue a;

	private final HasValue b;

	public Addition(final HasValue a, final HasValue b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public double getValue() {
		return a.getValue() + b.getValue();
	}

	@Override
	public String asString() {
		return a.asString() + " + " + b.asString();
	}

}
