package de.benjaminborbe.util.math;

public abstract class Expression implements HasValue {

	private final HasValue valueA;

	private final HasValue valueB;

	public Expression(final HasValue valueA, final HasValue valueB) {
		this.valueA = valueA;
		this.valueB = valueB;
	}

	@Override
	public abstract long getValue();

	public HasValue getValueA() {
		return valueA;
	}

	public HasValue getValueB() {
		return valueB;
	}

}
