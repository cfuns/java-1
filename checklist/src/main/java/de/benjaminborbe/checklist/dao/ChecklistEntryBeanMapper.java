package de.benjaminborbe.checklist.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperBoolean;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperCalendar;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperString;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class ChecklistEntryBeanMapper extends MapObjectMapperAdapter<ChecklistEntryBean> {

	public static final String LIST_ID = "listId";

	public static final String OWNER = "owner";

	@Inject
	public ChecklistEntryBeanMapper(
			final Provider<ChecklistEntryBean> provider,
			final ParseUtil parseUtil,
			final TimeZoneUtil timeZoneUtil,
			final CalendarUtil calendarUtil,
			final MapperCalendar mapperCalendar) {
		super(provider, buildMappings(parseUtil, timeZoneUtil, calendarUtil, mapperCalendar));
	}

	private static Collection<StringObjectMapper<ChecklistEntryBean>> buildMappings(final ParseUtil parseUtil, final TimeZoneUtil timeZoneUtil, final CalendarUtil calendarUtil,
			final MapperCalendar mapperCalendar) {
		final List<StringObjectMapper<ChecklistEntryBean>> result = new ArrayList<StringObjectMapper<ChecklistEntryBean>>();
		result.add(new StringObjectMapperEntryIdentifier<ChecklistEntryBean>("id"));
		result.add(new StringObjectMapperListIdentifier<ChecklistEntryBean>(LIST_ID));
		result.add(new StringObjectMapperUserIdentifier<ChecklistEntryBean>(OWNER));
		result.add(new StringObjectMapperString<ChecklistEntryBean>("name"));
		result.add(new StringObjectMapperBoolean<ChecklistEntryBean>("completed", parseUtil));
		result.add(new StringObjectMapperCalendar<ChecklistEntryBean>("created", mapperCalendar));
		result.add(new StringObjectMapperCalendar<ChecklistEntryBean>("modified", mapperCalendar));
		return result;
	}
}
