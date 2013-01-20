package de.benjaminborbe.monitoring.dao;

import de.benjaminborbe.tools.mapper.Mapper;

public class MapperMonitoringNodeIdentifier implements Mapper<MonitoringNodeIdentifier> {

	@Override
	public String toString(final MonitoringNodeIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public MonitoringNodeIdentifier fromString(final String value) {
		return value != null ? new MonitoringNodeIdentifier(value) : null;
	}

}
