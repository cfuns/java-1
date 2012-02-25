package de.benjaminborbe.authorization.permission;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.StorageDao;

@Singleton
public class PermissionDaoImpl extends StorageDao<PermissionBean, PermissionIdentifier> implements PermissionDao {

	private static final String COLUMN_FAMILY = "permission";

	@Inject
	public PermissionDaoImpl(final Logger logger, final StorageService storageService, final Provider<PermissionBean> beanProvider, final PermissionBeanMapper mapper) {
		super(logger, storageService, beanProvider, mapper);
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

}