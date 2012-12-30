package de.benjaminborbe.wow.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.tools.mapper.MapperBoolean;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;
import de.benjaminborbe.wow.util.MapperUserIdentifier;
import de.benjaminborbe.wow.util.MapperWowAccountIdentifier;

@Singleton
public class WowAccountBeanMapper extends MapObjectMapperAdapter<WowAccountBean> {

	@Inject
	public WowAccountBeanMapper(
			final Provider<WowAccountBean> provider,
			final MapperWowAccountIdentifier mapperWowAccountIdentifier,
			final MapperUserIdentifier mapperUserIdentifier,
			final MapperString mapperString,
			final MapperBoolean mapperBoolean,
			final MapperCalendar mapperCalendar) {
		super(provider, buildMappings(mapperWowAccountIdentifier, mapperUserIdentifier, mapperString, mapperBoolean, mapperCalendar));
	}

	private static Collection<StringObjectMapper<WowAccountBean>> buildMappings(final MapperWowAccountIdentifier mapperWowAccountIdentifier,
			final MapperUserIdentifier mapperUserIdentifier, final MapperString mapperString, final MapperBoolean mapperBoolean, final MapperCalendar mapperCalendar) {
		final List<StringObjectMapper<WowAccountBean>> result = new ArrayList<StringObjectMapper<WowAccountBean>>();
		result.add(new StringObjectMapperAdapter<WowAccountBean, WowAccountIdentifier>("id", mapperWowAccountIdentifier));
		result.add(new StringObjectMapperAdapter<WowAccountBean, UserIdentifier>("owner", mapperUserIdentifier));
		result.add(new StringObjectMapperAdapter<WowAccountBean, String>("email", mapperString));
		result.add(new StringObjectMapperAdapter<WowAccountBean, String>("password", mapperString));
		result.add(new StringObjectMapperAdapter<WowAccountBean, String>("accoun", mapperString));
		result.add(new StringObjectMapperAdapter<WowAccountBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperAdapter<WowAccountBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}
