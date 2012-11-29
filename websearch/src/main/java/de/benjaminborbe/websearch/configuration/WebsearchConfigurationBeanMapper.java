package de.benjaminborbe.websearch.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;

import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.mapper.SingleMap;
import de.benjaminborbe.tools.mapper.SingleMapCalendar;
import de.benjaminborbe.tools.mapper.SingleMapStringList;
import de.benjaminborbe.tools.mapper.SingleMapURL;
import de.benjaminborbe.tools.mapper.SingleMappler;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.websearch.util.SingleMapUserIdentifier;
import de.benjaminborbe.websearch.util.SingleMapWebsearchConfigurationIdentifier;

public class WebsearchConfigurationBeanMapper extends SingleMappler<WebsearchConfigurationBean> {

	@Inject
	public WebsearchConfigurationBeanMapper(
			final Provider<WebsearchConfigurationBean> provider,
			final ParseUtil parseUtil,
			final TimeZoneUtil timeZoneUtil,
			final CalendarUtil calendarUtil) {
		super(provider, buildMappings(parseUtil, timeZoneUtil, calendarUtil));
	}

	private static Collection<SingleMap<WebsearchConfigurationBean>> buildMappings(final ParseUtil parseUtil, final TimeZoneUtil timeZoneUtil, final CalendarUtil calendarUtil) {
		final List<SingleMap<WebsearchConfigurationBean>> result = new ArrayList<SingleMap<WebsearchConfigurationBean>>();
		result.add(new SingleMapWebsearchConfigurationIdentifier<WebsearchConfigurationBean>("id"));
		result.add(new SingleMapUserIdentifier<WebsearchConfigurationBean>("owner"));
		result.add(new SingleMapURL<WebsearchConfigurationBean>("url", parseUtil));
		result.add(new SingleMapStringList<WebsearchConfigurationBean>("excludes"));
		result.add(new SingleMapCalendar<WebsearchConfigurationBean>("created", timeZoneUtil, calendarUtil, parseUtil));
		result.add(new SingleMapCalendar<WebsearchConfigurationBean>("modified", timeZoneUtil, calendarUtil, parseUtil));
		return result;
	}
}
