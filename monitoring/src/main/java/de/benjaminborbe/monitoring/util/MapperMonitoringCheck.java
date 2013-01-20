package de.benjaminborbe.monitoring.util;

import com.google.inject.Inject;

import de.benjaminborbe.monitoring.api.MonitoringCheckType;
import de.benjaminborbe.tools.mapper.MapperEnum;
import de.benjaminborbe.tools.util.ParseUtil;

public class MapperMonitoringCheck extends MapperEnum<MonitoringCheckType> {

	@Inject
	public MapperMonitoringCheck(final ParseUtil parseUtil) {
		super(parseUtil);
	}

	@Override
	protected Class<MonitoringCheckType> getEnumClass() {
		return MonitoringCheckType.class;
	}

}
