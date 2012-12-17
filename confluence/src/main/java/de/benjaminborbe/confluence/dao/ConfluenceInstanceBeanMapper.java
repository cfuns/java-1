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
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperBoolean;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperCalendar;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperInteger;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperLong;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperString;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class ConfluenceInstanceBeanMapper extends MapObjectMapperAdapter<ConfluenceInstanceBean> {

	@Inject
	public ConfluenceInstanceBeanMapper(
			final Provider<ConfluenceInstanceBean> provider,
			final ParseUtil parseUtil,
			final TimeZoneUtil timeZoneUtil,
			final CalendarUtil calendarUtil,
			final MapperCalendar mapperCalendar) {
		super(provider, buildMappings(parseUtil, timeZoneUtil, calendarUtil, mapperCalendar));
	}

	private static Collection<StringObjectMapper<ConfluenceInstanceBean>> buildMappings(final ParseUtil parseUtil, final TimeZoneUtil timeZoneUtil, final CalendarUtil calendarUtil,
			final MapperCalendar mapperCalendar) {
		final List<StringObjectMapper<ConfluenceInstanceBean>> result = new ArrayList<StringObjectMapper<ConfluenceInstanceBean>>();
		result.add(new StringObjectMapperConfluenceInstanceIdentifier<ConfluenceInstanceBean>("id"));
		result.add(new StringObjectMapperUserIdentifier<ConfluenceInstanceBean>("owner"));
		result.add(new StringObjectMapperString<ConfluenceInstanceBean>("url"));
		result.add(new StringObjectMapperString<ConfluenceInstanceBean>("username"));
		result.add(new StringObjectMapperLong<ConfluenceInstanceBean>("delay", parseUtil));
		result.add(new StringObjectMapperString<ConfluenceInstanceBean>("password"));
		result.add(new StringObjectMapperBoolean<ConfluenceInstanceBean>("shared", parseUtil));
		result.add(new StringObjectMapperBoolean<ConfluenceInstanceBean>("activated", parseUtil));
		result.add(new StringObjectMapperInteger<ConfluenceInstanceBean>("expire", parseUtil));
		result.add(new StringObjectMapperCalendar<ConfluenceInstanceBean>("created", mapperCalendar));
		result.add(new StringObjectMapperCalendar<ConfluenceInstanceBean>("modified", mapperCalendar));
		return result;
	}
}
