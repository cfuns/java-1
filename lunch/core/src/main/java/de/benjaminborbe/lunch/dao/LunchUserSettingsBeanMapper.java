package de.benjaminborbe.lunch.dao;

import com.google.inject.Provider;
import de.benjaminborbe.tools.mapper.MapperBoolean;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@Singleton
public class LunchUserSettingsBeanMapper extends MapObjectMapperAdapter<LunchUserSettingsBean> {

	@Inject
	public LunchUserSettingsBeanMapper(
		final Provider<LunchUserSettingsBean> provider,
		final MapperListIdentifier mapperListIdentifier,
		final MapperCalendar mapperCalendar,
		final MapperBoolean mapperBoolean
	) {
		super(provider, buildMappings(mapperListIdentifier, mapperCalendar, mapperBoolean));
	}

	private static Collection<StringObjectMapper<LunchUserSettingsBean>> buildMappings(
		final MapperListIdentifier mapperListIdentifier,
		final MapperCalendar mapperCalendar, final MapperBoolean mapperBoolean
	) {
		final List<StringObjectMapper<LunchUserSettingsBean>> result = new ArrayList<>();
		result.add(new StringObjectMapperAdapter<LunchUserSettingsBean, LunchUserSettingsIdentifier>("id", mapperListIdentifier));
		result.add(new StringObjectMapperAdapter<LunchUserSettingsBean, Boolean>("notificationActivated", mapperBoolean));
		result.add(new StringObjectMapperAdapter<LunchUserSettingsBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperAdapter<LunchUserSettingsBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}
