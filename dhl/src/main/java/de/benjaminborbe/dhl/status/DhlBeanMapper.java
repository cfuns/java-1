package de.benjaminborbe.dhl.status;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.dhl.api.DhlIdentifier;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperLong;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

@Singleton
public class DhlBeanMapper extends MapObjectMapperAdapter<DhlBean> {

	@Inject
	public DhlBeanMapper(final Provider<DhlBean> provider, final MapperLong mapperLong, final MapperCalendar mapperCalendar, final MapperDhlIdentifier mapperDhlIdentifier) {
		super(provider, buildMappings(mapperLong, mapperCalendar, mapperDhlIdentifier));
	}

	private static Collection<StringObjectMapper<DhlBean>> buildMappings(final MapperLong mapperLong, final MapperCalendar mapperCalendar,
			final MapperDhlIdentifier mapperDhlIdentifier) {
		final List<StringObjectMapper<DhlBean>> result = new ArrayList<StringObjectMapper<DhlBean>>();
		result.add(new StringObjectMapperAdapter<DhlBean, DhlIdentifier>("id", mapperDhlIdentifier));
		result.add(new StringObjectMapperAdapter<DhlBean, Long>("trackingnumber", mapperLong));
		result.add(new StringObjectMapperAdapter<DhlBean, Long>("zip", mapperLong));
		result.add(new StringObjectMapperAdapter<DhlBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperAdapter<DhlBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}
