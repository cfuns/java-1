package de.benjaminborbe.projectile.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperBase;

@Singleton
public class ProjectileReportBeanMapper extends MapObjectMapperAdapter<ProjectileReportBean> {

	public static final String USERNAME = "username";

	@Inject
	public ProjectileReportBeanMapper(
			final Provider<ProjectileReportBean> provider,
			final ProjectileReportIdentifierMapper mapperListIdentifier,
			final MapperString mapperString,
			final MapperCalendar mapperCalendar

	) {
		super(provider, buildMappings(mapperListIdentifier, mapperString, mapperCalendar));
	}

	private static Collection<StringObjectMapper<ProjectileReportBean>> buildMappings(final ProjectileReportIdentifierMapper mapperListIdentifier, final MapperString mapperString,
			final MapperCalendar mapperCalendar) {
		final List<StringObjectMapper<ProjectileReportBean>> result = new ArrayList<StringObjectMapper<ProjectileReportBean>>();
		result.add(new StringObjectMapperBase<ProjectileReportBean, ProjectileReportIdentifier>("id", mapperListIdentifier));
		result.add(new StringObjectMapperBase<ProjectileReportBean, String>(USERNAME, mapperString));
		result.add(new StringObjectMapperBase<ProjectileReportBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperBase<ProjectileReportBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}
