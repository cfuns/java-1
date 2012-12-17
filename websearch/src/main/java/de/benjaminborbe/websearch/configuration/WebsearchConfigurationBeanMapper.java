package de.benjaminborbe.websearch.configuration;

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
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperStringList;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperUrl;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.websearch.util.StringObjectMapperUserIdentifier;
import de.benjaminborbe.websearch.util.StringObjectMapperWebsearchConfigurationIdentifier;

public class WebsearchConfigurationBeanMapper extends MapObjectMapperAdapter<WebsearchConfigurationBean> {

	@Inject
	public WebsearchConfigurationBeanMapper(
			final Provider<WebsearchConfigurationBean> provider,
			final ParseUtil parseUtil,
			final TimeZoneUtil timeZoneUtil,
			final CalendarUtil calendarUtil,
			final MapperCalendar mapperCalendar) {
		super(provider, buildMappings(parseUtil, timeZoneUtil, calendarUtil, mapperCalendar));
	}

	private static Collection<StringObjectMapper<WebsearchConfigurationBean>> buildMappings(final ParseUtil parseUtil, final TimeZoneUtil timeZoneUtil,
			final CalendarUtil calendarUtil, final MapperCalendar mapperCalendar) {
		final List<StringObjectMapper<WebsearchConfigurationBean>> result = new ArrayList<StringObjectMapper<WebsearchConfigurationBean>>();
		result.add(new StringObjectMapperWebsearchConfigurationIdentifier<WebsearchConfigurationBean>("id"));
		result.add(new StringObjectMapperUserIdentifier<WebsearchConfigurationBean>("owner"));
		result.add(new StringObjectMapperUrl<WebsearchConfigurationBean>("url", parseUtil));
		result.add(new StringObjectMapperStringList<WebsearchConfigurationBean>("excludes"));
		result.add(new StringObjectMapperInteger<WebsearchConfigurationBean>("expire", parseUtil));
		result.add(new StringObjectMapperLong<WebsearchConfigurationBean>("delay", parseUtil));
		result.add(new StringObjectMapperBoolean<WebsearchConfigurationBean>("activated", parseUtil));
		result.add(new StringObjectMapperCalendar<WebsearchConfigurationBean>("created", mapperCalendar));
		result.add(new StringObjectMapperCalendar<WebsearchConfigurationBean>("modified", mapperCalendar));
		return result;
	}
}
