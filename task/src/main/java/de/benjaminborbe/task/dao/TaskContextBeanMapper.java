package de.benjaminborbe.task.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;

import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.mapper.SingleMap;
import de.benjaminborbe.tools.mapper.SingleMapCalendar;
import de.benjaminborbe.tools.mapper.SingleMapString;
import de.benjaminborbe.tools.mapper.SingleMappler;
import de.benjaminborbe.tools.util.ParseUtil;

public class TaskContextBeanMapper extends SingleMappler<TaskContextBean> {

	@Inject
	public TaskContextBeanMapper(final Provider<TaskContextBean> provider, final ParseUtil parseUtil, final CalendarUtil calendarUtil) {
		super(provider, buildMappings(parseUtil, calendarUtil));
	}

	private static Collection<SingleMap<TaskContextBean>> buildMappings(final ParseUtil parseUtil, final CalendarUtil calendarUtil) {
		final List<SingleMap<TaskContextBean>> result = new ArrayList<SingleMap<TaskContextBean>>();
		result.add(new SingleMapTaskContextIdentifier<TaskContextBean>("id"));
		result.add(new SingleMapString<TaskContextBean>("name"));
		result.add(new SingleMapUserIdentifier<TaskContextBean>("owner"));
		result.add(new SingleMapCalendar<TaskContextBean>("created", calendarUtil, parseUtil));
		result.add(new SingleMapCalendar<TaskContextBean>("modified", calendarUtil, parseUtil));
		return result;
	}
}
