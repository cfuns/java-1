package de.benjaminborbe.websearch.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;
import de.benjaminborbe.websearch.api.WebsearchPageIdentifier;
import de.benjaminborbe.websearch.util.MapperWebsearchPageIdentifier;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@Singleton
public class WebsearchPageBeanMapper extends MapObjectMapperAdapter<WebsearchPageBean> {

	@Inject
	public WebsearchPageBeanMapper(final Provider<WebsearchPageBean> provider, final MapperCalendar mapperCalendar, final MapperWebsearchPageIdentifier mapperWebsearchPageIdentifier) {
		super(provider, buildMappings(mapperCalendar, mapperWebsearchPageIdentifier));
	}

	private static Collection<StringObjectMapper<WebsearchPageBean>> buildMappings(final MapperCalendar mapperCalendar,
																																								 final MapperWebsearchPageIdentifier mapperWebsearchPageIdentifier) {
		final List<StringObjectMapper<WebsearchPageBean>> result = new ArrayList<>();
		result.add(new StringObjectMapperAdapter<WebsearchPageBean, WebsearchPageIdentifier>("id", mapperWebsearchPageIdentifier));
		result.add(new StringObjectMapperAdapter<WebsearchPageBean, Calendar>("lastVisit", mapperCalendar));
		result.add(new StringObjectMapperAdapter<WebsearchPageBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperAdapter<WebsearchPageBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}
