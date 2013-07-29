package de.benjaminborbe.websearch.core.dao;

import com.google.inject.Provider;
import de.benjaminborbe.httpdownloader.api.HttpContent;
import de.benjaminborbe.httpdownloader.api.HttpHeader;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperInteger;
import de.benjaminborbe.tools.mapper.MapperLong;
import de.benjaminborbe.tools.mapper.MapperUrl;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;
import de.benjaminborbe.websearch.api.WebsearchPageIdentifier;
import de.benjaminborbe.websearch.core.util.MapperHttpContent;
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

	public static final String RETURN_CODE = "returnCode";

	public static final String DURATION = "duration";

	public static final String DEPTH = "depth";

	public static final String TIMEOUT = "timeout";

	@Inject
	public WebsearchPageBeanMapper(
		final Provider<WebsearchPageBean> provider,
		final MapperCalendar mapperCalendar,
		final MapperWebsearchPageIdentifier mapperWebsearchPageIdentifier,
		final MapperUrl mapperUrl,
		final MapperHttpHeader mapperHttpHeader,
		final MapperInteger mapperInteger, final MapperHttpContent mapperHttpContent, final MapperLong mapperLong
	) {
		super(provider, buildMappings(mapperCalendar, mapperUrl, mapperWebsearchPageIdentifier, mapperHttpHeader, mapperInteger, mapperHttpContent, mapperLong));
	}

	private static Collection<StringObjectMapper<WebsearchPageBean>> buildMappings(
		final MapperCalendar mapperCalendar,
		final MapperUrl mapperUrl,
		final MapperWebsearchPageIdentifier mapperWebsearchPageIdentifier,
		final MapperHttpHeader mapperHttpHeader,
		final MapperInteger mapperInteger,
		final MapperHttpContent mapperHttpContent, final MapperLong mapperLong
	) {
		final List<StringObjectMapper<WebsearchPageBean>> result = new ArrayList<StringObjectMapper<WebsearchPageBean>>();
		result.add(new StringObjectMapperAdapter<WebsearchPageBean, WebsearchPageIdentifier>(ID, mapperWebsearchPageIdentifier));
		result.add(new StringObjectMapperAdapter<WebsearchPageBean, URL>(URL, mapperUrl));
		result.add(new StringObjectMapperAdapter<WebsearchPageBean, Calendar>(LAST_VISIT, mapperCalendar));
		result.add(new StringObjectMapperAdapter<WebsearchPageBean, Calendar>(CREATED, mapperCalendar));
		result.add(new StringObjectMapperAdapter<WebsearchPageBean, Calendar>(MODIFIED, mapperCalendar));
		result.add(new StringObjectMapperAdapter<WebsearchPageBean, HttpContent>(CONTENT, mapperHttpContent));
		result.add(new StringObjectMapperAdapter<WebsearchPageBean, HttpHeader>(HEADER, mapperHttpHeader));
		result.add(new StringObjectMapperAdapter<WebsearchPageBean, Integer>(RETURN_CODE, mapperInteger));
		result.add(new StringObjectMapperAdapter<WebsearchPageBean, Long>(DURATION, mapperLong));
		result.add(new StringObjectMapperAdapter<WebsearchPageBean, Long>(DEPTH, mapperLong));
		result.add(new StringObjectMapperAdapter<WebsearchPageBean, Integer>(TIMEOUT, mapperInteger));
		return result;
	}
}
