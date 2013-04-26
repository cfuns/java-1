package de.benjaminborbe.websearch.core.dao;

import com.google.inject.Provider;
import de.benjaminborbe.httpdownloader.api.HttpHeader;
import de.benjaminborbe.tools.mapper.MapperByteArray;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperUrl;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;
import de.benjaminborbe.websearch.api.WebsearchPageIdentifier;
import de.benjaminborbe.websearch.core.util.MapperHttpHeader;
import de.benjaminborbe.websearch.core.util.MapperWebsearchPageIdentifier;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@Singleton
public class WebsearchPageBeanMapper extends MapObjectMapperAdapter<WebsearchPageBean> {

	public static final String ID = "id";

	public static final String URL = "url";

	public static final String LAST_VISIT = "lastVisit";

	public static final String CREATED = "created";

	public static final String MODIFIED = "modified";

	public static final String CONTENT = "content";

	public static final String HEADER = "header";

	@Inject
	public WebsearchPageBeanMapper(
		final Provider<WebsearchPageBean> provider,
		final MapperCalendar mapperCalendar,
		final MapperWebsearchPageIdentifier mapperWebsearchPageIdentifier,
		final MapperUrl mapperUrl,
		final MapperByteArray mapperByteArray,
		final MapperHttpHeader mapperHttpHeader
	) {
		super(provider, buildMappings(mapperCalendar, mapperUrl, mapperWebsearchPageIdentifier, mapperByteArray, mapperHttpHeader));
	}

	private static Collection<StringObjectMapper<WebsearchPageBean>> buildMappings(
		final MapperCalendar mapperCalendar,
		final MapperUrl mapperUrl,
		final MapperWebsearchPageIdentifier mapperWebsearchPageIdentifier,
		final MapperByteArray mapperByteArray,
		final MapperHttpHeader mapperHttpHeader
	) {
		final List<StringObjectMapper<WebsearchPageBean>> result = new ArrayList<>();
		result.add(new StringObjectMapperAdapter<WebsearchPageBean, WebsearchPageIdentifier>(ID, mapperWebsearchPageIdentifier));
		result.add(new StringObjectMapperAdapter<WebsearchPageBean, URL>(URL, mapperUrl));
		result.add(new StringObjectMapperAdapter<WebsearchPageBean, Calendar>(LAST_VISIT, mapperCalendar));
		result.add(new StringObjectMapperAdapter<WebsearchPageBean, Calendar>(CREATED, mapperCalendar));
		result.add(new StringObjectMapperAdapter<WebsearchPageBean, Calendar>(MODIFIED, mapperCalendar));
		result.add(new StringObjectMapperAdapter<WebsearchPageBean, byte[]>(CONTENT, mapperByteArray));
		result.add(new StringObjectMapperAdapter<WebsearchPageBean, HttpHeader>(HEADER, mapperHttpHeader));
		return result;
	}
}
