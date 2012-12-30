package de.benjaminborbe.authorization.dao;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.authorization.api.RoleIdentifier;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.ManyToManyRelationStorage;

@Singleton
public class PermissionRoleManyToManyRelation extends ManyToManyRelationStorage<PermissionIdentifier, RoleIdentifier> {

	private static final String COLUMN_FAMILY = "permission_role";

	@Inject
	public PermissionRoleManyToManyRelation(final Logger logger, final StorageService storageService) {
		super(logger, storageService);
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}
}
