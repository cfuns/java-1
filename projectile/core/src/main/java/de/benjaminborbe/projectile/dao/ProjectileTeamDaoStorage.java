package de.benjaminborbe.projectile.dao;

import com.google.inject.Provider;
import de.benjaminborbe.projectile.api.ProjectileTeamIdentifier;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.tools.date.CalendarUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ProjectileTeamDaoStorage extends DaoStorage<ProjectileTeamBean, ProjectileTeamIdentifier> implements ProjectileTeamDao {

	@Inject
	public ProjectileTeamDaoStorage(
		final Logger logger,
		final StorageService storageService,
		final Provider<ProjectileTeamBean> beanProvider,
		final ProjectileTeamBeanMapper mapper,
		final ProjectileTeamIdentifierBuilder identifierBuilder,
		final CalendarUtil calendarUtil
	) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
	}

	private static final String COLUMN_FAMILY = "projectile_team";

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

}
