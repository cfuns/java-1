package de.benjaminborbe.monitoring.check;

import de.benjaminborbe.monitoring.api.Check;

public class HasCheckNodeImpl implements HasCheckNode {

	private final Check check;

	public HasCheckNodeImpl(final Check check) {
		this.check = check;
	}

	@Override
	public Check getCheck() {
		return check;
	}

	@Override
	public String toString() {
		return "HasCheckNodeImpl with check " + check;
	}
}
