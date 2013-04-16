package de.benjaminborbe.util.core.math.operation;

import de.benjaminborbe.util.core.math.HasValue;

public class Division implements Operation {

	@Override
	public double calucate(final HasValue valueA, final HasValue valueB) {
		return valueA.getValue() / valueB.getValue();
	}

	@Override
	public String asString(final HasValue valueA, final HasValue valueB) {
		return valueA.asString() + " / " + valueB.asString();
	}

}
