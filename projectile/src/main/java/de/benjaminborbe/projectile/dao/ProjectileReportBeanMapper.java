package de.benjaminborbe.projectile.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.projectile.util.MapperProjectileReportIdentifier;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperDouble;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

@Singleton
public class ProjectileReportBeanMapper extends MapObjectMapperAdapter<ProjectileReportBean> {

	public static final String USERNAME = "name";

	@Inject
	public ProjectileReportBeanMapper(
			final Provider<ProjectileReportBean> provider,
			final MapperProjectileReportIdentifier mapperListIdentifier,
			final MapperString mapperString,
			final MapperCalendar mapperCalendar,
			final MapperDouble mapperDouble) {
		super(provider, buildMappings(mapperListIdentifier, mapperString, mapperCalendar, mapperDouble));
	}

	private static Collection<StringObjectMapper<ProjectileReportBean>> buildMappings(final MapperProjectileReportIdentifier mapperListIdentifier, final MapperString mapperString,
			final MapperCalendar mapperCalendar, final MapperDouble mapperDouble) {
		final List<StringObjectMapper<ProjectileReportBean>> result = new ArrayList<StringObjectMapper<ProjectileReportBean>>();
		result.add(new StringObjectMapperAdapter<ProjectileReportBean, ProjectileReportIdentifier>("id", mapperListIdentifier));
		result.add(new StringObjectMapperAdapter<ProjectileReportBean, String>(USERNAME, mapperString));

		result.add(new StringObjectMapperAdapter<ProjectileReportBean, Double>("weekIntern", mapperDouble));
		result.add(new StringObjectMapperAdapter<ProjectileReportBean, Double>("weekExtern", mapperDouble));
		result.add(new StringObjectMapperAdapter<ProjectileReportBean, Double>("weekTarget", mapperDouble));
		result.add(new StringObjectMapperAdapter<ProjectileReportBean, Double>("weekBillable", mapperDouble));
		result.add(new StringObjectMapperAdapter<ProjectileReportBean, Calendar>("weekUpdateDate", mapperCalendar));

		result.add(new StringObjectMapperAdapter<ProjectileReportBean, Double>("monthIntern", mapperDouble));
		result.add(new StringObjectMapperAdapter<ProjectileReportBean, Double>("monthExtern", mapperDouble));
		result.add(new StringObjectMapperAdapter<ProjectileReportBean, Double>("monthTarget", mapperDouble));
		result.add(new StringObjectMapperAdapter<ProjectileReportBean, Double>("monthBillable", mapperDouble));
		result.add(new StringObjectMapperAdapter<ProjectileReportBean, Calendar>("monthUpdateDate", mapperCalendar));

		result.add(new StringObjectMapperAdapter<ProjectileReportBean, Double>("yearIntern", mapperDouble));
		result.add(new StringObjectMapperAdapter<ProjectileReportBean, Double>("yearExtern", mapperDouble));
		result.add(new StringObjectMapperAdapter<ProjectileReportBean, Double>("yearTarget", mapperDouble));
		result.add(new StringObjectMapperAdapter<ProjectileReportBean, Double>("yearBillable", mapperDouble));
		result.add(new StringObjectMapperAdapter<ProjectileReportBean, Calendar>("yearUpdateDate", mapperCalendar));

		result.add(new StringObjectMapperAdapter<ProjectileReportBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperAdapter<ProjectileReportBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}
