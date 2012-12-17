package de.benjaminborbe.messageservice.dao;

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
public class MessageBeanMapper extends MapObjectMapperAdapter<MessageBean> {

	public static final String TYPE = "type";

	@Inject
	public MessageBeanMapper(
			final Provider<MessageBean> provider,
			final ParseUtil parseUtil,
			final TimeZoneUtil timeZoneUtil,
			final CalendarUtil calendarUtil,
			final MapperCalendar mapperCalendar) {
		super(provider, buildMappings(parseUtil, timeZoneUtil, calendarUtil, mapperCalendar));
	}

	private static Collection<StringObjectMapper<MessageBean>> buildMappings(final ParseUtil parseUtil, final TimeZoneUtil timeZoneUtil, final CalendarUtil calendarUtil,
			final MapperCalendar mapperCalendar) {
		final List<StringObjectMapper<MessageBean>> result = new ArrayList<StringObjectMapper<MessageBean>>();
		result.add(new StringObjectMapperMessageIdentifier<MessageBean>("id"));
		result.add(new StringObjectMapperString<MessageBean>("content"));
		result.add(new StringObjectMapperString<MessageBean>(TYPE));
		result.add(new StringObjectMapperCalendar<MessageBean>("created", mapperCalendar));
		result.add(new StringObjectMapperCalendar<MessageBean>("modified", mapperCalendar));
		return result;
	}
}
