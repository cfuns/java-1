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
import de.benjaminborbe.tools.mapper.SingleMapCalendar;
import de.benjaminborbe.tools.mapper.SingleMapString;
import de.benjaminborbe.tools.mapper.SingleMappler;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class ConfluencePageBeanMapper extends SingleMappler<ConfluencePageBean> {

	@Inject
	public ConfluencePageBeanMapper(final Provider<ConfluencePageBean> provider, final ParseUtil parseUtil, final TimeZoneUtil timeZoneUtil, final CalendarUtil calendarUtil) {
		super(provider, buildMappings(parseUtil, timeZoneUtil, calendarUtil));
	}

	private static Collection<SingleMap<ConfluencePageBean>> buildMappings(final ParseUtil parseUtil, final TimeZoneUtil timeZoneUtil, final CalendarUtil calendarUtil) {
		final List<SingleMap<ConfluencePageBean>> result = new ArrayList<SingleMap<ConfluencePageBean>>();
		result.add(new ConfluencePageIdentifierMapper<ConfluencePageBean>("id"));
		result.add(new SingleMapUserIdentifier<ConfluencePageBean>("owner"));
		result.add(new ConfluenceInstanceIdentifierMapper<ConfluencePageBean>("instanceId"));
		result.add(new SingleMapString<ConfluencePageBean>("pageId"));
		result.add(new SingleMapCalendar<ConfluencePageBean>("lastVisit", timeZoneUtil, calendarUtil, parseUtil));
		result.add(new SingleMapCalendar<ConfluencePageBean>("created", timeZoneUtil, calendarUtil, parseUtil));
		result.add(new SingleMapCalendar<ConfluencePageBean>("modified", timeZoneUtil, calendarUtil, parseUtil));
		return result;
	}
}
