package de.benjaminborbe.projectile.dao;

import com.google.inject.Provider;
import de.benjaminborbe.projectile.api.ProjectileTeamIdentifier;
import de.benjaminborbe.projectile.util.MapperProjectileTeamIdentifier;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperString;
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
public class ProjectileTeamBeanMapper extends MapObjectMapperAdapter<ProjectileTeamBean> {

	public static final String NAME = "name";

	@Inject
	public ProjectileTeamBeanMapper(
		final Provider<ProjectileTeamBean> provider,
		final MapperProjectileTeamIdentifier mapperListIdentifier,
		final MapperString mapperString,
		final MapperCalendar mapperCalendar
	) {
		super(provider, buildMappings(mapperListIdentifier, mapperString, mapperCalendar));
	}

	private static Collection<StringObjectMapper<ProjectileTeamBean>> buildMappings(
		final MapperProjectileTeamIdentifier mapperListIdentifier, final MapperString mapperString,
		final MapperCalendar mapperCalendar
	) {
		final List<StringObjectMapper<ProjectileTeamBean>> result = new ArrayList<StringObjectMapper<ProjectileTeamBean>>();
		result.add(new StringObjectMapperAdapter<ProjectileTeamBean, ProjectileTeamIdentifier>("id", mapperListIdentifier));
		result.add(new StringObjectMapperAdapter<ProjectileTeamBean, String>(NAME, mapperString));
		result.add(new StringObjectMapperAdapter<ProjectileTeamBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperAdapter<ProjectileTeamBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}
