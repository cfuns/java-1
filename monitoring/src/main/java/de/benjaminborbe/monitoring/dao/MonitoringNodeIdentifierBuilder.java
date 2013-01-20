package de.benjaminborbe.monitoring.dao;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.monitoring.api.MonitoringNodeIdentifier;

public class MonitoringNodeIdentifierBuilder implements IdentifierBuilder<String, MonitoringNodeIdentifier> {

	@Override
	public MonitoringNodeIdentifier buildIdentifier(final String value) {
		return new MonitoringNodeIdentifier(value);
	}

}
