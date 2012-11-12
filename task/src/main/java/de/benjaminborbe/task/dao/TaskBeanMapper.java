package de.benjaminborbe.task.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;

import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.mapper.SingleMap;
import de.benjaminborbe.tools.mapper.SingleMapBoolean;
import de.benjaminborbe.tools.mapper.SingleMapCalendar;
import de.benjaminborbe.tools.mapper.SingleMapInteger;
import de.benjaminborbe.tools.mapper.SingleMapLong;
import de.benjaminborbe.tools.mapper.SingleMapString;
import de.benjaminborbe.tools.mapper.SingleMappler;
import de.benjaminborbe.tools.util.ParseUtil;

public class TaskBeanMapper extends SingleMappler<TaskBean> {

	@Inject
	public TaskBeanMapper(final Provider<TaskBean> provider, final ParseUtil parseUtil, final TimeZoneUtil timeZoneUtil, final CalendarUtil calendarUtil) {
		super(provider, buildMappings(parseUtil, timeZoneUtil, calendarUtil));
	}

	private static Collection<SingleMap<TaskBean>> buildMappings(final ParseUtil parseUtil, final TimeZoneUtil timeZoneUtil, final CalendarUtil calendarUtil) {
		final List<SingleMap<TaskBean>> result = new ArrayList<SingleMap<TaskBean>>();
		result.add(new SingleMapTaskIdentifier<TaskBean>("id"));
		result.add(new SingleMapTaskIdentifier<TaskBean>("parentId"));
		result.add(new SingleMapString<TaskBean>("name"));
		result.add(new SingleMapString<TaskBean>("description"));
		result.add(new SingleMapUserIdentifier<TaskBean>("owner"));
		result.add(new SingleMapLong<TaskBean>("duration", parseUtil));
		result.add(new SingleMapBoolean<TaskBean>("completed", parseUtil));
		result.add(new SingleMapCalendar<TaskBean>("completionDate", timeZoneUtil, calendarUtil, parseUtil));
		result.add(new SingleMapCalendar<TaskBean>("created", timeZoneUtil, calendarUtil, parseUtil));
		result.add(new SingleMapCalendar<TaskBean>("modified", timeZoneUtil, calendarUtil, parseUtil));
		result.add(new SingleMapCalendar<TaskBean>("start", timeZoneUtil, calendarUtil, parseUtil));
		result.add(new SingleMapCalendar<TaskBean>("due", timeZoneUtil, calendarUtil, parseUtil));
		result.add(new SingleMapTaskIdentifier<TaskBean>("parentId"));
		result.add(new SingleMapInteger<TaskBean>("priority", parseUtil));
		result.add(new SingleMapLong<TaskBean>("repeatStart", parseUtil));
		result.add(new SingleMapLong<TaskBean>("repeatDue", parseUtil));
		result.add(new SingleMapString<TaskBean>("url"));
		return result;
	}
}
