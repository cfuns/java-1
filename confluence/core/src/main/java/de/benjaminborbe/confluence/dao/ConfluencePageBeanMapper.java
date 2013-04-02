package de.benjaminborbe.confluence.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.confluence.api.ConfluenceInstanceIdentifier;
import de.benjaminborbe.confluence.api.ConfluencePageIdentifier;
import de.benjaminborbe.confluence.util.MapperConfluenceInstanceIdentifier;
import de.benjaminborbe.confluence.util.MapperConfluencePageIdentifier;
import de.benjaminborbe.confluence.util.MapperUserIdentifier;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.MapperUrl;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@Singleton
public class ConfluencePageBeanMapper extends MapObjectMapperAdapter<ConfluencePageBean> {

	@Inject
	public ConfluencePageBeanMapper(
		final Provider<ConfluencePageBean> provider,
		final MapperConfluencePageIdentifier mapperConfluencePageIdentifier,
		final MapperUserIdentifier mapperUserIdentifier,
		final MapperConfluenceInstanceIdentifier mapperConfluenceInstanceIdentifier,
		final MapperString mapperString,
		final MapperCalendar mapperCalendar,
		final MapperUrl mapperUrl) {
		super(provider, buildMappings(mapperConfluencePageIdentifier, mapperUserIdentifier, mapperConfluenceInstanceIdentifier, mapperString, mapperCalendar, mapperUrl));
	}

	private static Collection<StringObjectMapper<ConfluencePageBean>> buildMappings(final MapperConfluencePageIdentifier mapperConfluencePageIdentifier,
																																									final MapperUserIdentifier mapperUserIdentifier, final MapperConfluenceInstanceIdentifier mapperConfluenceInstanceIdentifier, final MapperString mapperString,
																																									final MapperCalendar mapperCalendar, final MapperUrl mapperUrl) {
		final List<StringObjectMapper<ConfluencePageBean>> result = new ArrayList<>();
		result.add(new StringObjectMapperAdapter<ConfluencePageBean, ConfluencePageIdentifier>("id", mapperConfluencePageIdentifier));
		result.add(new StringObjectMapperAdapter<ConfluencePageBean, UserIdentifier>("owner", mapperUserIdentifier));
		result.add(new StringObjectMapperAdapter<ConfluencePageBean, ConfluenceInstanceIdentifier>("instanceId", mapperConfluenceInstanceIdentifier));
		result.add(new StringObjectMapperAdapter<ConfluencePageBean, String>("pageId", mapperString));
		result.add(new StringObjectMapperAdapter<ConfluencePageBean, URL>("url", mapperUrl));
		result.add(new StringObjectMapperAdapter<ConfluencePageBean, Calendar>("lastVisit", mapperCalendar));
		result.add(new StringObjectMapperAdapter<ConfluencePageBean, Calendar>("lastModified", mapperCalendar));
		result.add(new StringObjectMapperAdapter<ConfluencePageBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperAdapter<ConfluencePageBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}
