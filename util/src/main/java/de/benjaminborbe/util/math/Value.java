package de.benjaminborbe.util.math;

public class Value implements HasValue {

	private final long value;

	public Value(final int value) {
		this.value = value;
	}

	public Value(final long value) {
		this.value = value;
	}

	@Override
	public long getValue() {
		return value;
	}

}
