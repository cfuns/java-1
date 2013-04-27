package de.benjaminborbe.authorization.dao;

import com.google.inject.Provider;
import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.authorization.util.MapperPermissionIdentifier;
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
public class PermissionBeanMapper extends MapObjectMapperAdapter<PermissionBean> {

	@Inject
	public PermissionBeanMapper(
		final Provider<PermissionBean> provider,
		final MapperCalendar mapperCalendar,
		final MapperPermissionIdentifier mapperPermissionIdentifier
	) {
		super(provider, buildMappings(mapperCalendar, mapperPermissionIdentifier));
	}

	private static Collection<StringObjectMapper<PermissionBean>> buildMappings(
		final MapperCalendar mapperCalendar,
		final MapperPermissionIdentifier mapperPermissionIdentifier
	) {
		final List<StringObjectMapper<PermissionBean>> result = new ArrayList<>();
		result.add(new StringObjectMapperAdapter<PermissionBean, PermissionIdentifier>("id", mapperPermissionIdentifier));
		result.add(new StringObjectMapperAdapter<PermissionBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperAdapter<PermissionBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}
