package de.benjaminborbe.monitoring.check;

import de.benjaminborbe.monitoring.api.MonitoringCheck;

public class HasCheckNodeImpl implements HasCheckNode {

	private final MonitoringCheck check;

	public HasCheckNodeImpl(final MonitoringCheck check) {
		this.check = check;
	}

	@Override
	public MonitoringCheck getCheck() {
		return check;
	}

	@Override
	public String toString() {
		return "HasCheckNodeImpl with check " + check;
	}
}
