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
public class ChecklistEntryBeanMapper extends SingleMappler<ChecklistEntryBean> {

	public static final String LIST_ID = "listId";

	public static final String OWNER = "owner";

	@Inject
	public ChecklistEntryBeanMapper(final Provider<ChecklistEntryBean> provider, final ParseUtil parseUtil, final TimeZoneUtil timeZoneUtil, final CalendarUtil calendarUtil) {
		super(provider, buildMappings(parseUtil, timeZoneUtil, calendarUtil));
	}

	private static Collection<SingleMap<ChecklistEntryBean>> buildMappings(final ParseUtil parseUtil, final TimeZoneUtil timeZoneUtil, final CalendarUtil calendarUtil) {
		final List<SingleMap<ChecklistEntryBean>> result = new ArrayList<SingleMap<ChecklistEntryBean>>();
		result.add(new SingleMapEntryIdentifier<ChecklistEntryBean>("id"));
		result.add(new SingleMapListIdentifier<ChecklistEntryBean>(LIST_ID));
		result.add(new SingleMapUserIdentifier<ChecklistEntryBean>(OWNER));
		result.add(new SingleMapString<ChecklistEntryBean>("name"));
		result.add(new SingleMapCalendar<ChecklistEntryBean>("created", timeZoneUtil, calendarUtil, parseUtil));
		result.add(new SingleMapCalendar<ChecklistEntryBean>("modified", timeZoneUtil, calendarUtil, parseUtil));
		return result;
	}
}
