package de.benjaminborbe.websearch.configuration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperBoolean;
import de.benjaminborbe.tools.mapper.MapperInteger;
import de.benjaminborbe.tools.mapper.MapperLong;
import de.benjaminborbe.tools.mapper.MapperListString;
import de.benjaminborbe.tools.mapper.MapperUrl;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;
import de.benjaminborbe.websearch.api.WebsearchConfigurationIdentifier;
import de.benjaminborbe.websearch.util.MapperUserIdentifier;
import de.benjaminborbe.websearch.util.MapperWebsearchConfigurationIdentifier;

public class WebsearchConfigurationBeanMapper extends MapObjectMapperAdapter<WebsearchConfigurationBean> {

	@Inject
	public WebsearchConfigurationBeanMapper(
			final Provider<WebsearchConfigurationBean> provider,
			final MapperWebsearchConfigurationIdentifier mapperWebsearchConfigurationIdentifier,
			final MapperUserIdentifier mapperUserIdentifier,
			final MapperUrl mapperUrl,
			final MapperBoolean MapperBoolean,
			final MapperCalendar mapperCalendar,
			final MapperLong MapperLong,
			final MapperInteger MapperInteger,
			final MapperListString MapperStringList) {
		super(provider, buildMappings(mapperWebsearchConfigurationIdentifier, mapperUserIdentifier, mapperUrl, MapperBoolean, mapperCalendar, MapperLong, MapperInteger,
				MapperStringList));
	}

	private static Collection<StringObjectMapper<WebsearchConfigurationBean>> buildMappings(final MapperWebsearchConfigurationIdentifier mapperWebsearchConfigurationIdentifier,
			final MapperUserIdentifier mapperUserIdentifier, final MapperUrl mapperUrl, final MapperBoolean MapperBoolean, final MapperCalendar mapperCalendar,
			final MapperLong MapperLong, final MapperInteger MapperInteger, final MapperListString MapperStringList) {
		final List<StringObjectMapper<WebsearchConfigurationBean>> result = new ArrayList<StringObjectMapper<WebsearchConfigurationBean>>();
		result.add(new StringObjectMapperAdapter<WebsearchConfigurationBean, WebsearchConfigurationIdentifier>("id", mapperWebsearchConfigurationIdentifier));
		result.add(new StringObjectMapperAdapter<WebsearchConfigurationBean, UserIdentifier>("owner", mapperUserIdentifier));
		result.add(new StringObjectMapperAdapter<WebsearchConfigurationBean, URL>("url", mapperUrl));
		result.add(new StringObjectMapperAdapter<WebsearchConfigurationBean, List<String>>("excludes", MapperStringList));
		result.add(new StringObjectMapperAdapter<WebsearchConfigurationBean, Integer>("expire", MapperInteger));
		result.add(new StringObjectMapperAdapter<WebsearchConfigurationBean, Long>("delay", MapperLong));
		result.add(new StringObjectMapperAdapter<WebsearchConfigurationBean, Boolean>("activated", MapperBoolean));
		result.add(new StringObjectMapperAdapter<WebsearchConfigurationBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperAdapter<WebsearchConfigurationBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}
