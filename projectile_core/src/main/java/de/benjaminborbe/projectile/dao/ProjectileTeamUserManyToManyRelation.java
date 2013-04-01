package de.benjaminborbe.projectile.dao;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.projectile.api.ProjectileTeamIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.ManyToManyRelationStorage;

@Singleton
public class ProjectileTeamUserManyToManyRelation extends ManyToManyRelationStorage<ProjectileTeamIdentifier, UserIdentifier> {

	private static final String COLUMN_FAMILY = "projectile_team_user";

	@Inject
	public ProjectileTeamUserManyToManyRelation(final Logger logger, final StorageService storageService) throws StorageException {
		super(logger, storageService);
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}
}
