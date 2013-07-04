package de.benjaminborbe.authorization.dao;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.RoleIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.ManyToManyRelationStorage;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserRoleManyToManyRelation extends ManyToManyRelationStorage<UserIdentifier, RoleIdentifier> {

	private static final String COLUMN_FAMILY = "user_role";

	@Inject
	public UserRoleManyToManyRelation(final Logger logger, final StorageService storageService) throws StorageException {
		super(logger, storageService);
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}
}
