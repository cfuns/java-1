package de.benjaminborbe.wow.core.dao;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;
import de.benjaminborbe.wow.core.util.MapperUserIdentifier;
import de.benjaminborbe.wow.core.util.MapperWowAccountIdentifier;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@Singleton
public class WowAccountBeanMapper extends MapObjectMapperAdapter<WowAccountBean> {

	@Inject
	public WowAccountBeanMapper(
		final Provider<WowAccountBean> provider,
		final MapperWowAccountIdentifier mapperWowAccountIdentifier,
		final MapperUserIdentifier mapperUserIdentifier,
		final MapperString mapperString,
		final MapperCalendar mapperCalendar
	) {
		super(provider, buildMappings(mapperWowAccountIdentifier, mapperUserIdentifier, mapperString, mapperCalendar));
	}

	private static Collection<StringObjectMapper<WowAccountBean>> buildMappings(
		final MapperWowAccountIdentifier mapperWowAccountIdentifier,
		final MapperUserIdentifier mapperUserIdentifier, final MapperString mapperString, final MapperCalendar mapperCalendar
	) {
		final List<StringObjectMapper<WowAccountBean>> result = new ArrayList<>();
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
