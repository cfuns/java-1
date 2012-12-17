package de.benjaminborbe.messageservice.dao;

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
public class MessageBeanMapper extends SingleMappler<MessageBean> {

	public static final String TYPE = "type";

	@Inject
	public MessageBeanMapper(final Provider<MessageBean> provider, final ParseUtil parseUtil, final TimeZoneUtil timeZoneUtil, final CalendarUtil calendarUtil) {
		super(provider, buildMappings(parseUtil, timeZoneUtil, calendarUtil));
	}

	private static Collection<SingleMap<MessageBean>> buildMappings(final ParseUtil parseUtil, final TimeZoneUtil timeZoneUtil, final CalendarUtil calendarUtil) {
		final List<SingleMap<MessageBean>> result = new ArrayList<SingleMap<MessageBean>>();
		result.add(new SingleMapMessageIdentifier<MessageBean>("id"));
		result.add(new SingleMapString<MessageBean>("content"));
		result.add(new SingleMapString<MessageBean>(TYPE));
		result.add(new SingleMapCalendar<MessageBean>("created", timeZoneUtil, calendarUtil, parseUtil));
		result.add(new SingleMapCalendar<MessageBean>("modified", timeZoneUtil, calendarUtil, parseUtil));
		return result;
	}
}
