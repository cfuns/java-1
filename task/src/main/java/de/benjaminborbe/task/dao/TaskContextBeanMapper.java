package de.benjaminborbe.task.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;

import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperCalendar;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperString;
import de.benjaminborbe.tools.util.ParseUtil;

public class TaskContextBeanMapper extends MapObjectMapperAdapter<TaskContextBean> {

	public static final String OWNER = "owner";

	public static final String NAME = "name";

	@Inject
	public TaskContextBeanMapper(
			final Provider<TaskContextBean> provider,
			final ParseUtil parseUtil,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final MapperCalendar mapperCalendar) {
		super(provider, buildMappings(parseUtil, calendarUtil, timeZoneUtil, mapperCalendar));
	}

	private static Collection<StringObjectMapper<TaskContextBean>> buildMappings(final ParseUtil parseUtil, final CalendarUtil calendarUtil, final TimeZoneUtil timeZoneUtil,
			final MapperCalendar mapperCalendar) {
		final List<StringObjectMapper<TaskContextBean>> result = new ArrayList<StringObjectMapper<TaskContextBean>>();
		result.add(new StringObjectMapperTaskContextIdentifier<TaskContextBean>("id"));
		result.add(new StringObjectMapperString<TaskContextBean>(NAME));
		result.add(new StringObjectMapperUserIdentifier<TaskContextBean>(OWNER));
		result.add(new StringObjectMapperCalendar<TaskContextBean>("created", mapperCalendar));
		result.add(new StringObjectMapperCalendar<TaskContextBean>("modified", mapperCalendar));
		return result;
	}
}
