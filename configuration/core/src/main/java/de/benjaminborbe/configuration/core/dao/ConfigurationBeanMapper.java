package de.benjaminborbe.configuration.core.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import de.benjaminborbe.configuration.api.ConfigurationIdentifier;
import de.benjaminborbe.configuration.core.util.MapperConfigurationIdentifier;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

public class ConfigurationBeanMapper extends MapObjectMapperAdapter<ConfigurationBean> {

	@Inject
	public ConfigurationBeanMapper(
		final Provider<ConfigurationBean> provider,
		final MapperString mapperString,
		final MapperCalendar mapperCalendar,
		final MapperConfigurationIdentifier mapperConfigurationIdentifier) {
		super(provider, buildMappings(mapperString, mapperCalendar, mapperConfigurationIdentifier));
	}

	private static Collection<StringObjectMapper<ConfigurationBean>> buildMappings(final MapperString mapperString,
																																								 final MapperCalendar mapperCalendar, final MapperConfigurationIdentifier mapperConfigurationIdentifier) {
		final List<StringObjectMapper<ConfigurationBean>> result = new ArrayList<>();
		result.add(new StringObjectMapperAdapter<ConfigurationBean, ConfigurationIdentifier>("id", mapperConfigurationIdentifier));
		result.add(new StringObjectMapperAdapter<ConfigurationBean, String>("value", mapperString));
		result.add(new StringObjectMapperAdapter<ConfigurationBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperAdapter<ConfigurationBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}
