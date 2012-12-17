package de.benjaminborbe.confluence.dao;

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
public class ConfluencePageBeanMapper extends MapObjectMapperAdapter<ConfluencePageBean> {

	@Inject
	public ConfluencePageBeanMapper(
			final Provider<ConfluencePageBean> provider,
			final ParseUtil parseUtil,
			final TimeZoneUtil timeZoneUtil,
			final CalendarUtil calendarUtil,
			final MapperCalendar mapperCalendar) {
		super(provider, buildMappings(parseUtil, timeZoneUtil, calendarUtil, mapperCalendar));
	}

	private static Collection<StringObjectMapper<ConfluencePageBean>> buildMappings(final ParseUtil parseUtil, final TimeZoneUtil timeZoneUtil, final CalendarUtil calendarUtil,
			final MapperCalendar mapperCalendar) {
		final List<StringObjectMapper<ConfluencePageBean>> result = new ArrayList<StringObjectMapper<ConfluencePageBean>>();
		result.add(new StringObjectMapperConfluencePageIdentifier<ConfluencePageBean>("id"));
		result.add(new StringObjectMapperUserIdentifier<ConfluencePageBean>("owner"));
		result.add(new StringObjectMapperConfluenceInstanceIdentifier<ConfluencePageBean>("instanceId"));
		result.add(new StringObjectMapperString<ConfluencePageBean>("pageId"));
		result.add(new StringObjectMapperCalendar<ConfluencePageBean>("lastVisit", mapperCalendar));
		result.add(new StringObjectMapperCalendar<ConfluencePageBean>("created", mapperCalendar));
		result.add(new StringObjectMapperCalendar<ConfluencePageBean>("modified", mapperCalendar));
		return result;
	}
}
