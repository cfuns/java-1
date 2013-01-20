package de.benjaminborbe.monitoring.api;

import de.benjaminborbe.api.IdentifierBase;

public class MonitoringNodeIdentifier extends IdentifierBase<String> {

	public MonitoringNodeIdentifier(final long id) {
		this(String.valueOf(id));
	}

	public MonitoringNodeIdentifier(final String id) {
		super(id);
	}

}
