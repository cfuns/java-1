package de.benjaminborbe.checklist.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.mapper.SingleMap;
import de.benjaminborbe.tools.mapper.SingleMapCalendar;
import de.benjaminborbe.tools.mapper.SingleMapString;
import de.benjaminborbe.tools.mapper.SingleMappler;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class ChecklistListBeanMapper extends SingleMappler<ChecklistListBean> {

	public static final String OWNER = "owner";

	@Inject
	public ChecklistListBeanMapper(final Provider<ChecklistListBean> provider, final ParseUtil parseUtil, final TimeZoneUtil timeZoneUtil, final CalendarUtil calendarUtil) {
		super(provider, buildMappings(parseUtil, timeZoneUtil, calendarUtil));
	}

	private static Collection<SingleMap<ChecklistListBean>> buildMappings(final ParseUtil parseUtil, final TimeZoneUtil timeZoneUtil, final CalendarUtil calendarUtil) {
		final List<SingleMap<ChecklistListBean>> result = new ArrayList<SingleMap<ChecklistListBean>>();
		result.add(new SingleMapListIdentifier<ChecklistListBean>("id"));
		result.add(new SingleMapUserIdentifier<ChecklistListBean>(OWNER));
		result.add(new SingleMapString<ChecklistListBean>("name"));
		result.add(new SingleMapCalendar<ChecklistListBean>("created", timeZoneUtil, calendarUtil, parseUtil));
		result.add(new SingleMapCalendar<ChecklistListBean>("modified", timeZoneUtil, calendarUtil, parseUtil));
		return result;
	}
}
