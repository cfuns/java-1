package de.benjaminborbe.confluence.dao;

import com.google.inject.Provider;
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

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@Singleton
public class ConfluencePageBeanMapper extends MapObjectMapperAdapter<ConfluencePageBean> {

	public static final String ID = "id";

	public static final String OWNER = "owner";

	public static final String INSTANCE_ID = "instanceId";

	public static final String PAGE_ID = "pageId";

	public static final String URL = "url";

	public static final String LAST_VISIT = "lastVisit";

	public static final String LAST_MODIFIED = "lastModified";

	public static final String MODIFIED = "modified";

	public static final String CREATED = "created";

	@Inject
	public ConfluencePageBeanMapper(
		final Provider<ConfluencePageBean> provider,
		final MapperConfluencePageIdentifier mapperConfluencePageIdentifier,
		final MapperUserIdentifier mapperUserIdentifier,
		final MapperConfluenceInstanceIdentifier mapperConfluenceInstanceIdentifier,
		final MapperString mapperString,
		final MapperCalendar mapperCalendar,
		final MapperUrl mapperUrl
	) {
		super(provider, buildMappings(mapperConfluencePageIdentifier, mapperUserIdentifier, mapperConfluenceInstanceIdentifier, mapperString, mapperCalendar, mapperUrl));
	}

	private static Collection<StringObjectMapper<ConfluencePageBean>> buildMappings(
		final MapperConfluencePageIdentifier mapperConfluencePageIdentifier,
		final MapperUserIdentifier mapperUserIdentifier,
		final MapperConfluenceInstanceIdentifier mapperConfluenceInstanceIdentifier,
		final MapperString mapperString,
		final MapperCalendar mapperCalendar,
		final MapperUrl mapperUrl
	) {
		final List<StringObjectMapper<ConfluencePageBean>> result = new ArrayList<StringObjectMapper<ConfluencePageBean>>();
		result.add(new StringObjectMapperAdapter<ConfluencePageBean, ConfluencePageIdentifier>(ID, mapperConfluencePageIdentifier));
		result.add(new StringObjectMapperAdapter<ConfluencePageBean, UserIdentifier>(OWNER, mapperUserIdentifier));
		result.add(new StringObjectMapperAdapter<ConfluencePageBean, ConfluenceInstanceIdentifier>(INSTANCE_ID, mapperConfluenceInstanceIdentifier));
		result.add(new StringObjectMapperAdapter<ConfluencePageBean, String>(PAGE_ID, mapperString));
		result.add(new StringObjectMapperAdapter<ConfluencePageBean, URL>(URL, mapperUrl));
		result.add(new StringObjectMapperAdapter<ConfluencePageBean, Calendar>(LAST_VISIT, mapperCalendar));
		result.add(new StringObjectMapperAdapter<ConfluencePageBean, Calendar>(LAST_MODIFIED, mapperCalendar));
		result.add(new StringObjectMapperAdapter<ConfluencePageBean, Calendar>(CREATED, mapperCalendar));
		result.add(new StringObjectMapperAdapter<ConfluencePageBean, Calendar>(MODIFIED, mapperCalendar));
		return result;
	}
}
