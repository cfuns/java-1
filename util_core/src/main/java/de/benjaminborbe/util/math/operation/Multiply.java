package de.benjaminborbe.util.math.operation;

import de.benjaminborbe.util.math.HasValue;

public class Multiply implements Operation {

	@Override
	public double calucate(final HasValue valueA, final HasValue valueB) {
		return valueA.getValue() * valueB.getValue();
	}

	@Override
	public String asString(final HasValue valueA, final HasValue valueB) {
		return valueA.asString() + " * " + valueB.asString();
	}

}
