package de.benjaminborbe.dhl.dao;

import javax.inject.Inject;
import com.google.inject.Provider;
import javax.inject.Singleton;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.dhl.api.DhlIdentifier;
import de.benjaminborbe.dhl.util.MapperDhlIdentifier;
import de.benjaminborbe.dhl.util.MapperUserIdentifier;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperLong;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@Singleton
public class DhlBeanMapper extends MapObjectMapperAdapter<DhlBean> {

	@Inject
	public DhlBeanMapper(
		final Provider<DhlBean> provider,
		final MapperLong mapperLong,
		final MapperCalendar mapperCalendar,
		final MapperDhlIdentifier mapperDhlIdentifier,
		final MapperString mapperString,
		final MapperUserIdentifier mapperUserIdentifier) {
		super(provider, buildMappings(mapperLong, mapperCalendar, mapperDhlIdentifier, mapperString, mapperUserIdentifier));
	}

	private static Collection<StringObjectMapper<DhlBean>> buildMappings(final MapperLong mapperLong, final MapperCalendar mapperCalendar,
																																			 final MapperDhlIdentifier mapperDhlIdentifier, final MapperString mapperString, final MapperUserIdentifier mapperUserIdentifier) {
		final List<StringObjectMapper<DhlBean>> result = new ArrayList<>();
		result.add(new StringObjectMapperAdapter<DhlBean, DhlIdentifier>("id", mapperDhlIdentifier));
		result.add(new StringObjectMapperAdapter<DhlBean, String>("trackingNumber", mapperString));
		result.add(new StringObjectMapperAdapter<DhlBean, String>("status", mapperString));
		result.add(new StringObjectMapperAdapter<DhlBean, Long>("zip", mapperLong));
		result.add(new StringObjectMapperAdapter<DhlBean, UserIdentifier>("owner", mapperUserIdentifier));
		result.add(new StringObjectMapperAdapter<DhlBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperAdapter<DhlBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}
