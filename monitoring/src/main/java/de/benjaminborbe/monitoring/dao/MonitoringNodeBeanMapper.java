package de.benjaminborbe.monitoring.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.monitoring.api.MonitoringCheckType;
import de.benjaminborbe.monitoring.api.MonitoringNodeIdentifier;
import de.benjaminborbe.monitoring.util.MapperMonitoringCheck;
import de.benjaminborbe.monitoring.util.MapperMonitoringNodeIdentifier;
import de.benjaminborbe.tools.mapper.MapperBoolean;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperMapString;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

@Singleton
public class MonitoringNodeBeanMapper extends MapObjectMapperAdapter<MonitoringNodeBean> {

	public static final String MODIFIED = "modified";

	public static final String CREATED = "created";

	public static final String NAME = "name";

	public static final String PARAMETER = "parameter";

	public static final String CHECK_TYPE = "checkType";

	public static final String ID = "id";

	public static final String SILENT = "silent";

	public static final String ACTIVE = "active";

	public static final String RESULT = "result";

	public static final String MESSAGE = "message";

	private static final String PARENT_ID = "parentId";

	@Inject
	public MonitoringNodeBeanMapper(
			final Provider<MonitoringNodeBean> provider,
			final MapperMonitoringNodeIdentifier mapperMonitoringNodeIdentifier,
			final MapperString mapperString,
			final MapperBoolean mapperBoolean,
			final MapperCalendar mapperCalendar,
			final MapperMapString mapperMapString,
			final MapperMonitoringCheck mapperMonitoringCheck) {
		super(provider, buildMappings(mapperMonitoringNodeIdentifier, mapperString, mapperCalendar, mapperMonitoringCheck, mapperMapString, mapperBoolean));
	}

	private static Collection<StringObjectMapper<MonitoringNodeBean>> buildMappings(final MapperMonitoringNodeIdentifier mapperMonitoringNodeIdentifier,
			final MapperString mapperString, final MapperCalendar mapperCalendar, final MapperMonitoringCheck mapperMonitoringCheck, final MapperMapString mapperMapString,
			final MapperBoolean mapperBoolean) {
		final List<StringObjectMapper<MonitoringNodeBean>> result = new ArrayList<StringObjectMapper<MonitoringNodeBean>>();
		result.add(new StringObjectMapperAdapter<MonitoringNodeBean, MonitoringNodeIdentifier>(ID, mapperMonitoringNodeIdentifier));
		result.add(new StringObjectMapperAdapter<MonitoringNodeBean, MonitoringNodeIdentifier>(PARENT_ID, mapperMonitoringNodeIdentifier));
		result.add(new StringObjectMapperAdapter<MonitoringNodeBean, MonitoringCheckType>(CHECK_TYPE, mapperMonitoringCheck));
		result.add(new StringObjectMapperAdapter<MonitoringNodeBean, Map<String, String>>(PARAMETER, mapperMapString));
		result.add(new StringObjectMapperAdapter<MonitoringNodeBean, String>(NAME, mapperString));
		result.add(new StringObjectMapperAdapter<MonitoringNodeBean, Boolean>(SILENT, mapperBoolean));
		result.add(new StringObjectMapperAdapter<MonitoringNodeBean, Boolean>(ACTIVE, mapperBoolean));
		result.add(new StringObjectMapperAdapter<MonitoringNodeBean, Calendar>(CREATED, mapperCalendar));
		result.add(new StringObjectMapperAdapter<MonitoringNodeBean, Calendar>(MODIFIED, mapperCalendar));
		result.add(new StringObjectMapperAdapter<MonitoringNodeBean, String>(MESSAGE, mapperString));
		result.add(new StringObjectMapperAdapter<MonitoringNodeBean, Boolean>(RESULT, mapperBoolean));
		return result;
	}
}
