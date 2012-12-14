package de.benjaminborbe.confluence.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.mapper.SingleMap;
import de.benjaminborbe.tools.mapper.SingleMapBoolean;
import de.benjaminborbe.tools.mapper.SingleMapCalendar;
import de.benjaminborbe.tools.mapper.SingleMapInteger;
import de.benjaminborbe.tools.mapper.SingleMapLong;
import de.benjaminborbe.tools.mapper.SingleMapString;
import de.benjaminborbe.tools.mapper.SingleMappler;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class ConfluenceInstanceBeanMapper extends SingleMappler<ConfluenceInstanceBean> {

	@Inject
	public ConfluenceInstanceBeanMapper(final Provider<ConfluenceInstanceBean> provider, final ParseUtil parseUtil, final TimeZoneUtil timeZoneUtil, final CalendarUtil calendarUtil) {
		super(provider, buildMappings(parseUtil, timeZoneUtil, calendarUtil));
	}

	private static Collection<SingleMap<ConfluenceInstanceBean>> buildMappings(final ParseUtil parseUtil, final TimeZoneUtil timeZoneUtil, final CalendarUtil calendarUtil) {
		final List<SingleMap<ConfluenceInstanceBean>> result = new ArrayList<SingleMap<ConfluenceInstanceBean>>();
		result.add(new ConfluenceInstanceIdentifierMapper<ConfluenceInstanceBean>("id"));
		result.add(new SingleMapUserIdentifier<ConfluenceInstanceBean>("owner"));
		result.add(new SingleMapString<ConfluenceInstanceBean>("url"));
		result.add(new SingleMapString<ConfluenceInstanceBean>("username"));
		result.add(new SingleMapLong<ConfluenceInstanceBean>("delay", parseUtil));
		result.add(new SingleMapString<ConfluenceInstanceBean>("password"));
		result.add(new SingleMapBoolean<ConfluenceInstanceBean>("shared", parseUtil));
		result.add(new SingleMapInteger<ConfluenceInstanceBean>("expire", parseUtil));
		result.add(new SingleMapCalendar<ConfluenceInstanceBean>("created", timeZoneUtil, calendarUtil, parseUtil));
		result.add(new SingleMapCalendar<ConfluenceInstanceBean>("modified", timeZoneUtil, calendarUtil, parseUtil));
		return result;
	}
}
