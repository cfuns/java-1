package de.benjaminborbe.monitoring.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.monitoring.api.MonitoringCheckType;
import de.benjaminborbe.monitoring.api.MonitoringNodeIdentifier;
import de.benjaminborbe.monitoring.util.MapperMonitoringCheck;
import de.benjaminborbe.monitoring.util.MapperMonitoringNodeIdentifier;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

@Singleton
public class MonitoringNodeBeanMapper extends MapObjectMapperAdapter<MonitoringNodeBean> {

	@Inject
	public MonitoringNodeBeanMapper(
			final Provider<MonitoringNodeBean> provider,
			final MapperMonitoringNodeIdentifier mapperMonitoringNodeIdentifier,
			final MapperString mapperString,
			final MapperCalendar mapperCalendar,
			final MapperMonitoringCheck mapperMonitoringCheck) {
		super(provider, buildMappings(mapperMonitoringNodeIdentifier, mapperString, mapperCalendar, mapperMonitoringCheck));
	}

	private static Collection<StringObjectMapper<MonitoringNodeBean>> buildMappings(final MapperMonitoringNodeIdentifier mapperMonitoringNodeIdentifier,
			final MapperString mapperString, final MapperCalendar mapperCalendar, final MapperMonitoringCheck mapperMonitoringCheck) {
		final List<StringObjectMapper<MonitoringNodeBean>> result = new ArrayList<StringObjectMapper<MonitoringNodeBean>>();
		result.add(new StringObjectMapperAdapter<MonitoringNodeBean, MonitoringNodeIdentifier>("id", mapperMonitoringNodeIdentifier));
		result.add(new StringObjectMapperAdapter<MonitoringNodeBean, MonitoringCheckType>("checkType", mapperMonitoringCheck));
		result.add(new StringObjectMapperAdapter<MonitoringNodeBean, String>("name", mapperString));
		result.add(new StringObjectMapperAdapter<MonitoringNodeBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperAdapter<MonitoringNodeBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}
