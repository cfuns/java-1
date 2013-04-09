package de.benjaminborbe.projectile.dao;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;

public interface ProjectileReportDao extends Dao<ProjectileReportBean, ProjectileReportIdentifier> {

	ProjectileReportBean getReportForUser(UserIdentifier userIdentifier) throws StorageException;

	ProjectileReportBean findOrCreateByUsername(String username) throws StorageException;

}
