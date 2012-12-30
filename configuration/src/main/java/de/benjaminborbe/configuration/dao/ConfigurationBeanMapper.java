package de.benjaminborbe.configuration.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import com.google.inject.Inject;
import com.google.inject.Provider;

import de.benjaminborbe.configuration.api.ConfigurationIdentifier;
import de.benjaminborbe.configuration.util.MapperConfigurationIdentifier;
import de.benjaminborbe.tools.mapper.MapperBoolean;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

public class ConfigurationBeanMapper extends MapObjectMapperAdapter<ConfigurationBean> {

	@Inject
	public ConfigurationBeanMapper(
			final Provider<ConfigurationBean> provider,
			final MapperString mapperString,
			final MapperBoolean mapperBoolean,
			final MapperCalendar mapperCalendar,
			final MapperConfigurationIdentifier mapperConfigurationIdentifier) {
		super(provider, buildMappings(mapperString, mapperBoolean, mapperCalendar, mapperConfigurationIdentifier));
	}

	private static Collection<StringObjectMapper<ConfigurationBean>> buildMappings(final MapperString mapperString, final MapperBoolean mapperBoolean,
			final MapperCalendar mapperCalendar, final MapperConfigurationIdentifier mapperConfigurationIdentifier) {
		final List<StringObjectMapper<ConfigurationBean>> result = new ArrayList<StringObjectMapper<ConfigurationBean>>();
		result.add(new StringObjectMapperAdapter<ConfigurationBean, ConfigurationIdentifier>("id", mapperConfigurationIdentifier));
		result.add(new StringObjectMapperAdapter<ConfigurationBean, String>("value", mapperString));
		result.add(new StringObjectMapperAdapter<ConfigurationBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperAdapter<ConfigurationBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}
