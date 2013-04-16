package de.benjaminborbe.util.core.math.operation;

import de.benjaminborbe.util.core.math.HasValue;

public class OperationValue implements HasValue {

	private final Operation operation;

	private final HasValue valueA;

	private final HasValue valueB;

	public OperationValue(final Operation operation, final HasValue valueA, final HasValue valueB) {
		this.operation = operation;
		this.valueA = valueA;
		this.valueB = valueB;
	}

	@Override
	public double getValue() {
		return operation.calucate(valueA, valueB);
	}

	@Override
	public String asString() {
		return operation.asString(valueA, valueB);
	}
}
