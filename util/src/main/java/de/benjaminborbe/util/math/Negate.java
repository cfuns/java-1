package de.benjaminborbe.util.math;

public class Negate implements HasValue {

	private final HasValue value;

	public Negate(final HasValue value) {
		this.value = value;
	}

	@Override
	public long getValue() {
		return value.getValue() * -1;
	}

	@Override
	public String toString() {
		return value.getValue() + " * -1";
	}

}
