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
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperBoolean;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperCalendar;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperInteger;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperLong;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperString;
import de.benjaminborbe.tools.util.ParseUtil;

public class TaskBeanMapper extends MapObjectMapperAdapter<TaskBean> {

	@Inject
	public TaskBeanMapper(
			final Provider<TaskBean> provider,
			final ParseUtil parseUtil,
			final TimeZoneUtil timeZoneUtil,
			final CalendarUtil calendarUtil,
			final MapperCalendar mapperCalendar) {
		super(provider, buildMappings(parseUtil, timeZoneUtil, calendarUtil, mapperCalendar));
	}

	private static Collection<StringObjectMapper<TaskBean>> buildMappings(final ParseUtil parseUtil, final TimeZoneUtil timeZoneUtil, final CalendarUtil calendarUtil,
			final MapperCalendar mapperCalendar) {
		final List<StringObjectMapper<TaskBean>> result = new ArrayList<StringObjectMapper<TaskBean>>();
		result.add(new StringObjectMapperTaskIdentifier<TaskBean>("id"));
		result.add(new StringObjectMapperTaskIdentifier<TaskBean>("parentId"));
		result.add(new StringObjectMapperString<TaskBean>("name"));
		result.add(new StringObjectMapperString<TaskBean>("description"));
		result.add(new StringObjectMapperUserIdentifier<TaskBean>("owner"));
		result.add(new StringObjectMapperLong<TaskBean>("duration", parseUtil));
		result.add(new StringObjectMapperBoolean<TaskBean>("completed", parseUtil));
		result.add(new StringObjectMapperCalendar<TaskBean>("completionDate", mapperCalendar));
		result.add(new StringObjectMapperCalendar<TaskBean>("created", mapperCalendar));
		result.add(new StringObjectMapperCalendar<TaskBean>("modified", mapperCalendar));
		result.add(new StringObjectMapperCalendar<TaskBean>("start", mapperCalendar));
		result.add(new StringObjectMapperCalendar<TaskBean>("due", mapperCalendar));
		result.add(new StringObjectMapperInteger<TaskBean>("priority", parseUtil));
		result.add(new StringObjectMapperLong<TaskBean>("repeatStart", parseUtil));
		result.add(new StringObjectMapperLong<TaskBean>("repeatDue", parseUtil));
		result.add(new StringObjectMapperString<TaskBean>("url"));
		return result;
	}
}
