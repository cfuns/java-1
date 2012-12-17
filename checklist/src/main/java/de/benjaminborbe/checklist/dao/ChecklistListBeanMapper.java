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
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperCalendar;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperString;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class ChecklistListBeanMapper extends MapObjectMapperAdapter<ChecklistListBean> {

	public static final String OWNER = "owner";

	@Inject
	public ChecklistListBeanMapper(
			final Provider<ChecklistListBean> provider,
			final ParseUtil parseUtil,
			final TimeZoneUtil timeZoneUtil,
			final CalendarUtil calendarUtil,
			final MapperCalendar mapperCalendar) {
		super(provider, buildMappings(parseUtil, timeZoneUtil, calendarUtil, mapperCalendar));
	}

	private static Collection<StringObjectMapper<ChecklistListBean>> buildMappings(final ParseUtil parseUtil, final TimeZoneUtil timeZoneUtil, final CalendarUtil calendarUtil,
			final MapperCalendar mapperCalendar) {
		final List<StringObjectMapper<ChecklistListBean>> result = new ArrayList<StringObjectMapper<ChecklistListBean>>();
		result.add(new StringObjectMapperListIdentifier<ChecklistListBean>("id"));
		result.add(new StringObjectMapperUserIdentifier<ChecklistListBean>(OWNER));
		result.add(new StringObjectMapperString<ChecklistListBean>("name"));
		result.add(new StringObjectMapperCalendar<ChecklistListBean>("created", mapperCalendar));
		result.add(new StringObjectMapperCalendar<ChecklistListBean>("modified", mapperCalendar));
		return result;
	}
}
