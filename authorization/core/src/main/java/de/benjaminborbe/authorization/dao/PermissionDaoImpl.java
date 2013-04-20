package de.benjaminborbe.authorization.dao;

import org.slf4j.Logger;

import javax.inject.Inject;
import com.google.inject.Provider;
import javax.inject.Singleton;

import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.tools.date.CalendarUtil;

@Singleton
public class PermissionDaoImpl extends DaoStorage<PermissionBean, PermissionIdentifier> implements PermissionDao {

	private static final String COLUMN_FAMILY = "permission";

	private final PermissionRoleManyToManyRelation permissionRoleManyToManyRelation;

	@Inject
	public PermissionDaoImpl(
			final Logger logger,
			final StorageService storageService,
			final Provider<PermissionBean> beanProvider,
			final PermissionBeanMapper mapper,
			final PermissionIdentifierBuilder identifierBuilder,
			final PermissionRoleManyToManyRelation permissionRoleManyToManyRelation,
			final CalendarUtil calendarUtil) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
		this.permissionRoleManyToManyRelation = permissionRoleManyToManyRelation;
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

	@Override
	public void delete(final PermissionIdentifier id) throws StorageException {
		super.delete(id);
		permissionRoleManyToManyRelation.removeA(id);
	}

	@Override
	public void delete(final PermissionBean entity) throws StorageException {
		super.delete(entity);
		permissionRoleManyToManyRelation.removeA(entity.getId());
	}

}
