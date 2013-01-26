package de.benjaminborbe.monitoring.api;

import de.benjaminborbe.api.IdentifierBase;

public class MonitoringCheckIdentifier extends IdentifierBase<String> {

	public MonitoringCheckIdentifier(final long id) {
		this(String.valueOf(id));
	}

	public MonitoringCheckIdentifier(final String id) {
		super(id);
	}

}
