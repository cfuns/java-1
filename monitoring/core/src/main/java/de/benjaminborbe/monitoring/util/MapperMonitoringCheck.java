package de.benjaminborbe.monitoring.util;

import com.google.inject.Inject;

import de.benjaminborbe.monitoring.api.MonitoringCheckIdentifier;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.mapper.Mapper;

public class MapperMonitoringCheck implements Mapper<MonitoringCheckIdentifier> {

	@Inject
	public MapperMonitoringCheck() {
	}

	@Override
	public MonitoringCheckIdentifier fromString(final String id) throws MapException {
		return id != null ? new MonitoringCheckIdentifier(id) : null;
	}

	@Override
	public String toString(final MonitoringCheckIdentifier object) throws MapException {
		return object != null ? object.getId() : null;
	}

}
