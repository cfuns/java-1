package de.benjaminborbe.projectile.dao;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.tools.date.CalendarUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ProjectileReportDaoStorage extends DaoStorage<ProjectileReportBean, ProjectileReportIdentifier> implements ProjectileReportDao {

	@Inject
	public ProjectileReportDaoStorage(
		final Logger logger,
		final StorageService storageService,
		final Provider<ProjectileReportBean> beanProvider,
		final ProjectileReportBeanMapper mapper,
		final ProjectileReportIdentifierBuilder identifierBuilder,
		final CalendarUtil calendarUtil
	) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
	}

	private static final String COLUMN_FAMILY = "projectile_report";

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

	@Override
	public ProjectileReportBean getReportForUser(final UserIdentifier userIdentifier) throws StorageException {
		return load(new ProjectileReportIdentifier(userIdentifier.getId()));
	}

	@Override
	public ProjectileReportBean findOrCreateByUsername(final String username) throws StorageException {
		{
			final ProjectileReportBean bean = load(new ProjectileReportIdentifier(username));
			if (bean != null) {
				return bean;
			}
		}
		{
			final ProjectileReportBean bean = create();
			bean.setId(new ProjectileReportIdentifier(username));
			bean.setName(username);
			return bean;
		}
	}

}
